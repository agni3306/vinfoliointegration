package com.vinfolio.client;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.vinfolio.business.IConnection;
import com.vinfolio.businessImpl.Connection;

public class QuartzJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println("Job run at time: " + java.time.LocalTime.now());
		IConnection connection;
		
		connection = new Connection();
		connection.connectNetsuite();
		try {
			connection.connectVWA();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
