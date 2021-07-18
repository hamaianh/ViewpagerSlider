package com.example.myapplication.lib;

import android.view.View;

import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.nineoldandroids.view.ViewHelper;

public class RotateUpTransformerV2 extends BaseTransformerV2 {

    private static final float ROT_MOD = -15f;

    @Override
    protected void onTransform(View view, float position) {
        final float width = view.getWidth();
        final float rotation = ROT_MOD * position;

        ViewHelper.setPivotX(view,width * 0.5f);
        ViewHelper.setPivotY(view,0f);
        ViewHelper.setTranslationX(view,0f);
        ViewHelper.setRotation(view,rotation);
    }

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }

}
