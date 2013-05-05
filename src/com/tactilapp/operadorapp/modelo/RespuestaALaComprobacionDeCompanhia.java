package com.tactilapp.operadorapp.modelo;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RespuestaALaComprobacionDeCompanhia implements Serializable {

	private static final long serialVersionUID = -4426226846600818154L;

	@SerializedName("result")
	public Resultado resultado;

	@SerializedName("apiv")
	public Integer version;

	@SerializedName("errors")
	public List<String> errores;

	public boolean laComprobacionEsExitosa() {
		return errores == null || errores.isEmpty();
	}

	public boolean hayResultado() {
		return resultado != null;
	}

	public String obtenerElNumeroDeTelefono() {
		if (hayResultado()) {
			return resultado.numeroDeTelefono;
		}
		return "";
	}

	public String obtenerLaCompanhia() {
		if (hayResultado()) {
			return resultado.companhia;
		}
		return "";
	}

	public String obtenerElMensajeDeError() {
		if (!laComprobacionEsExitosa()) {
			return errores.get(0);
		}
		return "";
	}
}
