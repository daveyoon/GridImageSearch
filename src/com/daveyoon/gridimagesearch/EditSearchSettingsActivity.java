package com.daveyoon.gridimagesearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class EditSearchSettingsActivity extends Activity {
	private ArrayList<String> searchSettings; //Array of strings to store the setting values
	private String imageSize; 
	private String colorFilter;
	private String imageType;
	private String siteFilter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_settings);
				 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	private void readSettings() { 
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "search_settings.txt");
    	try { 
    		searchSettings = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e){
    		searchSettings = new ArrayList<String>();
    	}
    }
    
    private void writeSettings() { 
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "search_settings.txt");
    	try { 
    		FileUtils.writeLines(todoFile, searchSettings);    	
    	} catch(IOException e) { 
    		e.printStackTrace();
    	}
    }
}
