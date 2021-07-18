package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
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


class MainActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener,
    ViewPagerExV2.OnPageChangeListener, TransformerAdapterV2.OnItemClickListener {

    private var mDemoSlider: SliderLayoutV2? = null
    private var mRecyclerEffect: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDemoSlider = findViewById<View>(R.id.slider) as SliderLayoutV2
        mRecyclerEffect = findViewById<View>(R.id.transformers) as RecyclerView

        //Slider
        val images_maps = getUrlMap()//list image
        for (name in images_maps.keys) {
            val textSliderView = TextSliderView(this)
            textSliderView
                .description(name)
                .image(images_maps[name]!!)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .setOnSliderClickListener(this)

            //add your extra information
            textSliderView.bundle(Bundle())
            textSliderView.bundle
                .putString("extra", name)
            mDemoSlider!!.addSlider(textSliderView)
        }
        mDemoSlider!!.setPresetTransformer(SliderLayout.Transformer.Accordion)
        mDemoSlider!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        mDemoSlider!!.setCustomAnimation(DescriptionAnimation())
        mDemoSlider!!.setDuration(2000)
        mDemoSlider!!.addOnPageChangeListener(this)

        //List animation
        mRecyclerEffect?.hasFixedSize()
        val adapterV2 = TransformerAdapterV2(getListAnimation())
        adapterV2.setOnItemClickListener(this)
        mRecyclerEffect?.adapter = adapterV2
    }

    private fun getUrlMap(): HashMap1<String, String> {
        val url_maps = HashMap1<String, String>()
        url_maps["Lý Hải - Minh Hà"] = "https://kenh14cdn.com/203336854389633024/2021/5/12/ronaldo-web01-1620834844642649742899.jpg"
        url_maps["Lý Hải - Tôi làm gì cũng chậm hơn người khác"] = "https://kenh14cdn.com/203336854389633024/2021/5/13/web02-16208704209261285151421.jpg"
        url_maps["Minh Hà - 18 tuổi tôi yêu anh Hải"] = "https://kenh14cdn.com/203336854389633024/2021/5/14/web02-1620935953729923178889.jpg"
        url_maps["Lý Hải - Mỗi ám ảnh cảnh nghèo"] = "https://kenh14cdn.com/203336854389633024/2021/5/13/web06-1620870330769517045401.jpg"
        url_maps["Minh Hà - Anh Hải đi đâu cũng rủ tôi"] = "https://kenh14cdn.com/203336854389633024/2021/5/13/web11-1620870330778511416473.jpg"
        url_maps["Tôi làm phim vì đó là giấc mơ"] = "https://kenh14cdn.com/203336854389633024/2021/5/13/web02-1620870626495509754037.jpg"
        return url_maps
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

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        Log.d("Slider Demo", "Page Changed: $position")
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onItemAnimationClick(animation: SliderLayoutV2.Transformer?) {
        if(animation != null) {
            mDemoSlider!!.setPresetTransformer(animation.name)
            Toast.makeText(this@MainActivity, animation.name, Toast.LENGTH_SHORT).show()
        }
    }
}