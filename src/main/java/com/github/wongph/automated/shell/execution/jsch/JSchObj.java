package com.github.wongph.automated.shell.execution.jsch;

import java.util.Collections;
import java.util.List;

import com.github.wongph.automated.shell.execution.json.JsonObj;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

public class JSchObj {
	
	private Session session;
	private Channel channel;
	private String host;
	private String username;
	private String password;
	private String schema;
	private List<String> runCommands;
	private JsonObj jsonObj;
	
	
	public JSchObj() {}

	private JSchObj(JSchObjBuilder jSchObjBuilder) {
		this.session = jSchObjBuilder.session;
		this.channel = jSchObjBuilder.channel;
		this.host = jSchObjBuilder.host;
		this.username = jSchObjBuilder.username;
		this.password = jSchObjBuilder.password;
		this.schema = jSchObjBuilder.schema;
		this.runCommands = jSchObjBuilder.runCommands;
		this.jsonObj = jSchObjBuilder.jsonObj;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public List<String> getRunCommands() {
		return runCommands;
	}

	public void setRunCommands(List<String> runCommands) {
		this.runCommands = Collections.unmodifiableList(runCommands);
	}

	public JsonObj getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(JsonObj jsonObj) {
		this.jsonObj = jsonObj;
	}
	
	
	public static class JSchObjBuilder {
		private Session session;
		private Channel channel;
		private String host;
		private String username;
		private String password;
		private String schema;
		private List<String> runCommands;
		private JsonObj jsonObj;
		
		public JSchObjBuilder session(Session session) {
			this.session = session;
			return this;
		}
		public JSchObjBuilder channel(Channel channel) {
			this.channel = channel;
			return this;
		}
		public JSchObjBuilder host(String host) {
			this.host = host;
			return this;
		}
		public JSchObjBuilder username(String username) {
			this.username = username;
			return this;
		}
		public JSchObjBuilder password(String password) {
			this.password = password;
			return this;
		}
		public JSchObjBuilder schema(String schema) {
			this.schema = schema;
			return this;
		}
		public JSchObjBuilder runCommands(List<String> runCommands) {
			this.runCommands = Collections.unmodifiableList(runCommands);
			return this;
		}
		public JSchObjBuilder jsonObj(JsonObj jsonObj) {
			this.jsonObj = jsonObj;
			return this;
		}
		public JSchObj build() {
			return new JSchObj(this);
		}
		
	}
}
