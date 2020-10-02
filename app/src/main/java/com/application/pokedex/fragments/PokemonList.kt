package com.application.pokedex.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
class PokemonList : Fragment(), PopupMenu.OnMenuItemClickListener {

    internal var compositeDisposable = CompositeDisposable()
    internal var iPokemonList:IPokemonList
    internal lateinit var recycler_view:RecyclerView
    internal lateinit var adapter:PokemonListAdapter
    internal lateinit var search_adapter:PokemonListAdapter
    internal var last_suggested:MutableList<String> = ArrayList()
    internal lateinit var search_bar:MaterialSearchBar

    init {
        val retrofit:Retrofit = RetrofitClient.instance
        iPokemonList = retrofit.create(IPokemonList::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView:View = inflater.inflate(R.layout.fragment_pokemon_list, container, false)
        recycler_view = itemView.findViewById(R.id.pokemon_recyclerview) as RecyclerView
        recycler_view.setHasFixedSize(true)
        recycler_view.clipChildren = false
        recycler_view.layoutManager = GridLayoutManager(activity, 3)


        search_bar = itemView.findViewById(R.id.search_bar) as MaterialSearchBar

        search_bar.inflateMenu(R.menu.pokedex_menu);
        search_bar.menu.setOnMenuItemClickListener(this)

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
                        if(search.toLowerCase(Locale.getDefault()).contains(search_bar.text.toLowerCase(Locale.getDefault())))
                            suggest.add(search)
                    search_bar.lastSuggestions = suggest
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

        fetchData()

        return itemView
    }

    private fun startSearchQuery(text: String) {
        if(UtilitySingleton.pokemonList.isNotEmpty()){
            val result = ArrayList<Pokemon>()
            for(pokemon:Pokemon in UtilitySingleton.pokemonList){
                if(pokemon.name!!.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
                result.add(pokemon)
            }
            search_adapter = PokemonListAdapter(requireActivity(), result)
            pokemon_recyclerview.adapter = search_adapter
        }
        else{
            Toast.makeText(context,"No Pokémon found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchData() {
        compositeDisposable.add(iPokemonList.observeablePokedex
            .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe{pokedex -> UtilitySingleton.pokemonList = pokedex.pokemon!!
           adapter = PokemonListAdapter(requireActivity(), UtilitySingleton.pokemonList)
               recycler_view.adapter = adapter
               search_bar.visibility = View.VISIBLE

               search_bar.lastSuggestions = last_suggested

               for (pokemon:Pokemon in UtilitySingleton.pokemonList){
                   last_suggested.add(pokemon.name!!)
               }
           })
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.choose_pokedex_id -> {
                Toast.makeText(context, "Pokedex switch fragment", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.about_id -> {
                Toast.makeText(context, "Switches to about fragment", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.setting_id -> {
                Toast.makeText(context, "Switches to settings fragment", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

