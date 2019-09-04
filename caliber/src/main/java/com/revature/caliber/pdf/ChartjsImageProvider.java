package com.revature.caliber.pdf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.revature.caliber.exceptions.PDFGenerationException;

/**
 * Decodes Base64 images into iText image
 * @author Patrick Walsh
 *
 */
public class ChartjsImageProvider extends AbstractImageProvider {
	
	private static final Logger log = Logger.getLogger(ChartjsImageProvider.class);
	
	// capability to parse Base64 from <img src=..> to create Image
	@Override
	public Image retrieve(String src) {
		int pos = src.indexOf("base64,");
		try {
			if (src.startsWith("data") && pos > 0) {
				byte[] img = Base64.decode(src.substring(pos + 7));
				return Image.getInstance(img);
			} else {
				return Image.getInstance(src);
			}
		} catch (BadElementException | IOException e) {
			log.debug("Error creating PDF file: " + e);
			throw new PDFGenerationException();
		}
	}

	@Override
	public String getImageRootPath() {
		throw new PDFGenerationException();
	}
}
