package com.example.profilesettings

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.profilesettings.ViewModel.MainViewModel
import com.example.profilesettings.ViewModel.MainViewModelFactory
import com.example.profilesettings.data.App
import com.example.profilesettings.databinding.ActivityMainBinding
import com.example.profilesettings.utils.NetworkResult
import com.google.android.material.tabs.TabLayoutMediator

open class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var settingList: ArrayList<App> = ArrayList()
    private var viewPagerItemList: ArrayList<String> = ArrayList()
    private var appList: ArrayList<App> = ArrayList()
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPagerItemList = arrayListOf("Application", "Setting")

        binding.back.setOnClickListener {
            Toast.makeText(this@MainActivity, "There is no back screen", Toast.LENGTH_SHORT).show()
        }

        val repository = (application as ProfileApplication).repository

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        mainViewModel.profileDetails.observe(this, Observer {

            when (it) {
                is NetworkResult.Loading -> {
                    showProgressDialog(true)
                }

                is NetworkResult.Success -> {
                    binding.activityView.visibility = View.VISIBLE
                    hideProgressDialog()
                    appList = it.data?.data?.app_list?: ArrayList()
                    if (appList[0].status == "Active"){
                        binding.isConnected.text = getString(R.string.connected) + "  "
                        binding.isConnected.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);
                    } else {
                        binding.isConnected.text = getString(R.string.connected) + "  "
                        binding.isConnected.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_no, 0);
                    }
                    val adapter =
                        PageAdapter(
                            supportFragmentManager,
                            lifecycle,
                            appList,
                            settingList
                        )
                    binding.viewPager.adapter = adapter
                    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                        tab.text = viewPagerItemList[position]
                    }.attach()
                }

                is NetworkResult.Error -> {
                    binding.activityView.visibility = View.VISIBLE
                    when (it.message) {
                        "No Network Connection" -> {
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })





    }

    class PageAdapter(
        fm: FragmentManager, lifecycle: Lifecycle, private var applicationList: ArrayList<App>,
        private var settingList: ArrayList<App>
    ) : FragmentStateAdapter(fm, lifecycle) {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ViewPagerItemFragment.newInstance("", applicationList)
                }
                1 -> {
                    ViewPagerItemFragment.newInstance("", settingList)
                }
                else -> {
                    ViewPagerItemFragment.newInstance("", applicationList)
                }
            }
        }

    }

    open fun showProgressDialog(isDismissOnBack: Boolean = true) {
        progressDialog?.show()
    }

    open fun hideProgressDialog() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

}