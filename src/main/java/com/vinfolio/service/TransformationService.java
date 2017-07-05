package com.vinfolio.service;

import java.util.HashMap;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vinfolio.repository.daoImpl.ProcessingDAO;
import com.vinfolio.services.dtos.Wine;

public class TransformationService {

	/**
	 * Method is written to load the static fields mapping stored in Database
	 * table Mapping.
	 * 
	 * @Return Map
	 * 
	 */
	public static HashMap<String, String> objMappingFields = new HashMap<String, String>();

	public static void loadStaticMappingData() {

		ProcessingDAO dao = new ProcessingDAO();
		objMappingFields = dao.buildStaticMap();

	}

	public Wine[] jsonWineTransform(String result) throws ParseException {

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

		// get an array from the JSON object
		JSONArray elements = (JSONArray) jsonObject.get("elements");

		// take the elements of the json array
		for (int i = 0; i < elements.size(); i++) {
			System.out.println("The " + i + " element of the array: " + elements.get(i));
		}
		Wine arr[] = new Wine[elements.size()];
		Iterator i = elements.iterator();
		int counter = 0;
		// take each value from the json array separately
		while (i.hasNext()) {
			Wine wineobj = new Wine();
			JSONObject innerObj = (JSONObject) i.next();
			// get a String from the JSON object
			String id = (innerObj.get("id") != null) ? innerObj.get("id").toString() : "";
			wineobj.setId(id);
			String name = (innerObj.get("name") != null) ? innerObj.get("name").toString() : "";
			wineobj.setName(name);
			String region = (innerObj.get("region") != null) ? innerObj.get("region").toString() : "";
			wineobj.setRegion(region);
			String village = (innerObj.get("village") != null) ? innerObj.get("village").toString() : "";
			wineobj.setVillage(village);
			String vineyard = (innerObj.get("vineyard") != null) ? innerObj.get("vineyard").toString() : "";
			wineobj.setVineyard(vineyard);
			String varietal = (innerObj.get("varietal") != null) ? innerObj.get("varietal").toString() : "";
			wineobj.setVarietal(varietal);
			String country = (innerObj.get("country") != null) ? innerObj.get("country").toString() : "";
			wineobj.setCountry(country);
			String producerName = (innerObj.get("producerName") != null) ? innerObj.get("producerName").toString() : "";
			wineobj.setProducerName(producerName);
			String color = (innerObj.get("color") != null) ? innerObj.get("color").toString() : "";
			wineobj.setColor(color);
			String category = (innerObj.get("category") != null) ? innerObj.get("category").toString() : "";
			wineobj.setCategory(category);
			String quality = (innerObj.get("quality") != null) ? innerObj.get("quality").toString() : "";
			wineobj.setQuality(quality);
			String type = (innerObj.get("type") != null) ? innerObj.get("type").toString() : "";
			wineobj.setType(type);
			arr[counter] = wineobj;
			counter++;
		}

		return arr;
		// objMappingFields.

	}

}
