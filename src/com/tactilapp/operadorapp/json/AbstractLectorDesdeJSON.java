package com.tactilapp.operadorapp.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.google.gson.Gson;

public abstract class AbstractLectorDesdeJSON<E> {

	protected String url;
	protected String urlFinal;
	protected String cookie;

	@SuppressWarnings("rawtypes")
	protected abstract Class obtenerClaseRecibida();

	public AbstractLectorDesdeJSON(String url) {
		super();
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	protected E obtenerRespuesta() {
		return (E) new Gson().fromJson(leerJSON(), obtenerClaseRecibida());
	}

	protected String obtenerLaURLFinal() {
		return urlFinal;
	}
	
	protected String obtenerLaCookie() {
		return cookie;
	}

	public String leerJSON() {
		BufferedReader entrada = null;

		final HttpClient cliente = new DefaultHttpClient();
		cliente.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				"android");
		HttpParams params = cliente.getParams();
		HttpClientParams.setRedirecting(params, false);

		if (url == null || "".equals(url.trim())) {
			throw new IllegalStateException(
					"NO PUEDES PASAR UNA URL VACÍA AL LECTOR DE JSON");
		}

		HttpGet peticion = new HttpGet(url);
		urlFinal = url;

		try {
			HttpResponse respuesta = cliente.execute(peticion);
			int codigoDeEstado = respuesta.getStatusLine().getStatusCode();

			if (codigoDeEstado == HttpStatus.SC_MOVED_PERMANENTLY) {
				final Header cabecera = respuesta.getLastHeader("Location");
				if (cabecera != null) {
					peticion.abort();

					urlFinal = cabecera.getValue();
					peticion = new HttpGet(urlFinal);
					respuesta = cliente.execute(peticion);
					codigoDeEstado = respuesta.getStatusLine().getStatusCode();
				}
			}
			
			if (codigoDeEstado != HttpStatus.SC_OK) {
				Log.e(getClass().getSimpleName(), "Error " + codigoDeEstado
						+ " para la URL " + url);
				return null;
			}
			
			
			cookie = respuesta.getLastHeader("Set-Cookie").getValue();
			final HttpEntity entidadDeLaRespuesta = respuesta.getEntity();
			entrada = new BufferedReader(new InputStreamReader(
					entidadDeLaRespuesta.getContent()));

			final StringBuffer contenido = new StringBuffer("");
			String linea = "";

			String caracterDeNuevaLinea = System.getProperty("line.separator");
			while ((linea = entrada.readLine()) != null) {
				contenido.append(linea + caracterDeNuevaLinea);
			}
			entrada.close();

			return contenido.toString();
		} catch (final Exception excepcion) {
			peticion.abort();
			Log.e(getClass().getSimpleName(), "Error para la URL " + url,
					excepcion);
		} finally {
			if (entrada != null) {
				try {
					entrada.close();
				} catch (IOException e) {
					Log.e(getClass().getSimpleName(), e.toString());
				}
			}
		}

		return null;
	}
}
