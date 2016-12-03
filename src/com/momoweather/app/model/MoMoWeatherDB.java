package com.momoweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.momoweather.app.db.MoMoWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//����������װһЩ���ݿ�������ǲ������ף������ظ�д����
public class MoMoWeatherDB {

	public static final String DB_NAME = "momo_weather" ;
	public static final int DB_VERSION = 1;
	private static MoMoWeatherDB momoWeatherDB ;
	private SQLiteDatabase db ;
	
	//���캯��
	MoMoWeatherDB(Context context){
		MoMoWeatherOpenHelper momoWeatherOpenHelper = new MoMoWeatherOpenHelper(context,DB_NAME, null, DB_VERSION) ;
		db = momoWeatherOpenHelper.getWritableDatabase();
	}
	
	//����getInstance()��ȡʵ��������Synchronized��֤ȫ�ַ�Χֻ��һ����ʵ����������һ������඼Ҫ����������ֻ����һ����ʵ����
	public synchronized static MoMoWeatherDB getInstance(Context context){
		if(momoWeatherDB == null){
			momoWeatherDB = new MoMoWeatherDB(context);
		}
		return momoWeatherDB ;
	}
	
	
	//��Provinceʵ�����浽���ݿ�
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues() ;
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	//�����ݿ��ȡȫ��ʡ����Ϣ
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
	
	//��Cityʵ�����浽���ݿ�
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues() ;
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
			}
	}
		
	//�����ݿ��ȡȫ��������Ϣ
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
	
	//��Countryʵ�����浽���ݿ�
	public void saveCountry(Country country){
		if(country != null){
			ContentValues values = new ContentValues() ;
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
			}
	}
		
	//�����ݿ��ȡȫ����������Ϣ
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
