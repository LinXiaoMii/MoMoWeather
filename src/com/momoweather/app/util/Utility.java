package com.momoweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.momoweather.app.model.City;
import com.momoweather.app.model.Country;
import com.momoweather.app.model.MoMoWeatherDB;
import com.momoweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
		
		//解析和处理服务器返回的天气数据
		public static void handleWeatherResponse(Context context,String response){
			try{
				JSONObject jsonObject = new JSONObject(response);
				JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
				String cityName = weatherInfo.getString("city");
				String weatherCode = weatherInfo.getString("cityid");
				String temp1 = weatherInfo.getString("temp1");
				String temp2 = weatherInfo.getString("temp2");
				String weatherDesp = weatherInfo.getString("weather");
				String publishTime = weatherInfo.getString("ptime");
				saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		//将服务器返回的所有天气信息存储到SharedPreferences文件中
		private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
				String temp2, String weatherDesp, String publishTime) {
			// TODO Auto-generated method stub
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putBoolean("city_selected", true);
			editor.putString("city_name", cityName);
			editor.putString("weather_code", weatherCode);
			editor.putString("temp1", temp1);
			editor.putString("temp2", temp2);
			editor.putString("weather_desp", weatherDesp);
			editor.putString("current_data", sdf.format(new Date()));
			editor.putString("publish_time", publishTime);
			editor.commit();
		}
		
}




















