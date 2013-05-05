package com.tactilapp.operadorapp.componente;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class TextViewQueSeAdaptaAUnaLinea
		extends TextView {

	private Paint dibujo;

	public TextViewQueSeAdaptaAUnaLinea(final Context contexto) {
		super(contexto);
		inicializar();
	}

	public TextViewQueSeAdaptaAUnaLinea(final Context contexto,
			final AttributeSet atributos) {
		super(contexto, atributos);
		inicializar();
	}

	private void inicializar() {
		dibujo = new Paint();
		dibujo.set(getPaint());
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int anchura = MeasureSpec.getSize(widthMeasureSpec);
		final int altura = getMeasuredHeight();

		ajustarElTextoALaAnchura(getText().toString(), anchura);

		setMeasuredDimension(anchura, altura);
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start,
			final int before, final int after) {
		ajustarElTextoALaAnchura(text.toString(), getWidth());
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw,
			final int oldh) {
		if (w != oldw) {
			ajustarElTextoALaAnchura(getText().toString(), w);
		}
	}

	private void ajustarElTextoALaAnchura(final String texto, final int anchura) {
		if (anchura <= 0) {
			return;
		}

		final int anchuraMaximaDelTexto =
				anchura - getPaddingLeft() - getPaddingRight();

		float maximo = 100;
		float minimo = 2;
		final float umbral = 0.5f;

		dibujo.set(getPaint());

		while (maximo - minimo > umbral) {
			final float tamanho = (maximo + minimo) / 2;
			dibujo.setTextSize(tamanho);

			if (dibujo.measureText(texto) >= anchuraMaximaDelTexto) {
				// Muy grande
				maximo = tamanho;
			} else {
				// Muy pequeño
				minimo = tamanho;
			}
		}
		// Usamos el menor ante la duda
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, minimo);
	}

}