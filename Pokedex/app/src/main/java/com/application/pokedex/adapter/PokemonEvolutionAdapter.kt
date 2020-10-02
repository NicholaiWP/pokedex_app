package com.application.pokedex.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.application.pokedex.R
import com.application.pokedex.misc.UtilitySingleton
import com.application.pokedex.model.Evolution
import com.robertlevonyan.views.chip.Chip

class PokemonEvolutionAdapter(internal var context: Context, var evolutionList:List<Evolution>):
    RecyclerView.Adapter<PokemonEvolutionAdapter.MyViewHolder>() {

    init {
        if(evolutionList == null){
            evolutionList = ArrayList()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonEvolutionAdapter.MyViewHolder {
        val itemview:View = LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
       return evolutionList.size
    }

    override fun onBindViewHolder(holder: PokemonEvolutionAdapter.MyViewHolder, position: Int) {
        holder.chip.chipText = evolutionList[position].name
        holder.chip.changeBackgroundColor(UtilitySingleton.getColorByType(UtilitySingleton.findPokemonByNum(
            evolutionList[position].num!!)!!.type!![0]))

        holder.chip.setOnChipClickListener{
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(UtilitySingleton.KEY_NUM_EVOLUTION)
                .putExtra("num", evolutionList[holder.adapterPosition].num))
        }

    }
    inner class MyViewHolder(itemview:View): RecyclerView.ViewHolder(itemview) {
        internal var chip: Chip

        init {
            chip = itemView.findViewById(R.id.chip) as Chip
        }

    }

}

