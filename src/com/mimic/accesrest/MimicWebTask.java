package com.mimic.accesrest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class MimicWebTask extends AsyncTask<Integer, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private MainActivity activity;
	private static final String debugtag = "Backgroundtask";
	private String user, password;
	
	public MimicWebTask(MainActivity activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("username", "madfresco");
		password = prefs.getString("password", "genocide212");
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		
	}
	
	@Override
	protected String doInBackground(Integer... s) {
		int k = s[0];
		try{
			Log.d(debugtag, "Background");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/posts/?page="+k+"&format=json", user, password);
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<MimicData> mimicdata= new ArrayList<MimicData>(); 

		
		
		if(result.length() == 0){	
			this.activity.alert("Nada denada");
		}
		
		try{
			JSONObject s= new JSONObject(result);
			
			JSONArray array = s.getJSONArray("results");
			String next = s.getString("next");
			for (int i=0; i<array.length(); i++){
				JSONObject post= array.getJSONObject(i);
				JSONObject user = post.getJSONObject("user");
				String username = user.getString("username");
				String dpurl = user.getString("profilepictureurl");
				String profileurl = user.getString("url");
				String url= post.getString("url");
				String posturl = post.getString("posturls");
				String description = post.getString("description");
				boolean owner = post.getBoolean("own");
//				description = "Woah, nice app. I'm really liking this. Really cool!";
				if (description == "null"){
					description = "";
				}
				int comments = post.getInt("commentscount");
				int likes= post.getInt("likescount");
				int postid = post.getInt("id");
				Boolean likesbool = post.getBoolean("favourites");
				String timestamp = post.getString("time");
				
				if (next == "null"){
					Log.d("ending", "end is here");
					mimicdata.add(new MimicData(username, dpurl, url, postid, likes, comments, posturl, description, likesbool, timestamp, profileurl, false, owner));
				}else{
				mimicdata.add(new MimicData(username, dpurl, url, postid, likes, comments, posturl, description, likesbool, timestamp, profileurl, true, owner));
				}
				
				}
			
			
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
			
		
		try{
			if ((this.activity.prog != null)&&   
					this.activity.prog.isShowing()){
				Log.d("wha", this.activity.prog.isShowing()+ " ");
				this.activity.mPullToRefreshLayout.setRefreshComplete();
				this.activity.prog.dismiss();
		}
		}catch (Exception e){
			
		}
	 
			 this.activity.setUsers(mimicdata);
			 this.activity.prog = null;
			 Log.d("here?", "yeap");
		 }
		 }
		
	
	


