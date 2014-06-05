package com.goal.mundial.video;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;


@SuppressLint("WrongViewCast")
public class PantallaVideoPalabra extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {

	private Bundle bundle;
	
	private String nombre;

	private String definicion;

	private Button saveF;

	private Button definitionButton;

	private TextView wordDef;

	private ImageButton exitButton;

	public static final String API_KEY = "AIzaSyBWhF9xlg0bWM6MGfuEWhSbioQW4dXXdL0";
	public static String VIDEO_ID;

	private YouTubePlayerView youTubePlayerView;
	private String enlace;
	protected String urlVideo;
	protected String urlVideo2;
	public static final String TAG = "PantallaVideoPalabraActivity";

	private boolean favorito = false;
	private boolean showVideo = true;

	private Typeface tf;

	public void onCreate(Bundle savedInstanceState) {


		
		// Quitamos barra de titulo de la aplicacion
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		tf = Typeface.createFromAsset(getAssets(), "brasilfont.otf");
		setContentView(R.layout.mediaplayer);

		if (getResources().getDisplayMetrics().density <= 0.75) {

			RelativeLayout rl = (RelativeLayout) findViewById(R.id.RlayoutVideo);
			rl.setMinimumWidth(500);

		}

		youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayerview);
		youTubePlayerView.initialize(API_KEY, this);

		wordDef = (TextView) findViewById(R.id.wordDefinition);
		wordDef.setVisibility(View.GONE);

		bundle = getIntent().getExtras();
		enlace = bundle.getString("enlacevideo");
		// Log.d(TAG, enlace);

		// parsear enlace, quitar embed y url a partir de ?
		String separador1 = "embed/";
		String separador2 = "\\?";
		String[] palabras = enlace.split(separador1);
		String idvideo = palabras[1];
		Log.d(TAG, idvideo);
		String[] idvideofinal = idvideo.split(separador2);
		String urlYoutube = idvideofinal[0];
		Log.d(TAG, idvideofinal[0]);

		VIDEO_ID = urlYoutube;

		nombre = bundle.getString("nombrevideo");
		Log.d("Contenido del bundle", bundle.getString("definicionvideo") + "");

		saveF = (Button) findViewById(R.id.buttonSaveF);
		definitionButton = (Button) findViewById(R.id.buttonDefinicion);

		definicion = bundle.getString("definicionvideo");
		if (definicion.equals("")) {
			definitionButton.setVisibility(View.GONE);
		}

		TextView titlevid = (TextView) findViewById(R.id.tituloVideo);

		titlevid.setText(nombre);
		titlevid.setTypeface(tf);

		SharedPreferences pref = getSharedPreferences("SIGNATICPreferencias",
				Context.MODE_PRIVATE);

		StringFav fav;
		String listfav = pref.getString("favoritos", "nada");

		if (!listfav.equalsIgnoreCase("nada")) {

			fav = new StringFav(listfav);

		} else {
			// primera vez que agrego a favoritos
			fav = new StringFav("");
		}

		favorito = fav.isFavorito(nombre);

		if (favorito) {

			saveF.setText(R.string.ButtonDeleteFav);

		} else {

			saveF.setText(R.string.ButtonSaveFav);
		}
		saveF.setTypeface(tf);
		saveF.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				SharedPreferences pref = getSharedPreferences(
						"SIGNATICPreferencias", Context.MODE_PRIVATE);
				StringFav fav;
				String listfav = pref.getString("favoritos", "nada");

				if (!listfav.equalsIgnoreCase("nada")) {

					fav = new StringFav(listfav);

				} else {
					// primera vez que agrego a favoritos
					fav = new StringFav("");
				}

				favorito = fav.isFavorito(nombre);

				if (favorito) {
					// eliminar
					if (!listfav.equalsIgnoreCase("nada")) {

						fav = new StringFav(listfav);
						fav.deleteNombre(nombre);
					}

					Editor ed = pref.edit();
					ed.putString("favoritos", fav.toString());
					ed.commit();

					saveF.setText(R.string.ButtonSaveFav);

				} else {

					// añadir
					if (!listfav.equalsIgnoreCase("nada")) {

						fav = new StringFav(listfav);
						fav.setNombre(nombre);

					} else {
						// primera vez que agrego a favoritos
						fav = new StringFav(nombre);
					}

					Editor ed = pref.edit();
					ed.putString("favoritos", fav.toString());
					ed.commit();

					saveF.setText(R.string.ButtonDeleteFav);

				}

			}

		});

		exitButton = (ImageButton) findViewById(R.id.buttonExitVideo);
		exitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				finish();
			}

		});
		definitionButton.setTypeface(tf);
		definitionButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (showVideo) {
					youTubePlayerView.setVisibility(View.GONE);
					definitionButton.setText(definitionButton.getResources()
							.getString(R.string.vervideo));
					wordDef.setText(definicion);
					wordDef.setVisibility(View.VISIBLE);
				} else {
					wordDef.setVisibility(View.GONE);
					definitionButton.setText(definitionButton.getResources()
							.getString(R.string.verdefinicion));
					youTubePlayerView.setVisibility(View.VISIBLE);
				}
				showVideo = !showVideo;

			}

		});
		wordDef.setTypeface(tf);
		wordDef.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {
					speak((String) wordDef.getText());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	protected void speak(String text) {
		Intent intent = new Intent();
		intent.putExtra("text", text);
		intent.putExtra("idEvent", 8000);
		intent.putExtra("idAction", 254);
		startService(intent);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * YouTube Player API override methods
	 */

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {

		Toast.makeText(getApplicationContext(), "onInitializationFailure()",
				Toast.LENGTH_LONG).show();
	}

	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean wasRestored) {

		if (!wasRestored) {
			player.cueVideo(VIDEO_ID, 1000);
			player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
			// Log.d(TAG, "El player está : " + player.isPlaying());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * Google Analytics override methods
	 */

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    Toast.makeText(this, "Cambio la pantalla", Toast.LENGTH_LONG).show();
	    int orient = getResources().getConfiguration().orientation; 
	    switch(orient) {
	                case Configuration.ORIENTATION_LANDSCAPE:
	                    Log.d("Orientation", "Landscape");
	                    break;
	                case Configuration.ORIENTATION_PORTRAIT:
	                	Log.d("Orientation", "portrait");
	                    break;
	                default:
	                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	                }
	}

}