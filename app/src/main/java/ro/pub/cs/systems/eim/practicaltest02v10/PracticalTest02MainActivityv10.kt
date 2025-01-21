package ro.pub.cs.systems.eim.practicaltest02v10

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.bumptech.glide.Glide

interface PokemonApiService {
    @GET("api/v2/pokemon/{pokemon_name}")
    suspend fun getPokemon(@Path("pokemon_name") pokemonName: String): PokemonResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://pokeapi.co/"

    val api: PokemonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApiService::class.java)
    }
}

data class PokemonResponse(
    val types: List<TypeWrapper>,
    val abilities: List<AbilityWrapper>,
    val sprites: Sprites
)

data class Sprites(
    val front_default: String
)

data class TypeWrapper(
    val type: Type
)

data class Type(
    val name: String
)

data class AbilityWrapper(
    val ability: Ability
)

data class Ability(
    val name: String
)

class PracticalTest02MainActivityv10 : AppCompatActivity() {
    private lateinit var pokemonInput : EditText
    private lateinit var getInfoButton: Button
    private lateinit var type : TextView
    private lateinit var ability : TextView
    private lateinit var pokemonImage: ImageView
    private lateinit var switchToActivity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02v10_main)

        pokemonInput = findViewById(R.id.editTextText)
        getInfoButton = findViewById(R.id.button)
        type = findViewById(R.id.textView)
        ability = findViewById(R.id.textView2)
        pokemonImage = findViewById(R.id.imageView)
        switchToActivity = findViewById(R.id.button2)

        getInfoButton.setOnClickListener {
            val pokemonName = pokemonInput.text.toString().trim()
            if (pokemonName.isNotEmpty()) {
                fetchPokemonData(pokemonName)
            } else {
                Toast.makeText(this, "Please enter a Pokémon name", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun fetchPokemonData(pokemonName: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getPokemon(pokemonName)
                }
                Log.d("PracticalTest02v10", response.toString())


                displayPokemonData(response)
            } catch (e: Exception) {
                Toast.makeText(this@PracticalTest02MainActivityv10, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayPokemonData(response: PokemonResponse) {
        val pokemonType = response.types.joinToString(", ") { it.type.name }
        val abilities = response.abilities.joinToString(", ") { it.ability.name }

        type.text = "Types: $pokemonType"
        ability.text = "Abilities: $abilities"

        Log.d("PracticalTest02v10", type.text as String)
        Log.d("PracticalTest02v10", ability.text as String)


        val imageUrl = response.sprites.front_default

        Log.d("PracticalTest02v10", imageUrl)

        Glide.with(this)
            .load(imageUrl)
            .into(pokemonImage)
    }
}