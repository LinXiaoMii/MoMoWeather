package com.momoweather.app.model;

public class Province {

	String provinceName ;
	String provinceCode ;
	int id ;
	public int getId(){
		return id ;
	}
	
	public String getProvinceName(){
		return provinceName ;
	}
	
	public String getProvinceCode(){
		return provinceCode ;
	}
	
	public void setId(int id){
		this.id = id ;
	}
	
	public void setProvinceName(String provinceName){
		this.provinceName = provinceName ;
	}

	public void setProvinceCode(String provinceCode){
		this.provinceCode = provinceCode ;
	}
}
