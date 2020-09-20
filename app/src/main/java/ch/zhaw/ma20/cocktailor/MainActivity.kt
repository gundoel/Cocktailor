package ch.zhaw.ma20.cocktailor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ch.zhaw.ma20.cocktailor.fragments.CocktailFragment
import ch.zhaw.ma20.cocktailor.fragments.FinderFragment
import ch.zhaw.ma20.cocktailor.fragments.MyBarFragment
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val finderFragment = FinderFragment()
        val favoritesFragment = CocktailFragment()
        // CocktailFragment is being reused. Parameter needed for different behavior when displaying favorites
        val bundle: Bundle = bundleOf("type" to "favorites")
        favoritesFragment.arguments = bundle
        val myBarFragment = MyBarFragment()

        // set finder fragment as first fragment. do not add to backstack, otherwise blank screen is shown when back button is pressed
        makeCurrentFragment(finderFragment, false)

        bottom_navigation_menu.setOnNavigationItemSelectedListener {
            // reset backstack
            var c = supportFragmentManager.backStackEntryCount
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            c = supportFragmentManager.backStackEntryCount
            when (it.itemId) {
                R.id.nav_finder -> makeCurrentFragment(finderFragment, false)
                R.id.nav_favorites -> makeCurrentFragment(favoritesFragment, false)
                R.id.nav_mybar -> makeCurrentFragment(myBarFragment, false)
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun makeCurrentFragment(fragment: Fragment, addToBackstack: Boolean = true) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            if (addToBackstack) {
                addToBackStack(null)
            }
            commit()
        }
    }

    override fun onBackPressed() {
        // 1 or more fragments in backstack (user is on a sub-fragment): hitting back button should return to previous view.

        if (supportFragmentManager.backStackEntryCount >= 1) {
            super.onBackPressed()
            // main fragments from bottom navigation are never added to backstack.
            // therefore app needs to be closed when back buton is pressed on a fragment of main menu
        } else {
            // show confirmation dialog and finish if user presses YES
            val builder = AlertDialog.Builder(this)
            builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(applicationContext.getText(R.string.close_cocktailor))
                .setMessage(applicationContext.getText(R.string.exit_confirm_message))
                .setPositiveButton(
                    applicationContext.getText(R.string.yes)
                ) { _, _ ->
                    finish()
                    Toast.makeText(
                        applicationContext,
                        applicationContext.getText(R.string.cocktailor_closed),
                        Toast.LENGTH_SHORT
                    ).show()
                }.setNegativeButton(applicationContext.getText(R.string.no), null).show()
        }
    }

    override fun onPause() {
        super.onPause()
        PersistenceHandler.persistIngredientList(RemoteDataCache.getMyBarList())
        PersistenceHandler.persistCocktailList(RemoteDataCache.getFavorites())
    }

}