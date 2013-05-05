package com.tactilapp.operadorapp.json;

import android.util.Log;

import com.tactilapp.operadorapp.modelo.RespuestaALaPeticionDeCaptcha;

public class LectorDesdeJSONDeLaDireccionDelCaptcha extends AbstractLectorDesdeJSON<RespuestaALaPeticionDeCaptcha> {

	public LectorDesdeJSONDeLaDireccionDelCaptcha(String url) {
		super(url);
	}

	public static RespuestaALaPeticionDeCaptcha cargar(final String url) throws Exception {

		try {
			final LectorDesdeJSONDeLaDireccionDelCaptcha lector = new LectorDesdeJSONDeLaDireccionDelCaptcha(url);
			final RespuestaALaPeticionDeCaptcha respuesta = lector.obtenerRespuesta();
			respuesta.urlFinal = lector.obtenerLaURLFinal();
			respuesta.cookie = lector.obtenerLaCookie();
			return respuesta;
		} catch (final Exception excepcion) {
			Log.d("LectorDesdeJSON", "ERROR CON JSON", excepcion);
			return null;
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class obtenerClaseRecibida() {
		return RespuestaALaPeticionDeCaptcha.class;
	}

}
