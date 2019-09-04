package com.revature.caliber.email;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.data.BatchDAO;

@Component
public class GradeSubmitMailer implements Runnable{

	private static final Logger log = Logger.getLogger(GradeSubmitMailer.class);

	@Autowired
	private BatchDAO batchDAO;

	@Autowired
	private EmailAuthenticator authenticator;

	/**
	 * The EMAIL_TEMPLATE_NAME_TOKEN is the token that is in the HTML file that will
	 * be replaced with the actual name of the trainer for the email to be trainer
	 * specific
	 */
	private static final String EMAIL_TEMPLATE_NAME_TOKEN = "$TRAINER_NAME";

	/**
	 * The path to the email template
	 */
	private static final String EMAIL_TEMPLATE_PATH = "emailTemplate.html";

	/**
	 * Called by the scheduledThreadExecutor when the time is right based on the
	 * constants in EmailService Simply calls send(), which calculates which
	 * trainers need to be emailed and emails them
	 * 
	 * @precondition None.
	 * @param None.
	 * @postcondition Email thread is running on server
	 */
	@Override
	public void run() {
		send();
	}

	/**
	 * Sets up the properties and session in order to send emails then simply calls
	 * the sendEmails() method which does email sending given the trainers who need
	 * to submit grades
	 */
	private void send() {
		Properties properties = setProperties();
		Session session = getSession(properties);
		sendEmails(session, getTrainersWhoNeedToSubmitGrades());
	}

	/**
	 * Sets up the properties for the sending of emails We use gmail's SMTP server
	 * 
	 * @return The properties for our email sending procedure
	 */
	private Properties setProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		return properties;
	}

	/**
	 * Creates an email Session that can be used to send emails
	 * 
	 * @param properties
	 *            The configuration for this session
	 * @return A session used to send emails
	 */
	private Session getSession(Properties properties) {
		return Session.getDefaultInstance(properties, authenticator);
	}

	/**
	 * Iterates over trainersToSubmitGrades and emails each person individually that
	 * they need to submit their grades
	 * 
	 * @param session
	 *            The email session used to send emails
	 * @param trainersToSubmitGrades
	 *            The trainers who need to be emailed reminders
	 */
	private void sendEmails(Session session, Set<Trainer> trainersToSubmitGrades) {
		log.info("Trainers being sent emails: " + trainersToSubmitGrades);
		String emailTemplate = getEmailString();
		if (emailTemplate == null) {
			log.warn("Unable to load email template, exiting sendEmails()");
			return;
		}
		for (Trainer trainer : trainersToSubmitGrades) {
			try {
				MimeMessage message = new MimeMessage(session);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(trainer.getEmail()));
				message.setSubject("Submit Grades Reminder");
				// Parametrize the email to contain the name of the trainer being emailed
				String emailStr = emailTemplate.replace(EMAIL_TEMPLATE_NAME_TOKEN, trainer.getName());
				message.setContent(emailStr, "text/html");
				Transport.send(message);
				log.info("Email sent");
			} catch (MessagingException e) {
				log.warn(e);
				log.warn("Email exception");
			}
		}
	}

	/**
	 * Reads the email template located at EMAIL_TEMPLATE_PATH and returns a String
	 * containing the contents of it
	 * 
	 * @return A String containing the contents of the email template html file
	 */
	private String getEmailString() {
		try {
			String emailStr = null;
			ClassLoader classLoader = getClass().getClassLoader();
			emailStr = IOUtils.toString(classLoader.getResourceAsStream(EMAIL_TEMPLATE_PATH));
			log.info("loaded email template");
			return emailStr;
		} catch (IOException e) {
			log.warn("Unable to read email template");
			log.warn(e);
			return null;
		}
	}

	/**
	 * Gets all trainers who currently are conducting a batch.
	 * @return
	 */
	public Set<Trainer> getTrainersWhoNeedToSubmitGrades() {
		Set<Trainer> trainers = new HashSet<>();
		batchDAO.findAllInProgress().forEach(batch -> trainers.add(batch.getTrainer()));
		return trainers;
	}

}
