package com.ranjeet.peko.presentation.userprofile

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ranjeet.peko.R
import com.ranjeet.peko.databinding.ActivityProfileBinding
import com.ranjeet.peko.domain.entity.Repository
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.presentation.homescreen.MainActivityViewModel
import com.ranjeet.peko.presentation.userprofile.adapter.RepositoryAdapter
import com.ranjeet.peko.utils.Constant
import com.ranjeet.peko.utils.Utils
import com.ranjeet.peko.utils.gone
import com.ranjeet.peko.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {

    private val viewModel: UserProfileViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    lateinit var binding: ActivityProfileBinding
    var userData: User? = null

    var repositoryAdapter: RepositoryAdapter? = null
    var repositoryList = arrayListOf<Repository>()

    private var errorMessageJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataFromIntent()

        showUserDetails(userData)
        fetchUserRepositories()
        populateSortByOption()
        observeViewModel()
        populateRepoList()
        if (errorMessageJob == null) {
            observeErrorMessage()
        }
    }

    private fun populateSortByOption() {
        val sortingOptions = arrayOf(
            "Default",
            "Name",
            "Fork",
            "Stars",
            "Last Update"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortingOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.setAdapter(adapter)

        binding.sortSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var selectedOption = parent?.getItemAtPosition(position) as kotlin.String;
                updateList(selectedOption);
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        })
    }

    private fun updateList(selectedOption: kotlin.String) {
        // Perform different actions based on the selected option
        val sortedList = when (selectedOption) {
            "Default" -> repositoryList.sortedBy { it.name }// Create a copy of the original list
            "Fork" -> repositoryList.sortedBy { it.forks_count }
            "Stars" -> repositoryList.sortedBy { it.stargazers_count }
            "Last Update" -> repositoryList.sortedBy { it.updated_at }
            else -> repositoryList.toList() // Handle unexpected options by default
        }
        repositoryAdapter?.updateData(sortedList)

    }

    private fun populateRepoList() {
        repositoryAdapter = RepositoryAdapter(repositoryList) {

        }
        binding.let {
            it.ivBack.setOnClickListener {
                finish()
            }
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.rvRepositories.layoutManager = layoutManager
            it.rvRepositories.adapter = repositoryAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainActivityViewModel.user.collect { user ->
                user.let {
                    userData = it
                    showUserDetails(userData)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.userRepository.collect { repositoryList ->
                repositoryList?.let {
                    if (repositoryList.size > 0) {
                        this@UserProfileActivity.repositoryList.clear()
                        this@UserProfileActivity.repositoryList.addAll(repositoryList)
                        repositoryAdapter?.notifyItemRangeInserted(0, repositoryList.size)
                        binding.cvRepository.visible()
                        binding.llSortSpinner.visible()
                    } else {
                        binding.cvRepository.gone()
                    }
                    binding.llProgressBar.gone()
                }
            }
        }
    }

    private fun fetchUserRepositories() {
        userData?.login?.let {
            viewModel.fetchUserRepositories(it)
            mainActivityViewModel.searchUser(it)
        }
    }

    private fun showUserDetails(userData: User?) {
        with(binding) {
            userData?.let {
                if (it.name != null) {
                    textViewName.text = it.name
                }
                it.login.let {
                    textViewUserName.text = "@${it}"
                }
                it.avatar_url?.let {
                    imageViewAvatar.load(it)
                }
                it.bio?.let {
                    textViewBio.text = "Bio : ${it}"
                    textViewBio.visible()
                }
                it.followers?.let {
                    textViewFollowers.text = "Followers : ${it}"
                }
                it.public_repos?.let {
                    textViewRepositories.text = "Repositories : ${it}"
                }
                it.following?.let {
                    textViewFollowing.text = "Following : ${it}"
                }
                it.created_at?.let {
                    textViewCreated.text = "Joined on : ${Utils.convertTimestampToReadable(it)}"
                }
                it.location?.let {
                    textViewLocation.text = it
                    cvLocation.visible()
                }
            }
        }
        binding.cvLocation.setOnClickListener {
            val map = "http://maps.google.co.in/maps?q=${userData?.location}"
            val uri = String.format(Locale.ENGLISH, map)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }

    private fun getDataFromIntent() {
        userData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constant.USER, User::class.java)
        } else {
            intent.getParcelableExtra<User>(Constant.USER)
        }
    }

    private fun observeErrorMessage() {
        errorMessageJob?.cancel()
        errorMessageJob = lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(this@UserProfileActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        errorMessageJob?.cancel()
        super.onDestroy()
    }
}