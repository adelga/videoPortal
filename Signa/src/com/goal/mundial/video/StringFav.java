package com.goal.mundial.video;

import java.util.ArrayList;

import android.util.Log;

/*
 * clase para tener en un string los nombres de las palabras favoritas, el
 * formato es "nombre1;nombre2;nombre3;"
 */
public class StringFav {

	private ArrayList<String> favList;

	public ArrayList<String> getFavList() {
		return favList;
	}

	public void setFavList(ArrayList<String> favList) {
		this.favList = favList;
	}

	public StringFav(String lista) {
		String[] l = lista.split(";");
		favList = new ArrayList<String>();
		for (int i = 0; i < l.length; i++) {
			Log.d("Stringauyx", "palabra: " + l[i]);
			favList.add(l[i]);
		}
		deleteNombre("");
	}

	public void setNombre(String nombre) {
		
		favList.add(nombre);
	}
	
	public void deleteNombre(String nombre){
		for(int i=0; i<favList.size();i++){
			if(favList.get(i).equalsIgnoreCase(nombre)){
				favList.remove(i);
			}
		}
	}

	@Override
	public String toString() {
		String aux = "";
		for (int i = 0; i < favList.size(); i++) {
			if (!aux.equals("")) {
				Log.d("StringFav", "añado: " + favList.get(i));
				aux = aux + ";" + favList.get(i);
			} else {
				aux = favList.get(i);
				Log.d("StringFav", "añado: " + favList.get(i));
			}
		}
		Log.d("StringFav", "favoritos " + aux);
		return aux;
	}
	
	public boolean isFavorito(String nombre){
		Log.d("StringFav", "favoritos " + favList.toString());
		boolean b=false;
		for(int i=0; i <favList.size();i++){
			if(favList.get(i).equalsIgnoreCase(nombre)){
				b=true;
				break;
			}
		}
		return b;
	}

}
