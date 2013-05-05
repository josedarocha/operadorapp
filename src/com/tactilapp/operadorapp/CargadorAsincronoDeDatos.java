package com.tactilapp.operadorapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class CargadorAsincronoDeDatos extends
		AsyncTask<Void, Void, Void> {

	private ProgressDialog barraDeProgreso = null;
	private final Activity actividad;

	protected abstract int obtenerTituloDeLaBarraDeProgreso();

	protected abstract String obtenerMensajeDeLaBarraDeProgreso();

	public CargadorAsincronoDeDatos(final Activity actividad) {
		this.actividad = actividad;
	}

	@Override
	protected void onPreExecute() {
		barraDeProgreso = new ProgressDialog(actividad);
		barraDeProgreso.setTitle(obtenerTituloDeLaBarraDeProgreso());
		barraDeProgreso.setMessage(obtenerMensajeDeLaBarraDeProgreso());
		barraDeProgreso.setCancelable(false);
		barraDeProgreso.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		if (barraDeProgreso.isShowing()) {
			barraDeProgreso.dismiss();
		}

		tareasTrasObtenerRespuesta();
	}

	protected void tareasTrasObtenerRespuesta() {
	}

}