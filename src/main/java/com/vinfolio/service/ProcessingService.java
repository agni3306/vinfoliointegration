package com.vinfolio.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.json.simple.parser.ParseException;

import com.netsuite.webservices.platform_2016_2.NetSuitePortType;
import com.vinfolio.repository.daoImpl.ProcessingDAO;
import com.vinfolio.services.dtos.Wine;

public class ProcessingService {
	
	public void createTransactionEntry(){
		ProcessingDAO daoObj = new ProcessingDAO();
		daoObj.createEntryInTransactionTable();
	}

	public Date fetchLastSuccessfulExecution() {
		
		ProcessingDAO daoObj = new ProcessingDAO();
		return daoObj.fetchLastSuccessfulExecution();
	}

	public void executeSyncWineRecords(NetSuitePortType port) {
		TransportService transService = new TransportService();
		TransformationService transformationObj = new TransformationService();
		try {
			String result = transService.getWineRecordsfromVWA();
			try {
				 Wine[] wineRecords = transformationObj.jsonWineTransform(result);
				 transService.upsertWineList(port,wineRecords);
				 
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}
	

}
