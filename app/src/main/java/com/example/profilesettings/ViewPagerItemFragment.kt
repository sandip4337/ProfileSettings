package com.example.profilesettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.profilesettings.data.App
import com.example.profilesettings.databinding.FragmentViewPagerItemBinding
import java.util.*
import kotlin.collections.ArrayList

class ViewPagerItemFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerItemBinding
    private val binder get() = binding
    private var type: String = "Applications"
    private lateinit var list: ArrayList<App>
    private var appNameList: ArrayList<String>? = null
    lateinit var adapter: AppListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerItemBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (list.size != 0) {
            val layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binder.rvAppList.layoutManager = layoutManager
            adapter = AppListAdapter(requireActivity(), list)
            binder.rvAppList.adapter = adapter
        }

        binder.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<App>()
            for (i in list) {
                if (i.app_name.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String, list: ArrayList<App>): ViewPagerItemFragment {
            val fragment = ViewPagerItemFragment()
            fragment.type = type
            fragment.list = list
            return fragment
        }

    }
}