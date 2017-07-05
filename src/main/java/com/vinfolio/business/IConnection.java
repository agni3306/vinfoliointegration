package com.vinfolio.business;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.netsuite.webservices.platform_2016_2.NetSuitePortType;

public interface IConnection {
	public NetSuitePortType connectNetsuite();
	public void connectVWA() throws FileNotFoundException, IOException;
}
