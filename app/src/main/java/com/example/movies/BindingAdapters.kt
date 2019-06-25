package com.example.movies

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movies.enums.ApiStatus
import com.example.onlinebakiyekotlin.network.model.Hesap
import com.example.onlinebakiyekotlin.network.model.Sube
import com.example.movies.utility.Message
import com.example.movies.utility.Utility
import java.lang.StringBuilder

// http://image.tmdb.org/t/p/w185//w9kR8qbmQ01HwnvK4alvnQ2ca0L.jpg
const val IMG_BASE_URL = "image.tmdb.org/t/p/w185/"

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, pathUrl: String?) {

    var imgUrl = pathUrl
    imgUrl = imgUrl?.trim()
    imgUrl = IMG_BASE_URL + imgUrl
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("http").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("messageApiStatus")
fun bindMessageApiStatus(statusImageView: ImageView, message: Message?) {
    when (message?.apiStatus) {

        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.ERROR -> {

            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.warning)
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.success)
        }

    }
}

@BindingAdapter("messageApiStatusFlipper")
fun bindMessageApiStatusFlipper(flipper: ViewFlipper, message: Message?) {
    when (message?.apiStatus) {

        ApiStatus.LOADING -> {
        }
        ApiStatus.ERROR -> {

            Log.i("GenericDialog", "BindingAdapters ApiStatus: Error")
            flipper.setInAnimation(flipper.context, R.anim.slide_in_right)
            flipper.setOutAnimation(flipper.context, R.anim.slide_out_right)
            flipper.displayedChild = 1
            Log.i("test", "messageApiStatusFlipper | error1")
        }
        ApiStatus.DONE -> {
            flipper.setInAnimation(flipper.context, R.anim.fade_in)
            flipper.setOutAnimation(flipper.context, R.anim.fade_out)
            flipper.displayedChild = 1
        }

    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("messageText")
fun bindMessageText(textView: TextView, sube: Sube?) {

    textView.text = sube?.SubeKodu.toString() + " / " + sube?.SubeAdi
}

@SuppressLint("SetTextI18n")
@BindingAdapter("subeMoneyText")
fun bindSubeMoneyText(moneyTxt: TextView, sube: Sube?) {

    //textView.text

    var stringBuilder = StringBuilder()
    for (bakiye in sube?.Bakiye!!) {
        stringBuilder.append(bakiye.Value.toString() + " " + bakiye.Key)
        stringBuilder.append("\n")
    }
    moneyTxt.text = stringBuilder.toString()
}

@SuppressLint("SetTextI18n")
@BindingAdapter("hesapMoneyText")
fun bindHesapMoneyText(moneyTxt: TextView, hesap: Hesap?) {

    //textView.text

    var stringBuilder = StringBuilder()
    stringBuilder.append(hesap?.DovizCinsi + " " + hesap?.Bakiye?.toString())
    moneyTxt.text = stringBuilder.toString()
}




