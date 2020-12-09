package com.idevdroidapps.gitter.ui.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
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