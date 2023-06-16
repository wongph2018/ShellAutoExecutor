package com.github.wongph.automated.shell.execution.json;

import java.util.List;

public class JsonObj {
	private String dbSchema;
	private String beforeDBDump;
	private String afterDBDump;
	private List<BatchJobObj> BatchJobs;
	
	public String getDbSchema() {
		return dbSchema;
	}
	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}
	public String getBeforeDBDump() {
		return beforeDBDump;
	}
	public void setBeforeDBDump(String beforeDBDump) {
		this.beforeDBDump = beforeDBDump;
	}
	public String getAfterDBDump() {
		return afterDBDump;
	}
	public void setAfterDBDump(String afterDBDump) {
		this.afterDBDump = afterDBDump;
	}
	public List<BatchJobObj> getBatchJobs() {
		return BatchJobs;
	}
	public void setBatchJobs(List<BatchJobObj> batchJobs) {
		this.BatchJobs = batchJobs;
	}
	
}
