package com.tactilapp.operadorapp.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Companhia implements Serializable {

	private static final long serialVersionUID = -4426226846600818154L;

	private String nombre;
	private String colorSuperior;
	private String colorInferior;
	private List<String> posiblesCadenas = new ArrayList<String>();

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getColorSuperior() {
		return colorSuperior;
	}

	public void setColorSuperior(String colorSuperior) {
		this.colorSuperior = colorSuperior;
	}

	public boolean tieneColorSuperior() {
		return colorSuperior != null && !"".equals(colorSuperior);
	}

	public String getColorInferior() {
		return colorInferior;
	}

	public void setColorInferior(String colorInferior) {
		this.colorInferior = colorInferior;
	}

	public boolean tieneColorInferior() {
		return colorInferior != null && !"".equals(colorInferior);
	}

	public List<String> getPosiblesCadenas() {
		return posiblesCadenas;
	}

	public void setPosiblesCadenas(List<String> posiblesCadenas) {
		this.posiblesCadenas = posiblesCadenas;
	}

	public void anhadirPosibleCadena(final String cadena) {
		if (posiblesCadenas == null) {
			posiblesCadenas = new ArrayList<String>();
		}

		posiblesCadenas.add(cadena);
	}

}
