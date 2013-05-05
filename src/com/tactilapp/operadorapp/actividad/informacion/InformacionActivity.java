package com.tactilapp.operadorapp.actividad.informacion;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;

import com.tactilapp.operadorapp.Constantes;
import com.tactilapp.operadorapp.R;
import com.tactilapp.operadorapp.actividad.AbstractActivity;

public class InformacionActivity extends AbstractActivity {

	@Override
	protected int obtenerVista() {
		return R.layout.informacion;
	}

	public void irALaCMT(final View view) {
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse(Constantes.URL_CMT)));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public void irATactilapp(final View view) {
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse(Constantes.URL_TACTILAPP)));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public void irAEnviarSugerencia(final View view) {
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND_MULTIPLE);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { Constantes.MAIL_SOPORTE_TACTILAPP });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.sugerencia_asunto));
		emailIntent.putExtra(
				android.content.Intent.EXTRA_TEXT,
				Html.fromHtml(getResources().getString(
						R.string.sugerencia_texto)));
		emailIntent.setType("image/png");

		startActivity(Intent.createChooser(emailIntent, getResources()
				.getString(R.string.sugerencia_cabecera)));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}
