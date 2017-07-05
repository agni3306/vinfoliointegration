package com.vinfolio.services.dtos;



/**
 * @author richa.gupta
 *
 */
public class Wine{
   
	private String id;
    private String name;
    private String region;
    private String village;
    private String vineyard;
    private String varietal;
    private String country;
    private String producerName;
    private String color;
    private String category;
    private String quality;
    private String type;
    
	public Wine(String id, String name, String region, String village, String vineyard, String varietal, String country,
			String producerName, String color, String category, String quality, String type) {
		super();
		this.id = id;
		this.name = name;
		this.region = region;
		this.village = village;
		this.vineyard = vineyard;
		this.varietal = varietal;
		this.country = country;
		this.producerName = producerName;
		this.color = color;
		this.category = category;
		this.quality = quality;
		this.type = type;
	}
	public Wine() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getVineyard() {
		return vineyard;
	}
	public void setVineyard(String vineyard) {
		this.vineyard = vineyard;
	}
	public String getVarietal() {
		return varietal;
	}
	public void setVarietal(String varietal) {
		this.varietal = varietal;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProducerName() {
		return producerName;
	}
	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
   


}
