package com.tactilapp.operadorapp.componente;

import java.io.Serializable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tactilapp.operadorapp.R;

public class Aviso extends AlertDialog implements Serializable {

	private static final long serialVersionUID = 5138030482911061332L;

	private AlAceptarListener alAceptarListener;

	public Aviso(final Context context) {
		super(context);
		setTitle(R.string.paso2_titulo);
		setMessage(getContext().getResources().getString(
				R.string.sin_conexion_a_internet));
		setCancelable(false);
		setButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
				if (alAceptarListener != null) {
					alAceptarListener.accionLanzadaAlAceptar();
				}
			}
		});

	}

	public void fijarAlAceptarListener(final AlAceptarListener alAceptarListener) {
		this.alAceptarListener = alAceptarListener;
	}

	public interface AlAceptarListener {
		public void accionLanzadaAlAceptar();
	}
}