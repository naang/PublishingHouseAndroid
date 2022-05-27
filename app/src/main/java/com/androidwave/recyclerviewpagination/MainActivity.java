package com.androidwave.recyclerviewpagination;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidwave.recyclerviewpagination.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	public static Button btnLogin;
	ImageView logo;
	EditText edtUserName, edtPassword;
	TextView txtInfo;
	public static RequestQueue queue;
	public static String currentUser;
	public static String currentUserId;
	private String password;
	private static int clr;
	public static String token;
	public  static  boolean isFirstLoad = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isFirstLoad = true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionBarSetup();
		queue = Volley.newRequestQueue(getApplicationContext());
		logo = (ImageView) findViewById(R.id.logo);
		btnLogin = (Button) findViewById(R.id.btnlogin);
		edtUserName= (EditText) findViewById(R.id.username);
		edtPassword = (EditText) findViewById(R.id.password);
		txtInfo= (TextView) findViewById(R.id.infoTxtView);
		setBtnEnabled();
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentUser = edtUserName.getText().toString();
				password = edtPassword.getText().toString();
				if(currentUser.equals("")|| password.equals(""))
				{
					txtInfo.setText("შეავსეთ ველი!");
				}
				else
				{
					setBtnDisabled();
					final String url = "http://92.241.77.162:1188/Home/LoginWithDevice?username="+currentUser+"&password="+password;
					//queue.getCache().remove(url);
					//queue.getCache().clear();
					StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
							new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject jsonResult = null;
								JSONObject myJsonObject = new JSONObject(response);
								jsonResult = myJsonObject.getJSONObject("jsonResponse");

								String sauccsses = jsonResult.getString("IsSuccess");
								String message = jsonResult.getString("Message");
								setBtnDisabled();
								if(sauccsses == "true")
								{
									currentUser = jsonResult.getString("UserName");
									currentUserId = jsonResult.getString("UserId");
									Intent intent = new Intent("com.androidwave.recyclerviewpagination.Main3Activity");
									startActivity(intent);
								}
								else
								{
									txtInfo.setText(message);
									setBtnEnabled();
								}
								setBtnEnabled();
							} catch (JSONException e) {
								setBtnEnabled();
								Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
								e.printStackTrace();
							}
						}
					},
							new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {

									Log.e(error.getMessage(), "login Method");
									if (error instanceof NoConnectionError) {
										setBtnEnabled();
										Toast.makeText(getApplicationContext(), "NoConnection Error", Toast.LENGTH_LONG).show();
									} else if (error instanceof AuthFailureError) {
										setBtnEnabled();
										Toast.makeText(getApplicationContext(), "AuthFailure Error", Toast.LENGTH_LONG).show();
									} else if (error instanceof ServerError) {
										setBtnEnabled();
										Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
									} else if (error instanceof NetworkError) {
										setBtnEnabled();
										Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
									}
								}
							});
					setBtnEnabled();
					queue.add(stringRequest);
				}
			}
		});

		logo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RestartApp();
			}
		});
		if (token != null && !token.isEmpty() && !token.equals("null"))
		{
			RestartApp();
		}
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void actionBarSetup() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getSupportActionBar().setTitle(Html.fromHtml("<small>ბაკურ სულაკაურის გამომცემლობა</small>"));

		}
	}

	public static void setBtnEnabled()
	{
		clr = Color.parseColor("#990000");
		btnLogin.setBackgroundColor(clr);
		btnLogin.setEnabled(true);
	}

	private void setBtnDisabled()
	{
		btnLogin.setBackgroundColor(0xff444444);
		btnLogin.setEnabled(false);
	}


	public  void RestartApp()
	{
		Intent mStartActivity = new Intent(getApplicationContext(), MainActivity.class);
		int mPendingIntentId = 123456;
		PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
		System.exit(0);

	}
}
