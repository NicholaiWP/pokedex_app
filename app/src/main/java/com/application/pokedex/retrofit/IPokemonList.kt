package com.application.pokedex.retrofit

import com.application.pokedex.model.Pokedex
import retrofit2.http.GET
import io.reactivex.Observable

interface IPokemonList {
    //http get call to baseurl's (see retrofitClient object) json document
    @get:GET("pokedex_kanto.json?")
     val observeablePokedex:Observable<Pokedex>
}