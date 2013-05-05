package com.tactilapp.operadorapp.actividad.splash;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.tactilapp.operadorapp.Constantes;
import com.tactilapp.operadorapp.OperadorappApplication;
import com.tactilapp.operadorapp.R;
import com.tactilapp.operadorapp.actividad.paso1.Paso1Activity;
import com.tactilapp.operadorapp.xml.XML;

public class SplashActivity extends Activity {

	private Activity actividad;
	private OperadorappApplication contenedor;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		contenedor = (OperadorappApplication) getApplication();
		actividad = this;

		new CargadorAsincronoDeDatos().execute();
	}

	private class CargadorAsincronoDeDatos extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				final Date inicio = new Date();
				contenedor.fijarCompanhias(XML.parsearXml(actividad));
				final Date fin = new Date();
				final Long diferenciaEnMS = fin.getTime() - inicio.getTime();
				if (diferenciaEnMS <= Constantes.TIEMPO_SPLASH) {
					final int tiempoDeEspera = (int) (Constantes.TIEMPO_SPLASH - diferenciaEnMS);
					Thread.sleep(tiempoDeEspera);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			actividad.startActivity(new Intent(actividad, Paso1Activity.class));
			actividad.finish();
			actividad
					.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}

	}
}