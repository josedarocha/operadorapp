package com.tactilapp.operadorapp.post;

import android.util.Log;

import com.tactilapp.operadorapp.modelo.RespuestaALaPeticionDeCaptcha;
import com.tactilapp.operadorapp.modelo.RespuestaALaComprobacionDeCompanhia;

public class EnvioDePostParaComprobarLaCompanhia extends AbstractEnvioDePost<RespuestaALaComprobacionDeCompanhia> {

	protected String url;

	public EnvioDePostParaComprobarLaCompanhia(String url) {
		super(url);
	}

	public static RespuestaALaComprobacionDeCompanhia enviar(final RespuestaALaPeticionDeCaptcha datosDeEntrada,
			final String informacionAEnviar) throws Exception {

		try {
			final EnvioDePostParaComprobarLaCompanhia lector = new EnvioDePostParaComprobarLaCompanhia(datosDeEntrada.urlFinal);
			final RespuestaALaComprobacionDeCompanhia respuesta = lector.enviarPeticion(
					datosDeEntrada.cookie, informacionAEnviar);
			return respuesta;
		} catch (final Exception excepcion) {
			Log.d("EnvioDePost", "ERROR con el envío de la petición POST",
					excepcion);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class obtenerClaseAGenerar() {
		return RespuestaALaComprobacionDeCompanhia.class;
	}

}
