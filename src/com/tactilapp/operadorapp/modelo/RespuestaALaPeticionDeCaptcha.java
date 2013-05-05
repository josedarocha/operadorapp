package com.tactilapp.operadorapp.modelo;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class RespuestaALaPeticionDeCaptcha implements Serializable {

	private static final long serialVersionUID = -4426226846600818154L;

	@SerializedName("captcha_url")
	public String urlDelCaptcha;

	@SerializedName("apiv")
	public String version;

	public String urlFinal;

	public String cookie;

}
