
package com.goal.mundial.video;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AvatarWord implements Parcelable{

	private long id;
	private String nombre;
	private String definicion;
	private String enlacevideo;
	private List<String> listaTagsPalabras = new ArrayList<String>();


	
	public String getDefinicion() {
		return definicion;
	}

	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}

	public String getEnlacevideo() {
		return enlacevideo;
	}

	public void setEnlacevideo(String enlacevideo) {
		this.enlacevideo = enlacevideo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {

		return nombre;

	}

	public String getEnlaceVideo() {

		return enlacevideo;

	}

	public void setNombre(String n) {

		nombre = n;

	}

	public void setEnlaceVideo(String v) {

		enlacevideo = v;

	}

	/**
	 * @return the listaTagsPalabras
	 */
	public List<String> getListaTagsPalabras() {
		return listaTagsPalabras;
	}

	/**
	 * @param listaTagsPalabras
	 *            the listaTagsPalabras to set
	 */
	public void setListaTagsPalabras(String elementolistaTagsPalabras) {
		Log.d("Applicacion", "name: " + nombre + "  añado disc : "
				+ !listaTagsPalabras.contains(elementolistaTagsPalabras)
				+ " - " + elementolistaTagsPalabras);
		if (!listaTagsPalabras.contains(elementolistaTagsPalabras)) {
			this.listaTagsPalabras.add(elementolistaTagsPalabras);

			Log.d("Applicacion", "name: " + nombre + "  añado  : " + " - "
					+ elementolistaTagsPalabras);
		}
		// this.listaTagsPalabras = listaTagsPalabras;
	}
	
	@Override
	public String toString() {
		return "Campo [id=" + id + ", nombre=" + nombre + ", enlacevideo="
				+ enlacevideo + ", listaTagsPalabras=" + listaTagsPalabras
				+ "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(id);
	    dest.writeString(definicion);
		dest.writeString(enlacevideo);
		dest.writeString(nombre);
		dest.writeStringList(listaTagsPalabras);
	}
	
	public AvatarWord(Parcel in){
		id = in.readLong();
		definicion = in.readString();
		enlacevideo = in.readString();
		nombre = in.readString();
		in.readStringList(listaTagsPalabras);
	}
	
	public AvatarWord(){}
	
	public static final Parcelable.Creator<AvatarWord> CREATOR
    = new Parcelable.Creator<AvatarWord>() 
   {
         public AvatarWord createFromParcel(Parcel in) {
             return new AvatarWord(in);
         }

         public AvatarWord[] newArray (int size) {
             return new AvatarWord[size];
         }
    };

}
