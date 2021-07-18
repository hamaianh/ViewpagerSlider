package com.example.myapplication.lib;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class AccordionTransformerV2 extends BaseTransformerV2 {

    @Override
    protected void onTransform(View view, float position) {
        ViewHelper.setPivotX(view,position < 0 ? 0 : view.getWidth());
        ViewHelper.setScaleX(view,position < 0 ? 1f + position : 1f - position);
    }

}