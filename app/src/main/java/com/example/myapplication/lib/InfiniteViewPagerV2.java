package com.example.myapplication.lib;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.PagerAdapter;


public class InfiniteViewPagerV2 extends ViewPagerExV2 {

    public InfiniteViewPagerV2(Context context) {
        super(context);
    }

    public InfiniteViewPagerV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

}