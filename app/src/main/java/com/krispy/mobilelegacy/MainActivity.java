package com.krispy.mobilelegacy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(
            "Mobile Legacy\n\n" +
            "OpenGL 4.0 renderer plugin for Zalith Launcher 2.\n\n" +
            "Install this APK, then select Mobile Legacy in the launcher renderer settings."
        );
        view.setTextSize(18);
        view.setPadding(48, 48, 48, 48);
        setContentView(view);
    }
}
