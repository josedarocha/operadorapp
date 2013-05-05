package com.tactilapp.operadorapp.modelo;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Resultado implements Serializable {

	private static final long serialVersionUID = -4426226846600818154L;

	@SerializedName("mobile")
	public String numeroDeTelefono;

	@SerializedName("company")
	public String companhia;

}
