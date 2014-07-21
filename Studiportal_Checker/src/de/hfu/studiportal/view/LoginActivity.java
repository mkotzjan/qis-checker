package de.hfu.studiportal.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import de.hfu.funfpunktnull.R;
import de.hfu.studiportal.network.LoginVerifactionTask;
import de.hfu.studiportal.network.RefreshTask;

public class LoginActivity extends DialogHostActivity implements DialogHost {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//inflate Layout
		this.setContentView(R.layout.activity_login);
		
		//Get SharedPrefs
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		//Set editTextPassword font and content
		EditText password = (EditText) findViewById(R.id.editTextPassword);
		password.setTypeface(Typeface.DEFAULT);
		password.setText(sp.getString(getResources().getString(R.string.preference_password), ""));
		password.setTransformationMethod(new PasswordTransformationMethod());
		
		//Set editTextUser content
		EditText user = (EditText) findViewById(R.id.editTextUser);
		user.setText(sp.getString(getResources().getString(R.string.preference_user), ""));
				
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		this.dismiss();
		
	}
	
	public void cancel(View v) {
		this.finish();
		
	}
	
	public void checkEnteredData(View v) {
		if(this.getEnteredPassword().length() == 0 || this.getEnteredUsername().length() == 0) {
			this.showDialog(getResources().getString(R.string.text_error), getResources().getString(R.string.text_enter_user_password));
			return;
			
		}
			
		RefreshTask rt = new LoginVerifactionTask(this, 
				this.getEnteredUsername(),
				this.getEnteredPassword());
		rt.execute();
	}
	
	private String getEnteredPassword() {
		return ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
	}
	
	private String getEnteredUsername() {
		return ((EditText) findViewById(R.id.editTextUser)).getText().toString();
	}
	
	public void saveEnteredData() {
		Editor sp = PreferenceManager.getDefaultSharedPreferences(this).edit();
		sp.putString(getResources().getString(R.string.preference_password), this.getEnteredPassword());
		sp.putString(getResources().getString(R.string.preference_user), this.getEnteredUsername());
		sp.apply();
		
		//Start the new activity if available
		Bundle extras = (Bundle) getIntent().getExtras();
		
		if(extras != null) {
			Intent startOnSuccess = (Intent) extras.get(getResources().getString(R.string.extra_start_on_success));
			if(startOnSuccess != null)
				this.startActivity(startOnSuccess);
		}
		

		//finish this
		this.finish();
	}
}