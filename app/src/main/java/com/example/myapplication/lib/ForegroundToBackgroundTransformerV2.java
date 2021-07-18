package com.example.myapplication.lib;

import android.view.View;

import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.nineoldandroids.view.ViewHelper;

public class ForegroundToBackgroundTransformerV2 extends BaseTransformerV2 {

    @Override
    protected void onTransform(View view, float position) {
        final float height = view.getHeight();
        final float width = view.getWidth();
        final float scale = min(position > 0 ? 1f : Math.abs(1f + position), 0.5f);

        ViewHelper.setScaleX(view,scale);
        ViewHelper.setScaleY(view,scale);
        ViewHelper.setPivotX(view,width * 0.5f);
        ViewHelper.setPivotY(view,height * 0.5f);
        ViewHelper.setTranslationX(view,position > 0 ? width * position : -width * position * 0.25f);
    }

    private static final float min(float val, float min) {
        return val < min ? min : val;
    }

}
