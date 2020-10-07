package com.application.pokedex.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.pokedex.R
import com.application.pokedex.adapter.PokemonEvolutionAdapter
import com.application.pokedex.adapter.PokemonTypeAdapter
import com.application.pokedex.misc.UtilitySingleton
import com.application.pokedex.model.Pokemon
import com.bumptech.glide.Glide


/**
 * A simple [Fragment] subclass.
 */
class PokemonDetail : Fragment() {

    internal lateinit var pokemon_img:ImageView

    internal lateinit var pokemon_name:TextView
    internal lateinit var pokemon_description:TextView
    internal lateinit var pokemon_height:TextView
    internal lateinit var pokemon_weight:TextView

     internal lateinit var recycler_type:RecyclerView
     internal lateinit var recycler_weakness:RecyclerView
     internal lateinit var recycler_prev_evo:RecyclerView
     internal lateinit var recycler_next_evo:RecyclerView

    companion object{
        private var instance:PokemonDetail? = null

        fun getInstance():PokemonDetail{
            if(instance == null){
                instance = PokemonDetail()
            }
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val itemView:View = inflater.inflate(R.layout.fragment_pokemon_detail, container, false)

        val pokemon = UtilitySingleton.findPokemonByNum(requireArguments().getString("num"))

        pokemon_img = itemView.findViewById(R.id.pokemon_image) as ImageView
        pokemon_name = itemView.findViewById(R.id.name) as TextView
        pokemon_description = itemView.findViewById(R.id.description) as TextView
        pokemon_height = itemView.findViewById(R.id.height) as TextView
        pokemon_weight = itemView.findViewById(R.id.weight) as TextView

        recycler_next_evo = itemView.findViewById(R.id.recycler_next_evolution) as RecyclerView
        recycler_next_evo.setHasFixedSize(true)
        recycler_next_evo.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recycler_prev_evo = itemView.findViewById(R.id.recycler_prev_evolution) as RecyclerView
        recycler_prev_evo.setHasFixedSize(true)
        recycler_prev_evo.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recycler_type = itemView.findViewById(R.id.recycler_type) as RecyclerView
        recycler_type.setHasFixedSize(true)
        recycler_type.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        recycler_weakness = itemView.findViewById(R.id.recycler_weakness) as RecyclerView
        recycler_weakness.setHasFixedSize(true)
        recycler_weakness.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        setDetailAboutPokemon(pokemon)

        return itemView
    }

    private fun setDetailAboutPokemon(pokemon: Pokemon?) {
        Glide.with(requireActivity()).load(pokemon!!.img).into(pokemon_img)
        pokemon_name.text = pokemon.name

       val height =  resources.getString(R.string.height, pokemon.height)
        val styledTextHeight: CharSequence = HtmlCompat.fromHtml(height, HtmlCompat.FROM_HTML_MODE_LEGACY)
        pokemon_height.text = styledTextHeight

        val weight = resources.getString(R.string.weight, pokemon.weight)
        val styledTextWeight: CharSequence = HtmlCompat.fromHtml(weight, HtmlCompat.FROM_HTML_MODE_LEGACY)
        pokemon_weight.text = styledTextWeight

        pokemon_description.text = resources.getString(R.string.Description, pokemon.description)

        val pokemonTypeAdapter = PokemonTypeAdapter(requireActivity(), pokemon.type!!)
        recycler_type.adapter = pokemonTypeAdapter

        val weaknessAdapter = PokemonTypeAdapter(requireActivity(), pokemon.weaknesses!!)
            recycler_weakness.adapter = weaknessAdapter

        if(pokemon.prev_evolution != null){
            val evolutionAdapterPrev = PokemonEvolutionAdapter(requireActivity(), pokemon.prev_evolution!!)
            recycler_prev_evo.adapter = evolutionAdapterPrev
        }

       if(pokemon.next_evolution != null){
            val evolutionAdapterNext = PokemonEvolutionAdapter(requireActivity(), pokemon.next_evolution!!)
            recycler_next_evo.adapter = evolutionAdapterNext
        }

    }

}
