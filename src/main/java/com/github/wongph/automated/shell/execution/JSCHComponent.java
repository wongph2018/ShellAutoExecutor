package com.github.wongph.automated.shell.execution;


import java.io.ByteArrayOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;



@Component
public class JSCHComponent {
	
	private static Logger LOGGER = LogManager.getLogger(JSCHComponent.class);
	
	private final JSch jsch;
	
	public JSCHComponent() {
		jsch = new JSch();
	}
	
	public Session getSession(String host, String username, String keyPath) throws JSchException {
		Session session = jsch.getSession(username, host, 22);
		jsch.addIdentity(keyPath);
		session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey");
        session.connect();
        return session;
	}
	
	public ChannelExec getChannelExec(Session session) throws JSchException {
		ChannelExec channel = null;
		channel = (ChannelExec) session.openChannel("exec");
        return channel;
	}
	
	public ChannelShell getChannelShell(Session session) throws JSchException {
		ChannelShell channel = null;
		channel = (ChannelShell) session.openChannel("shell");
        return channel;
	}
	
	public void execCommand(ChannelExec channel, String command) throws JSchException, InterruptedException {
		channel.setCommand(command);
		ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        channel.setOutputStream(responseStream);
        channel.connect();
        
        while (channel.isConnected()) {
            Thread.sleep(100);
        }
        
        String responseString = new String(responseStream.toByteArray());
        LOGGER.info(responseString);
	}
	
	public void disconnectSession(Session session) {
		if(session != null) {
			session.disconnect();
		}
	}
	
	public void disconnectChannel(Channel channel) {
		if(channel != null) {
			channel.disconnect();
		}
	}
}
