package com.revature.caliber.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.Category;
import com.revature.caliber.services.CategoryService;

@RestController
@PreAuthorize("isAuthenticated()")
@CrossOrigin(origins = "*")
public class CategoryController {

	private static final Logger log = Logger.getLogger(CategoryController.class);
	private CategoryService categoryService;

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	//Calls a method that will return all ACTIVE Categories. This will NOT return INACTIVE Categories. 
	@RequestMapping(value = "/category/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public ResponseEntity<List<Category>> findAllActive() {
		log.debug("Getting categories");
		List<Category> categories = categoryService.findAllActive();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	//Calls a method that will return all Categories both ACTIVE and INACTIVE. Intended to be used by VP only.
	@RequestMapping(value = "/vp/category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	public ResponseEntity<List<Category>> findAll() {
		log.debug("Getting categories");
		List<Category> categories = categoryService.findAll();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	//Calls a method that will find a category based on id. 
	@RequestMapping(value = "/category/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	public ResponseEntity<Category> findCategoryById(@PathVariable int id) {
		log.debug("Getting category: " + id);
		Category category = categoryService.findCategory(id);
		log.debug(category.toString());
		return new ResponseEntity<>(category, HttpStatus.OK);
	}
	//Calls a method that will update a categories name or activity
	@RequestMapping(value = "/vp/category/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category) {
		categoryService.updateCategory(category);
		return new ResponseEntity<>(category, HttpStatus.OK);
	}
	//Calls a method that creates a new Category
	@RequestMapping(value = "/vp/category", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP')")
	public ResponseEntity<Category> saveCategory(@Valid @RequestBody Category category) {
		categoryService.saveCategory(category);
		return new ResponseEntity<>(category, HttpStatus.CREATED);
	}
}
