package com.revature.caliber.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.revature.caliber.exceptions.PDFGenerationException;

/**
 * Event Handler that applies header and footer images at the start of every
 * page.
 * 
 * @author Patrick Walsh
 *
 */
public class RevaturePDFPageEventHandler extends PdfPageEventHelper {

	private static final Logger log = Logger.getLogger(RevaturePDFPageEventHandler.class);
	private static final String ERROR = "Error creating PDF file: ";
	private byte[] foot;
	private byte[] head;
	private String title;

	public RevaturePDFPageEventHandler(InputStream header, InputStream footer, String title) {
		try {
			this.title = title;
			this.head = IOUtils.toByteArray(header);
			this.foot = IOUtils.toByteArray(footer);
		} catch (IOException e) {
			log.error("Error reading header/footer image: " + e);
			throw new PDFGenerationException();
		}
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		// set header
		Image header;
		try {
			header = Image.getInstance(head);
			header.setAbsolutePosition(0, document.getPageSize().getHeight() - header.getScaledHeight());
			header.setSpacingAfter(20);
			writer.getDirectContent().addImage(header);
			String reportTitle = "<html><head><style> #report-title { color: #FFFFFF; font-size: 14pt; font-family: Arial; margin-bottom: 50px; }</style></head><body><div><h1 id='report-title'>"
					+ title + "</h1></div><br/></body></html>";
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(reportTitle));

		} catch (DocumentException | IOException e) {
			log.error(ERROR + e);
			throw new PDFGenerationException();
		}

		// set footer
		Image footer;
		try {
			footer = Image.getInstance(foot);
			footer.setAbsolutePosition(0, 0);
			footer.setSpacingBefore(20);
			writer.getDirectContent().addImage(footer);
		} catch (DocumentException | IOException e) {
			log.error(ERROR + e);
			throw new PDFGenerationException();
		}
	}
}
