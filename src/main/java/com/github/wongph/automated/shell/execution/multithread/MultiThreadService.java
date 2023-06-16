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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.wongph.automated.shell.execution.jsch.JSchObj;
import com.github.wongph.automated.shell.execution.jsch.JSchService;

@Service
public class MultiThreadService {
	
	private static Logger LOGGER = LogManager.getLogger(MultiThreadService.class);
	
	private static final String SSH_HOST = "";
	private static final String SSH_LOGIN = "";
	private static final String SSH_PWD = "";
	private static final String SCHEMA = "";
	
	private static final String SSH_LOGIN2 = "";
	private static final String SSH_PWD2 = "";
	private static final String SCHEMA2 = "";
	
	private static final String SSH_LOGIN3 = "";
	private static final String SSH_PWD3 = "";
	private static final String SCHEMA3 = "";
	
	private static final String SSH_LOGIN4 = "";
	private static final String SSH_PWD4 = "";
	private static final String SCHEMA4 = "";
	
	
	@Autowired
	private JSchService jSchService;
	
	@Async
	public void executeConcurrentRequests() throws InterruptedException, ExecutionException {
		

		List<JSchObj> jSchObjList = hardcodeJSchList();
		
		
		List<Callable<String>> tasks = new ArrayList<>();
		for(JSchObj j : jSchObjList) {
			tasks.add(() -> jSchService.runJobFlow(j));
		}
		
		// Create an executor service with a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        
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
		List<JSchObj> jSchObjList = new ArrayList<>();
		
//		JSchObj j1 = new JSchObj.JSchObjBuilder()
//				.host(SSH_HOST)
//				.username(SSH_LOGIN)
//				.password(SSH_PWD)
//				.schema(SCHEMA)
//				.build();
		
		JSchObj j2 = new JSchObj.JSchObjBuilder()
				.host(SSH_HOST)
				.username(SSH_LOGIN2)
				.password(SSH_PWD2)
				.schema(SCHEMA2)
				.build();
		
		JSchObj j3 = new JSchObj.JSchObjBuilder()
				.host(SSH_HOST)
				.username(SSH_LOGIN3)
				.password(SSH_PWD3)
				.schema(SCHEMA3)
				.build();
		
		JSchObj j4 = new JSchObj.JSchObjBuilder()
				.host(SSH_HOST)
				.username(SSH_LOGIN4)
				.password(SSH_PWD4)
				.schema(SCHEMA4)
				.build();
		
//		jSchObjList.add(j1);
		jSchObjList.add(j2);
		jSchObjList.add(j3);
		jSchObjList.add(j4);
		
		
		
		return jSchObjList;
	}
}
