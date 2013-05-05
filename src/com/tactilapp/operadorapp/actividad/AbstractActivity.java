package com.tactilapp.operadorapp.actividad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdView;
import com.tactilapp.operadorapp.R;
import com.tactilapp.operadorapp.actividad.informacion.InformacionActivity;

public abstract class AbstractActivity extends Activity {

	protected abstract int obtenerVista();

	protected RelativeLayout fondo;

	protected LinearLayout panel;

	protected View plantilla;
	protected View vista;
	protected AdView anuncio;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ensamblarLaVistaConLaPlantilla();

		pintarElContenedorDeLaInformacion();
	}

	protected void ensamblarLaVistaConLaPlantilla() {
		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		plantilla = inflater.inflate(obtenerLaPlantilla(), null);
		fondo = (RelativeLayout) plantilla.findViewById(obtenerIdDelFondo());
		anuncio = (AdView) plantilla.findViewById(obtenerIdDelAnuncio());
		panel = (LinearLayout) plantilla.findViewById(obtenerIdDelContenedor());

		vista = inflater.inflate(obtenerVista(), null);
		panel.addView(vista);

		setContentView(plantilla);
		
		if ("ejemplo".equalsIgnoreCase(getString(R.string.admob_unit_id))) {
			anuncio.setVisibility(View.GONE);
		}
	}

	protected void pintarElContenedorDeLaInformacion() {
		fondo.post(new Runnable() {
			@Override
			public void run() {
				final int anchuraDelPanel = (int) (fondo.getWidth() * 0.82);
				panel.getLayoutParams().width = anchuraDelPanel;
			}
		});
	}

	protected int obtenerLaPlantilla() {
		return R.layout.plantilla;
	}

	protected int obtenerIdDelContenedor() {
		return R.id.panel;
	}

	protected int obtenerIdDelFondo() {
		return R.id.fondo;
	}

	protected int obtenerIdDelAnuncio() {
		return R.id.pie;
	}

	public void irAInformacion(final View view) {
		if (!InformacionActivity.class.equals(getClass())) {
			final Intent intent = new Intent(this, InformacionActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
	}

	public void volver(final View view) {
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	public void onStart() {
		super.onStart();
		final String idFlurry = getString(R.string.flurry_id);

		if (!"ejemplo".equalsIgnoreCase(idFlurry)) {
			FlurryAgent.onStartSession(this, idFlurry);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		final String idFlurry = getString(R.string.flurry_id);
		if (!"ejemplo".equalsIgnoreCase(idFlurry)) {
			FlurryAgent.onEndSession(this);
		}
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
			mostrarAvisoDeQueSeVaACerrarLaAplicacion();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	protected void mostrarAvisoDeQueSeVaACerrarLaAplicacion() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.salir)
				.setMessage(R.string.seguro_de_salir)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int which) {
								finish();
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

}