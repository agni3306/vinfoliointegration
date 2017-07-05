package com.vinfolio.client;

import com.netsuite.webservices.platform_2016_2.NetSuitePortType;
import com.vinfolio.business.IConnection;
import com.vinfolio.businessImpl.Connection;
import com.vinfolio.service.ProcessingService;

public class Client {

	public static void main(String args[]){
		
		IConnection connection;
		
			connection = new Connection();
			NetSuitePortType port = connection.connectNetsuite();
			
			
			/*try {
				connection.connectVWA();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
	
		ProcessingService service = new ProcessingService();
			/*		service.createTransactionEntry();
			
			Date lastSuccess= service.fetchLastSuccessfulExecution();
			System.out.println("Last Successful run:"+lastSuccess);*/
			
		service.executeSyncWineRecords(port);
			
			/*TransportService transportservice = new TransportService();
			try {
				try {
					 Record[] wineRecords ={};
					transportservice.upsertWineList(port,wineRecords);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		*/
	}

}
