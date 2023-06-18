package com.github.wongph.automated.shell.execution.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.wongph.automated.shell.execution.jsch.JSchObj;
import com.github.wongph.automated.shell.execution.jsch.JSchService;

@Service
public class MultiThreadService {
	
	private static Logger LOGGER = LogManager.getLogger(MultiThreadService.class);
	
	
	@Autowired
	private JSchService jSchService;
	
	@Autowired
	private Environment env;

	
	@Async
	public void executeConcurrentRequests() throws InterruptedException, ExecutionException {
		

		List<JSchObj> jSchObjList = hardcodeJSchList();
		
		
		List<Callable<String>> tasks = new ArrayList<>();
		for(JSchObj j : jSchObjList) {
			tasks.add(() -> jSchService.runJobFlow(j));
		}
		
		// Create an executor service with a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        
        // Submit the tasks and obtain a list of futures
        List<Future<String>> futures = executorService.invokeAll(tasks);
        
        LOGGER.info("Wait for all the futures to complete and print their results");
        for (Future<String> future : futures) {
            LOGGER.info(future.get());
        }
        
        // Shutdown the executor service
        executorService.shutdown();
	}
	
	private List<JSchObj> hardcodeJSchList() {
		
		final String SSH_HOST = env.getProperty("hostname");
		final String KEY_PATH = env.getProperty("key.path");
		
		final String SSH_LOGIN = env.getProperty("username.1");
		
		final String SSH_LOGIN2 = env.getProperty("username.2");
		
		final String SSH_LOGIN3 = env.getProperty("username.3");
		
		final String SSH_LOGIN4 = env.getProperty("username.4");
		
		List<JSchObj> jSchObjList = new ArrayList<>();
		
		JSchObj j1 = new JSchObj.JSchObjBuilder()
				.host(SSH_HOST)
				.username(SSH_LOGIN)
				.keyPath(KEY_PATH)
				.build();
		
		
		JSchObj j2 = new JSchObj.JSchObjBuilder()
				.host(SSH_HOST)
				.username(SSH_LOGIN2)
				.keyPath(KEY_PATH)
				.build();
		
		JSchObj j3 = new JSchObj.JSchObjBuilder()
				.host(SSH_HOST)
				.username(SSH_LOGIN3)
				.keyPath(KEY_PATH)
				.build();
		
		JSchObj j4 = new JSchObj.JSchObjBuilder()
				.host(SSH_HOST)
				.username(SSH_LOGIN4)
				.keyPath(KEY_PATH)
				.build();
		
		jSchObjList.add(j1);
//		jSchObjList.add(j2);
//		jSchObjList.add(j3);
//		jSchObjList.add(j4);
		
		


		
		
		return jSchObjList;
	}
}
