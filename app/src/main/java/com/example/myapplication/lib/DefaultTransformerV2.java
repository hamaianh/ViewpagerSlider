package com.example.myapplication.lib;

import android.view.View;

public class DefaultTransformerV2 extends BaseTransformerV2 {

    @Override
    protected void onTransform(View view, float position) {
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

}
