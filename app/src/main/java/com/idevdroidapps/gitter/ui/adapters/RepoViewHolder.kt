package com.idevdroidapps.gitter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idevdroidapps.gitter.R
import com.idevdroidapps.gitter.data.models.Owner
import com.idevdroidapps.gitter.data.models.Repo
import com.idevdroidapps.gitter.databinding.ListItemRepoBinding

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(private val binding: ListItemRepoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: Repo?) {
        if (repo == null) {
            val resources = itemView.resources
            val textUnknown = resources.getString(R.string.unknown)
            val textLoading = resources.getString(R.string.loading)
            val item = Repo(
                0, textLoading, textUnknown, textUnknown, textUnknown,
                0, 0, textUnknown, Owner(textUnknown, textUnknown)
            )
            binding.repo = item
        } else {
            binding.repo = repo
        }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RepoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemRepoBinding.inflate(layoutInflater, parent, false)
            return RepoViewHolder(binding)
        }
    }
}