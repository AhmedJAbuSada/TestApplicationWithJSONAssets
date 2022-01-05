package com.teknasyontestapplication.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.teknasyontestapplication.data.model.Satellite
import com.teknasyontestapplication.databinding.ActivityMainBinding
import com.teknasyontestapplication.ui.MainApplication
import com.teknasyontestapplication.ui.details.DetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var mViewModel: MainViewModel? = null
    private var adapter: SatellitesListAdapter? = null
    private var binding: ActivityMainBinding? = null
    private var list = mutableListOf<Satellite>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.lifecycleOwner = this
        binding?.viewmodel = mViewModel
        setContentView(binding?.root)

        doSearch()

        adapter = SatellitesListAdapter(this, object : SatellitesListAdapter.Interaction {
            override fun clickItem(item: Satellite, position: Int) {
                startActivity(
                    Intent(this@MainActivity, DetailsActivity::class.java)
                        .putExtra(DetailsActivity.satelliteObjKey, item)
                )
            }

        })
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        parseList()


    }

    private fun doSearch() {
        GlobalScope.launch(Dispatchers.Main) {
            mViewModel?.searchStateFlow?.collect { query ->
                adapter?.submitList(list.filter { it.name?.lowercase()?.contains(query.lowercase()) == true })
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun parseList() {
        try {
            val json = MainApplication.loadJSONFromAsset(this, MainApplication.JsonFileType.LIST)
            val turnsType = object : TypeToken<MutableList<Satellite>>() {}.type
            list = Gson().fromJson(json, turnsType)
            adapter?.submitList(list)
            adapter?.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}