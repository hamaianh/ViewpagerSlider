package com.example.myapplication.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.daimajia.slider.library.Animations.BaseAnimationInterface;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.FixedSpeedScroller;
import com.example.myapplication.R;
import com.example.myapplication.SlideObject;
import com.example.myapplication.ViewPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SliderLayoutV2 extends RelativeLayout {

    private Context mContext;
    private ViewPager2 mViewPager;
    //private SliderAdapterV2 mSliderAdapter;
    private ViewPagerAdapter mViewPagerAdapter;
    //private PagerIndicatorV2 mIndicator;
    private List<SlideObject> listImagesMap;

    private Timer mCycleTimer;
    private TimerTask mCycleTask;
    private Timer mResumingTimer;
    private TimerTask mResumingTask;

    private boolean mCycling;

    private boolean mAutoRecover = true;

    private int mTransformerId;

    private int mTransformerSpan = 1100;

    private boolean mAutoCycle;

    private long mSliderDuration = 4000;

    //private PagerIndicatorV2.IndicatorVisibility mIndicatorVisibility = PagerIndicatorV2.IndicatorVisibility.Visible;

    private BaseTransformerV2 mViewPagerTransformer;

    private BaseAnimationInterface mCustomAnimation;

    public SliderLayoutV2(Context context) {
        this(context,null);
    }

    public SliderLayoutV2(Context context, AttributeSet attrs) {
        this(context,attrs,R.attr.SliderStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    public SliderLayoutV2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.slider_layout_v2, this, true);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,R.styleable.SliderLayout,
                defStyle,0);

        mTransformerSpan = attributes.getInteger(R.styleable.SliderLayout_pager_animation_span, 1100);
        mTransformerId = attributes.getInt(R.styleable.SliderLayout_pager_animation, SliderLayout.Transformer.Default.ordinal());
        mAutoCycle = attributes.getBoolean(R.styleable.SliderLayout_auto_cycle,true);
        //int visibility = attributes.getInt(R.styleable.SliderLayout_indicator_visibility,0);
        /*for(PagerIndicatorV2.IndicatorVisibility v: PagerIndicatorV2.IndicatorVisibility.values()){
            if(v.ordinal() == visibility){
                mIndicatorVisibility = v;
                break;
            }
        }*/
        //mSliderAdapter = new SliderAdapterV2(mContext);
        //PagerAdapter wrappedAdapter = new InfinitePagerAdapterV2(mSliderAdapter);
        listImagesMap = new ArrayList<>();
        mViewPagerAdapter = new ViewPagerAdapter(mContext, listImagesMap);

        mViewPager = (ViewPager2) findViewById(R.id.slider_viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("Slider Demo", "Page Changed: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        mViewPager.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {
                recoverCycle();
            }
            return false;
        });

        attributes.recycle();
        //setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        setPresetTransformer(mTransformerId);
        setSliderTransformDuration(mTransformerSpan,null);
        //setIndicatorVisibility(mIndicatorVisibility);
        if(mAutoCycle){
            startAutoCycle();
        }
    }

    /*public void addOnPageChangeListener(ViewPager2.OnPageChangeCallback onPageChangeListener){
        if(onPageChangeListener != null){
            mViewPager.addOnLayoutChangeListener((OnLayoutChangeListener) onPageChangeListener);
        }
    }*/

    /*public void removeOnPageChangeListener(ViewPagerExV2.OnPageChangeListener onPageChangeListener) {
        mViewPager.removeOnPageChangeListener((ViewPagerExV2.OnPageChangeListener) onPageChangeListener);
    }*/

    /*public void setCustomIndicator(PagerIndicatorV2 indicator){
        if(mIndicator != null){
            mIndicator.destroySelf();
        }
        mIndicator = indicator;
        mIndicator.setIndicatorVisibility(mIndicatorVisibility);
        //mIndicator.setViewPager(mViewPager);
        mIndicator.redraw();
    }*/

    public void addSlider(List<SlideObject> listImageContent){
        mViewPagerAdapter.setList(listImageContent);
    }

    @SuppressLint("HandlerLeak")
    private android.os.Handler mh = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            moveNextPosition(true);
        }
    };

    public void startAutoCycle(){
        startAutoCycle(1000, mSliderDuration, mAutoRecover);
    }

    public void startAutoCycle(long delay,long duration,boolean autoRecover){
        if(mCycleTimer != null) mCycleTimer.cancel();
        if(mCycleTask != null) mCycleTask.cancel();
        if(mResumingTask != null) mResumingTask.cancel();
        if(mResumingTimer != null) mResumingTimer.cancel();
        mSliderDuration = duration;
        mCycleTimer = new Timer();
        mAutoRecover = autoRecover;
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                mh.sendEmptyMessage(0);
            }
        };
        mCycleTimer.schedule(mCycleTask,delay,mSliderDuration);
        mCycling = true;
        mAutoCycle = true;
    }

    private void pauseAutoCycle(){
        if(mCycling){
            mCycleTimer.cancel();
            mCycleTask.cancel();
            mCycling = false;
        }else{
            if(mResumingTimer != null && mResumingTask != null){
                recoverCycle();
            }
        }
    }

    public void setDuration(long duration){
        if(duration >= 500){
            mSliderDuration = duration;
            if(mAutoCycle && mCycling){
                startAutoCycle();
            }
        }
    }

    public void stopAutoCycle(){
        if(mCycleTask!=null){
            mCycleTask.cancel();
        }
        if(mCycleTimer!= null){
            mCycleTimer.cancel();
        }
        if(mResumingTimer!= null){
            mResumingTimer.cancel();
        }
        if(mResumingTask!=null){
            mResumingTask.cancel();
        }
        mAutoCycle = false;
        mCycling = false;
    }

    private void recoverCycle(){
        if(!mAutoRecover || !mAutoCycle){
            return;
        }

        if(!mCycling){
            if(mResumingTask != null && mResumingTimer!= null){
                mResumingTimer.cancel();
                mResumingTask.cancel();
            }
            mResumingTimer = new Timer();
            mResumingTask = new TimerTask() {
                @Override
                public void run() {
                    startAutoCycle();
                }
            };
            mResumingTimer.schedule(mResumingTask, 6000);
        }
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseAutoCycle();
                break;
        }
        return false;
    }

    public void setPagerTransformer(boolean reverseDrawingOrder,BaseTransformerV2 transformer){
        mViewPagerTransformer = transformer;
        mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        mViewPager.setPageTransformer(mViewPagerTransformer);//ANHHA
    }



    public void setSliderTransformDuration(int period, Interpolator interpolator){
        try{
            Field mScroller = ViewPager2.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(),interpolator, period);
            mScroller.set(mViewPager,scroller);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public enum Transformer{
        Default("Default"),
        Accordion("Accordion"),
        Background2Foreground("Background To Foreground"),
        CubeIn("Cube In"),
        DepthPage("Depth Page"),
        Fade("Fade"),
        FlipHorizontal("Flip Horizontal"),
        FlipPage("Flip Page"),
        Foreground2Background("Foreground To Background"),
        RotateDown("Rotate Down"),
        RotateUp("Rotate Up"),
        Stack("Stack"),
        Tablet("Tablet"),
        ZoomIn("Zoom In"),
        ZoomOutSlide("Zoom Out Slide"),
        ZoomOut("Zoom Out");

        private final String name;

        private Transformer(String s){
            name = s;
        }
        public String toString(){
            return name;
        }

        public boolean equals(String other){
            return name.equals(other);
        }
    };

    public void setPresetTransformer(int transformerId){
        for(SliderLayout.Transformer t : SliderLayout.Transformer.values()){
            if(t.ordinal() == transformerId){
                setPresetTransformer(t);
                break;
            }
        }
    }

    public void setPresetTransformer(String transformerName){
        for(SliderLayout.Transformer t : SliderLayout.Transformer.values()){
            if(t.equals(transformerName)){
                setPresetTransformer(t);
                return;
            }
        }
    }

    public void setCustomAnimation(BaseAnimationInterface animation){
        mCustomAnimation = animation;
        if(mViewPagerTransformer != null){
            mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        }
    }

    public void setPresetTransformer(SliderLayout.Transformer ts){
        BaseTransformerV2 t = null;
        switch (ts){
            case Default:
                t = new DefaultTransformerV2();
                break;
            case Accordion:
                t = new AccordionTransformerV2();
                break;
            case Background2Foreground:
                t = new BackgroundToForegroundTransformerV2();
                break;
            case CubeIn:
                t = new CubeInTransformerV2();
                break;
            case DepthPage:
                t = new DepthPageTransformerV2();
                break;
            case Fade:
                t = new FadeTransformerV2();
                break;
            case FlipHorizontal:
                t = new FlipHorizontalTransformerV2();
                break;
            case FlipPage:
                t = new FlipPageViewTransformerV2();
                break;
            case Foreground2Background:
                t = new ForegroundToBackgroundTransformerV2();
                break;
            case RotateDown:
                t = new RotateDownTransformerV2();
                break;
            case RotateUp:
                t = new RotateUpTransformerV2();
                break;
            case Stack:
                t = new StackTransformerV2();
                break;
            case Tablet:
                t = new TabletTransformerV2();
                break;
            case ZoomIn:
                t = new ZoomInTransformerV2();
                break;
            case ZoomOutSlide:
                t = new ZoomOutSlideTransformerV2();
                break;
            case ZoomOut:
                t = new ZoomOutTransformerV2();
                break;
        }
        setPagerTransformer(true,t);
    }



   /* public void setIndicatorVisibility(PagerIndicatorV2.IndicatorVisibility visibility){
        if(mIndicator == null){
            return;
        }

        mIndicator.setIndicatorVisibility(visibility);
    }

    public PagerIndicatorV2.IndicatorVisibility getIndicatorVisibility(){
        if(mIndicator == null){
            return mIndicator.getIndicatorVisibility();
        }
        return PagerIndicatorV2.IndicatorVisibility.Invisible;

    }

    public PagerIndicatorV2 getPagerIndicatorV2(){
        return mIndicator;
    }

    public enum PresetIndicators{
        Center_Bottom("Center_Bottom", R.id.default_center_bottom_indicator),
        Right_Bottom("Right_Bottom",R.id.default_bottom_right_indicator),
        Left_Bottom("Left_Bottom",R.id.default_bottom_left_indicator),
        Center_Top("Center_Top",R.id.default_center_top_indicator),
        Right_Top("Right_Top",R.id.default_center_top_right_indicator),
        Left_Top("Left_Top",R.id.default_center_top_left_indicator);

        private final String name;
        private final int id;
        private PresetIndicators(String name,int id){
            this.name = name;
            this.id = id;
        }

        public String toString(){
            return name;
        }

        public int getResourceId(){
            return id;
        }
    }*/
    /*public void setPresetIndicator(SliderLayout.PresetIndicators presetIndicator){
        PagerIndicatorV2 PagerIndicatorV2 = (PagerIndicatorV2)findViewById(presetIndicator.getResourceId());
        setCustomIndicator(PagerIndicatorV2);
    }*/

   /* private InfinitePagerAdapterV2 getWrapperAdapter(){
        PagerAdapter adapter = mViewPager.getAdapter();
        if(adapter!=null){
            return (InfinitePagerAdapterV2)adapter;
        }else{
            return null;
        }
    }*/

    private ViewPagerAdapter getRealAdapter(){
        ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();
        return adapter;
    }

    /*public int getCurrentPosition(){

        if(getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        return mViewPager.getCurrentItem() % getRealAdapter().getCount();

    }

    public BaseSliderView getCurrentSlider(){

        if(getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        int count = getRealAdapter().getCount();
        int realCount = mViewPager.getCurrentItem() % count;
        return  getRealAdapter().getSliderView(realCount);
    }

    public void removeSliderAt(int position){
        if(getRealAdapter()!=null){
            getRealAdapter().removeSliderAt(position);
            mViewPager.setCurrentItem(mViewPager.getCurrentItem(),false);
        }
    }

    public void removeAllSliders(){
        if(getRealAdapter()!=null){
            int count = getRealAdapter().getCount();
            getRealAdapter().removeAllSliders();
            //a small bug, but fixed by this trick.
            //bug: when remove adapter's all the sliders.some caching slider still alive.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() +  count,false);
        }
    }*/

    public void setCurrentPosition(int position, boolean smooth) {
        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");
        if(position >= getRealAdapter().getItemCount()){
            throw new IllegalStateException("Item position is not exist");
        }
        int p = mViewPager.getCurrentItem() % getRealAdapter().getItemCount();
        int n = (position - p) + mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(n, smooth);
    }

    public void setCurrentPosition(int position) {
        setCurrentPosition(position, true);
    }

    public void movePrevPosition(boolean smooth) {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, smooth);
    }

    public void movePrevPosition(){
        movePrevPosition(true);
    }

    public void moveNextPosition(boolean smooth) {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, smooth);
    }

    public void moveNextPosition() {
        moveNextPosition(true);
    }
}