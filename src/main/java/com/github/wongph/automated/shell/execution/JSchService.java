package com.github.wongph.automated.shell.execution;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service
public class JSchService {
	private static Logger LOGGER = LogManager.getLogger(JSchService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JSCHComponent jschComponent;
	
	public String connectToServer(String host, String username, String keyPath, String digit) throws IOException {
		Session session = null;
		ChannelShell channel = null;
		String response;
		
		try {
			Integer sleepTime = env.getProperty("sleep.time", Integer.class);
			
			session = jschComponent.getSession(host, username, keyPath);
			channel = jschComponent.getChannelShell(session);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			channel.setOutputStream(outputStream);
			channel.connect();
			
			Thread.sleep(sleepTime);
			
			response = waitForPrompt(outputStream, "$");
			LOGGER.info("response: {}", response);
			
			List<String> commands = new ArrayList<>();
			commands.add("cd script");
			commands.add("pwd");
			commands.add("./interactive.sh");
			commands.add(username);
			commands.add(digit);
			commands.add(digit);
			
			executeCommands(channel, commands);
			LOGGER.info(username + " Executed Successfully");
			return username + " Executed Successfully";
			

		} catch(JSchException jSchException) {
			LOGGER.error("JSchException", jSchException);
//		} catch(InterruptedException interruptedException) {
//			LOGGER.error("InterruptedException", interruptedException);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("IOException", e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			LOGGER.error("InterruptedException", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("Exception", e);
		} finally {
			jschComponent.disconnectChannel(channel);
			jschComponent.disconnectSession(session);
		}
		return username + " Should not be here";
	}
	
	
	public String waitForPrompt(ByteArrayOutputStream outputStream, String prompt) throws Exception {
	    int retries = 3;
	    for (int x = 1; x < retries; x++) {
	        TimeUnit.SECONDS.sleep(1);
	        if (outputStream.toString().indexOf(prompt) > 0) {
	            String responseString = outputStream.toString();
	            outputStream.reset();
	            return responseString;
	        }
	    }
	    throw new Exception("Prompt failed to show after specified timeout");
	}
	
	private void executeCommands(Channel channel, List<String> commands){

	    try{
	    	
	        LOGGER.info("Sending commands...");
	        sendCommands(channel, commands);

	        readChannelOutput(channel);
	        LOGGER.info("Finished sending commands!");
	        exitCommands(channel);
	        readChannelOutput(channel);
	        LOGGER.info("Finished Exit shell!");

	    }catch(Exception e){
	        LOGGER.error("An error ocurred during executeCommands: "+e);
	    }
	}

	private void sendCommands(Channel channel, List<String> commands){

	    try{
	        PrintStream out = new PrintStream(channel.getOutputStream());

//	        out.println("#!/bin/bash");
	        for(String command : commands){
	            out.println(command);
	        }

	        out.flush();
	    }catch(Exception e){
	        System.out.println("Error while sending commands: "+ e);
	    }

	}
	
	private void exitCommands(Channel channel) {
		try{
	        PrintStream out = new PrintStream(channel.getOutputStream());
	        out.println("#!/bin/bash");
	        out.println("exit");
	        out.flush();
	    }catch(Exception e){
	        System.out.println("Error while sending commands: "+ e);
	    }
	}
	
	private void readChannelOutput(Channel channel){

	    byte[] buffer = new byte[1024];

	    try{
	        InputStream in = channel.getInputStream();
	        String line = "";
	        
	        while (true){
	            while (in.available() > 0) {
	                int i = in.read(buffer, 0, 1024);
	                if (i < 0) {
	                    break;
	                }
	                line = new String(buffer, 0, i);
	                LOGGER.info("read output: {}", line);
	            }

	            if(line.contains("logout")){
	                break;
	            }
	            
	            if(line.trim().endsWith("$")) {
	            	break;
	            }

	            if (channel.isClosed()){
	                break;
	            }
	            try {
	                Thread.sleep(1000);
	            } catch (Exception ee){}
	        }
	    }catch(Exception e){
	        LOGGER.error("Error while reading channel output: "+ e);
	    }

	}
}
