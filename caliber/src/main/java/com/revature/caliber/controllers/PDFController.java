package com.revature.caliber.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.exceptions.PDFGenerationException;
import com.revature.caliber.services.PDFService;

@RestController
@PreAuthorize("isAuthenticated()")
@CrossOrigin(origins = "http://ec2-54-163-132-124.compute-1.amazonaws.com")
public class PDFController {

	private static final Logger log = Logger.getLogger(PDFController.class);
	private PDFService pdfService;

	@Autowired
	public void setPdfService(PDFService pdfService) {
		this.pdfService = pdfService;
	}

	@RequestMapping(value = "/report/generate", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER','PANEL')")
	public HttpEntity<byte[]> generate(
			@RequestParam(name = "title", value = "title", defaultValue = "Performance at a Glance") String title,
			@RequestBody String html) {
		try {
			byte[] pdf = pdfService.generatePDF(title, html);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "pdf"));
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");
			headers.setContentLength(pdf.length);
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			log.debug("PDF Generated");
			return new HttpEntity<>(pdf, headers);
		} catch (Exception e) {
			log.error("Error creating PDF file: " + e + " " + e.getClass() + " " + e.getMessage());
			throw new PDFGenerationException();
		}
	}
}
