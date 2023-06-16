package com.github.wongph.automated.shell.execution.jsch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Component
public class JSchComponent {
	private static Logger LOGGER = LogManager.getLogger(JSchComponent.class);
	
	private final JSch jsch;
	
	public JSchComponent() {
		jsch = new JSch();
	}
	
	public Session getSession(String host, String username, String pwd) throws JSchException {
		Session session = jsch.getSession(username, host, 22);
		session.setPassword(pwd);
    	session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
    	session.setConfig("StrictHostKeyChecking", "no"); // disable check for RSA key
        return session;
	}
	
	public Channel getChannel(Session session, String type) throws JSchException {
		Channel channel = null;
		channel = session.openChannel(type);
		return channel;
	}
	
    public void closeChannel(Channel channel) {
        try {
        	if (channel != null) {
                channel.disconnect();
        	}
        } catch (Exception ignored) {
        	LOGGER.error("closeChannel Excpetion Ignore", ignored);
        }
    }
    
    public void closeSession(Session session) {
        try {
        	if (session != null) {
        		session.disconnect();
        	}
        } catch (Exception ignored) {
        	LOGGER.error("closeSession Excpetion Ignore", ignored);
        }
    }
    
    
    public void waitAndOutFile(ByteArrayOutputStream outputStream, String searchKey, String sleepUnit, int sleepTime, String filename) {
    	FileOutputStream fos = null;
    	LOGGER.info("start waiting output");
    	try {
    		 
	        while (true) {
	        	
	        	String output = outputStream.toString();
	        	LOGGER.info("output: {}", output);
	        	if(output != null) {
		        	if(output.indexOf(searchKey) > 0) {
		        		LOGGER.info("search key found");
		        		break;
		        	}
		        	if(output.contains("$") || output.contains("#")) {
		        		LOGGER.info("Ends with $");
		        		break;
		        	}
	        	}
	        	
	        	LOGGER.info("sleep for {} {}", sleepTime, sleepUnit);
	        	
	        	if (sleepUnit != null && "MINUTES".equals(sleepUnit)) {
	                TimeUnit.MINUTES.sleep(sleepTime);
	        	} else {
	        		TimeUnit.SECONDS.sleep(sleepTime);
	        	}
	        	
	        	LOGGER.info("wake, check searchKey: {}", searchKey);
	        }
	        File f = new File("D:\\" + filename);
	        fos = new FileOutputStream(f);
	        outputStream.writeTo(fos);
	        outputStream.reset();
	        
    	} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    
    
	public void waitChannelOutputByUsername(Channel channel, String username) {
        String endString = username + ">";
        LOGGER.info("For {}, Reading output from shell...", username);
        waitChannelOutputKeyword(channel, endString, null);
	}
	
	public void waitChannelOutputKeyword(Channel channel, String endWord, String keyword) {

	    byte[] buffer = new byte[1024];

	    try{
	        InputStream in = channel.getInputStream();
	        String line = "";
	        LOGGER.debug("Reading output from shell, check output end with: {} or contains: {}", endWord, keyword);
	        while (true){
	            while (in.available() > 0) {
	                int i = in.read(buffer, 0, 1024);
	                if (i < 0) {
	                    break;
	                }
	                line = new String(buffer, 0, i);
	                System.out.print(line);
	            }
	            
	            if(line.contains("logout")){
	                break;
	            }
	            LOGGER.info("line: {}", line);
	            if(line.trim().endsWith("$") || StringUtils.endsWith(line.trim(), endWord)) {
	            	break;
	            }
	            
	            if(StringUtils.contains(line, keyword)) {
	            	LOGGER.info("keyword found");
	            	break;
	            }

	            if (channel.isClosed()){
	                break;
	            }
	            try {
	                Thread.sleep(1000);
	            } catch (Exception ee){}
	        }
	        LOGGER.debug("Finish reading output from shell, wait for end word {}", endWord);
	        
	    } catch(Exception e) {
	        LOGGER.error("Error while reading channel output: ", e);
	    }
	}
	
	
	public String getChannelOutputKeyword(Channel channel, String endWord, String... keyword) {
		StringBuffer sb = new StringBuffer();
	    byte[] buffer = new byte[1024];

	    try{
	        InputStream in = channel.getInputStream();
	        String line = "";
	        LOGGER.debug("Reading output from shell, check output end with: {} or contains: {}", endWord, keyword);
	        while (true){
	            while (in.available() > 0) {
	                int i = in.read(buffer, 0, 1024);
	                if (i < 0) {
	                    break;
	                }
	                line = new String(buffer, 0, i);
	                System.out.print(line);
	                sb.append(line);
	            }
	            LOGGER.info("line: {}", line);
	            if(line.contains("logout")){
	                break;
	            }
	            
	            if(line.trim().endsWith("$") || StringUtils.endsWith(RegExUtils.removeAll(line, "\\s+"), endWord)) {
	            	break;
	            }
	            
	            if(StringUtils.containsAny(line, keyword)) {
	            	LOGGER.debug("keyword found");
	            	break;
	            }

	            if (channel.isClosed()){
	                break;
	            }
	            try {
	                Thread.sleep(2000);
	            } catch (Exception ee){}
	        }
	        LOGGER.debug("Finish reading output from shell, check output end with: {} or contains: {}", endWord, keyword);
	        
	    } catch(Exception e) {
	        LOGGER.error("Error while reading channel output: ", e);
	    }
	    System.out.println();
	    return sb.toString();
	}
	
	

}
