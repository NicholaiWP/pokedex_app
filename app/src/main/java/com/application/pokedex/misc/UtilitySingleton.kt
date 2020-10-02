package com.application.pokedex.misc

import android.graphics.Color
import com.application.pokedex.model.Pokemon


object UtilitySingleton {

    var pokemonList:List<Pokemon> = ArrayList()
    val KEY_POSITION = "position"
    val KEY_NUM_EVOLUTION = "evolution"

    //find pokémon by its id-number
    fun findPokemonByNum(number: String?): Pokemon? {
        for (pokemon:Pokemon in UtilitySingleton.pokemonList){
            if(pokemon.num.equals(number)){
                return pokemon
            }
        }
        return null
    }

    fun findPokemonsByType(type: String?): List<Pokemon>? {
        val result: MutableList<Pokemon> = ArrayList()
        for (pokemon in UtilitySingleton.pokemonList) {
            if (pokemon.type!!.contains(type!!)) result.add(pokemon)
        }
        return result
    }


    fun getColorByType(type: String): Int {
        when (type) {

            "Normal" -> return Color.parseColor("#A4A27A")

            "Dragon" -> return Color.parseColor("#743BFB")

            "Psychic" -> return Color.parseColor("#F15B85")

            "Electric" -> return Color.parseColor("#E9CA3C")

            "Ground" -> return Color.parseColor("#D9BF6C")

            "Grass" -> return Color.parseColor("#81C85B")

            "Poison" -> return Color.parseColor("#A441A3")

            "Steel" -> return Color.parseColor("#BAB7D2")

            "Fairy" -> return Color.parseColor("#DDA2DF")

            "Fire" -> return Color.parseColor("#F48130")

            "Fight" -> return Color.parseColor("#BE3027")

            "Bug" -> return Color.parseColor("#A8B822")

            "Ghost" -> return Color.parseColor("#705693")

            "Dark" -> return Color.parseColor("#745945")

            "Ice" -> return Color.parseColor("#9BD8D8")

            "Water" -> return Color.parseColor("#658FF1")
            else -> return Color.parseColor("#658FA0")
        }
    }

}