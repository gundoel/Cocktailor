package ch.zhaw.ma20.cocktailor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.appconst.PersistentData
import ch.zhaw.ma20.cocktailor.fragments.CocktailFragment
import ch.zhaw.ma20.cocktailor.fragments.FinderFragment
import ch.zhaw.ma20.cocktailor.fragments.MyBarFragment
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private var cacheTearDown : CacheTearDown? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val finderFragment = FinderFragment()
        val favoritesFragment = CocktailFragment()
        // CocktailFragment is being reused. Parameter needed for different behavior when displaying favorites
        val bundle : Bundle = bundleOf("type" to "favorites")
        favoritesFragment.arguments = bundle
        val myBarFragment = MyBarFragment()

        cacheTearDown = CacheTearDown()

        // set finder fragment as first fragment
        makeCurrentFragment(finderFragment)

        bottom_navigation_menu.setOnNavigationItemSelectedListener {
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
            addToBackStack(null)
            commit()
        }
    }

    override fun onPause(){
        super.onPause()
        cacheTearDown?.persistIngredientList(RemoteDataCache.myBarList, PersistentData.MY_BAR_LIST)
        cacheTearDown?.persistCocktailList(RemoteDataCache.favoriteCocktailsList,PersistentData.MY_FAVORITES_LIST)
    }
}