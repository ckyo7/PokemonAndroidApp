package com.example.pokemonsapp.domain.usecases

import com.example.pokemonsapp.data.api.models.PokemonResponse
import com.example.pokemonsapp.domain.Resource
import com.example.pokemonsapp.domain.interfaces.IPokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.sql.Time

class GetPokemonListUseCase(
    private val repository: IPokemonRepository
) {
    fun invoke(): Flow<Resource<out PokemonResponse>> =
        flow {
            emit(Resource.Loading)
            delay(2500)
            var response: Resource<out PokemonResponse> = try {
                val response = repository.getPokemonList()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Respuesta vacía")
                } else {
                    Resource.Error("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: IOException) {
                Resource.Error("Error de red: ${e.localizedMessage}")
            } catch (e: Exception) {
                Resource.Error("Excepción: ${e.localizedMessage}")
            }

            emit(response)
        }.flowOn(Dispatchers.IO)
}