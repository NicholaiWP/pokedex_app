package com.application.pokedex.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.application.pokedex.R
import com.application.pokedex.fragments.PokemonDetail
import com.application.pokedex.misc.UtilitySingleton

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBar(navController)

        //start pokemon detail (info) fragment
        LocalBroadcastManager.getInstance(this).registerReceiver(showDetail, IntentFilter(UtilitySingleton.KEY_POSITION))

        LocalBroadcastManager.getInstance(this).registerReceiver(showEvolution, IntentFilter(UtilitySingleton.KEY_NUM_EVOLUTION))

    }

    private fun setupActionBar(navController:NavController){
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }

    private val showEvolution = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val detailFragment = PokemonDetail.getInstance()
            val bundle = Bundle()
            val num = intent?.getStringExtra("num")
            bundle.putString("num", num)
            detailFragment.arguments = bundle
            navController.popBackStack()
            navController.navigate(R.id.pokemonDetail, bundle)
        }
    }

    private val showDetail = object:BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            if(intent!!.action!!.toString() == UtilitySingleton.KEY_POSITION){
                val detailFragment = PokemonDetail.getInstance()
                val num = intent.getStringExtra("num")
                val bundle = Bundle()
                bundle.putString("num", num)
                detailFragment.arguments = bundle

                navController.navigate(R.id.pokemonDetail, bundle)
            }
        }

    }

}
