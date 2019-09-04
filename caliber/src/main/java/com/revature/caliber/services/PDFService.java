package com.revature.caliber.services;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.revature.caliber.pdf.RevaturePDF;

@Service
public class PDFService {

	private static final Logger log = Logger.getLogger(PDFService.class);

	/**
	 * Consumes HTML content and renders into a PDF format.
	 * 
	 * @param html
	 * @return Raw PDF as byte[]
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] generatePDF(String title, String html) throws DocumentException, IOException {
		log.debug(html);
		RevaturePDF pdf = new RevaturePDF(title, html);
		return pdf.toByteArray();
	}

}
