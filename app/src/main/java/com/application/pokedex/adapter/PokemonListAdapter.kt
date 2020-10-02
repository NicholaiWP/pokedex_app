package com.application.pokedex.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.application.pokedex.R
import com.application.pokedex.interfaces.ItemClickListener
import com.application.pokedex.misc.UtilitySingleton
import com.application.pokedex.model.Pokemon
import com.bumptech.glide.Glide

class PokemonListAdapter(internal var context: Context, internal var pokemonList:List<Pokemon>):RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.pokemon_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(context).load(pokemonList[position].img).into(holder.imgPokemon)
        holder.textPokemon.text = pokemonList[position].name
        holder.numbPokemon.text = pokemonList[position].num

            holder.setItemClickListener(object:ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(Intent(UtilitySingleton.KEY_POSITION).putExtra("num", pokemonList[position].num))
                }
            })
    }

    inner class MyViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        internal var textPokemon:TextView
        internal var imgPokemon:ImageView
        internal var numbPokemon:TextView

        internal var itemClickListener:ItemClickListener? = null

        fun setItemClickListener(itemclickListener:ItemClickListener){
            this.itemClickListener = itemclickListener
        }

        init {
            imgPokemon = itemview.findViewById(R.id.pokemon_image) as ImageView
            textPokemon = itemview.findViewById(R.id.pokemon_name) as TextView
            numbPokemon = itemview.findViewById(R.id.pokemon_number) as TextView
            itemview.setOnClickListener { view -> itemClickListener!!.onClick(view, adapterPosition) }
        }


    }

}

