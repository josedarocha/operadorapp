package com.tactilapp.operadorapp.xml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.tactilapp.operadorapp.R;
import com.tactilapp.operadorapp.modelo.Companhia;

public class XML implements Serializable {

	private static final long serialVersionUID = -4610900994424063890L;

	public enum ESTADO {
		SIN_COMENZAR, LEIDO_NOMBRE_DE_LA_COMPANHIA, LEYENDO_PARAMETROS, LEYENDO_COLOR_SUPERIOR, LEYENDO_COLOR_INFERIOR, LEYENDO_POSIBLES_CADENAS
	}

	private static final String DICT = "dict";
	private static final String ARRAY = "array";
	private static final String POSIBLES_CADENAS = "strings";
	private static final String COLOR_SUPERIOR = "top";
	private static final String COLOR_INFERIOR = "bottom";

	public static Map<String, Companhia> parsearXml(final Context contexto) {

		ESTADO estado = ESTADO.SIN_COMENZAR;
		final Map<String, Companhia> companhias = new HashMap<String, Companhia>();
		Companhia companhia = new Companhia();

		final XmlResourceParser xpp = contexto.getResources().getXml(R.xml.companies_color);
		try {
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xpp.getEventType() == XmlPullParser.START_TAG
						&& ESTADO.LEIDO_NOMBRE_DE_LA_COMPANHIA.equals(estado)
						&& DICT.equals(xpp.getName())) {
					estado = ESTADO.LEYENDO_PARAMETROS;
				} else if (xpp.getEventType() == XmlPullParser.END_TAG
						&& ESTADO.LEYENDO_POSIBLES_CADENAS.equals(estado)
						&& ARRAY.equals(xpp.getName())) {
					estado = ESTADO.LEYENDO_PARAMETROS;
				} else if (xpp.getEventType() == XmlPullParser.END_TAG
						&& DICT.equals(xpp.getName())) {
					estado = ESTADO.SIN_COMENZAR;
					companhias.put(companhia.getNombre(), companhia);
					companhia = new Companhia();
				} else if (xpp.getEventType() == XmlPullParser.END_TAG) {
				} else if (xpp.getEventType() == XmlPullParser.TEXT) {

					if (ESTADO.SIN_COMENZAR.equals(estado)) {
						companhia.setNombre(xpp.getText());
						estado = ESTADO.LEIDO_NOMBRE_DE_LA_COMPANHIA;
					} else if (ESTADO.LEYENDO_PARAMETROS.equals(estado)) {

						if (COLOR_INFERIOR.equalsIgnoreCase(xpp.getText())) {
							estado = ESTADO.LEYENDO_COLOR_INFERIOR;
						} else if (COLOR_SUPERIOR.equalsIgnoreCase(xpp
								.getText())) {
							estado = ESTADO.LEYENDO_COLOR_SUPERIOR;
						} else if (POSIBLES_CADENAS.equalsIgnoreCase(xpp
								.getText())) {
							estado = ESTADO.LEYENDO_POSIBLES_CADENAS;
						}

					} else {

						if (ESTADO.LEYENDO_COLOR_INFERIOR.equals(estado)) {
							companhia.setColorInferior(xpp.getText());
							estado = ESTADO.LEYENDO_PARAMETROS;
						} else if (ESTADO.LEYENDO_COLOR_SUPERIOR.equals(estado)) {
							companhia.setColorSuperior(xpp.getText());
							estado = ESTADO.LEYENDO_PARAMETROS;
						} else if (ESTADO.LEYENDO_POSIBLES_CADENAS == estado) {
							companhia.anhadirPosibleCadena(xpp.getText());
						}
					}
				}
				xpp.next();
			}

			return companhias;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}