package com.bijumondal.doctorhealthcare.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso


class ImageLoader {

    companion object {

        fun loadCircleImageFromUrl(
            @NonNull imageView: ImageView,
            @NonNull imageUrl: String,
            @DrawableRes fallbackImage: Int
        ) {
            if (TextUtils.isEmpty(imageUrl)) {
                imageView.setImageResource(fallbackImage)
            } else {
                val context = imageView.context

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(fallbackImage)
                        .transform(CircleTransform())
                        .error(fallbackImage)
                        .into(imageView)
                } else {
                    Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(getCompatibleDrawable(context, fallbackImage))
                        .error(getCompatibleDrawable(context, fallbackImage))
                        .into(imageView)
                }
            }
        }

        fun loadImageFromUrl(
            @NonNull imageView: ImageView,
            @NonNull imageUrl: String,
            @DrawableRes fallbackImage: Int
        ) {
            if (TextUtils.isEmpty(imageUrl)) {
                imageView.setImageResource(fallbackImage)
            } else {
                val context = imageView.context
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(fallbackImage)
                        .into(imageView)
                } else {
                    Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(fallbackImage)
                        .error(getCompatibleDrawable(context, fallbackImage))
                        .into(imageView)
                }
            }
        }

        private fun getCompatibleDrawable(
            @NonNull context: Context, @DrawableRes drawableRes: Int
        ): Drawable {
            return ContextCompat.getDrawable(context, drawableRes)!!.current
        }
    }


}