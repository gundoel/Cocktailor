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
import ch.zhaw.ma20.cocktailor.model.Ingredients
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.beust.klaxon.Klaxon
import com.example.cocktailor.R
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_finder.*

class MainActivity : AppCompatActivity() {
    var adapter: IngredientsSearchAdapter? = null;
    val INGREDIENTS_URL = "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        var finderFragment = FinderFragment()
        val favoritesFragment = FavoritesFragment()
        val myBarFragment = MyBarFragment()

        makeCurrentFragment(finderFragment)

        bottom_navigation_menu.setOnNavigationItemSelectedListener {
            //TODO save current fragment state and restore it on reload
            when (it.itemId) {
                R.id.nav_finder -> makeCurrentFragment(finderFragment)
                R.id.nav_favorites -> makeCurrentFragment(favoritesFragment)
                R.id.nav_mybar -> makeCurrentFragment(myBarFragment)
            }
            true
        }

        // Ingredients-Request gleich beim Start absetzen
        val requestQueue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET, INGREDIENTS_URL,
            Response.Listener<String> { response ->
                //val inputStream = baseContext.resources.openRawResource(R.raw.stationboard)
                val ingredient = Klaxon().parse<Ingredients>(response)
                adapter = IngredientsSearchAdapter(ingredient!!.drinks, baseContext);
                ingredients_list.adapter = adapter
                //find the response string in "response"
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