package com.momoweather.app.model;

public class Country {
	
	private String countryName ;
	private String countryCode ;
	private int id ;
	private int cityId ;
	public int getId(){
		return id ;
	}
	
	public String getCountryName(){
		return countryName ;
	}
	
	public String getCountryCode(){
		return countryCode ;
	}
	
	public int getCityId(){
		return cityId ;
	}
	
	public void setId(int id){
		this.id = id ;
	}
	
	public void setCountryName(String countryName){
		this.countryName = countryName ;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode ;
	}
	
	public void setCityId(int cityId){
		this.cityId = cityId ;
	}
}
