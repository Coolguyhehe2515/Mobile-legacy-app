package com.krispy.mobilelegacy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

        setContentView(new AuthorizationView(MainActivity.this));
    }

    private static final class AuthorizationView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF button = new RectF();
        private final RectF homeTab = new RectF();
        private final RectF aboutTab = new RectF();
        private final RectF rendererLink = new RectF();
        private final RectF appLink = new RectF();
        private final Activity activity;

        private boolean authorized;
        private boolean about;
        private boolean animating;
        private long animationStart;

        AuthorizationView(Activity activity) {
            super(activity);
            this.activity = activity;
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

            if (about) drawAbout(canvas, w, h, accent);
            else drawHome(canvas, w, h, accent);
            drawNavigation(canvas, w, h, accent);

            if (animating) drawMachineEffect(canvas, w, h, accent);
        }

        private void drawHome(Canvas canvas, float w, float h, int accent) {
            String state = authorized ? "AUTHORIZED" : "NOT AUTHORIZED";
            glow.setStyle(Paint.Style.FILL);
            glow.setTextAlign(Paint.Align.CENTER);
            glow.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            glow.setTextSize(Math.min(w * 0.095f, 54f));
            glow.setColor(accent);
            glow.setShadowLayer(24f, 0f, 0f, accent);
            canvas.drawText(state, w / 2f, h * 0.30f, glow);
            glow.setShadowLayer(8f, 0f, 0f, accent);
            canvas.drawText(state, w / 2f, h * 0.30f, glow);
            glow.clearShadowLayer();

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
            paint.setTextSize(13f);
            paint.setColor(Color.rgb(125, 132, 140));
            canvas.drawText(authorized ? "RENDERER ACCESS GRANTED" : "RENDERER ACCESS LOCKED",
                w / 2f, h * 0.37f, paint);

            button.set(w * 0.20f, h * 0.47f, w * 0.80f, h * 0.55f);
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
            canvas.drawText(authorized ? "AUTHORIZED" : "AUTHORIZE", w / 2f, h * 0.522f, paint);

            paint.setTextSize(11f);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
            paint.setColor(Color.rgb(95, 102, 110));
            canvas.drawText(authorized ? "Mobile Legacy renderer is ready." : "Renderer access requires authorization.",
                w / 2f, h * 0.61f, paint);
        }

        private void drawAbout(Canvas canvas, float w, float h, int accent) {
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            paint.setTextSize(24f);
            paint.setColor(accent);
            canvas.drawText("ABOUT", w * 0.10f, h * 0.19f, paint);

            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
            paint.setTextSize(13f);
            paint.setColor(Color.rgb(150, 156, 164));
            canvas.drawText("Mobile Legacy", w * 0.10f, h * 0.27f, paint);
            canvas.drawText("Version 1.0", w * 0.10f, h * 0.32f, paint);
            canvas.drawText("State: ALPHA", w * 0.10f, h * 0.37f, paint);
            canvas.drawText("OpenGL 4.0 compatibility renderer", w * 0.10f, h * 0.42f, paint);
            canvas.drawText("Minecraft Java 1.12.2+", w * 0.10f, h * 0.47f, paint);

            drawLink(canvas, rendererLink, w * 0.10f, h * 0.56f, w * 0.90f, h * 0.63f, "RENDERER REPOSITORY", accent);
            drawLink(canvas, appLink, w * 0.10f, h * 0.66f, w * 0.90f, h * 0.73f, "APP REPOSITORY", accent);
        }

        private void drawLink(Canvas canvas, RectF rect, float l, float t, float r, float b, String label, int accent) {
            rect.set(l, t, r, b);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1.5f);
            paint.setColor(accent);
            canvas.drawRoundRect(rect, 6f, 6f, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            paint.setTextSize(12f);
            paint.setColor(accent);
            canvas.drawText(label, (l + r) / 2f, t + 27f, paint);
        }

        private void drawNavigation(Canvas canvas, float w, float h, int accent) {
            float top = h - 76f;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(220, 10, 13, 16));
            canvas.drawRect(0, top, w, h, paint);
            homeTab.set(0, top, w / 2f, h);
            aboutTab.set(w / 2f, top, w, h);

            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            paint.setTextSize(13f);
            paint.setColor(!about ? accent : Color.rgb(105, 112, 120));
            canvas.drawText("HOME", w * 0.25f, top + 42f, paint);
            paint.setColor(about ? accent : Color.rgb(105, 112, 120));
            canvas.drawText("ABOUT", w * 0.75f, top + 42f, paint);
        }

        private void open(String url) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }

        private void drawGrid(Canvas canvas, float w, float h, int color) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1f);
            paint.setColor(Color.argb(42, Color.red(color), Color.green(color), Color.blue(color)));
            float step = 42f;
            for (float x = 0; x <= w; x += step) canvas.drawLine(x, 0, x, h, paint);
            for (float y = 0; y <= h; y += step) canvas.drawLine(0, y, w, y, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float x = event.getX();
                float y = event.getY();
                if (about && rendererLink.contains(x, y)) { open("https://github.com/Coolguyhehe2515/Mobile-legacy"); return true; }
                if (about && appLink.contains(x, y)) { open("https://github.com/Coolguyhehe2515/Mobile-legacy-app"); return true; }
                if (homeTab.contains(x, y)) { about = false; invalidate(); return true; }
                if (aboutTab.contains(x, y)) { about = true; invalidate(); return true; }
                if (!about && !animating && !authorized && button.contains(x, y)) {
                animating = true;
                animationStart = System.currentTimeMillis();
                performClick();
                invalidate();
                return true;
                }
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
