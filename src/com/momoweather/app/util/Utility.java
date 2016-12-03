package com.momoweather.app.util;

import com.momoweather.app.model.City;
import com.momoweather.app.model.Country;
import com.momoweather.app.model.MoMoWeatherDB;
import com.momoweather.app.model.Province;

import android.text.TextUtils;

//解析和处理服务器返回的数据
public class Utility {

		// TODO Auto-generated constructor stub
		//解析和处理服务器返回的省级数据
		public synchronized static boolean handleProvincesResponse(MoMoWeatherDB momoWeatherDB,String response){
			if(!TextUtils.isEmpty(response)){
				String allProvinces[] = response.split(",");
				if(allProvinces != null && allProvinces.length>0 ) {
					for(String p:allProvinces){
						String array[] = p.split("\\|");
						Province province = new Province();
						province.setProvinceCode(array[0]);
						province.setProvinceName(array[1]);
						momoWeatherDB.saveProvince(province);
					}
					return true ;
				}
			}
			return false ;
		}
		//解析和处理服务器返回的市级数据
		public synchronized static boolean handleCitiesResponse(MoMoWeatherDB momoWeatherDB,String response,int provinceID){
			if(!TextUtils.isEmpty(response)){
				String allCities[] = response.split(",");
				if(allCities != null && allCities.length>0 ) {
					for(String p:allCities){
						String array[] = p.split("\\|");
						City city = new City();
						city.setCityCode(array[0]);
						city.setCityName(array[1]);
						city.setProvinceId(provinceID);
						momoWeatherDB.saveCity(city);
					}
					return true ;
				}
			}
			return false ;
		}
			//解析和处理服务器返回的县级数据
		public synchronized static boolean handleCountriesResponse(MoMoWeatherDB momoWeatherDB,String response,int cityID){
			if(!TextUtils.isEmpty(response)){
				String allCountries[] = response.split(",");
				if(allCountries != null && allCountries.length>0 ) {
					for(String p:allCountries){
						String array[] = p.split("\\|");
						Country country = new Country();
						country.setCountryCode(array[0]);
						country.setCountryName(array[1]);
						country.setCityId(cityID);
						momoWeatherDB.saveCountry(country);
					}
					return true ;
				}
			}
		return false ;
	}
}
