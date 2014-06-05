package com.goal.mundial.video;

import java.util.Locale;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

@SuppressLint("NewApi")
public class TTSService extends Service implements TextToSpeech.OnInitListener {
	 private static final int EVENT_TTS = 8000;
	private TextToSpeech tts;
	

		private String TAG="TTS service";
		private String text = " ";
	
	
		@Override
	public void onInit(int status) {
			Log.d(TAG, "ON init");
		   if (status == TextToSpeech.SUCCESS) {
			   Log.d(TAG, "locale: " + tts.getLanguage().toString());
			  Locale lc =  new Locale("es", "ES");
	            int result = tts.setLanguage(lc);
	 
	            if (result == TextToSpeech.LANG_MISSING_DATA
	                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	                Log.e("TTS", "This Language is not supported");
	            } 
//	            else speakOut(text); 
	        } else {
	            Log.e("TTS", "Initilization Failed!");
	        }
		
	}

	private void speakOut(String text) {
		Log.d(TAG, "speak " + text);
	int i =tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		Log.d(TAG, "speak i " + i);
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
	tts = new TextToSpeech(this, this);
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		tts = new TextToSpeech(this, this);
//		Log.d(TAG, tts.getEngines().toString());
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		try {
			int idAction=intent.getExtras().getInt("idAction");
			int idEvent = intent.getExtras().getInt("idEvent");
			text  = intent.getExtras().getString("text");
			String msg = "";
			switch (idEvent) {
			case EVENT_TTS:
				speakOut(text!=null ? text: " ");
				break;

			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	 @Override
	    public void onDestroy() {
	        // Don't forget to shutdown tts!
	        if (tts != null) {
	            tts.stop();
	            tts.shutdown();
	        }
	        super.onDestroy();
	    }
	 


}
