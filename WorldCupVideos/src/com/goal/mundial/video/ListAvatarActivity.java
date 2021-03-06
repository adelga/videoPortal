package com.goal.mundial.video;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SectionIndexer;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;


@SuppressLint({ "NewApi", "NewApi" })
public class ListAvatarActivity extends Activity implements TextWatcher {

	private Handler myhandlerUI;

	private Timer timer;

	private int time = 0;

	private AlertDialog.Builder alert;

	private ProgressDialog progress;

	private static final int REQ_START_STANDALONE_PLAYER = 1;
	private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
	// ProgressBar progressBar;
	private ProgressBar progressBar1;
	private ProgressBar progressBar2;
	private boolean inputMethodActivado = false;
	// Button buttonStartProgress;
	private ImageButton buttonSearch;
	private ImageButton buttonFav;
	private ImageButton buttonInf;
	private ImageButton shareButton;
	private Button cmbOpciones;
	// TextView wordDef;

	private String storage;
	protected String filename;
	protected String nombreVideo;
	protected String defVideo;
	private String tag = "SignosActivity";
	private String auxiliar = "";

	private String[] catList;

	private String[] catListIds;

	private InputMethodManager imm;

	// Runnable pb;

	protected List<String> listFiles;
	protected ArrayList<AvatarWord> receivedList = new ArrayList<AvatarWord>();
	protected ArrayList<AvatarWord> avataresFiltrados;
	protected List<String> categoriasSelected;
	// List<String> listAvataresOrdenados;

	protected XmlData xmlData;

	// List<String> palabrasCategorizadas = new ArrayList<String>();
	// List<AvatarWord> palabras;

	protected SimpleAdapter adapter;
	// SimpleAdapter adapcat;
	protected SimpleAdapter adapfav;

	private IndexableListView listView;

	private LinearLayout pantallaAcerca;

	private AutoCompleteTextView autoCtextView;

	// private TextView textoTituloAcercaApp1;

	private TextView textoCompartirApp;

	protected boolean oncreateok = false;
	protected boolean downloaded;
	private boolean favoritos = false;
	private boolean buscar = false;
	private boolean listgone = false;
	private boolean keyboardOn = false;
	private boolean catTodasSelec = false;
	private boolean textSpinnerChanged = false;
	public static final String API_KEY = "AIzaSyBWhF9xlg0bWM6MGfuEWhSbioQW4dXXdL0";

	private static final int REQ_CAT_SELECTED = 4;
	Display display;
	Typeface tf;
	private float textSize = 22f;

	protected Context mContext;

	protected boolean notShowFavAgain;

