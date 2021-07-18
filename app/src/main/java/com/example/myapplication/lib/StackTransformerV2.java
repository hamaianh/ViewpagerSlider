package com.example.myapplication.lib;

import android.view.View;

import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.nineoldandroids.view.ViewHelper;

public class StackTransformerV2 extends BaseTransformerV2 {

    @Override
    protected void onTransform(View view, float position) {
        ViewHelper.setTranslationX(view,position < 0 ? 0f : -view.getWidth() * position);
    }

}