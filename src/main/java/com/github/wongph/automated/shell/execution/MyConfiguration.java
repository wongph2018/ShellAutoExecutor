package com.github.wongph.automated.shell.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

	@Autowired
	ShellRunnerComponent shellRunnerService() {
		return new ShellRunnerComponent();
	}
	
}
