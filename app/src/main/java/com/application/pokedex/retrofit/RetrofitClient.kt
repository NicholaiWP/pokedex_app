package com.application.pokedex.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retroinstance:Retrofit?=null

    val instance:Retrofit
    get() {
        if(retroinstance == null){
            retroinstance = Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/NicholaiWP/pokemon_file/master/")
               // .baseUrl("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retroinstance!!
    }
}