package com.telerik.academy.voiceshoppinglist.utilities;

import android.support.v7.widget.ContentFrameLayout;
import android.view.View;
import android.widget.LinearLayout;

public class RmsDBStateParams {
    private final static int MAX_RMS_DB_VALUE = 100;
    private View micResultView;
    private double dbToWidthCoefficient;
    private int currentMargin;

    public RmsDBStateParams(ContentFrameLayout layoutContent, View micResultView) {
        this.micResultView = micResultView;
        this.dbToWidthCoefficient = layoutContent.getMeasuredWidth() / MAX_RMS_DB_VALUE;
        this.currentMargin = ((LinearLayout.LayoutParams) this.micResultView.getLayoutParams()).topMargin;
    }

    public LinearLayout.LayoutParams getLayoutParams(float rmsdB) {
        int newWidth = (int) Math.ceil((rmsdB * 10) * this.dbToWidthCoefficient);

        LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(newWidth, this.micResultView.getMeasuredHeight());
        newLayoutParams.setMargins(0, this.currentMargin, 0, 0);

        return newLayoutParams;
    }
}
