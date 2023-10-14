package com.conductor.apni.littleleappwa;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.conductor.apni.littleleappwa.services.Cons;
import com.conductor.apni.littleleappwa.utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

	public String user_id;
	private SessionManager sessionManager;
	private String daily,inviteid;

	@SuppressWarnings("static-access")
	@TargetApi(Build.VERSION_CODES.FROYO)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_activity);
		Speech.init(SplashActivity.this, getPackageName());
		FirebaseApp.initializeApp(this);
		sessionManager=SessionManager.getSessionmanager(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// possible launched from notification
			// check if desired notification data present in extras then its
			// confirmed that launched from notification
			Log.d("checkExtra","extras are "+extras.getString("daily"));
			daily=extras.getString("daily");
		}else{
			// not launched from notification
			Log.d("checkExtra","extras are "+extras);
			daily=null;
		}

		inviteid = getIntent().getStringExtra("invitedby");
		Log.d("checkExtra", "extras are " + inviteid);

		//Toast.makeText(this, " iviteId id "+inviteid, Toast.LENGTH_SHORT).show();


		FirebaseDynamicLinks.getInstance()
				.getDynamicLink(getIntent())
				.addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
					@Override
					public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
						// Get deep link from result (may be null if no link is found)
						Uri deepLink = null;
						if (pendingDynamicLinkData != null) {
							deepLink = pendingDynamicLinkData.getLink();
							Log.d("checkLog","deepLink log "+deepLink.toString());
							String referrerUid = deepLink.toString();
							String [] splitString=referrerUid.split("/");
							if(splitString.length>1){
								String resultString=splitString[splitString.length-1];
								Log.d("checkLog"," resultString is "+resultString);
								resultString=resultString.replace("?","");
								resultString=resultString.replace("3D","");
								Log.d("checkLog"," resultString is "+resultString);
								if(resultString.contains("&")) {
									String[] resultStringThree = resultString.split("&");
									String[] resultStringTwo = resultStringThree[0].split("%");
									String referralCode=resultStringTwo[resultStringTwo.length-1];
									sessionManager.setReferId(referralCode);
									Log.d("checkLog"," referralCode is "+referralCode);
									String[] resultStringFour = resultStringThree[1].split("=");
									String gameId=resultStringFour[resultStringFour.length-1];
									sessionManager.setGameId(gameId);
									sessionManager.setGameIdOne(gameId);
									Log.d("checkLog"," gameId is "+gameId);
								}else{
									String[] resultStringTwo = resultString.split("%");
									String referralCode=resultStringTwo[resultStringTwo.length-1];
									sessionManager.setReferId(referralCode);
									Log.d("checkLog"," else referralCode is "+referralCode);
								}
							}
							//Toast.makeText(SplashActivity.this, " invitedby id "+sessionManager.getReferId(), Toast.LENGTH_SHORT).show();
						}else{
						//	Toast.makeText(SplashActivity.this, "inside else ", Toast.LENGTH_SHORT).show();
						}
						//
						// If the user isn't signed in and the pending Dynamic Link is
						// an invitation, sign in the user anonymously, and record the
						// referrer's UID.
						//
//						FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//						if (user == null
//								&& deepLink != null
//								&& deepLink.getBooleanQueryParameter("invitedby", false)) {
//							String referrerUid = deepLink.getQueryParameter("invitedby");
//							createAnonymousAccountWithReferrerInfo(referrerUid);
//						}
					}
				});

		//speakOne();
		setReferrer();
		goNext(2500);
	}


	//https://play.google.com/store/apps/details?id=com.yourpackage&referrer=9BE46300
	private void setReferrer(){
		InstallReferrerClient mReferrerClient;
		mReferrerClient = InstallReferrerClient.newBuilder(this).build();

		mReferrerClient.startConnection(new InstallReferrerStateListener() {
			@Override
			public void onInstallReferrerSetupFinished(int responseCode) {
				switch (responseCode) {
					case InstallReferrerClient.InstallReferrerResponse.OK:
						// Connection established
						try {
							ReferrerDetails response =
									mReferrerClient.getInstallReferrer();
							String referrerUrl = response.getInstallReferrer();
							long referrerClickTime = response.getReferrerClickTimestampSeconds();
							long appInstallTime = response.getInstallBeginTimestampSeconds();
							boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
							if(!referrerUrl.contains("utm_source=google-play&utm_medium=organic")) {
								sessionManager.setUncaughtError(referrerUrl);
								Log.d("checkLog","referral code is if "+referrerUrl);
							}else{
								Log.d("checkLog","referral code else "+referrerUrl);
								sessionManager.setUncaughtError(referrerUrl);
							}
							// Toast.makeText(SplashActivity.this, referrerUrl, Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							e.printStackTrace();
						}
						mReferrerClient.endConnection();
						break;
					case
							InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
						// API not available on the current Play Store app
						break;
					case
							InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
						// Connection could not be established
						break;
				}
			}

			@Override
			public void onInstallReferrerServiceDisconnected() {
				// Try to restart the connection on the next request to
				// Google Play by calling the startConnection() method.
			}
		});
	}


	private void speakOne() {
		//String msg="आपका स्वागत है।";
		String msg="you are welcome.";
		Speech.getInstance().setTextToSpeechRate(Cons.speed);
		Locale locale = new Locale("hi_IN");
		//Locale mLocale=new Locale("hi_IN");
		//Locale mLocale=new Locale("en_IN");
		// Locale mLocale=new Locale("hi", "IN");
		//Speech.getInstance().setLocale(Locale.ENGLISH);
		//Speech.getInstance().setLocale(locale);
		Speech.getInstance().say(msg, new TextToSpeechCallback() {
			@Override
			public void onStart() {
				// onBeatSound();
				// Log.i("speech", "speech started");
				//Toast.makeText(HomeActivity.this, "Start..", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCompleted() {
				goNext(1500);
				// offBeatSound();
				// Log.i("speech", "speech completed");
				// Toast.makeText(HomeActivity.this, "Completed..", Toast.LENGTH_SHORT).show();
				//speechRecognizer.startListening(speechRecognizerIntent);
			}

			@Override
			public void onError() {
				Log.i("speech", "speech error");
				goNext(1500);
				// Toast.makeText(HomeActivity.this, "Error..", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
				.getActiveNetworkInfo().isConnectedOrConnecting();
	}


	private void goNext(int timeMillis){
	//	speakOne();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("daily",daily);
				startActivity(intent);
				finish();
			}
		}, timeMillis);
	}

//	private boolean isLogined(){
//		user_id = sessionManager.getUserId();
//		if (user_id != null && user_id.length()>0) {
//			return true;
//		}else{
//			return false;
//		}
//	}

}
