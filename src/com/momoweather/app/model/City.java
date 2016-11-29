package com.momoweather.app.model;

public class City {
	String cityName ;
	String cityCode ;
	int id ;
	int provinceId ;
	
	public int getId(){
		return id ;
	}
	
	public String getCityName(){
		return cityName ;
	}
	
	public String getCityCode(){
		return cityCode ;
	}
	
	public int getProvinceId(){
		return provinceId ;
	}
	
	public void setId(int id){
		this.id = id ;
	}
	
	public void setCityName(String cityName){
		this.cityName = cityName ;
	}

	public void setCityCode(String cityCode){
		this.cityCode = cityCode ;
	}
	
	public void setProvinceId(int provinceId){
		this.provinceId = provinceId ;
	}
}