	private SharedPreferences prefs;
	@SuppressLint("NewApi")
	public void onCreate(Bundle bundle) {
		

		super.onCreate(bundle);
		Log.d("onCreate", "Entra al onCreate");
		oncreateok = true;
		mContext = this;
		receivedList = getIntent().getExtras().getParcelableArrayList(
				"Lista" + 0);
		catList = getIntent().getExtras().getStringArray("catList");
		catListIds = getIntent().getExtras().getStringArray("catListIds");

		try {
			display = getWindowManager().getDefaultDisplay();

			tf = Typeface.createFromAsset(getAssets(), "brasilfont.otf");

			storage = Environment.getExternalStorageDirectory() + "/avatares/";

			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			// Quitamos barra de titulo de la aplicacion

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();

			StrictMode.setThreadPolicy(policy);

			setContentView(R.layout.activity_list_avatar);
			TextView tittle = (TextView) findViewById(R.id.TextViewHeader1);
			tittle.setTypeface(tf);
			Log.d("TAG", "n1");
			categoriasSelected = new ArrayList<String>();
			try {
				WebView wV = (WebView) findViewById(R.id.webview);
				WebSettings settings = wV.getSettings();
				settings.setBuiltInZoomControls(false);
				settings.setUseWideViewPort(true);
				settings.setJavaScriptEnabled(true);
				settings.setSupportMultipleWindows(true);
				settings.setJavaScriptCanOpenWindowsAutomatically(true);
				settings.setLoadsImagesAutomatically(true);
				settings.setLightTouchEnabled(true);
				settings.setDomStorageEnabled(true);
				settings.setLoadWithOverviewMode(true);

				wV.loadUrl("file:///android_asset/banner.html");
			} catch (Exception e) {
				Log.e("WEBVIEW", "msg: " + e.getMessage());
				e.printStackTrace();
			}

			prefs = getSharedPreferences(
					"SIGNATICPreferencias", Context.MODE_PRIVATE);
			notShowFavAgain=prefs.getBoolean("notShowFavAgain", false);
			/**
			 * Comentado la primera pantalla de aceptaci�n de t�rminos y
			 * condiciones para la prueba piloto de la app de Garrigou. Para
			 * implementaci�n de versi�n final y para subirla a Google Play,
			 * descomentar e incluir el texto oficial de t�rminos y condiciones
			 * **/

			
			if (!prefs.getBoolean("terminos", false)) {

				Log.d("DIALOG", "ter; " + prefs.getBoolean("terminos", false));

				final Dialog initDialog = new Dialog(this,
						R.style.CustomDialogTheme);

				initDialog.setContentView(R.layout.dialog_init);

				TextView termsText = (TextView) initDialog
						.findViewById(R.id.layouttext);
				TextView textView = (TextView) initDialog
						.findViewById(R.id.titulo);
				termsText.setTypeface(tf);
				termsText.setPaddingRelative(10, 10, 10, 10);
				textView.setTypeface(tf);
				textView.setText(R.string.tituloterminos);

				termsText.setMovementMethod(new ScrollingMovementMethod());

				Button btnAceptar = (Button) initDialog
						.findViewById(R.id.buttonDialogAceptar);
				btnAceptar.setTypeface(tf);
				btnAceptar.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						prefs = getSharedPreferences(
								"SIGNATICPreferencias", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putBoolean("terminos", true);
						editor.commit();

						initDialog.dismiss();
					}
				});

				Button btnCancelar = (Button) initDialog
						.findViewById(R.id.buttonDialogCancelar);
				btnCancelar.setTypeface(tf);
				btnCancelar.setBackgroundResource(R.drawable.green);

				btnCancelar.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						finish();
					}
				});

				initDialog.setCancelable(false);
				initDialog.show();
			}

			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (obtainData(avatares) != null) {

			listView = (IndexableListView) findViewById(R.id.list);
			listView.setBackgroundColor(0xFF013668);
			listView.setAling(IndexableListView.ALING_LEFT);
			adapter = new ContentAdapter(this, obtainData(receivedList),
					R.layout.list_row, new String[] { "name" },
					new int[] { R.id.title });

			listView.setAdapter(adapter);
			listView.setFastScrollEnabled(true);

			Log.d("TAG", "n2");

			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			cmbOpciones = (Button) findViewById(R.id.CmbOpciones);
			cmbOpciones.setTypeface(tf);
			cmbOpciones.setTextSize(textSize);
			cmbOpciones.setSingleLine(true);
			cmbOpciones.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					crearDialogoSeleccion();
//					Intent intent = new Intent();
//					intent.setClass(mContext, SelectCategory.class);
//					intent.putExtra("catList", catList);
//					intent.putExtra("CatListIds", catListIds);
//					startActivity(intent);

				}
			});

			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			buttonInf = (ImageButton) findViewById(R.id.button1);
			buttonInf.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Log.d("EL Teclo", "El estado del teclado" + imm.toString());
					buscar = false;
					if (inputMethodActivado) {
						try {
							inputMethodActivado = false;
							imm.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(), 0);
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (listgone == true) {

						pantallaAcerca.setVisibility(View.GONE);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						listView.setVisibility(View.VISIBLE);
						buttonInf.setSelected(false);
						listgone = false;

						Log.d("Estoy en",
								"Bot�n Informaci�n pulsado, listgone a false.");

					} else {

						Log.d("Estoy en",
								"Bot�n Informaci�n pulsado, listgone a true.");
						buttonFav.setSelected(false);
						autoCtextView.setText("");
						autoCtextView.setVisibility(View.GONE);

						listgone = true;
						listView.setVisibility(View.GONE);
						buttonInf.setSelected(true);
						buttonSearch.setSelected(false);
						pantallaAcerca = (LinearLayout) findViewById(R.id.layoutAcercade);
						pantallaAcerca.setVisibility(View.VISIBLE);
						textoCompartirApp = (TextView) findViewById(R.id.textoCompartirApp);
						textoCompartirApp.setVisibility(View.VISIBLE);
						textoCompartirApp.setTypeface(tf);
						textoCompartirApp.setTextSize(textSize);
						
						shareButton = (ImageButton) findViewById(R.id.shareButton);
						shareButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_share));
						shareButton.setBackground(getResources().getDrawable(R.drawable.blue_button));
						shareButton.setScaleType(ScaleType.CENTER);
						shareButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {

								Intent sharingIntent = new Intent(
										Intent.ACTION_SEND);
								sharingIntent.setType("text/plain");
								sharingIntent
										.putExtra(
												android.content.Intent.EXTRA_TEXT,
												v.getContext()
														.getResources()
														.getString(
																R.string.notification_share_subject)
														.toUpperCase()
														+ "\n"
														+ v.getContext()
																.getResources()
																.getString(
																		R.string.notification_share_text));
								startActivity(Intent.createChooser(
										sharingIntent,
										getString(R.string.shareapp)));

							};
						});

					}

				}

			});

			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			buttonFav = (ImageButton) findViewById(R.id.button2);
			buttonFav.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					Log.d("El teclado", "est� a : " + imm.isAcceptingText());

					buscar = false;
					if (inputMethodActivado) {
						inputMethodActivado = false;
						try {

							imm.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(), 0);
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					Log.d("Estoy en", "Bot�n Favoritos pulsado");

					SharedPreferences pref = getSharedPreferences(
							"SIGNATICPreferencias", Context.MODE_PRIVATE);

					String listfav = pref.getString("favoritos", "nada");
					if (!listfav.equalsIgnoreCase("nada")) {
						StringFav fav = new StringFav(listfav);
						listFiles = fav.getFavList();
					} else {
						listFiles = new ArrayList();
					}

					Log.d(tag, "favoritos " + listFiles.toString());
					Collections.sort(listFiles, new Comparador());
					
					adapfav = new SimpleAdapter(getApplicationContext(),
							obtainDatafav(listFiles), R.layout.list_row,
							new String[] { "name" }, new int[] { R.id.title }) {
						
						@Override 
						public View getView(int position, View convertView, ViewGroup parent) {
					        View view = super.getView(position, convertView, parent);
					        TextView textview = (TextView) view.findViewById(R.id.title);
					        textview.setTypeface(tf);
					        return view;
					    }
					};

					
					favoritos = !favoritos;
					if (listgone == true) {

						Log.d("Estoy en",
								"Bot�n favoritos pulsado y listgone a true");
						pantallaAcerca.setVisibility(View.GONE);

						listView.setVisibility(View.VISIBLE);
						
						buttonInf.setSelected(false);

						listgone = false;

					}
					if (favoritos) {

						Log.d("Estoy en",
								"Bot�n favoritos pulsado y favoritos a true");

						if (buttonFav.isSelected()) {

							buttonFav.setSelected(false);
							listView.setAdapter(adapter);
							adapter.notifyDataSetChanged();

						} else {
							buttonFav.setSelected(true);
							listView.setAdapter(adapfav);
							adapfav.notifyDataSetChanged();
							buttonSearch.setSelected(false);
							autoCtextView.setText("");
							autoCtextView.setVisibility(View.GONE);

							Log.d("El teclado est� con",
									"el activo a :" + imm.isActive(v));

						}

						// if (buscar) {

						Log.d("Estoy en",
								"Bot�n favoritos pulsado y buscar a true");
						autoCtextView.setText("");
						autoCtextView.setVisibility(View.GONE);
						buttonSearch.setSelected(false);
						// if (imm.isActive(v)) {

						// }
						// }
					} else {

						Log.d("Estoy en",
								"Bot�n favoritos pulsado y buscar a false, o listgone a false, o favoritos a false");

						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						buttonFav.setSelected(false);

					}
					
					
					if (notShowFavAgain){
						

						
						
						AlertDialog.Builder builderfav = new AlertDialog.Builder(mContext);

						builderfav.setPositiveButton(getString(R.string.aceptar),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										modifyFavorito();
									}

								});
						TextView favtext = new TextView(mContext);
						favtext.setBackground(getResources().getDrawable(R.drawable.blue));
						favtext.setText("Do you want enable bookmarks?");
						favtext.setTypeface(tf);
						favtext.setGravity(Gravity.CENTER);
						favtext.setTextSize(textSize);
						builderfav.setCustomTitle(favtext);
						CheckBox check = new CheckBox(mContext);
						check.setText("Enable Bookmarks");
						check.setTypeface(tf);
						check.setChecked(false);
						check.setBackground(getResources().getDrawable(R.drawable.blue));
						check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									notShowFavAgain=!isChecked;
									Editor ed = prefs.edit();
									ed.putBoolean("notShowFavAgain", notShowFavAgain);
									ed.commit();
							}
						});
						favtext.setGravity(Gravity.CENTER);
						favtext.setTextSize(textSize);
						builderfav.setView(check);
						builderfav.setNegativeButton(getString(R.string.cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}

								});
					
						AlertDialog dialog = builderfav.create();
						dialog.show();
						Button cancel = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
						cancel.setTypeface(tf);
						cancel.setTextSize(textSize);
						cancel.setBackgroundResource(R.drawable.yellow_button);
						Button accept = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
						accept.setTypeface(tf);
						accept.setTextSize(textSize);
						accept.setBackgroundResource(R.drawable.green_button);
						
					}

				}
			});

			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			final String[] videostotal = new String[receivedList.size()];

			for (int i = 0; i < receivedList.size(); i++) {
				videostotal[i] = receivedList.get(i).getNombre();
			}

			autoCtextView = (AutoCompleteTextView) findViewById(R.id.autotextview);

			buttonSearch = (ImageButton) findViewById(R.id.button3);
			buttonSearch.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					favoritos = false;

					buscar = !buscar;

					if (listgone == true) {

						Log.d("Estoy en",
								"Bot�n buscar pulsado y listgone a true");
						pantallaAcerca.setVisibility(View.GONE);
						// textoAcerca.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
						listgone = false;
						buttonSearch.setSelected(true);
						buttonInf.setSelected(false);
						autoCtextView.requestFocus();
						if (autoCtextView.isShown()) {
							Log.d("El Teclado", "Est� activo supuestamente");

						}
						if (imm.isActive(v)) {
							Log.d("Teclados", "Est� activo ");
						}

					}

					if (buscar) {

						Log.d("Estoy en",
								"Bot�n buscar pulsado y buscar a true");

						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						buttonFav.setSelected(false);
						autoCtextView.setVisibility(View.VISIBLE);
						autoCtextView.setAdapter(adapter);
						autoCtextView.setThreshold(1);
						autoCtextView.performValidation();

						if ((!imm.isActive(v)) && autoCtextView.isShown()) {
							Log.d("Teclados", "activando ");
							inputMethodActivado = true;
							imm.toggleSoftInput(
									InputMethodManager.SHOW_IMPLICIT, 0);

						} else {

							Log.d("Ahora se dice",
									"Que el teclado no est� activo");

						}

						if (!imm.isActive(v)) {
							Log.d("Teclados", "Est� activo buscar ");

						}

						autoCtextView
								.setOnItemClickListener(new OnItemClickListener() {

									public void onItemClick(AdapterView<?> a,
											View v, int position, long id) {
										listView.setAdapter(adapter);
										TextView txtView = (TextView) v
												.findViewById(R.id.title);
										txtView.setTypeface(tf);
										String name = (String) txtView
												.getText();
										Log.d("AVATARES", "name: " + name);
										if (!name.contains(" ")) {
											autoCtextView.setText(name);
											adapter.notifyDataSetChanged();
										} else {
											autoCtextView.setText(name);
											List<String> lst = new ArrayList<String>();
											lst.add(name);
											SimpleAdapter aaa = new SimpleAdapter(
													getApplicationContext(),
													obtainDatafav(lst),
													R.layout.list_row,
													new String[] { "name" },
													new int[] { R.id.title });
											listView.setAdapter(aaa);

										}

									}

								});

						buttonSearch.setSelected(true);
						buttonInf.setSelected(false);
						autoCtextView.requestFocus();

					} else {

						Log.d("Estoy en", "estoy en no buscar ");
						autoCtextView.setVisibility(View.GONE);
						buttonSearch.setSelected(false);
						// if (imm.isActive(v)) {
						inputMethodActivado = false;
						Log.d("Estoy en",
								" activo para matarlo " + imm.isActive(v));
						imm.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(), 0);
						// }

						if (autoCtextView.isShown()) {
							Log.d("El Teclado", "Est� activo supuestamente");

						}

						if (imm.isActive(v)) {
							Log.d("Teclados", "Est� activo ");
							Log.d("Estoy en", "estoy en no boscar ");
						}

					}
				}
			});

			/*
		 * 
		 */

			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			Log.d("TAG", "n3");
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {

					Log.d("clik", "posrrr:" + pos);

					TextView txtView = (TextView) arg1.findViewById(R.id.title);
					txtView.setTypeface(tf);
					String name = (String) txtView.getText();
					String enlace = "";

					for (int i = 0; i < receivedList.size(); i++) {

						String normalizedName = Normalizer.normalize(
								receivedList.get(i).getNombre(), Form.NFC);
						Log.d("avatares", "nombre de la lista: "
								+ normalizedName + " " + name);

						if (normalizedName.equalsIgnoreCase(name)) {
							enlace = receivedList.get(i).getEnlaceVideo();
							nombreVideo = receivedList.get(i).getNombre();
							defVideo = receivedList.get(i).getDefinicion();

							break;
						}

					}
					String[] enl = enlace.split("/");
					String en = enl[enl.length - 1];

					Intent intent = null;

					intent = YouTubeStandalonePlayer.createVideoIntent(
							(Activity) mContext, API_KEY, en.split("\\?")[0],
							0, true, true);

					if (intent != null) {
						if (canResolveIntent(intent)) {
							startActivityForResult(intent,
									REQ_START_STANDALONE_PLAYER);
						} else {
							// Could not resolve the intent - must need to
							// install or update the YouTube API service.
							Log.e(tag, "ERRORR");
							YouTubeInitializationResult.SERVICE_MISSING
									.getErrorDialog((Activity) mContext,
											REQ_RESOLVE_SERVICE_MISSING).show();
						}
					}
				}

			});

			// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		} catch (Exception e) {
			e.printStackTrace();
		}

		// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * Google Analytics override methods
	 */

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean canResolveIntent(Intent intent) {
		List<ResolveInfo> resolveInfo = getPackageManager()
				.queryIntentActivities(intent, 0);
		return resolveInfo != null && !resolveInfo.isEmpty();
	}

	@Override
	public void onStart() {
	
		super.onStart();
	prefs = getSharedPreferences("SIGNATICPreferencias",
				Context.MODE_PRIVATE);
		EasyTracker.getInstance(this).activityStart(this);

	}

	@Override
	public void onStop() {

		super.onStop();

		EasyTracker.getInstance(this).activityStop(this);

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void onBackPressed() {

		Log.d("variables", "buscar " + buscar + "  favoritos : " + favoritos
				+ "listgone : " + listgone);

		if (buscar) {

			autoCtextView.setText("");
			autoCtextView.setVisibility(View.GONE);
			buttonSearch.setSelected(false);
			buscar = false;

		} else {
			if (listgone) {

				pantallaAcerca.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				buttonInf.setSelected(false);
				listgone = false;

			} else {
				if (favoritos) {

					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					buttonFav.setSelected(false);
					favoritos = false;

				} else {
					Log.d("Avatar", "Me voy a pirar");
					keyboardOn = false;
					finish();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private List<? extends Map<String, ?>> obtainDatafav(List<String> listFiles2) {

		try {
			ArrayList result = new ArrayList();

			for (int i = 0; i < listFiles2.size(); i++) {
				HashMap m = new HashMap();
				m.put("name", listFiles2.get(i));

				result.add(m);
			}
			return (List) result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private List<? extends Map<String, ?>> obtainData(
			ArrayList<AvatarWord> avatares) {
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

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void onResume() {

		super.onResume();

		if (receivedList == null) {

			if (oncreateok) {

				oncreateok = false;

			} else {

				myhandlerUI.sendEmptyMessage(3);

			}
		}

	}

	/**
	 * //create the send intent Intent shareIntent = new
	 * 
	 * //set the type shareIntent.setType("text/plain");
	 * 
	 * //add a subject
	 * shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
	 * "Insert Subject Here");
	 * 
	 * //build the body of the message to be shared String shareMessage =
	 * "Insert message body here.";
	 * 
	 * //add the message shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
	 * shareMessage);
	 * 
	 * //start the chooser for sharing
	 * startActivity(Intent.createChooser(shareIntent,
	 * "Insert share chooser title here"));
	 **/

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

					}
				});

		alert.setNegativeButton(getResources().getString(R.string.cancelar),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();

					}
				});

		alert.create();

		return alert.show();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void startTimer() {

		timer = new Timer();

		timer.schedule(new TimerTask() {

			public void run() {

				time += 500;

				if (time == 30000) {

					time = 0;
					myhandlerUI.sendEmptyMessage(0);
					myhandlerUI.sendEmptyMessage(2);
					timer.cancel();

				} else {

					if (exiteConexionInternet()) {

						// Log.i(getClass().toString(), "OK Connection");
						myhandlerUI.sendEmptyMessage(0);
						myhandlerUI.sendEmptyMessage(3);
						timer.cancel();
						// Log.i(getClass().toString(), "Timer close");

					} else {

						if (!progress.isShowing()) {

							myhandlerUI.sendEmptyMessage(1);

						}

					}
				}
			}

		}, 0, 500);

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private Dialog crearDialogoComprobacionConexion() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getResources().getString(R.string.errorConexion));
		alert.setMessage(getResources().getString(R.string.reconexion));

		alert.setPositiveButton(getResources().getString(R.string.irA),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						setResult(RESULT_FIRST_USER, null);
						Intent intconex = new Intent(
								android.provider.Settings.ACTION_WIFI_SETTINGS);
						startActivity(intconex);

					}
				});

		alert.setNegativeButton(getResources().getString(R.string.cancelar),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();

					}
				});

		alert.create();

		return alert.show();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void crearDialogoEsperaConexion() {

		progress = ProgressDialog.show(ListAvatarActivity.this, getResources()
				.getString(R.string.cargando),
				getString(R.string.esperaConexion));

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean exiteConexionInternet() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {

			return true;

		}

		return false;

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private Dialog crearDialogoSeleccion() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getResources().getString(R.string.selectionCategory));
		TextView customTitleView= new TextView(mContext);
		
		customTitleView.setBackground(getResources().getDrawable(R.drawable.blue));
		customTitleView.setText(getResources().getString(R.string.selectionCategory));
		customTitleView.setTypeface(tf);
		customTitleView.setGravity(Gravity.CENTER);
		customTitleView.setTextSize(textSize);
		
		builder.setCustomTitle(customTitleView);



		builder.setPositiveButton(getResources()
				.getString(R.string.confSpinner),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						avataresFiltrados = new ArrayList<AvatarWord>(
								receivedList);
						Log.d("avataresfiltrados", avataresFiltrados+"");
						if (catTodasSelec == true) {
							auxiliar = getResources().getString(
									R.string.tituloSpinner);
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.toastSelectAll),
									Toast.LENGTH_LONG).show();

							if (textSpinnerChanged == true) {
								cmbOpciones.setText(auxiliar.trim());
							}

						} else {
							for (int i = 0; i < categoriasSelected.size(); i++) {

								for (int j = 0; j < avataresFiltrados.size(); j++) {

									if (!avataresFiltrados
											.get(j)
											.getListaTagsPalabras()
											.contains(categoriasSelected.get(i))) {

										avataresFiltrados.remove(j);
										j--;
									}
								}
							}

							cmbOpciones.setText("Categories "
									+ auxiliar.substring(0, auxiliar.length()));
							textSpinnerChanged = true;

						}

						adapter = new ContentAdapter(getApplicationContext(),
								obtainData(avataresFiltrados),
								R.layout.list_row, new String[] { "name" },
								new int[] { R.id.title });

						listView.setAdapter(adapter);
						listView.setFastScrollEnabled(true);
						dialog.cancel();

					}
				});
		
		ScrollView sclView=new ScrollView(mContext);
		LinearLayout ll = new LinearLayout(mContext);
		
		ll.setOrientation(LinearLayout.VERTICAL);
		for (int i =0; i<catList.length;i++){
			CheckBox checkBox= new CheckBox(mContext);
			checkBox.setText(catList[i]);
			Log.d(tag, "categorias selected: " + categoriasSelected.toString());
			if(categoriasSelected.contains(catListIds[i])) checkBox.setChecked(true);
			checkBox.setTypeface(tf);
			checkBox.setId(584788888+i);
			checkBox.setBackground(getResources().getDrawable(R.drawable.blue));
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int item =buttonView.getId() -584788888;
						checkCategory(item, isChecked);
						
				}
			});
			ll.addView(checkBox, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT ));
		}
		sclView.addView(ll, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		builder.setView(sclView);
		AlertDialog dialg=builder.create();
		

		dialg.show();
		Button accept =  dialg.getButton(DialogInterface.BUTTON_POSITIVE);
		tf= Typeface.createFromAsset(getAssets(), "brasilfont.otf");
		accept.setTypeface(tf);
		accept.setTextSize(textSize);
		accept.setBackgroundResource(R.drawable.green_button);
	
		
		
		return dialg;

	}

	private void checkCategory(int item, boolean isChecked){

		Log.i("Dialogos", "Opci�n elegida: " + catList[item]);

		catTodasSelec = false;

		if (item == 0) {

			if (isChecked == true) {
				catTodasSelec = true;
				Log.d("Entro en el Todas", "True");
			} else {
				catTodasSelec = false;
				Log.d("Entro en el Todas", "False");
			}

		} else {
			if (isChecked == true) {

				categoriasSelected.add(catListIds[item]);

				if (categoriasSelected.size() == 1) {

					auxiliar = catListIds[item];

				} else {

					auxiliar = auxiliar + " , "
							+ catListIds[item];
				}

				Log.d(tag, "a�ado categoria: "
						+ catListIds[item] + "   "
						+ categoriasSelected.size());

			} else {

				categoriasSelected.remove(catListIds[item]);

				if (auxiliar.contains(", " + catListIds[item])) {

					auxiliar = auxiliar.replace(", "
							+ catListIds[item], "");
					auxiliar.trim();

				} else if (auxiliar.contains(catListIds[item]
						+ " ,")) {

					auxiliar = auxiliar.replace(
							catListIds[item] + " ,", "");
					auxiliar.trim();

				} else if (auxiliar.contains(catListIds[item])){
					auxiliar = "";
				}

				Log.d(tag, "elemino categoria "
						+ catListIds[item]);
			}
		}

	
	}
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private class ContentAdapter extends SimpleAdapter implements
			SectionIndexer {

		private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public ContentAdapter(Context context,
				List<? extends Map<String, ?>> data, int textViewResourceId,
				String[] keys, int[] ids) {
			super(context, data, textViewResourceId, keys, ids);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			RelativeLayout v = (RelativeLayout) super.getView(position,
					convertView, parent);
			if (v != null) {
				// CheckBox chk= (CheckBox) v.findViewById(R.id.favCheck);
				// chk.setChecked(true);
				// chk.setButtonDrawable(R.drawable.favcheckbox);
				// v.addView(chk);
				int width = display.getWidth();
				int heightwindow = display.getHeight();

				TextView txtView = (TextView) v.findViewById(R.id.title);

				txtView.setTypeface(tf);

				//
				// v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				// (int)(heightwindow /12 )));
				//
			}

			return v;
		}

		public int getPositionForSection(int section) {
			// If there is no item for current section, previous section will be
			// selected
			Log.d("CLASE", "clase: ");

			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {

						// For numeric section
						for (int k = 0; k <= 9; k++) {
							HashMap<String, String> hm = (HashMap<String, String>) getItem(j);

							if (StringMatcher.match(
									String.valueOf(hm.get("name").charAt(0)),
									String.valueOf(k)))
								return j;
						}
					} else {
						HashMap<String, String> hmap = (HashMap<String, String>) getItem(j);
						if (StringMatcher.match(
								String.valueOf(hmap.get("name").charAt(0)),
								String.valueOf(mSections.charAt(i))))
							return j;
					}

				}
			}
			return 0;
		}

		public int getSectionForPosition(int position) {

			return 0;
		}

		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));

			return sections;
		}

	}

	class Comparador implements Comparator<String> {
		public int compare(String s1, String s2) {
			return s1.toLowerCase().compareTo(s2.toLowerCase());
		}
	}

	class ComparadorMainList implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			AvatarWord p1 = (AvatarWord) o1;
			AvatarWord p2 = (AvatarWord) o2;
			return p1.getNombre().toLowerCase()
					.compareToIgnoreCase(p2.getNombre());
		}
	}
	

	public void afterTextChanged(Editable arg0) {
		autoCtextView.setText(arg0 + "");

	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		autoCtextView.setText(arg0 + "");

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		autoCtextView.setText(arg0 + "");

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Toast.makeText(this, "Cambio la pantalla", Toast.LENGTH_LONG).show();
		int orient = getResources().getConfiguration().orientation;
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQ_START_STANDALONE_PLAYER
				&& resultCode != RESULT_OK) {
			YouTubeInitializationResult errorReason = YouTubeStandalonePlayer
					.getReturnedInitializationResult(data);
			if (errorReason.isUserRecoverableError()) {
				errorReason.getErrorDialog(this, 0).show();
			} else {
				String errorMessage = String.format(
						getString(R.string.error_player),
						errorReason.toString());
				Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
			}
		} else if(requestCode == REQ_CAT_SELECTED){
			
			
			
		} else{
			if(!notShowFavAgain){
		
			StringFav fav;
			String listfav = prefs.getString("favoritos", "nada");
			String tittleFav="";
			if (!listfav.equalsIgnoreCase("nada")) {

				fav = new StringFav(listfav);

			} else {
				// primera vez que agrego a favoritos
				fav = new StringFav("");
			}
			if(fav.isFavorito(nombreVideo)){
				tittleFav=getString(R.string.ButtonDeleteFav);
			}else{
				tittleFav= getString(R.string.fav);
			}
			Log.d(tag, "OnActivityOnresult data: " + data.toString());
			AlertDialog.Builder builderfav = new AlertDialog.Builder(this);

			builderfav.setPositiveButton(getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							modifyFavorito();
						}

					});
			TextView favtext = new TextView(mContext);
			favtext.setBackground(getResources().getDrawable(R.drawable.blue));
			favtext.setText(tittleFav);
			favtext.setTypeface(tf);
			favtext.setGravity(Gravity.CENTER);
			favtext.setTextSize(textSize);
			builderfav.setCustomTitle(favtext);
			CheckBox check = new CheckBox(mContext);
			check.setText(getString(R.string.textoradiobuttondialogfve));
			check.setTypeface(tf);
			check.setBackground(getResources().getDrawable(R.drawable.blue));
			check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						notShowFavAgain=isChecked;
						Editor ed = prefs.edit();
						ed.putBoolean("notShowFavAgain", notShowFavAgain);
						ed.commit();
				}
			});
			favtext.setGravity(Gravity.CENTER);
			favtext.setTextSize(textSize);
			builderfav.setView(check);
			builderfav.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}

					});
		
			AlertDialog dialog = builderfav.create();
			dialog.show();
			Button cancel = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
			cancel.setTypeface(tf);
			cancel.setTextSize(textSize);
			cancel.setBackgroundResource(R.drawable.yellow_button);
			Button accept = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
			accept.setTypeface(tf);
			accept.setTextSize(textSize);
			accept.setBackgroundResource(R.drawable.green_button);
			}
		}
	}

	protected void modifyFavorito() {
		
		StringFav fav;
		String listfav = prefs.getString("favoritos", "nada");
		String nombre = nombreVideo;
		if (!listfav.equalsIgnoreCase("nada")) {

			fav = new StringFav(listfav);

		} else {
			// primera vez que agrego a favoritos
			fav = new StringFav("");
		}

		boolean favorito = fav.isFavorito(nombre);

		if (favorito) {
			// eliminar
			if (!listfav.equalsIgnoreCase("nada")) {

				fav = new StringFav(listfav);
				fav.deleteNombre(nombre);
			}

			Editor ed = prefs.edit();
			ed.putString("favoritos", fav.toString());
			ed.commit();

		} else {

			if (!listfav.equalsIgnoreCase("nada")) {

				fav = new StringFav(listfav);
				fav.setNombre(nombre);

			} else {
				// primera vez que agrego a favoritos
				fav = new StringFav(nombre);
			}

			Editor ed = prefs.edit();
			ed.putString("favoritos", fav.toString());
			ed.commit();

		}

	}
	
	private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
	    public int compare(String str1, String str2) {
	        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
	        if (res == 0) {
	            res = str1.compareTo(str2);
	        }
	        return res;
	    }
	};

}
