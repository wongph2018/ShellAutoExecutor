package com.github.wongph.automated.shell.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ShellRunnerService {
	
	private static Logger LOGGER = LogManager.getLogger(ShellRunnerComponent.class);
	
	@Autowired
	private ShellRunnerComponent shellRunnerComponent;
	
	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;
	
	
	public void autoExecuteJob() {
		LOGGER.info("startProgram NOW!!!!!!!!!!!");
		
		taskExecutor.execute(() -> {
			String[] commands = { "src/main/resources/interactive.sh" };
			shellRunnerComponent.executeCommand(commands);
			shellRunnerComponent.passUserInput("Who am I");
			shellRunnerComponent.passUserInput("1");
			shellRunnerComponent.passUserInput("2");
			String result = shellRunnerComponent.getShellOutput();
			
			LOGGER.info("Result: {}", result);
		});
		
	}

}
