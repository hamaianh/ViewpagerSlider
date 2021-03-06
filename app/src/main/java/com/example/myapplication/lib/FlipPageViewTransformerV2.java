package com.example.myapplication.lib;


import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.nineoldandroids.view.ViewHelper;

public class FlipPageViewTransformerV2 extends BaseTransformerV2 {

    @Override
    protected void onTransform(View view, float position) {
        float percentage = 1 - Math.abs(position);
        view.setCameraDistance(12000);
        setVisibility(view, position);
        setTranslation(view);
        setSize(view, position, percentage);
        setRotation(view, position, percentage);
    }

    private void setVisibility(View page, float position) {
        if (position < 0.5 && position > -0.5) {
            page.setVisibility(View.VISIBLE);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }

    private void setTranslation(View view) {
        //ViewPager2 viewPager = (ViewPager2) view.getParent();
        int scroll =  view.getScrollX() - view.getLeft();
        ViewHelper.setTranslationX(view,scroll);
    }

    private void setSize(View view, float position, float percentage) {
        ViewHelper.setScaleX(view,(position != 0 && position != 1) ? percentage : 1);
        ViewHelper.setScaleY(view,(position != 0 && position != 1) ? percentage : 1);
    }

    private void setRotation(View view, float position, float percentage) {
        if (position > 0) {
            ViewHelper.setRotationY(view,-180 * (percentage + 1));
        } else {
            ViewHelper.setRotationY(view,180 * (percentage + 1));
        }
    }
}