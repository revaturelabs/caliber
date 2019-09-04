package com.revature.caliber.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.TrainerDAO;
import com.revature.caliber.revpro.RevProCaliberTransformer;
import com.revature.caliber.revpro.beans.RevProBatch;
import com.revature.caliber.revpro.beans.RevProTrainee;
import com.revature.caliber.revpro.rest.models.AllBatchesResponse;
import com.revature.caliber.revpro.rest.models.BatchAssociatesResponse;
import com.revature.caliber.security.models.RevProUser;

@Service
public class RevProBatchImportService {

	private static final Logger log = Logger.getLogger(RevProBatchImportService.class);
	public static final String DEFAULT_TRAINER = "external@revature.com";
	private static final String ENROLLMENT_ADMIN = "enrollmentadmin@revature.com";

	@Value("#{systemEnvironment['REVPRO_LOGIN_URL']}")
	private String revProLoginUrl;
	@Value("#{systemEnvironment['REVPRO_BATCHES_URL']}")
	private String revProBatchesUrl;
	@Value("#{systemEnvironment['REVPRO_ASSOCIATES_URL']}")
	private String revProAssociatesUrl;

	@Autowired
	private BatchDAO batchDAO;
	@Autowired
	private TrainerDAO trainerDAO;
	@Autowired
	private RevProCaliberTransformer transformer;

	/**
	 * Find all RevPro batches. Used in the nightly batch update sync.
	 * 
	 * @return List of Batches
	 */
	public List<Batch> getAllBatches() {
		log.debug("Find all batches");
		List<Batch> allRevProBatches = getAllRevProBatches(-4);

		// load trainer and co-trainer from Caliber DB
		for (Batch batch : allRevProBatches) {
			log.debug(batch);
			if (batch.getTrainer() != null) {
				// use actual trainer
				Trainer trainer = trainerDAO.findByEmail(batch.getTrainer().getEmail());
				log.debug("Found trainer: " + trainer);
				if (trainer != null) {
					batch.setTrainer(trainer);
				}

				if (batch.getCoTrainer() != null) {
					Trainer cotrainer = trainerDAO.findByEmail(batch.getCoTrainer().getEmail());
					log.debug("Found trainer: " + cotrainer);
					if (cotrainer != null) {
						batch.setCoTrainer(cotrainer);
					}
				}
			}

			log.debug(batch.getTrainer());
			log.debug(batch.getCoTrainer());
		}

		return allRevProBatches;
	}

	/**
	 * Find all RevPro batches. Remove any batches that are already in the Caliber
	 * database. Used to populate drop-down of importable batches.
	 * 
	 * @return List of Batches
	 */
	public List<Batch> getAllRelevantBatches() {
		log.debug("Find all current batches by year");
		List<Batch> allRevProBatches = getAllRevProBatches(-1);
		List<Batch> allCaliberBatches = batchDAO.findAll();

		// load trainer and co-trainer from Caliber DB
		for (Batch batch : allRevProBatches) {
			log.debug(batch);
			if (batch.getTrainer() != null) {
				// use actual trainer
				Trainer trainer = trainerDAO.findByEmail(batch.getTrainer().getEmail());
				log.debug("Found trainer: " + trainer);
				if (trainer != null) {
					batch.setTrainer(trainer);
				}

				if (batch.getCoTrainer() != null) {
					Trainer cotrainer = trainerDAO.findByEmail(batch.getCoTrainer().getEmail());
					log.debug("Found trainer: " + cotrainer);
					if (cotrainer != null) {
						batch.setCoTrainer(cotrainer);
					}
				}
			}

			log.debug(batch.getTrainer());
			log.debug(batch.getCoTrainer());
		}

		// Removing batches already in Caliber database
		for (int cIndex = 0; cIndex < allCaliberBatches.size(); cIndex++) {
			String cResourceId = allCaliberBatches.get(cIndex).getResourceId();
			if (cResourceId == null) {
				continue;
			}
			for (int sfIndex = 0; sfIndex < allRevProBatches.size(); sfIndex++) {
				String sfResourceId = allRevProBatches.get(sfIndex).getResourceId();
				if (cResourceId.equals(sfResourceId)) {
					allRevProBatches.remove(sfIndex);
					break;
				}
			}
		}

		return allRevProBatches;
	}

	@Deprecated
	private Map<String, Trainer> loadTrainers() {
		List<Trainer> trainers = trainerDAO.findAll();
		Map<String, Trainer> trainerMap = new HashMap<>();
		for (Trainer t : trainers) {
			trainerMap.putIfAbsent(t.getEmail(), t);
		}
		return trainerMap;
	}

	/**
	 * FIND ALL TRAINEES
	 * 
	 * @return List of Trainees
	 */

	public List<Trainee> getAllTraineesFromBatch(String resourceId) {
		log.debug("Find all trainees");
		return getBatchDetails(resourceId);
	}

	/**
	 * Fetch batches from RevPro
	 * 
	 * @return
	 */
	private List<Batch> getAllRevProBatches(int monthsAgo) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("encryptedToken", getToken());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		RestTemplate http = new RestTemplate();

		// filter to last 3 monthses
		Date referenceDate = new Date();
		Calendar startDateAfter = Calendar.getInstance();
		startDateAfter.setTime(referenceDate);
		startDateAfter.add(Calendar.MONTH, monthsAgo);		
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");	
		
		String url = revProBatchesUrl + "?startDateAfter=" + dateformat.format(startDateAfter.getTime()) + "&startDateBefore="  + dateformat.format(referenceDate.getTime());
		log.debug(url);
		try {
			ResponseEntity<AllBatchesResponse> response = http.exchange(url, HttpMethod.GET, entity,
					AllBatchesResponse.class);
			// convert the batches to Caliber batch
			List<RevProBatch> revProBatches = response.getBody().getData();
			List<Batch> batches = new ArrayList<>();
			for (RevProBatch batch : revProBatches) {
				// only bring back sync-able batches
				if (batch.getSalesforceId() != null) {
					batches.add(transformer.transformBatch(batch));
				}
			}
			return batches;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Failed to fetch batches " + e.getClass() + " " + e.getMessage());
			return null;
		}
	}

	/**
	 * Retrieve the encrypted access token to talk to RevPro
	 * 
	 * @return
	 */
	private String getToken() {
		return ((RevProUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken()
				.getAccessToken();
	}

	/**
	 * Fetch trainees from RevPro
	 * 
	 * @param resourceId
	 * @return
	 */
	public List<Trainee> getBatchDetails(String resourceId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("encryptedToken", getToken());
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		RestTemplate http = new RestTemplate();
		// set batch resourceId as path param
		String url = String.format(revProAssociatesUrl, resourceId);
		log.debug(url);
		List<Trainee> trainees = new ArrayList<>();
		try {
			ResponseEntity<BatchAssociatesResponse> response = http.exchange(url, HttpMethod.GET, entity,
					BatchAssociatesResponse.class);

			// convert the batches to Caliber batch
			RevProBatch revProBatch = response.getBody().getData();

			// convert trainees
			List<RevProTrainee> revProTrainees = revProBatch.getBatchTrainees();
			for (RevProTrainee trainee : revProTrainees) {
				// filter out trainees that are not joining training on day one
				trainees.add(transformer.transformTrainee(trainee));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Failed to fetch details for batch :  " + resourceId + " with " + e.getClass() + " "
					+ e.getMessage());
		}
		return trainees;
	}

}
