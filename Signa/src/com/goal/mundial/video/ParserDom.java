
package com.goal.mundial.video;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

public class ParserDom {

	private URL rssUrl;
	
	private String categorias [];
	
	XmlData xmlData = new XmlData();

	public ParserDom(String url, Context ct) {

		try {

			this.rssUrl = new URL(url);

		} catch (MalformedURLException e) {

			throw new RuntimeException(e);
		}
	}

public XmlData parse() {

		// Instanciamos la fábrica para DOM
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		ArrayList<AvatarWord> avatares = new ArrayList<AvatarWord>();
		
		try {

			// Creamos un nuevo parser DOM
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Realizamos la lectura completa del XML
			Document dom = builder.parse(this.getInputStream());

			// Nos posicionamos en el nodo principal del árbol
			Element root = dom.getDocumentElement();

			// Localizamos todos los elementos
			NodeList institution = root.getElementsByTagName("institution");
			
			NodeList categories = root.getElementsByTagName("categories");
			
			NodeList words = root.getElementsByTagName("word");

			InstitutionCategories instCat = new InstitutionCategories();
			
			instCat.setWordCategoriesValue("Todas");
			
			instCat.setWordCategoriesId("TODAS");
			
			for (int a = 0; a < institution.getLength(); a ++){
				
				Node itemInst = institution.item(a);
				
				NodeList instData = itemInst.getChildNodes();

				for (int b = 0; b < instData.getLength(); b++) {

					// Log.d("Tiene ",j + " datos");
					Node instDataTag = instData.item(b);
					String instTag = instDataTag.getNodeName();
				
					if (instTag.equals("id")) {
						
						String instName = obtenerTexto(instDataTag);
						instCat.setInstName(instName);
						
					} else if (instTag.equals("desc")) {

						String instDesc = obtenerTexto(instDataTag);
						instCat.setInstDesc(instDesc);
						
					} else if (instTag.equals("link")) {

						String instLink = obtenerTexto(instDataTag);
						instCat.setInstLink(instLink);
						
					}
				
				}
						
			}
			
			for (int c = 0; c < categories.getLength(); c ++){
				
				Node itemInst = categories.item(c);
				
				NodeList catData = itemInst.getChildNodes();

				for (int d = 0; d < catData.getLength(); d++) {

					// Log.d("Tiene ",j + " datos");
					Node catDataTag = catData.item(d);
					String instTag = catDataTag.getNodeName();
				
					if (instTag.equals("category")) {
						
						NodeList datosCategoria = catDataTag.getChildNodes();

						for (int k = 0; k < datosCategoria.getLength(); k++) {

							Node datoCategoria = datosCategoria.item(k);
							String etiquetaCategoria = datoCategoria.getNodeName();

							if (etiquetaCategoria.equals("id")) {

								String catId = obtenerTexto(datoCategoria);
								instCat.setWordCategoriesId(catId);
								Log.d("Ids introducidos", catId);
								// Log.d("Titulo App: ", titulo);

							} else if (etiquetaCategoria.equals("value")) {
									
								String catName = obtenerTexto(datoCategoria);
								instCat.setWordCategoriesValue(catName);
								Log.d("Categorías introducidas: ", catName);
											
							}
						
						}

					}
				}	
				
//				Log.d("Estoy añadiendo bien las categorías", "Porque tiene tamaño " + instCat.getWordCategoriesId().size());
				
			}
			
			categorias = new String[instCat.getWordCategoriesId().size()];
			
			categorias = instCat.getWordCategoriesId().toArray(categorias);
			
//			Log.d("Longitud de ArrayList IdsCat", "Es " + instCat.getWordCategoriesId().size());
//			
//			for (int a=0; a < categorias.length;a++){
//				
//				Log.d("Categorías añadidas", categorias[a]);
//				
//			}
			
			xmlData.setInstitutionInfo(instCat);
	
//			Log.d("Longitud de ArrayList IdsCat", "Es " + instCat.getWordCategoriesId().size());
//			
//			for (int a=0; a < categorias.length;a++){
//				
//				Log.d("Categorías añadidas", categorias[a]);
//				
//			}
			
			// Recorremos la lista
			for (int i = 0; i < words.getLength(); i++) {

				AvatarWord avatarword = new AvatarWord();

				Node item = words.item(i);
				// Log.d("avatarword: ", "numero "+i);

				NodeList datosAvatar = item.getChildNodes();

				for (int j = 0; j < datosAvatar.getLength(); j++) {

					// Log.d("Tiene ",j + " datos");
					Node dato = datosAvatar.item(j);
					String etiqueta = dato.getNodeName();

					if (etiqueta.equals("name")) {

						NodeList datosTitulo = dato.getChildNodes();

						for (int k = 0; k < datosTitulo.getLength(); k++) {

							Node datoTitulo = datosTitulo.item(k);
							String etiquetaTag = datoTitulo.getNodeName();

							if (etiquetaTag.equals("valor")) {

								String titulo = obtenerTexto(datoTitulo);
								avatarword.setNombre(titulo);
								// Log.d("Titulo App: ", titulo);

							}

						}

						
					} else if (etiqueta.equals("def")) {

						String defpalabra = obtenerTexto(dato);
						avatarword.setDefinicion(defpalabra);	
						
					} else if (etiqueta.equals("url")) {

						String urlvideo = obtenerTexto(dato);
						avatarword.setEnlaceVideo(urlvideo);

					} else if (etiqueta.equals("tags")){
                    	
//                    	Log.d("Nombre etiqueta",dato.getNodeName());
                    	
                    	NodeList datosDiscapacidades = dato.getChildNodes();
                    	
                    	for(int n=0; n<datosDiscapacidades.getLength();n++){
                    		
                    		Node datoDiscapacidad = datosDiscapacidades.item(n);
                    		String etiquetaDiscapacidad = datoDiscapacidad.getNodeName();
                    		
                    		if(etiquetaDiscapacidad.equals("tag")){
                    			
//                    			Log.d("Nombre etiqueta hija",obtenerTexto(datoDiscapacidad));
                    			
                    			String tagCategory = obtenerTexto(datoDiscapacidad);
//    	                    	
//                    			Log.d("Tamaño de categorias", "" + categorias.length);
                    			
                    			
                    					for (int m = 0; m < categorias.length;m++){
                    						
                    						if(tagCategory.equals(categorias[m])){
                    							
                    							avatarword.setListaTagsPalabras(tagCategory);
//                    							Log.d("Se ha añadido", tagCategory + " a la lista");
                    						}
                    						
                    					}

    	                    	}

                    	}	
                    	
                    }

				}

				// Log.d("Tamaño Lista", "Número :" +
				// avatares.getListaDiscapacidades().size());

				avatares.add(avatarword);

				// Log.d("Tamaño Lista", "Número :" +
				// avatares.getListaDiscapacidades().size());
			}
			
			xmlData.setListWords(avatares);
			
			
			
			
		} catch (Exception ex) {

			ex.printStackTrace();
			return null;

		}

		return xmlData;
	}


	private String obtenerTexto(Node dato) {

		StringBuilder texto = new StringBuilder();
		NodeList fragmentos = dato.getChildNodes();

		for (int k = 0; k < fragmentos.getLength(); k++) {
	
			texto.append(fragmentos.item(k).getNodeValue());
	
		}
	
		return texto.toString();
	}


	private InputStream getInputStream() {

		try {

			return rssUrl.openConnection().getInputStream();

		} catch (IOException e) {

			throw new RuntimeException(e);

		}
	}

}
