package com.github.wongph.automated.shell.execution;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.github.wongph.automated.shell.execution.multithread.MultiThreadService;

@SpringBootApplication
public class Application {
	
	private static Logger LOGGER = LogManager.getLogger(Application.class);
	


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		String[] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
		
		
		MultiThreadService multiThreadService = context.getBean(MultiThreadService.class);
		try {
			LOGGER.info("Execute Multi Thread Service");
			multiThreadService.executeConcurrentRequests();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		
	}
	
	

}
