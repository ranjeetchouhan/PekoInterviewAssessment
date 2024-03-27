package com.ranjeet.peko.presentation.userprofile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ranjeet.peko.R
import com.ranjeet.peko.databinding.ItemViewRepositoryBinding
import com.ranjeet.peko.databinding.ItemViewUserBinding
import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.Utils
import com.ranjeet.peko.utils.visible

class RepositoryAdapter(
    var repositoryList: ArrayList<Repository>,
    var callback: (Repository) -> Unit
) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemViewRepositoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemViewRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var repository = repositoryList[position]
        with(holder) {
            with(binding) {
                textViewRepositoryName.text = repository.name
                textViewHtmlUrl.text = repository.html_url
                textViewDescription.text = repository.description
                textViewStar.text = repository.stargazers_count.toString()
                textViewFork.text = repository.forks_count.toString()
                textViewUpdated.text = "Last Update : ${Utils.convertTimestampToReadableDateOnly(repository.updated_at)}"
            }
        }
    }

    fun updateData(sortedList: List<Repository>) {
        repositoryList.clear()
        repositoryList.addAll(sortedList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }
}