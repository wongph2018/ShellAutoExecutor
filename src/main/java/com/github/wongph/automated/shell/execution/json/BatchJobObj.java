package com.github.wongph.automated.shell.execution.json;

import java.util.List;
import java.util.Map;

public class BatchJobObj {
	private String jobName;
	private String runNumber;
	private String handShakeInJob;
	private String systemDate;
	private Map<String, String> jobParams;
	private List<InputFilesObj> inputFilesList;
	private List<MiscFilesObj> miscFiles;
	private List<SpecificDBRecordObj> SpecificDBRecords;
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getRunNumber() {
		return runNumber;
	}
	public void setRunNumber(String runNumber) {
		this.runNumber = runNumber;
	}
	public String getHandShakeInJob() {
		return handShakeInJob;
	}
	public void setHandShakeInJob(String handShakeInJob) {
		this.handShakeInJob = handShakeInJob;
	}
	public String getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}
	public Map<String, String> getJobParams() {
		return jobParams;
	}
	public void setJobParams(Map<String, String> jobParams) {
		this.jobParams = jobParams;
	}
	public List<InputFilesObj> getInputFilesList() {
		return inputFilesList;
	}
	public void setInputFilesList(List<InputFilesObj> inputFilesList) {
		this.inputFilesList = inputFilesList;
	}
	public List<MiscFilesObj> getMiscFiles() {
		return miscFiles;
	}
	public void setMiscFiles(List<MiscFilesObj> miscFiles) {
		this.miscFiles = miscFiles;
	}
	public List<SpecificDBRecordObj> getSpecificDBRecords() {
		return SpecificDBRecords;
	}
	public void setSpecificDBRecords(List<SpecificDBRecordObj> specificDBRecords) {
		SpecificDBRecords = specificDBRecords;
	}
	
	
}
