package ch.zhaw.ma20.cocktailor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.fragments.FavoritesFragment
import ch.zhaw.ma20.cocktailor.fragments.FinderFragment
import ch.zhaw.ma20.cocktailor.fragments.MyBarFragment
import ch.zhaw.ma20.cocktailor.R
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // load data
        var finderFragment = FinderFragment()
        var favoritesFragment = FavoritesFragment()
        var myBarFragment = MyBarFragment()

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

    fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }
}