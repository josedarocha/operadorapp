package com.tactilapp.operadorapp;

import java.util.HashMap;
import java.util.Map;

import com.flurry.android.FlurryAgent;
import com.tactilapp.operadorapp.modelo.Companhia;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class OperadorappApplication extends android.app.Application {

	private Map<String, Companhia> companhias = new HashMap<String, Companhia>();

	@Override
	public void onCreate() {
		final AirshipConfigOptions options = AirshipConfigOptions
				.loadDefaultOptions(this);
		if (!"ejemplo".equalsIgnoreCase(options.developmentAppKey)) {
			UAirship.takeOff(this, options);
			PushManager.enablePush();
		}
	}

	public void fijarCompanhias(final Map<String, Companhia> companhias) {
		this.companhias = companhias;
	}

	public Companhia obtenerLaCompanhia(final String nombreDeLaCompanhia) {
		Companhia companhia = null;

		if (companhias.containsKey(nombreDeLaCompanhia)) {
			companhia = companhias.get(Constantes.COMPANHIA_POR_DEFECTO);
		} else {
			companhia = buscarLaCompahiaEntreLasPosiblesCadenas(nombreDeLaCompanhia);
		}

		if (companhia == null) {
			companhia = obtenerLaCompanhiaPorDefecto();
		} else {
			final Map<String, String> nombreCompanhia = new HashMap<String, String>();
			nombreCompanhia.put("nombre", nombreDeLaCompanhia);
			FlurryAgent.logEvent("Compañía NO cargada del plist",
					nombreCompanhia, true);
		}
		return companhia;
	}

	private Companhia buscarLaCompahiaEntreLasPosiblesCadenas(
			final String nombreDeLaCompanhia) {
		Companhia companhia = null;
		for (final Companhia companhiaPotencial : companhias.values()) {
			if (companhiaPotencial.getPosiblesCadenas().contains(
					nombreDeLaCompanhia)) {
				companhia = companhiaPotencial;
				break;
			}
		}
		return companhia;
	}

	public Companhia obtenerLaCompanhiaPorDefecto() {
		return companhias.get(Constantes.COMPANHIA_POR_DEFECTO);
	}
}
