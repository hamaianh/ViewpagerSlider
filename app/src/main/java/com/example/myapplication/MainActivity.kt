package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.example.myapplication.lib.SliderLayoutV2
import com.example.myapplication.lib.ViewPagerExV2
import kotlin.collections.HashMap as HashMap1


class MainActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener, TransformerAdapterV2.OnItemClickListener {

    private var mDemoSlider: SliderLayoutV2? = null
    private var mRecyclerEffect: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDemoSlider = findViewById<View>(R.id.slider) as SliderLayoutV2
        mRecyclerEffect = findViewById<View>(R.id.transformers) as RecyclerView

        //Slider
        val listImagesMap = getUrlMap()//list image
        mDemoSlider!!.addSlider(listImagesMap)
        mDemoSlider!!.setPresetTransformer(SliderLayout.Transformer.Accordion)
        //mDemoSlider!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        mDemoSlider!!.setCustomAnimation(DescriptionAnimation())
        mDemoSlider!!.setDuration(2000)

        //List animation
        mRecyclerEffect?.hasFixedSize()
        val adapterV2 = TransformerAdapterV2(getListAnimation())
        adapterV2.setOnItemClickListener(this)
        mRecyclerEffect?.adapter = adapterV2
    }

    private fun getUrlMap(): MutableList<SlideObject>{
        val listDemo = mutableListOf<SlideObject>()
        listDemo.add(SlideObject("Lý Hải - Minh Hà", "https://kenh14cdn.com/203336854389633024/2021/5/12/ronaldo-web01-1620834844642649742899.jpg"))
        listDemo.add(SlideObject("Lý Hải - Tôi làm gì cũng chậm hơn người khác", "https://kenh14cdn.com/203336854389633024/2021/5/13/web02-16208704209261285151421.jpg"))
        listDemo.add(SlideObject("Minh Hà - 18 tuổi tôi yêu anh Hải", "https://kenh14cdn.com/203336854389633024/2021/5/14/web02-1620935953729923178889.jpg"))
        listDemo.add(SlideObject("Lý Hải - Mỗi ám ảnh cảnh nghèo", "https://kenh14cdn.com/203336854389633024/2021/5/13/web06-1620870330769517045401.jpg"))
        listDemo.add(SlideObject("Minh Hà - Anh Hải đi đâu cũng rủ tôi", "https://kenh14cdn.com/203336854389633024/2021/5/13/web11-1620870330778511416473.jpg"))
        listDemo.add(SlideObject("Tôi làm phim vì đó là giấc mơ", "https://kenh14cdn.com/203336854389633024/2021/5/13/web02-1620870626495509754037.jpg"))
        return listDemo
    }

    private fun getListAnimation(): MutableList<SliderLayoutV2.Transformer>{
        val list = mutableListOf<SliderLayoutV2.Transformer>()
        list.add(SliderLayoutV2.Transformer.Default)
        list.add(SliderLayoutV2.Transformer.Accordion)
        list.add(SliderLayoutV2.Transformer.Background2Foreground)
        list.add(SliderLayoutV2.Transformer.CubeIn)
        list.add(SliderLayoutV2.Transformer.DepthPage)
        list.add(SliderLayoutV2.Transformer.Fade)
        list.add(SliderLayoutV2.Transformer.FlipHorizontal)
        list.add(SliderLayoutV2.Transformer.FlipPage)
        list.add(SliderLayoutV2.Transformer.Foreground2Background)
        list.add(SliderLayoutV2.Transformer.RotateDown)
        list.add(SliderLayoutV2.Transformer.RotateUp)
        list.add(SliderLayoutV2.Transformer.Stack)
        list.add(SliderLayoutV2.Transformer.Tablet)
        list.add(SliderLayoutV2.Transformer.ZoomIn)
        list.add(SliderLayoutV2.Transformer.ZoomOutSlide)
        list.add(SliderLayoutV2.Transformer.ZoomOut)
        return list
    }

    override fun onStop() {
        mDemoSlider!!.stopAutoCycle() //To prevent a memory leak on rotation, make sure to call stopAutoCycle()
        super.onStop()
    }

    override fun onSliderClick(slider: BaseSliderView) {
        Toast.makeText(this, "Click:: " + slider.bundle["extra"].toString() + "", Toast.LENGTH_SHORT).show()
    }

    override fun onItemAnimationClick(animation: SliderLayoutV2.Transformer?) {
        if(animation != null) {
            mDemoSlider!!.setPresetTransformer(animation.name)
            Toast.makeText(this@MainActivity, animation.name, Toast.LENGTH_SHORT).show()
        }
    }
}