package com.tactilapp.operadorapp.post;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import android.util.Log;

import com.google.gson.Gson;

public abstract class AbstractEnvioDePost<E> {

	protected String url;

	@SuppressWarnings("rawtypes")
	protected abstract Class obtenerClaseAGenerar();

	public AbstractEnvioDePost(String url) {
		super();
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	protected E enviarPeticion(final String cookie,
			final String informacionAEnviar) {
		return (E) new Gson().fromJson(
				generarPeticion(cookie, informacionAEnviar),
				obtenerClaseAGenerar());
	}

	protected String generarPeticion(final String cookie,
			final String informacionAEnviar) {
		URL urlALaQueConectar;
		HttpURLConnection conexion;

		Scanner entrada = null;
		try {
			urlALaQueConectar = new URL(url);

			conexion = (HttpURLConnection) urlALaQueConectar.openConnection();
			conexion.setDoOutput(true);
			conexion.setRequestMethod("POST");
			conexion.setFixedLengthStreamingMode(informacionAEnviar.getBytes().length);
			if (cookie != null && !"".equals(cookie)) {
				conexion.addRequestProperty("Cookie", cookie);
			}

			final PrintWriter salida = new PrintWriter(
					conexion.getOutputStream());
			salida.print(informacionAEnviar);
			salida.close();

			String respuesta = "";
			entrada = new Scanner(conexion.getInputStream());
			while (entrada.hasNextLine()) {
				respuesta += (entrada.nextLine());
			}

			entrada.close();
			return respuesta;
		} catch (final Exception excepcion) {
			Log.e(getClass().getSimpleName(), "Error para la URL " + url,
					excepcion);
		} finally {
			if (entrada != null) {
				entrada.close();
			}
		}
		return null;
	}

}
