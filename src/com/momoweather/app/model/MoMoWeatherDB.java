package com.momoweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.momoweather.app.db.MoMoWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//该类用来封装一些数据库操作，是操作简易，不用重复写代码
public class MoMoWeatherDB {

	public static final String DB_NAME = "momo_weather" ;
	public static final int DB_VERSION = 1;
	private static MoMoWeatherDB momoWeatherDB ;
	private SQLiteDatabase db ;
	
	//构造函数
	MoMoWeatherDB(Context context){
		MoMoWeatherOpenHelper momoWeatherOpenHelper = new MoMoWeatherOpenHelper(context,DB_NAME, null, DB_VERSION) ;
		db = momoWeatherOpenHelper.getWritableDatabase();
	}
	
	//利用getInstance()获取实例，利用Synchronized保证全局范围只有一个该实例（锁）（一般服务类都要求整个程序只存在一个该实例）
	public synchronized static MoMoWeatherDB getInstance(Context context){
		if(momoWeatherDB == null){
			momoWeatherDB = new MoMoWeatherDB(context);
		}
		return momoWeatherDB ;
	}
	
	
	//将Province实例保存到数据库
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues() ;
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	//从数据库读取全国省份信息
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null) ;
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list ;
	}
	
	//将City实例保存到数据库
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues() ;
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
			}
	}
		
	//从数据库读取全国城市信息
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null) ;
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list ;
	}
	
	//将Country实例保存到数据库
	public void saveCountry(Country country){
		if(country != null){
			ContentValues values = new ContentValues() ;
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
			}
	}
		
	//从数据库读取全市所有县信息
	public List<Country> loadCounties(int cityId){
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null) ;
		if(cursor.moveToFirst()){
			do{
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				list.add(country);
			}while(cursor.moveToNext());
		}
		return list ;
	}
	
}
