package com.github.wongph.automated.shell.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ShellRunnerComponent {
	
	private static Logger LOGGER = LogManager.getLogger(ShellRunnerComponent.class);
	
	private InputStream inputStream;
	private OutputStream outputStream;
	private Process process;
	
	
	public void executeCommand(String[] commands) {
		LOGGER.info("execute: {}", Arrays.toString(commands));
		
		ProcessBuilder pb = new ProcessBuilder(commands);
		pb.redirectErrorStream(true);
		try {
			process = pb.start();
			outputStream = process.getOutputStream();
			inputStream = process.getInputStream();
			
		} catch (IOException ioe) {
			LOGGER.error("IOException", ioe);
		}
	}
	
	public void passUserInput(String input) {
		LOGGER.info("Pass UserInput: {}", input);
		byte[] inputBytes = (input + System.lineSeparator()).getBytes();
		
		try {
			outputStream.write(inputBytes);
			outputStream.flush();
			Thread.sleep(1000);
		} catch (IOException ioe) {
			LOGGER.error("IOException", ioe);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			LOGGER.error("InterruptedException", e);
		}
	}
	
	public String getShellOutput() {
		LOGGER.info("Get Shell Output");
		
		try {
			int exitVal = process.waitFor();
			if(exitVal == 0) {
				LOGGER.info("Job Success");
			} else {
				LOGGER.warn("Job Failed: {}", exitVal);
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			StringBuilder sb = new StringBuilder();

			while((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
			
			return sb.toString();
			
		} catch (InterruptedException ie) {
			LOGGER.error("InterruptedException", ie);
		} catch (IOException ioe) {
			LOGGER.error("IOException", ioe);
		}
		return null;
	}
	
	public void endProcess() {
		destroyProcess();
		closeStream();
	}
	
	public void destroyProcess() {
		if(process == null) {
			return;
		}
		process.destroy();
	}
	
	public void closeStream() {
		try {
			if(inputStream != null) {
				inputStream.close();
			}
			if(outputStream != null) {
				outputStream.close();
			}
		} catch (IOException ioe) {
			LOGGER.error("IOException", ioe);
		}
	}
	
	
	
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	
	
}
