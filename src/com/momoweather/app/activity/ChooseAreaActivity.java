package com.momoweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.momoweather.R;
import com.momoweather.app.model.City;
import com.momoweather.app.model.Country;
import com.momoweather.app.model.MoMoWeatherDB;
import com.momoweather.app.model.Province;
import com.momoweather.app.util.HttpCallbackListener;
import com.momoweather.app.util.HttpUtil;
import com.momoweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class ChooseAreaActivity extends Activity{

	public static final int LEVEL_PROVINCE = 0 ;
	public static final int LEVEL_CITY = 1 ;
	public static final int LEVEL_COUNTRY = 2;
	public static int currentLevel ;
	
	private ProgressDialog dialog ;
	private ListView contentList ;
	private TextView titleText ;
	
	private ArrayAdapter<String> adapter ;
	private MoMoWeatherDB momoWeatherDB ;
	private List<String> dataList = new ArrayList<String>();
	private List<Province> provincesList ;
	private List<City> citiesList ;
	private List<Country> countriesList ;
	
	//选中的省市
	private Province selectedProvince ;
	private City selectedCity ;
	
	private boolean isFromWeatherActivity ;
	
	public ChooseAreaActivity() {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChooseAreaActivity.this);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		if(prefs.getBoolean("city_selected" , false) && !isFromWeatherActivity){
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return ;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		contentList = (ListView)findViewById(R.id.content_list);
		titleText = (TextView)findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		contentList.setAdapter(adapter);
		momoWeatherDB = MoMoWeatherDB.getInstance(this);//类中定义的方法
		contentList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provincesList.get(position);
					queryCities();
				}else if(currentLevel == LEVEL_CITY){
					selectedCity = citiesList.get(position);
					queryCountries();
				}else if(currentLevel == LEVEL_COUNTRY){
					String countryCode = countriesList.get(position).getCountryCode();
					Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("country_code",countryCode);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();//加载省级数据（注意位置）
	}

	protected void queryProvinces() {
		// TODO Auto-generated method stub
		provincesList = momoWeatherDB.loadProvinces() ;
		if(provincesList.size()>0){
			dataList.clear();
			for(Province province : provincesList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			contentList.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE ;
		}else{
			queryFromServer(null,"province");
		}
	}

	protected void queryCities() {
		// TODO Auto-generated method stub
		citiesList = momoWeatherDB.loadCities(selectedProvince.getId()) ;
		if(citiesList.size()>0){
			dataList.clear();
			for(City city : citiesList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			contentList.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY ;
		}else {
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}

	protected void queryCountries() {
		// TODO Auto-generated method stub
		countriesList = momoWeatherDB.loadCounties(selectedCity.getId()) ;
		if(countriesList.size()>0){
			dataList.clear();
			for(Country country : countriesList){
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			contentList.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTRY ;
		}else {
			queryFromServer(selectedCity.getCityCode(),"country") ;
		}
		
	}


	public void queryFromServer(final String code, final String type) {
		// TODO Auto-generated method stub
		String address ;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address = "http://www.weather.com.cn/data/list3/city.xml" ;
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false ;
				if("province".equals(type)){
					result = Utility.handleProvincesResponse(momoWeatherDB, response);
				}else if("city".equals(type)){
					result = Utility.handleCitiesResponse(momoWeatherDB, response, selectedProvince.getId());
				}else if("country".equals(type)){
					result = Utility.handleCountriesResponse(momoWeatherDB, response, selectedCity.getId());
				}
				if(result){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("country".equals(type)){
								queryCountries();
							}
						}

					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
					}
					
				});
			}
			
		});
	}

	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if(dialog == null){
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在加载···");
			dialog.setCanceledOnTouchOutside(false);
		}
		dialog.show();	
	}
	

	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if(dialog != null){
			dialog.dismiss();
		}
	}
	
	//设置back键的功能
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(currentLevel == LEVEL_COUNTRY){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			if(isFromWeatherActivity){
				Intent intent = new Intent(this,WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
}
