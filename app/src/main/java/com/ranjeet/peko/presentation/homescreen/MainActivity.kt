package com.ranjeet.peko.presentation.homescreen

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ranjeet.peko.databinding.ActivityMainBinding
import com.ranjeet.peko.domain.entity.User
import com.ranjeet.peko.presentation.homescreen.adapter.UserListAdapter
import com.ranjeet.peko.presentation.userprofile.UserProfileActivity
import com.ranjeet.peko.utils.Constant
import com.ranjeet.peko.utils.gone
import com.ranjeet.peko.utils.hideKeyboard
import com.ranjeet.peko.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    var userListAdapter: UserListAdapter? = null
    var userList = arrayListOf<User>()

    private var errorMessageJob: Job? = null
    val newTextFlow: MutableStateFlow<String> = MutableStateFlow("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        observeViewModel()
        fetchUserList()
        populateUserList()
        searchView()
        if (errorMessageJob == null) {
            observeErrorMessage()
        }
    }

    private fun searchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                fetchUser(query)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (!newText.equals("")) {
                    newTextFlow.value = newText
                    return true
                }
                return false
            }
        })

        newTextAsFlow().map { text ->
            // Perform actions with the new text
            fetchUser(text)
        }.launchIn(lifecycleScope)
    }

    fun newTextAsFlow(): Flow<String> {
        return newTextFlow
            .debounce(300)
            .filter { it.isNotBlank() } // Ensure there's actual text to search for
            .distinctUntilChanged() // Ensure only distinct, different values trigger API calls
    }
    private fun populateUserList() {
        userListAdapter = UserListAdapter(userList) {
            goToProfileActivity(it)
        }
        binding.let {
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.rvUserList.layoutManager = layoutManager
            it.rvUserList.adapter = userListAdapter
        }
    }

    private fun goToProfileActivity(it: User) {
        var intent = Intent(this, UserProfileActivity::class.java).apply {
            putExtra(Constant.USER, it)
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding.llProgressBar.visible()
                } else {
                    binding.llProgressBar.gone()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userList.collect { userList ->
                userList?.let {
                    binding.rvUserList.visible()
                    binding.animationView.gone()
                    this@MainActivity.userList.clear()
                    this@MainActivity.userList.addAll(userList)
                    userListAdapter?.notifyItemRangeInserted(0, userList.size)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                user.let {
                    binding.rvUserList.visible()
                    binding.animationView.gone()
                    this@MainActivity.userList.clear()
                    if (user != null) {
                        this@MainActivity.userList.add(user)
                    }
                    userListAdapter?.notifyDataSetChanged()
                }
            }
        }

    }

    internal fun fetchUserList() {
        viewModel.fetchUserList()
    }

    internal fun fetchUser(username: String) {
        viewModel.searchUser(username)
    }

    private fun observeErrorMessage() {
        errorMessageJob?.cancel()
        errorMessageJob = lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    binding.rvUserList.gone()
                    binding.animationView.visible()
                }
            }
        }
    }

    override fun onDestroy() {
        errorMessageJob?.cancel()
        super.onDestroy()
    }
}