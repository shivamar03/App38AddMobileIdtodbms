package com.example.app38addmobileidtodbms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Profile;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
TextView tv,tv2,tv3,tv4,tv5,tv6,tv7;
String mobileid,bname,userid;
AccountManager accountmanager;
LocationManager lm;
Location loc,loc2;
Geocoder geocoder;
String s1,s2,s3,s4,abc,h="";
JSONParser jsonparser;
String url,n,n1,n2,n3,n4,n5;
InputStream is;
Handler hd;
TelephonyManager tm;
BluetoothAdapter myDevice;
int update=0,i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView) findViewById(R.id.textView1);
        tv2=(TextView) findViewById(R.id.textView2);
        tv3=(TextView) findViewById(R.id.textView3);
        tv4=(TextView) findViewById(R.id.textView4);
        tv5=(TextView) findViewById(R.id.textView5);
        tv6=(TextView) findViewById(R.id.textView6);
        tv7=(TextView) findViewById(R.id.textView13);
        jsonparser=new JSONParser();
        tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        myDevice=BluetoothAdapter.getDefaultAdapter();
        lm=(LocationManager) getSystemService(LOCATION_SERVICE);
        url="http://192.168.1.106/webservices/mobile1.php";
        hd = new Handler();
        hd.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				my();
			}

			private void my() {
				// TODO Auto-generated method stub
				mobileid=tm.getDeviceId();
				tv.setText(mobileid);
				bname=myDevice.getName();
		        tv7.setText(bname);
		        String[] columnNames=new String[] {Profile.DISPLAY_NAME,Profile.PHOTO_ID};
		        Cursor c=MainActivity.this.getContentResolver().query(ContactsContract.Profile.CONTENT_URI,columnNames,null,null,null);
		        int count=c.getCount();
		        boolean b=c.moveToFirst();
		        int position=c.getPosition();
		        if(count==1 && position == 0){
		        	for(int j=0;j<columnNames.length; j++){
		        		userid=c.getString(0);
		        	}
		        }
		        c.close();
		        tv2.setText(userid);
		        loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		        loc2=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		        if(loc!=null){
		        	s1=Double.toString(loc.getLatitude());
		        	s2=Double.toString(loc.getLongitude());
		        }
		        else{
		        	s1="not exist";
		        	s2="not exist";
		        }
		        tv3.setText(s1);
		        tv4.setText(s2);
		        if(loc2!=null){
		        	s3=Double.toString(loc2.getLatitude());
		        	s4=Double.toString(loc2.getLongitude());
		        }
		        else{
		        	s3="not exist";
		        	s4="not exist";
		        }
		        tv5.setText(s3);
		        tv6.setText(s4);
		        new create().execute();
				hd.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						my();
					}
				}, 2000);
			}
		}, 2000);
    }
        
    class create extends AsyncTask<String, String, String>{

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				   List<NameValuePair> parms= new ArrayList<NameValuePair>();
			        parms.add(new BasicNameValuePair("bluetoothid",bname));
			        parms.add(new BasicNameValuePair("mobid",mobileid));
			        parms.add(new BasicNameValuePair("userid",userid));
			        parms.add(new BasicNameValuePair("latnetwork",s1));
			        parms.add(new BasicNameValuePair("longnetwork",s2));
			        parms.add(new BasicNameValuePair("latgps",s3));
			        parms.add(new BasicNameValuePair("longgps",s4));
			        JSONObject obj=jsonparser.makeHttpRequest(url,"POST",parms);
			        Log.d("CREATE REPONSE",obj.toString());
				return null;
			}
        }
}
