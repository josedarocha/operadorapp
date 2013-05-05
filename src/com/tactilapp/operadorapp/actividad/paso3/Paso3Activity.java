package com.tactilapp.operadorapp.actividad.paso3;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.tactilapp.operadorapp.OperadorappApplication;
import com.tactilapp.operadorapp.R;
import com.tactilapp.operadorapp.actividad.AbstractActivity;
import com.tactilapp.operadorapp.actividad.paso1.Paso1Activity;
import com.tactilapp.operadorapp.modelo.Companhia;

public class Paso3Activity extends AbstractActivity {

	private TextView textoDeLaCompanhia;

	private String numero;
	private String nombreDeLaCompanhia;
	private Companhia companhia;

	private OperadorappApplication contenedor;

	@Override
	protected int obtenerVista() {
		return R.layout.paso3;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contenedor = (OperadorappApplication) getApplication();

		numero = getIntent().getStringExtra("numero");
		nombreDeLaCompanhia = getIntent().getStringExtra("companhia");

		textoDeLaCompanhia = (TextView) findViewById(R.id.companhia);
		textoDeLaCompanhia.setText(nombreDeLaCompanhia);

		companhia = contenedor.obtenerLaCompanhia(nombreDeLaCompanhia);
		if (companhia != null) {
			final Map<String, String> nombreCompanhia = new HashMap<String, String>();
			nombreCompanhia.put("nombre", companhia.getNombre());
			FlurryAgent.logEvent("Compañía cargada", nombreCompanhia, true);
			
			final GradientDrawable degradado = new GradientDrawable(
					GradientDrawable.Orientation.TOP_BOTTOM,
					new int[] { obtenerElColorSuperior(),
							obtenerElColorInferior() });
			degradado.setCornerRadius(0f);
			textoDeLaCompanhia.setBackgroundDrawable(degradado);
		}
	}

	private int obtenerElColorSuperior() {
		Integer colorSuperior = null;
		if (companhia.tieneColorSuperior()) {
			colorSuperior = (int) (Long.parseLong(
					"FF" + companhia.getColorSuperior(), 16));
		} else if (companhia.tieneColorInferior()) {
			colorSuperior = (int) (Long.parseLong(
					"FF" + companhia.getColorInferior(), 16));
		} else {
			colorSuperior = (int) (Long.parseLong("FF"
					+ contenedor.obtenerLaCompanhiaPorDefecto()
							.getColorSuperior(), 16));
		}
		return colorSuperior;
	}

	private int obtenerElColorInferior() {
		Integer colorInferior = null;
		if (companhia.tieneColorInferior()) {
			colorInferior = (int) (Long.parseLong(
					"FF" + companhia.getColorInferior(), 16));
		} else if (companhia.tieneColorSuperior()) {
			colorInferior = (int) (Long.parseLong(
					"FF" + companhia.getColorSuperior(), 16));
		} else {
			colorInferior = (int) (Long.parseLong("FF"
					+ contenedor.obtenerLaCompanhiaPorDefecto()
							.getColorInferior(), 16));
		}
		return colorInferior;
	}

	public void irAlPaso1(final View view) {

		final Intent intent = new Intent(this, Paso1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public void irALlamar(final View view) {
		final Uri numeroDeTelefono = Uri.parse("tel:" + numero);
		startActivity(new Intent(Intent.ACTION_CALL, numeroDeTelefono));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	public void onBackPressed() {
		irAlPaso1(null);
	}

}