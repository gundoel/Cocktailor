package ch.zhaw.ma20.cocktailor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
import ch.zhaw.ma20.cocktailor.fragments.FavoritesFragment
import ch.zhaw.ma20.cocktailor.fragments.FinderFragment
import ch.zhaw.ma20.cocktailor.fragments.MyBarFragment
import ch.zhaw.ma20.cocktailor.model.IngredientListItem
import ch.zhaw.ma20.cocktailor.model.Ingredients
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.beust.klaxon.Klaxon
import com.example.cocktailor.R
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_finder.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // load data
        var finderFragment : FinderFragment
        var favoritesFragment : FavoritesFragment
        var myBarFragment : MyBarFragment

        val INGREDIENTSURL = "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list"

        // Ingredients-Request gleich beim Start absetzen
        val requestQueue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET, INGREDIENTSURL,
            Response.Listener<String> { response ->
                val ingredients = Klaxon().parse<Ingredients>(response)
                //val ingredients = mutableListOf<IngredientListItem>(IngredientListItem("Hans"))
                RemoteDataCache.addIngredientsList(ingredients!!.drinks)
                // TODO ugly design: UI should update itself, when data is done caching
                finderFragment = FinderFragment()
                favoritesFragment = FavoritesFragment()
                myBarFragment = MyBarFragment()
                makeCurrentFragment(finderFragment)

                bottom_navigation_menu.setOnNavigationItemSelectedListener {
                    // TODO save current fragment state and restore it on reload: Singleton-Datengefäss (Kotlin Object ist ein Singleton)
                    // TODO how to load pics from json url: BitmapFactory. Bild über URL als decodeByteArray laden. Beim Starten der App alles Cachen inkl. Bilder.
                    // TODO how to update list, filter list: 2. Liste erstellen (Kopie erstellen von voller Liste, Filter auf 2. Liste aufrufen und auf dem adapter.notifyChange aufrufen
                    // Vorsicht: Wenn man die Liste gefiltert hat, muss man sie neu auf dem Adapter setzen. Evtl. ist notify dann nicht mehr nötig.
                    // TODO how to save data: SharedPreferences
                    when (it.itemId) {
                        R.id.nav_finder -> makeCurrentFragment(finderFragment)
                        R.id.nav_favorites -> makeCurrentFragment(favoritesFragment)
                        R.id.nav_mybar -> makeCurrentFragment(myBarFragment)
                    }
                    true
                }
            },

            Response.ErrorListener {
                Log.e("VOLLEY ERROR:", it.toString())
            })
        //add the call to the request queue
        requestQueue.add(request)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }
}