package com.github.wongph.automated.shell.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {
	
	private static Logger LOGGER = LogManager.getLogger(Application.class);
	


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		String[] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
		
		LOGGER.info("Execute Shell Runner Service");
		ShellRunnerService shellRunnerService = context.getBean(ShellRunnerService.class);
		shellRunnerService.autoExecuteJob();
	}
	
	

}
