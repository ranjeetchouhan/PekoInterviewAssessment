package com.ranjeet.peko.presentation.homescreen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ranjeet.peko.R
import com.ranjeet.peko.databinding.ItemViewUserBinding
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.utils.visible

class UserListAdapter(var userList: ArrayList<User>, var callback: (User) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemViewUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemViewUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user = userList[position]
        with(holder) {
            with(binding) {
                if (user.name != null) {
                    textViewName.text = user.name
                } else textViewName.text = user.login
                user.avatar_url?.let {
                    imageViewAvatar.load(it)
                }
                user.bio?.let {
                    textViewBio.text = "Bio : ${it}"
                    textViewBio.visible()
                }
                user.followers?.let {
                    textViewFollowers.text = "Followers : ${it}"
                }
                user.public_repos?.let {
                    textViewRepositories.text = "Repositories : ${it}"
                }
                cvParent.setOnClickListener {
                    callback.invoke(user)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}