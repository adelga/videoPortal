package com.goal.mundial.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goal.mundial.video.ListAvatarActivity.ComparadorMainList;
import com.goal.mundial.video.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

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

	protected XmlData xmlData;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		new AsyncThread().execute("");
	}
	
	private class AsyncThread extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			pd = new ParserDom(
					getString(R.string.url),
					SplashActivity.this);

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
				
				Collections.sort(avatares, new ComparadorMainList());

				obtainData(avatares);

			}
			return null;
		}
		
		@Override
        protected void onPostExecute(String result) {
			Log.i(tag, "lista: " + avatares.toString());
			Intent yourIntent = new Intent(SplashActivity.this, ListAvatarActivity.class);
			ArrayList<ArrayList<AvatarWord>> lista = new ArrayList<ArrayList<AvatarWord>>();
			lista.add(avatares);
			
			for(int p = 0; p < lista.size(); p++){
				yourIntent.putParcelableArrayListExtra("Lista"+p, lista.get(p));
			}
			Bundle bundle =new Bundle();
			bundle.putStringArray("catList",catList);
			bundle.putStringArray("catListIds",catListIds);
			yourIntent.putExtras(bundle);
			startActivity(yourIntent);
			finish();
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
	
	class ComparadorMainList implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			AvatarWord p1 = (AvatarWord) o1;
			AvatarWord p2 = (AvatarWord) o2;
			int value = 0;
			if(p1.getNombre().matches("^\\d+$") && p2.getNombre().matches("^\\d+$")){
					int n1 = Integer.parseInt(p1.getNombre());
					int n2 = Integer.parseInt(p2.getNombre());
					if(n1>n2){
						value = 1;
					} else if (n1<n2) {
						value = -1;
					}
				} else{
					value = p1.getNombre().toLowerCase()
							.compareToIgnoreCase(p2.getNombre());
				}
			return value;
		}
	}
	
}
