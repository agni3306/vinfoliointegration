package com.vinfolio.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import com.netsuite.webservices.platform.core_2016_2.CustomFieldList;
import com.netsuite.webservices.platform.core_2016_2.CustomFieldRef;
import com.netsuite.webservices.platform.core_2016_2.Record;
import com.netsuite.webservices.platform.core_2016_2.RecordRef;
import com.netsuite.webservices.platform.core_2016_2.StringCustomFieldRef;
import com.netsuite.webservices.platform.faults_2016_2.ExceededRecordCountFault;
import com.netsuite.webservices.platform.faults_2016_2.ExceededUsageLimitFault;
import com.netsuite.webservices.platform.faults_2016_2.InvalidSessionFault;
import com.netsuite.webservices.platform.faults_2016_2.UnexpectedErrorFault;
import com.netsuite.webservices.platform.messages_2016_2.WriteResponseList;
import com.netsuite.webservices.platform_2016_2.NetSuitePortType;
import com.netsuite.webservices.setup.customization_2016_2.CustomRecord;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vinfolio.services.dtos.Wine;

public class TransportService {

	public String getWineRecordsfromVWA() throws FileNotFoundException, IOException {
		
		String output="";
		
		Properties _properties = new Properties();
		_properties.load(new FileInputStream("nsvwaclient.properties"));

		String url = _properties.getProperty("vwa.url")+"integration/wines?barch_size=10";

		Client restClient = Client.create();

		WebResource webResource =  restClient.resource(url);

		 ClientResponse resp = (webResource).accept("application/json")
					.header("dsat", _properties.getProperty("vwa.dsat"))
                  .get(ClientResponse.class);

		if (resp.getStatus() != 200) {
			System.err.println("Unable to connect to the server");
		}else{
			System.out.println("VWA Connection successful");
			 output = resp.getEntity(String.class);
		     System.out.println("response: "+output);
		     
		}
		return output;	
		
	}
	
