package com.application.pokedex.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.application.pokedex.R
import com.application.pokedex.adapter.PokemonListAdapter
import com.application.pokedex.misc.UtilitySingleton
import com.application.pokedex.model.Pokemon
import com.application.pokedex.retrofit.IPokemonList
import com.application.pokedex.retrofit.RetrofitClient
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class PokemonList : Fragment() {

    private var compositeDisposable = CompositeDisposable()
    private lateinit var iPokemonList:IPokemonList
    private lateinit var recycler_view:RecyclerView
    private lateinit var adapter:PokemonListAdapter
    private lateinit var search_adapter:PokemonListAdapter
    private var last_suggested:MutableList<String> = ArrayList()
    private lateinit var search_bar:MaterialSearchBar
    private var itemView:View? = null
    private  lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
                 itemView  = inflater.inflate(R.layout.fragment_pokemon_list, container, false)
                recycler_view = itemView!!.findViewById(R.id.pokemon_recyclerview) as RecyclerView
                recycler_view.setHasFixedSize(true)
                recycler_view.clipChildren = false
                recycler_view.layoutManager = GridLayoutManager(activity, 3)
                search_bar = itemView!!.findViewById(R.id.search_bar) as MaterialSearchBar

                 builder = AlertDialog.Builder(itemView!!.context)
                 builder.setTitle("No internet Connection")
                 builder.setMessage("Please turn on internet connection to continue")
                 builder.setNeutralButton("Close", {dialog, i -> dialog.dismiss()})
                 builder.setPositiveButton("Enable internet", { dialog, i -> startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)})
                alertDialog = builder.create()

                 search_bar.setHint("Enter a Pokémon Name")
                search_bar.setCardViewElevation(10)

                search_bar.addTextChangeListener(object :TextWatcher{
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val suggest = mutableListOf<String>()
                        for(search:String in last_suggested)
                            if(search.toLowerCase(Locale.getDefault()).contains(p0.toString()))
                                suggest.add(search)
                    }

                })

                search_bar.setOnSearchActionListener(object: OnSearchActionListener{
                    override fun onButtonClicked(buttonCode: Int) {
                    }

                    override fun onSearchStateChanged(enabled: Boolean) {
                        if(!enabled){
                            pokemon_recyclerview.adapter = adapter
                        }
                    }

                    override fun onSearchConfirmed(text: CharSequence?) {
                        startSearchQuery(text.toString())
                    }
                })

        isNetworkConnectionAvailable(itemView!!.context)
        return itemView

        }

    private fun startSearchQuery(text: String) {
        if(UtilitySingleton.pokemonList.isNotEmpty()){

            val resultList = ArrayList<Pokemon>()

            for(pokemon:Pokemon in UtilitySingleton.pokemonList){
                if(pokemon.name!!.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){
                    resultList.add(pokemon)
                    search_adapter = PokemonListAdapter(requireActivity(), resultList)
                    pokemon_recyclerview.adapter = search_adapter
                }
                else if(pokemon.type.toString().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                    resultList.add(pokemon)
                    search_adapter = PokemonListAdapter(requireActivity(), resultList)
                    pokemon_recyclerview.adapter = search_adapter
                }
                else if(text.contains("legendary") && pokemon.legendary == true || text.contains("legendary") && pokemon.legendary == true) {
                    resultList.add(pokemon)
                    search_adapter = PokemonListAdapter(requireActivity(), resultList)
                    pokemon_recyclerview.adapter = search_adapter
                }
            }

        }
        else{
            Toast.makeText(context,"No Pokémon found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchData() {
        val retrofit:Retrofit = RetrofitClient.instance
        iPokemonList = retrofit.create(IPokemonList::class.java)
        compositeDisposable.add(iPokemonList.observeablePokedex
            .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe{pokedex -> UtilitySingleton.pokemonList = pokedex.pokemon!!
           adapter = PokemonListAdapter(requireActivity(), UtilitySingleton.pokemonList)
               recycler_view.adapter = adapter
               search_bar.visibility = View.VISIBLE

               for (pokemon:Pokemon in UtilitySingleton.pokemonList){
                   last_suggested.add(pokemon.name!!)
               }
           })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun isNetworkConnectionAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnected
        return if (isConnected) {
            fetchData()
            Log.d("Network", "Connected")
            alertDialog.dismiss()
            true
        } else {
            alertDialog.show()
            Log.d("Network", "Not Connected")
            false
        }
    }

    override fun onResume() {
        super.onResume()
        isNetworkConnectionAvailable(itemView!!.context)
    }
}

