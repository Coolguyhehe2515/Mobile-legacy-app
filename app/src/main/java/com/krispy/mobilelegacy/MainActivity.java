package com.krispy.mobilelegacy;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setStatusBarColor(Color.rgb(8, 10, 12));
        window.setNavigationBarColor(Color.rgb(8, 10, 12));
        window.setFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        );

        setContentView(new new AuthorizationView(MainActivity.this));
    }

    private static final class AuthorizationView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF button = new RectF();

        private boolean authorized;
        private boolean animating;
        private long animationStart;

        AuthorizationView(Activity activity) {
            super(null);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
            glow.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
            setBackgroundColor(Color.rgb(8, 10, 12));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            final float w = getWidth();
            final float h = getHeight();
            final int accent = authorized ? Color.rgb(60, 255, 145) : Color.rgb(255, 55, 65);
            final int dim = authorized ? Color.rgb(28, 115, 70) : Color.rgb(115, 28, 35);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(8, 10, 12));
            canvas.drawRect(0, 0, w, h, paint);

            drawGrid(canvas, w, h, dim);

            String state = authorized ? "AUTHORIZED" : "NOT AUTHORIZED";

            glow.setStyle(Paint.Style.FILL);
            glow.setTextAlign(Paint.Align.CENTER);
            glow.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            glow.setTextSize(Math.min(w * 0.095f, 54f));
            glow.setColor(accent);
            glow.setShadowLayer(24f, 0f, 0f, accent);
            canvas.drawText(state, w / 2f, h * 0.39f, glow);
            glow.setShadowLayer(8f, 0f, 0f, accent);
            canvas.drawText(state, w / 2f, h * 0.39f, glow);
            glow.clearShadowLayer();

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
            paint.setTextSize(13f);
            paint.setColor(Color.rgb(125, 132, 140));
            canvas.drawText(
                authorized ? "RENDERER ACCESS GRANTED" : "RENDERER ACCESS LOCKED",
                w / 2f,
                h * 0.45f,
                paint
            );

            button.set(w * 0.20f, h * 0.56f, w * 0.80f, h * 0.64f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2f);
            paint.setColor(accent);
            paint.setShadowLayer(16f, 0f, 0f, accent);
            canvas.drawRoundRect(button, 8f, 8f, paint);
            paint.clearShadowLayer();

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(16f);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            paint.setColor(accent);
            canvas.drawText(authorized ? "AUTHORIZED" : "AUTHORIZE", w / 2f, h * 0.612f, paint);

            if (animating) {
                drawMachineEffect(canvas, w, h, accent);
            }
        }

        private void drawGrid(Canvas canvas, float w, float h, int color) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1f);
            paint.setColor(Color.argb(42, Color.red(color), Color.green(color), Color.blue(color)));

            float step = 42f;
            for (float x = 0; x <= w; x += step) {
                canvas.drawLine(x, 0, x, h, paint);
            }
            for (float y = 0; y <= h; y += step) {
                canvas.drawLine(0, y, w, y, paint);
            }
        }

        private void drawMachineEffect(Canvas canvas, float w, float h, int accent) {
            float progress = Math.min(1f, (System.currentTimeMillis() - animationStart) / 1100f);
            float scanY = h * 0.18f + h * 0.64f * progress;

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2f);
            paint.setColor(accent);
            paint.setShadowLayer(18f, 0f, 0f, accent);
            canvas.drawLine(w * 0.12f, scanY, w * 0.88f, scanY, paint);
            paint.clearShadowLayer();

            int lines = 9;
            for (int i = 0; i < lines; i++) {
                float x = w * (0.10f + i * 0.10f);
                float offset = (progress * 1.8f + i * 0.17f) % 1f;
                float top = h * (0.18f + offset * 0.64f);
                paint.setStrokeWidth(i % 2 == 0 ? 1.5f : 1f);
                paint.setColor(Color.argb(120, Color.red(accent), Color.green(accent), Color.blue(accent)));
                canvas.drawLine(x, top, x, Math.min(h * 0.82f, top + 32f), paint);
            }

            if (progress < 1f) {
                postInvalidateDelayed(16);
            } else {
                animating = false;
                authorized = true;
                postInvalidate();
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP && !animating &&
                button.contains(event.getX(), event.getY()) && !authorized) {
                animating = true;
                animationStart = System.currentTimeMillis();
                performClick();
                invalidate();
                return true;
            }
            return true;
        }

        @Override
        public boolean performClick() {
            super.performClick();
            return true;
        }
    }
}
