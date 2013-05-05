package com.tactilapp.operadorapp.actividad.paso2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.tactilapp.operadorapp.CargadorAsincronoDeDatos;
import com.tactilapp.operadorapp.Constantes;
import com.tactilapp.operadorapp.GestorAsincronoDeImagenes;
import com.tactilapp.operadorapp.GestorAsincronoDeImagenes.AlTerminarListener;
import com.tactilapp.operadorapp.R;
import com.tactilapp.operadorapp.Utils;
import com.tactilapp.operadorapp.actividad.AbstractActivity;
import com.tactilapp.operadorapp.actividad.paso3.Paso3Activity;
import com.tactilapp.operadorapp.componente.Aviso;
import com.tactilapp.operadorapp.componente.Aviso.AlAceptarListener;
import com.tactilapp.operadorapp.componente.ImagenDeAnchuraCompleta;
import com.tactilapp.operadorapp.json.LectorDesdeJSONDeLaDireccionDelCaptcha;
import com.tactilapp.operadorapp.modelo.RespuestaALaComprobacionDeCompanhia;
import com.tactilapp.operadorapp.modelo.RespuestaALaPeticionDeCaptcha;
import com.tactilapp.operadorapp.post.EnvioDePostParaComprobarLaCompanhia;

public class Paso2Activity extends AbstractActivity {

	private Activity actividad;

	private String numero;
	private EditText textoDelCaptcha;

	private GestorAsincronoDeImagenes gestorAsincronoDeImagenes;
	private ImagenDeAnchuraCompleta imagenDelCaptcha;
	private Bitmap imagenDefectoDelCaptcha;

	private Boolean hayCaptcha = false;
	private RespuestaALaPeticionDeCaptcha respuesta;

	@Override
	protected int obtenerVista() {
		return R.layout.paso2;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.actividad = this;

		numero = getIntent().getStringExtra("numero");

		textoDelCaptcha = (EditText) findViewById(R.id.texto_captcha);
		imagenDelCaptcha = (ImagenDeAnchuraCompleta) findViewById(R.id.imagen_captcha);

		imagenDefectoDelCaptcha = BitmapFactory.decodeResource(getResources(),
				R.drawable.cargando_captcha);
		gestorAsincronoDeImagenes = new GestorAsincronoDeImagenes(
				imagenDefectoDelCaptcha);

		gestorAsincronoDeImagenes
				.fijarAlTerminarListener(new AlTerminarListener() {

					@Override
					public void accionLanzadaAlTerminar(final Boolean exito) {
						if (Boolean.TRUE.equals(exito)) {
							hayCaptcha = true;
						} else {
							Toast.makeText(
									actividad,
									R.string.paso2_captcha_no_puede_descargarse,
									Toast.LENGTH_LONG).show();
							hayCaptcha = false;
						}

					}
				});

		generarLaTareaParaObtenerElCaptcha();
	}

	private void generarLaTareaParaObtenerElCaptcha() {
		final CargadorAsincronoDeDatos cargador = new CargadorAsincronoDeDatos(
				this) {

			private Boolean hayConexion = false;

			@Override
			protected int obtenerTituloDeLaBarraDeProgreso() {
				return R.string.paso2_titulo;
			}

			@Override
			protected String obtenerMensajeDeLaBarraDeProgreso() {
				return actividad.getResources().getString(
						R.string.paso2_mensaje_cargando);
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					if (Utils.hayConexionAInternet(actividad, null)) {
						hayConexion = true;
						respuesta = LectorDesdeJSONDeLaDireccionDelCaptcha
								.cargar(Constantes.URL_OPERADORAPP);
					} else {
						hayConexion = false;
						respuesta = null;
					}
				} catch (final Exception excepcion) {
					respuesta = null;
				}
				return null;
			}

			@Override
			protected void tareasTrasObtenerRespuesta() {
				if (Boolean.TRUE.equals(hayConexion) && respuesta != null) {
					cargarLaImagenDelCaptcha();
				} else {
					imagenDelCaptcha.setVisibility(View.GONE);
					mostrarAvisoDeErrorAlRecibirRespuesta(obtenerTituloDeLaBarraDeProgreso());
				}
			}
		};

