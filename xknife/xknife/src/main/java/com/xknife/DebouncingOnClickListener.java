package com.xknife;

import android.view.View;

public abstract class DebouncingOnClickListener implements View.OnClickListener {
    private static boolean enable = true;

    private static final Runnable ENABLE_AGAIN = new Runnable() {
        @Override
        public void run() {
            enable = true;
        }
    };

    @Override
    public void onClick(View v) {
        if (enable) {
            enable = false;
            v.post(ENABLE_AGAIN);
            doClick(v);
        }
    }

    public abstract void doClick(View view);
}
