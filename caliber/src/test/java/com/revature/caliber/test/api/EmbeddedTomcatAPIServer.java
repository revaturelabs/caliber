package com.revature.caliber.test.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
//@ComponentScan("com.revature")
@ImportResource(value={"integration-test.xml", "api-test-security.xml"})
public class EmbeddedTomcatAPIServer{

	private static Log log = LogFactory.getLog(EmbeddedTomcatAPIServer.class);
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(EmbeddedTomcatAPIServer.class, args);
	}
	
	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				log.debug("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				log.debug("ServletContext destroyed");
			}
		};
	}
}