		cargador.execute();
	}

	protected void mostrarAvisoDeErrorAlRecibirRespuesta(
			final Integer tituloDelAviso) {
		mostrarAvisoDeErrorAlRecibirRespuesta(tituloDelAviso, null);
	}

	protected void mostrarAvisoDeErrorAlRecibirRespuesta(
			final Integer tituloDelAviso, final Integer mensaje) {
		final Aviso aviso = new Aviso(this);
		aviso.setTitle(tituloDelAviso);
		if (mensaje == null) {
			aviso.setMessage(getResources().getString(
					R.string.sin_conexion_a_internet));
		} else {
			aviso.setMessage(getResources().getString(mensaje));
		}
		aviso.fijarAlAceptarListener(new AlAceptarListener() {
			@Override
			public void accionLanzadaAlAceptar() {
				volver(null);
			}
		});
		aviso.show();
	}

	protected void cargarLaImagenDelCaptcha() {
		gestorAsincronoDeImagenes.descargar(respuesta.urlDelCaptcha,
				imagenDelCaptcha);
	}

	public void recargarElCaptcha(final View view) {
		FlurryAgent.logEvent("Recargar Captcha", true);
		imagenDelCaptcha.setImageBitmap(imagenDefectoDelCaptcha);
		generarLaTareaParaObtenerElCaptcha();
	}

	public void irAlResultado(final View view) {
		if (Boolean.TRUE.equals(hayCaptcha)) {
			if (Utils.haMetidoAlgoValidoEnElCampo(this, textoDelCaptcha, 5,
					R.string.error_captcha_corto, R.string.error_captcha_vacio)
					&& Utils.hayConexionAInternet(this)) {
				comprobarElNumero();
			}
		} else {
			Toast.makeText(actividad, R.string.paso2_aun_no_hay_captcha,
					Toast.LENGTH_LONG).show();
		}
	}

	private void comprobarElNumero() {
		final CargadorAsincronoDeDatos cargador = new CargadorAsincronoDeDatos(
				this) {
			private RespuestaALaComprobacionDeCompanhia respuestaALaComprobacionDelCaptcha;
			private Boolean hayConexion = false;

			@Override
			protected int obtenerTituloDeLaBarraDeProgreso() {
				return R.string.paso2_titulo;
			}

			@Override
			protected String obtenerMensajeDeLaBarraDeProgreso() {
				return actividad.getResources().getString(
						R.string.paso2_mensaje_comprobando);
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					if (Utils.hayConexionAInternet(actividad, null)) {
						hayConexion = true;
						final String informacionAEnviar = "apiv="
								+ respuesta.version + "&mobile=" + numero
								+ "&captcha_str="
								+ textoDelCaptcha.getText().toString();
						respuestaALaComprobacionDelCaptcha = EnvioDePostParaComprobarLaCompanhia
								.enviar(respuesta, informacionAEnviar);
					} else {
						hayConexion = false;
						respuestaALaComprobacionDelCaptcha = null;
					}
				} catch (final Exception excepcion) {
					respuestaALaComprobacionDelCaptcha = null;
				}
				return null;
			}

			@Override
			protected void tareasTrasObtenerRespuesta() {
				if (Boolean.TRUE.equals(hayConexion)
						&& respuestaALaComprobacionDelCaptcha != null) {
					if (respuestaALaComprobacionDelCaptcha
							.laComprobacionEsExitosa()) {
						final Intent intent = new Intent(actividad,
								Paso3Activity.class);
						intent.putExtra("numero", numero);
						intent.putExtra("companhia",
								respuestaALaComprobacionDelCaptcha
										.obtenerLaCompanhia());
						startActivity(intent);
						setResult(RESULT_OK);
						finish();
						overridePendingTransition(R.anim.fade_in,
								R.anim.fade_out);
					} else {
						final Aviso aviso = new Aviso(actividad);
						aviso.setTitle(R.string.paso2_titulo);
						aviso.setMessage(respuestaALaComprobacionDelCaptcha
								.obtenerElMensajeDeError());
						aviso.fijarAlAceptarListener(new AlAceptarListener() {
							@Override
							public void accionLanzadaAlAceptar() {
								textoDelCaptcha.setText("");
								generarLaTareaParaObtenerElCaptcha();
							}
						});
						aviso.show();

					}
				} else {
					mostrarAvisoDeErrorAlRecibirRespuesta(
							obtenerTituloDeLaBarraDeProgreso(),
							R.string.paso2_mensaje_sin_respuesta);
				}

			}
		};

		cargador.execute();
	}
}