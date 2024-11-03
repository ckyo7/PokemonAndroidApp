package com.example.pokemonsapp.presentation.view.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemonsapp.R
import com.example.pokemonsapp.data.api.models.PokemonModel

class PokemonAdapter(
    private val pokemonList: List<PokemonModel>,
    val listener: OnPokemonClickListener
) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonImage: ImageView = itemView.findViewById(R.id.pokemon_image)
        val pokemonName: TextView = itemView.findViewById(R.id.pokemon_name)


        fun bind(pokemon: PokemonModel) {
            pokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }

            try {
                Glide.with(itemView).load(getImageUrlFromPokemonNumber(pokemon.url))
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.mipmap.ic_snorlak_placeholder)
                    .into(pokemonImage)
            } catch (exception: Exception) {
                Glide.with(itemView).load(R.drawable.ic_launcher_foreground)
                    .into(pokemonImage)
                Log.d(
                    null,
                    exception.message ?: "Error has occurred when catching image from server"
                )

            }
            itemView.rootView.setOnClickListener {
                listener.onPokemonClick(pokemon.name)
            }
        }

        private fun getImageUrlFromPokemonNumber(url: String): String {
            val pokemonId = extractPokemonIdFromUrl(url)
            return itemView.context.getString(R.string.url_pokemon_image, pokemonId)
        }

        private fun extractPokemonIdFromUrl(url: String): String {
            val urlParts = url.split("/")
            return urlParts[urlParts.lastIndex - 1]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.pokemon_card,
            parent, false
        )
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.bind(pokemon)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    interface OnPokemonClickListener {
        fun onPokemonClick(name: String)
    }
}