	public void upsertWineList(NetSuitePortType port, Wine[] wineRecords) throws RemoteException,ExceededUsageLimitFault,
    UnexpectedErrorFault,InvalidSessionFault,ExceededRecordCountFault
{
		
		// Create an array of Record objects to hold the customers
				Record[] records = new Record[wineRecords.length];

				// For each submitted nsKey, populate a customer object
				for (int i = 0; i < wineRecords.length; i++) {
					
					Wine rec = wineRecords[i];
					CustomRecord myCR = new CustomRecord();
					RecordRef rt = new RecordRef();
					rt.setInternalId("235");
					myCR.setRecType(rt);
					myCR.setName(rec.getName());

					// Set custom fields
					/*StringCustomFieldRef name = new StringCustomFieldRef();
					name.setScriptId("custrecord_name");
					name.setValue(rec.getName());
					*/
					StringCustomFieldRef externalId = new StringCustomFieldRef();
					externalId.setScriptId("custrecord_externalid");
					externalId.setValue(rec.getId());
					
					StringCustomFieldRef region = new StringCustomFieldRef();
					region.setScriptId("custrecord_region");
					region.setValue(rec.getRegion());
					
					StringCustomFieldRef village = new StringCustomFieldRef();
					village.setScriptId("custrecord_village");
					village.setValue(rec.getVillage());
					
					StringCustomFieldRef vineyard = new StringCustomFieldRef();
					vineyard.setScriptId("custrecord_vineyard");
					vineyard.setValue(rec.getVineyard());
					
					StringCustomFieldRef varietal = new StringCustomFieldRef();
					varietal.setScriptId("custrecord_varietal");
					varietal.setValue(rec.getVarietal());
					
					StringCustomFieldRef country = new StringCustomFieldRef();
					country.setScriptId("custrecord_country");
					country.setValue(rec.getCountry());
					
					StringCustomFieldRef producer = new StringCustomFieldRef();
					producer.setScriptId("custrecord_producer");
					producer.setValue(rec.getProducerName());
					
					StringCustomFieldRef color = new StringCustomFieldRef();
					color.setScriptId("custrecord_color");
					color.setValue(rec.getColor());
					
					StringCustomFieldRef category = new StringCustomFieldRef();
					category.setScriptId("custrecord_category");
					category.setValue(rec.getCategory());
					
					
					StringCustomFieldRef quality = new StringCustomFieldRef();
					quality.setScriptId("custrecord_quality");
					quality.setValue(rec.getQuality());
					
					StringCustomFieldRef type = new StringCustomFieldRef();
					type.setScriptId("custrecord_type");
					type.setValue(rec.getType());
					

					CustomFieldList myCFL = new CustomFieldList();
					myCFL.setCustomField(new CustomFieldRef[] { externalId,region,village,vineyard,varietal,country,producer,color,category,quality,type});
				
					myCR.setCustomFieldList(myCFL); // An already filled out CustomField
													
		myCR.setExternalId(rec.getId());

					records[i] = myCR;
				}

				 WriteResponseList wr = port.upsertList(records);
				   System.out.println(wr.toString());
		
	/*	Record[] records = new Record[1];
		
		
		Wine wineObj = new Wine();
		wineObj.setName("Java Wine");
		records[0] = wineObj;
		
		 // Invoke upsertList() operation to create or update customers
        WriteResponseList responseList = port.upsertList(records);

        // Process responses for all successful upserts
        WriteResponse[] responses = responseList.getWriteResponse();
		
		
		/* CustomRecord myCR = new CustomRecord();
		   RecordRef rt = new RecordRef();
		   rt.setName("Wine");
		  // rt.setId("customrecord_wine"); // This indicates a typeId of 3 and corresponds to the Authors CustomRecord type
		   myCR.setRecType(rt); 
		   myCR.setName("Ernest Hemmingway"); // This is what will show up in List-> CustomRecords->Authors under Name
		  // myCR.setCustomFieldList(myCFL); // An already filled out CustomField List
		   WriteResponse wr = port.add(myCR); */
		
	/*	CustomRecordRef myCRR = new CustomRecordRef(); 
		   myCRR.setInternalId("202"); // The key we got back 
		   myCRR.setTypeId("235"); // For Authors 
		   ReadResponse rr = port.get(myCRR);
		   Record rec = new CustomRecord();
		   rec = rr.getRecord();
		   rec.getTypeDesc();
		   System.out.println(rr.getRecord());*/
		
		/*CustomRecordSearch customRecordSearch = new CustomRecordSearch(); 
		RecordRef recordRef = new RecordRef(); 
		recordRef.setInternalId("235"); 
		CustomRecordSearchBasic basic = new CustomRecordSearchBasic();
		basic.setRecType(recordRef);
		customRecordSearch.setBasic(basic ); 
		SearchResult result = port.search(customRecordSearch);
			*/
		
		/*Record[] wineRecord = new Record[2];
		
		for(int i=0;i<2;i++){
		
		CustomRecord myCR = new CustomRecord();
		   RecordRef rt = new RecordRef();
		   rt.setInternalId("235");
		 // rt.setType("customrecord_wine"); // This indicates a typeId of 3 and corresponds to the Authors CustomRecord type
		   myCR.setRecType(rt); 
		   myCR.setName("hello world8"); // This is what will show up in List->
		   
		   //BooleanCustomFieldRef cf1 = new BooleanCustomFieldRef(true, "custcol_my_bool");
		   StringCustomFieldRef name = new StringCustomFieldRef();

		   name.setScriptId("custrecord_name");
		   name.setValue("hello world2");
		   
		   SelectCustomFieldRef selectCustomFieldRef = new SelectCustomFieldRef();

		   ListOrRecordRef custSelectValue = new ListOrRecordRef();
		   custSelectValue.setInternalId("2");
		  

		   selectCustomFieldRef.setValue(custSelectValue);
		   selectCustomFieldRef.setScriptId("custrecord_region");	   
		   CustomFieldList myCFL = new CustomFieldList();
		   
		   
		myCFL.setCustomField(new CustomFieldRef[]{name,selectCustomFieldRef});
		myCR.setCustomFieldList(myCFL ); // An already filled out CustomField List
*/		  
		   
		 
		
		
		   
		
		   
		   
}


}
