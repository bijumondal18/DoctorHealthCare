package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.banners.Data
import com.bijumondal.doctorhealthcare.utils.CacheImageManager
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import java.lang.Exception

class BannerSliderAdapter(
    private val context: Context,
    private val bannerList: ArrayList<String>

) :
    PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return bannerList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner_slider, container, false)
        val bannerImage: ImageView = view.findViewById(R.id.iv_banner_image)

        ImageLoader.loadImageFromUrl(bannerImage, bannerList[position], R.color.colorTransparent)
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


}