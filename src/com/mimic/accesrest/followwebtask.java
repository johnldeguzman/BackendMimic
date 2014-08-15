package com.mimic.accesrest;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class followwebtask extends AsyncTask<String, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private followinglist activity;
	private static final String debugtag = "profileBackgroundtask";
	private String user, label;
	public followwebtask (followinglist activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("username", "madfresco");
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		progdialog = ProgressDialog.show(this.activity, "Search", "Looking for your mimics", true, false);
	}
	
	@Override
	protected String doInBackground(String... q) {
		String query = q[0];
		String label = q[1];
		this.label = label;
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/follows/?"+this.label+"="+query+"&format=json", user);
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<searchdata> searchdata = new ArrayList<searchdata>(); 
		
		progdialog.dismiss();
		
		
		if(result.length() == 0){
		
		}
		
		Log.d("label", label+"");
		if (label.trim().equals("following")){
		try{
			Log.d("working?", "following");
			JSONObject x  = new JSONObject(result);
			JSONArray respobj = x.getJSONArray("results");
			for (int i=0; i<respobj.length(); i++){
			JSONObject returnval = respobj.getJSONObject(i);
			JSONObject follower = returnval.getJSONObject("follower");
			int profid = follower.getInt("profileid");
			String username = follower.getString("username");
			String dp = follower.getString("profilepictureurl");
			String profileurl = follower.getString("url");
			Boolean follows = false;
			searchdata.add(new searchdata(username, dp, follows, profid, profileurl));
			
			}
			
				
		
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		} else if (label.trim().equals("follower")){
			Log.d("label", "follower right?");
			try{
				Log.d("working?", "follower");
				JSONObject x  = new JSONObject(result);
				JSONArray respobj = x.getJSONArray("results");
				for (int i=0; i<respobj.length(); i++){
				JSONObject returnval = respobj.getJSONObject(i);
				JSONObject following = returnval.getJSONObject("following"); 
				int profid = following.getInt("profileid");
				String username = following.getString("username");
				String dp = following.getString("profilepictureurl");
				String profileurl = following.getString("url");
				Boolean follows = false;
				searchdata.add(new searchdata(username, dp, follows, profid, profileurl));
				Log.d("profid", profid+"");
				}
				
					
			
				
				} catch (JSONException e){
				
					e.printStackTrace();
				}
		}
		Log.d("seetUsers", "follow");
		this.activity.setUsers(searchdata);
		}
		
		
	}
	


