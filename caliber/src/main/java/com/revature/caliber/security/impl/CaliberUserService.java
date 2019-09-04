package com.revature.caliber.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.security.models.RevProUser;
import com.revature.caliber.security.models.RevProToken;
import com.revature.caliber.services.TrainingService;

/**
 * Spring Security class to gather user information into a User Principal object.
 * 
 * @author Patrick Walsh
 *
 */
@Service
public class CaliberUserService implements UserDetailsService{
	
	@Autowired
	private TrainingService trainingService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Trainer trainer = trainingService.findTrainer(email);
		if (trainer == null) {
			throw new UsernameNotFoundException(email);
		}
		// convert trainer into the User Principal
		RevProUser user = new RevProUser();
		user.setCaliberUser(trainer);
		user.setEmail(trainer.getEmail());
		user.setRole(trainer.getTier().toString()); 
		user.setId(Integer.toString(trainer.getTrainerId()));
		user.setUsername(trainer.getEmail());
		// initialize empty token to store RevPro token later
		RevProToken token = new RevProToken();
		user.setToken(token);
		return user;
	}
	
	public boolean checkPassword(String candidate, String hashed) {
		return passwordEncoder.matches(candidate, hashed);
	}
	
/*	public void saltAndSave(Trainer trainer) {
		String password = trainer.getPassword();
		// encode password
		trainer.setPassword(passwordEncoder.encode(password));
		trainingService.update(trainer);
	}*/

}
