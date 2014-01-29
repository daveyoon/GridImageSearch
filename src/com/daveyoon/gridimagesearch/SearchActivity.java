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
import android.view.MenuItem;
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
	
	//Settings 
	private String imageSize=""; 
	private String imageType=""; 
	private String siteFilter="";
	private String colorFilter="";			
	private int totalResults=0;
	
	//Constants for Intent Views
	private final int SAVE_SETTINGS_CODE = 1; 	
	
	
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
		gvResults.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	        	totalResults = totalItemsCount; 
	            loadDataFromApi(); 
	        }
	    });
	}
	
	public void onSettingsView(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), EditSearchSettingsActivity.class);
		i.putExtra("imageSize", imageSize);
		i.putExtra("imageType", imageType);
		i.putExtra("siteFilter", siteFilter);
		i.putExtra("colorFilter", colorFilter);
		startActivityForResult(i,SAVE_SETTINGS_CODE);				
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
	
	//wrapper to initiate and call the data query
	public void onImageSearch(View v) { 
		loadDataFromApi();
	}
	
	//Build the data query from the view and search filter settings
	public void loadDataFromApi() { 
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_LONG).show();
		AsyncHttpClient client = new AsyncHttpClient();
		String baseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=" + query;
		if (not_blank(colorFilter)) {
			baseUrl += "&imgcolor=" + colorFilter;
		}
								
		if (not_blank(imageSize)) {
			baseUrl += "&imgsz=" + imageSize;
		}
		
		if (not_blank(imageType)) {
			baseUrl += "&imgtype=" + imageType;
		}
		
		if (not_blank(siteFilter)) { 
			baseUrl += "&as_sitesearch="+ siteFilter;
		}
		
		baseUrl = baseUrl + "&start=" + totalResults;
		Log.d("debug", baseUrl);		
		client.get(baseUrl, new JsonHttpResponseHandler() {			
			@Override 
			public void onSuccess(JSONObject response) { 
				JSONArray imageJsonResults = null; 
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results"); 
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					totalResults += imageResults.size();					
				} catch (JSONException e) {
					e.printStackTrace();					
				}
			}
			public void onFailure(java.lang.Throwable t, org.json.JSONObject e){
				t.printStackTrace();				
			}
		});				
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == SAVE_SETTINGS_CODE) {
         imageSize = data.getExtras().getString("imageSize");
         imageType = data.getExtras().getString("imageType");
         siteFilter = data.getExtras().getString("siteFilter");
         colorFilter = data.getExtras().getString("colorFilter");              
         Log.d("DEBUG", "color:" + colorFilter + "size:" + imageSize + "type:" + imageType);         
      }
    } 
    
    private boolean not_blank(String s) { 
    	return s != null && s.trim().length() > 0; 
    }
	
}
