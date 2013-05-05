package com.tactilapp.operadorapp.componente;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImagenDeAnchuraCompleta extends ImageView {

	public ImagenDeAnchuraCompleta(Context contexto) {
		super(contexto);
	}

	public ImagenDeAnchuraCompleta(Context contexto, AttributeSet attrs) {
		super(contexto, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final Drawable imagen = getDrawable();

		if (imagen != null) {
			int anchura = MeasureSpec.getSize(widthMeasureSpec);
			int altura = (int) Math.ceil((float) anchura
					* (float) imagen.getIntrinsicHeight()
					/ (float) imagen.getIntrinsicWidth());
			setMeasuredDimension(anchura, altura);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}