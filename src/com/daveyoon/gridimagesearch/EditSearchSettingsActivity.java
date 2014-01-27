package com.daveyoon.gridimagesearch;



import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class EditSearchSettingsActivity extends Activity {
	
	private String imageSize; 
	private String colorFilter;
	private String imageType;
	private String siteFilter; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_settings);
		setupView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public void setupView() {			
		//initialize the setting values from the intent
		imageSize = getIntent().getStringExtra("imageSize");				
		colorFilter = getIntent().getStringExtra("imageSize");
		imageType = getIntent().getStringExtra("imageSize");
		siteFilter = getIntent().getStringExtra("imageSize");		
		 
		//attach the spinners and form and set the values		
		Spinner spColorFilter = (Spinner)findViewById(R.id.spColorFilter);
		Spinner spImageSize = (Spinner)findViewById(R.id.spImageSize);
		Spinner spImageType= (Spinner)findViewById(R.id.spImageType);
		EditText etSiteFilter = (EditText)findViewById(R.id.etSiteFilter);
		
		etSiteFilter.setText(siteFilter);
		
		ArrayAdapter<CharSequence> imageColorsAdapter = ArrayAdapter.createFromResource(this,
				R.array.image_colors, android.R.layout.simple_spinner_item);
		imageColorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spColorFilter.setAdapter(imageColorsAdapter);		
		spColorFilter.setSelection(imageColorsAdapter.getPosition(colorFilter));
		
		ArrayAdapter<CharSequence> imageSizesAdapter = ArrayAdapter.createFromResource(this,
		        R.array.image_sizes, android.R.layout.simple_spinner_item);
		imageSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spImageSize.setAdapter(imageSizesAdapter);
		spImageSize.setSelection(imageSizesAdapter.getPosition(imageSize));
		
		ArrayAdapter<CharSequence> imageTypesAdapter = ArrayAdapter.createFromResource(this,
		        R.array.image_types, android.R.layout.simple_spinner_item);		
		imageTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spImageType.setAdapter(imageTypesAdapter);
		spImageType.setSelection(imageTypesAdapter.getPosition(imageType));
		
	}
}
