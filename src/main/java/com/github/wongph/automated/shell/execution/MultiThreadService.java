package com.github.wongph.automated.shell.execution;

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

@Service
public class MultiThreadService {
	
	private static Logger LOGGER = LogManager.getLogger(MultiThreadService.class);
	
	@Autowired
	private JSchService jSchService;
	
	@Autowired
	private Environment env;
	
    @Async
    public void executeConcurrentRequests() throws InterruptedException, ExecutionException {
    	
    	
    	
		String host = env.getProperty("hostname");
		String keyPath = env.getProperty("key.path");
		String username1 = env.getProperty("username.1");
		String username2 = env.getProperty("username.2");
		String username3 = env.getProperty("username.3");
		String username4 = env.getProperty("username.4");
    	
        // Create a list of tasks to execute concurrently
        List<Callable<String>> tasks = new ArrayList<>();
        tasks.add(() -> jSchService.connectToServer(host, username1, keyPath, "1"));
        tasks.add(() -> jSchService.connectToServer(host, username2, keyPath, "2"));
        tasks.add(() -> jSchService.connectToServer(host, username3, keyPath, "3"));
        tasks.add(() -> jSchService.connectToServer(host, username4, keyPath, "4"));
        
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

}
