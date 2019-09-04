package com.revature.caliber.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Panel;
import com.revature.caliber.beans.PanelFeedback;
import com.revature.caliber.beans.PanelStatus;

@Component
public class PanelValidator implements ConstraintValidator<ValidPanel, Panel>{

	private static final Logger log = Logger.getLogger(PanelValidator.class);
	
	@Override
	public void initialize(ValidPanel constraintAnnotation) {
		// unused lifecycle method
	}

	@Override
	public boolean isValid(Panel panel, ConstraintValidatorContext context) {
		//verifying server side that the overall panel status field has not been tampered with
		boolean pass = true;
		if(panel.getFeedback() != null) {
			for(PanelFeedback feedback : panel.getFeedback()) {
				if(feedback.getStatus().equals(PanelStatus.Repanel)) {
					pass = false;
					break;
				}
			}
		}
		if((pass && !PanelStatus.Pass.equals(panel.getStatus() )) || 
		  (!pass && PanelStatus.Pass.equals(panel.getStatus()))) {
			log.warn("Failed to create panel. Overall Panel Status not as expected.");
			return false;
		}
		
		return true;
	}

}
