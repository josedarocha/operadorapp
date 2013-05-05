package com.tactilapp.operadorapp;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GestorAsincronoDeImagenes {

	private AlTerminarListener alTerminarListener;

	private final Bitmap imagenDefecto;

	public GestorAsincronoDeImagenes(final Bitmap imagenDefecto) {
		this.imagenDefecto = imagenDefecto;
	}

	public void descargar(final String url, final ImageView vista) {
		if (cancelarLaDescarga(url, vista)) {
			final TareaDeDescargaDeLaImagen tarea = new TareaDeDescargaDeLaImagen(
					vista);
			final ImagenDescargada imagenDescargada = new ImagenDescargada(
					tarea, imagenDefecto);
			vista.setImageDrawable(imagenDescargada);
			tarea.execute(url);
		}
	}

	public void fijarAlTerminarListener(
			final AlTerminarListener alTerminarListener) {
		this.alTerminarListener = alTerminarListener;
	}

	private static boolean cancelarLaDescarga(final String url,
			final ImageView vista) {
		final TareaDeDescargaDeLaImagen tareaDeDescarga = obtenerLaTareaDeDescargaDeLaImagen(vista);

		if (tareaDeDescarga != null) {
			final String bitmapUrl = tareaDeDescarga.url;
			if (bitmapUrl == null || !bitmapUrl.equals(url)) {
				tareaDeDescarga.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static TareaDeDescargaDeLaImagen obtenerLaTareaDeDescargaDeLaImagen(
			final ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof ImagenDescargada) {
				final ImagenDescargada downloadedDrawable = (ImagenDescargada) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	class TareaDeDescargaDeLaImagen extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> referenciaALaVista;

		public TareaDeDescargaDeLaImagen(final ImageView vista) {
			referenciaALaVista = new WeakReference<ImageView>(vista);
		}

		@Override
		protected Bitmap doInBackground(final String... params) {
			if (params[0] != null && !"".equals(params[0])) {
				return Utils.descargarImagen(params[0]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap imagen) {
			if (isCancelled()) {
				imagen = null;
			}

			if (referenciaALaVista != null && imagen != null) {
				final ImageView vista = referenciaALaVista.get();
				final TareaDeDescargaDeLaImagen tareaDeDescargaDeLaImagen = obtenerLaTareaDeDescargaDeLaImagen(vista);
				if (this == tareaDeDescargaDeLaImagen) {
					vista.setImageBitmap(imagen);
				}
			}

			if (alTerminarListener != null) {
				alTerminarListener.accionLanzadaAlTerminar(imagen != null);
			}
		}
	}

	static class ImagenDescargada extends BitmapDrawable {
		private final WeakReference<TareaDeDescargaDeLaImagen> referenciaALaTarea;

		public ImagenDescargada(
				final TareaDeDescargaDeLaImagen tareaDeDescarga,
				final Bitmap imagenDefecto) {
			super(imagenDefecto);
			referenciaALaTarea = new WeakReference<TareaDeDescargaDeLaImagen>(
					tareaDeDescarga);
		}

		public TareaDeDescargaDeLaImagen getBitmapDownloaderTask() {
			return referenciaALaTarea.get();
		}
	}

	public interface AlTerminarListener {
		public void accionLanzadaAlTerminar(final Boolean exito);
	}
}
