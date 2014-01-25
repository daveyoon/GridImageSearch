package com.daveyoon.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery; 
	GridView gvResults; 
	Button btnSearch; 
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultsArrayAdapter imageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews(); 
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults); 
		gvResults.setAdapter(imageAdapter); 
		gvResults.setOnItemClickListener(new OnItemClickListener(){
			@Override 
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long l){
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class); 
				ImageResult imageResult = imageResults.get(position); 
				i.putExtra("url", imageResult.getFullUrl());
				startActivity(i); 
			}
		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	private void setupViews() { 
		etQuery = (EditText)findViewById(R.id.etQuery);
		gvResults = (GridView)findViewById(R.id.gvResults);
		btnSearch = (Button)findViewById(R.id.btnSearch);
	}
	
	public void onImageSearch(View v) { 
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_LONG).show();
		AsyncHttpClient client = new AsyncHttpClient();
		String baseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";
		int pageNumber = 0;
		client.get(baseUrl + query + "&start=" + pageNumber, new JsonHttpResponseHandler() {
			@Override 
			public void onSuccess(JSONObject response) { 
				JSONArray imageJsonResults = null; 
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageResults.clear(); 
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
					
				}
			}
		});
	}
	
	public void onSettingsView(View v) {
		
	}
}
