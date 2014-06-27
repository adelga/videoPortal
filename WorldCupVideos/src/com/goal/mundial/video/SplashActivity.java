package com.goal.mundial.video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.goal.mundial.video.util.SystemUiHider;
import com.google.android.youtube.player.YouTubeIntents;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SplashActivity extends Activity {

	private String tag = "SplashActivity";
	private ParserDom pd;
	public String[] catList;
	public String[] catListIds;
	protected List<String> listFiles;
	protected ArrayList<AvatarWord> avatares;
	protected List<AvatarWord> avataresFiltrados;
	protected List<String> categoriasSelected;
	// List<String> listAvataresOrdenados;
	public Context cntx;
	protected XmlData xmlData;
	private Builder alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		cntx=this;
		if(YouTubeIntents.getInstalledYouTubeVersionCode(cntx)<4216){
			Toast.makeText(cntx, "You need to update Youtube app to use WCVideos", Toast.LENGTH_LONG).show();
			final String appPackageName = "com.google.android.youtube"; // getPackageName() from Context or Activity object
			try {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
			}
			finish();
		} else {
		new AsyncThread().execute("");
		}
	}

	private class AsyncThread extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			
			try {
				Log.d("Idioma", Locale.getDefault().getDisplayLanguage());
				pd = new ParserDom(getString(R.string.url), SplashActivity.this, Locale.getDefault().getDisplayLanguage());

				xmlData = pd.parse();

				if (xmlData != null) {

					avatares = xmlData.getListWords();

					catList = xmlData
							.getInstitutionInfo()
							.getWordCategoriesValue()
							.toArray(
									new String[xmlData.getInstitutionInfo()
											.getWordCategoriesValue().size()]);
					;

					// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

					/** Ordenar en orden alfabético las categorías **/

					// Arrays.sort(catList,new Comparador ());



				catListIds = xmlData
						.getInstitutionInfo()
						.getWordCategoriesId()
						.toArray(
								new String[xmlData.getInstitutionInfo()
										.getWordCategoriesId().size()]);
				
				for(int i = 0; i<catList.length; i++){
					catList[i].trim();
					catListIds[i].trim();
				}
				Arrays.sort(catList);
				Arrays.sort(catListIds);
				
				for(int i = 0; i<catList.length; i++){
					Log.d("ordenacion", catList[i]);
				}
				for(int i = 0; i<catListIds.length; i++){
					Log.d("ordenacion", catListIds[i]);
				}
				
				Collections.sort(avatares, new ComparadorMainList());


					Arrays.sort(catList);
					Arrays.sort(catListIds);

					for (int i = 0; i < catList.length; i++) {
						Log.d("ordenacion", catList[i]);
					}

					Collections.sort(avatares, new ComparadorMainList());

					obtainData(avatares);

				}
			} catch(Exception e) {
				e.printStackTrace();
				crearDialogoConexion();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if(avatares!=null){
			Log.i(tag, "lista: " + avatares.toString());
			Intent yourIntent = new Intent(SplashActivity.this,
					ListAvatarActivity.class);
			ArrayList<ArrayList<AvatarWord>> lista = new ArrayList<ArrayList<AvatarWord>>();
			lista.add(avatares);

			for (int p = 0; p < lista.size(); p++) {
				yourIntent.putParcelableArrayListExtra("Lista" + p,
						lista.get(p));
			}
			Bundle bundle = new Bundle();
			bundle.putStringArray("catList", catList);
			bundle.putStringArray("catListIds", catListIds);
			yourIntent.putExtras(bundle);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finish();
			startActivity(yourIntent);
			}else{
				crearDialogoConexion();
			}
			
		}

	}

	private List<? extends Map<String, ?>> obtainData(List<AvatarWord> avatares) {
		try {
			ArrayList result = new ArrayList();

			for (int i = 0; i < avatares.size(); i++) {
				HashMap m = new HashMap();
				m.put("name", avatares.get(i).getNombre());
				result.add(m);
			}
			return (List) result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Dialog crearDialogoConexion() {

		alert = new AlertDialog.Builder(this);

		alert.setTitle(getResources().getString(R.string.activarConexion));
		alert.setMessage(getResources().getString(R.string.conexionRed));

		alert.setPositiveButton(getResources().getString(R.string.irA),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						setResult(RESULT_FIRST_USER, null);
						Intent intconex = new Intent(
								android.provider.Settings.ACTION_WIFI_SETTINGS);
						startActivity(intconex);
						finish();

					}
				});

		alert.setNegativeButton(getResources().getString(R.string.cancelar),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
						finish();

					}
				});

		alert.create();

		return alert.show();
	}

	class ComparadorMainList implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			AvatarWord p1 = (AvatarWord) o1;
			AvatarWord p2 = (AvatarWord) o2;
			int value = 0;
			if (p1.getNombre().matches("^\\d+$")
					&& p2.getNombre().matches("^\\d+$")) {
				int n1 = Integer.parseInt(p1.getNombre());
				int n2 = Integer.parseInt(p2.getNombre());
				if (n1 > n2) {
					value = 1;
				} else if (n1 < n2) {
					value = -1;
				}
			} else {
				value = p1.getNombre().toLowerCase()
						.compareToIgnoreCase(p2.getNombre());
			}
			return value;
		}
	}

}
