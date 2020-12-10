package com.idevdroidapps.gitter.ui.adapters

import android.R
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.idevdroidapps.gitter.data.models.Repo

@BindingAdapter("repoStars")
fun TextView.repoStars(repo: Repo?) {
    repo?.let {
        text = it.stars.toString()
    }
}

@BindingAdapter("repoForks")
fun TextView.repoForks(repo: Repo?) {
    repo?.let {
        text = it.forks.toString()
    }
}

@BindingAdapter("userAvatar")
fun ImageView.userAvatar(avatarUrl: String?) {
    avatarUrl?.let { url ->
        val options = RequestOptions()
            .override(96, 96)
            .placeholder(ColorDrawable(ContextCompat.getColor(this.context, R.color.background_light)))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
        try {
            Glide
                .with(this.context)
                .load(url)
                .apply(options)
                .into(this)
        } catch (e: Exception) {
            Log.e("Glide", "Avatar Image Failed in Glide")
        }
    }
}