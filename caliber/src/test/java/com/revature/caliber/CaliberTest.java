package com.revature.caliber;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.security.models.RevProUser;

/**
 * To create a consistent testing approach that provides 1) transient data in an
 * embedded H2 database, 2) a single ApplicationContext instance across tests,
 * 3) a JdbcTemplate object to perform low-level SQL please inherit from this
 * class in your unit/integration tests.
 * 
 * @author Patrick Walsh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:integration-test.xml" })
@Sql(scripts = "/setup.sql", config = @SqlConfig(transactionMode = TransactionMode.ISOLATED), executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/teardown.sql", config = @SqlConfig(transactionMode = TransactionMode.ISOLATED), executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class CaliberTest {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public Authentication mockAuth(){
		RevProUser salesforceUser = new RevProUser();
		salesforceUser.setUserId("patrick@revature.com");
		Trainer trainer = new Trainer("Patrick Walsh", "Lead Trainer", "patrick.walsh@revature.com", TrainerRole.ROLE_VP);
		trainer.setTrainerId(1);
		salesforceUser.setCaliberUser(trainer);
		return new PreAuthenticatedAuthenticationToken(salesforceUser, salesforceUser.getUserId(),
				salesforceUser.getAuthorities());
	}

	@Test
	public void testJdbcTemplate() {
		assertNotNull(jdbcTemplate);
	}

}
