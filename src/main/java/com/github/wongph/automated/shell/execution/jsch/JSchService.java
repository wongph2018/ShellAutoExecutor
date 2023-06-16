package com.github.wongph.automated.shell.execution.jsch;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.wongph.automated.shell.execution.utils.FormattingUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service
public class JSchService {
	private static Logger LOGGER = LogManager.getLogger(JSchService.class);
	
	private ThreadLocal<JSchObj> threadLocal = new ThreadLocal<>();
	
	@Autowired
	private JSchComponent jSchComponent;
	
	public String runJobFlow(JSchObj jSchObj) {
		Session session = null;
		ChannelShell channel = null;
		threadLocal.set(jSchObj);
		
		try {
			connectSSH();
			//run something
			
			session = jSchObj.getSession();
			channel = (ChannelShell) jSchObj.getChannel();

		} catch (Exception e) {
			LOGGER.error("Exception", e);
			return "restore and RUN JMS FAILED!!";
		}
		finally {
			jSchComponent.closeChannel(channel);
			jSchComponent.closeSession(session);
			threadLocal.remove();
		}
		
		return "restore and RUN JMS DONE";
	}
	
	public void connectSSH() {
		Session session = null;
		ChannelShell channel = null;
		JSchObj JSchObj = threadLocal.get();
		String host = JSchObj.getHost();
		String username = JSchObj.getUsername();
		String pwd = JSchObj.getPassword();
		
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			session = createSession(host, username, pwd);
			session.connect();
			channel = (ChannelShell) createChannel(session, "shell");
			channel.setOutputStream(outputStream);
			channel.connect();
			JSchObj.setSession(session);
			JSchObj.setChannel(channel);
			
		} catch (Exception e) {
			LOGGER.error("Exception in connectSSH", e);
		}

	}
	
	public Session createSession(String host, String username, String pwd) {
		Session session = null;
		try {
			session = jSchComponent.getSession(host, username, pwd);
			
		} catch (JSchException e) {
			LOGGER.error("JSchException", e);
		}
		return session;
	}
	
	public Channel createChannel(Session session, String type) {
		Channel channel = null;
		try {
			channel = session.openChannel(type);
		} catch (JSchException e) {
			LOGGER.error("JSchException", e);
		}
		return channel;
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
	
	private void sendCommands(Channel channel, String username, List<String> commands){

	    try{
	        PrintStream out = new PrintStream(channel.getOutputStream());
	        for(String command : commands){
	        	LOGGER.info("Send command: {}", command);
	            out.println(command);
	            out.flush();
	            jSchComponent.waitChannelOutputByUsername(channel, username);
	        }
	        
	    } catch(Exception e){
	        LOGGER.error("Error while sending commands: ", e);
	    }
	}
		
	private void sendSingleCommand(Channel channel, String command, String endWord, String keyword){

	    try{
	        PrintStream out = new PrintStream(channel.getOutputStream());
        	LOGGER.info("Send command: {}", command);
            out.println(command);
            out.flush();
            jSchComponent.waitChannelOutputKeyword(channel, endWord, keyword);
	        
	    } catch(Exception e){
	        LOGGER.error("Error while sending commands: ", e);
	    }
	}
	
	private String getSingleCommandOutput(Channel channel, String command, String endWord, String... keywords) {
	    try{
	        PrintStream out = new PrintStream(channel.getOutputStream());
        	LOGGER.info("Send command: {}", command);
            out.println(command);
            out.flush();
            return jSchComponent.getChannelOutputKeyword(channel, endWord, keywords);
	        
	    } catch(Exception e){
	        LOGGER.error("Error while sending commands: ", e);
	    }
	    return null;
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
	
	public void executeAndLog(Session session, Channel channel, PipedInputStream pout, String command, String searchKey, String sleepUnit, int sleepTime, String filename) {

		try {
	        PrintStream stream = new PrintStream(channel.getOutputStream());
			stream.println(command);
	        stream.flush();
	        //waitForPrompt(channel);
	        //jSchComponent.waitAndOutFile(outputStream, searchKey, sleepUnit, sleepTime, filename);
	        LOGGER.info("start reading");
	        BufferedReader consoleOutput = new BufferedReader(new InputStreamReader(pout));
	        boolean end = false;
	        while(!end)
	        {
	           consoleOutput.mark(32);
	           if (consoleOutput.read()==0x03) {
	        	   end = true; //End of Text
	        	   LOGGER.info("End Reading");
	           }
	           else
	           { 
	             consoleOutput.reset();
	             LOGGER.info(consoleOutput.readLine());
	             end = false;
	           }
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jSchComponent.closeChannel(channel);
            if (session != null) {
            	session.disconnect();
            }
		}
	}
}
