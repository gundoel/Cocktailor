package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.MainActivity
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.CocktailAdapter
import ch.zhaw.ma20.cocktailor.appconst.SortingOptions
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import ch.zhaw.ma20.cocktailor.remote.RecipeRequestHandler
import kotlinx.android.synthetic.main.fragment_cocktails.view.*
import kotlinx.android.synthetic.main.fragment_my_bar.view.*

/**
 * Fragment to display cocktail search results and favorite cocktails.
 * Different behavior when displaying favoritexs is handled with bundle parameter.
 */
class CocktailFragment : Fragment() {
    private var cocktailList = mutableListOf<Cocktail>()
    private var adapter = CocktailAdapter(cocktailList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_cocktails, container, false)
        val types = arrayOf(
            SortingOptions.SORT_BY_NAME,
            SortingOptions.SORT_BY_MISSING_INGREDIENTS,
            SortingOptions.SORT_BY_AVAILABLE_INGREDIENTS
        )
        val spinner = layout.sortlist_spinner
        val type: String? = arguments?.getString("type")
        /* Fragment is being used to display favorites and to display search results. Loading recipes is only needed with favorites,
        since recipes are already in cache when displaying search results */
        if (type == "favorites") {
            // display empty Text if no favorites are in list
            val listView: ListView = layout.cocktails as ListView
            val emptyText = layout.emptyFavorites as TextView
            emptyText.setText(R.string.empty_favorites)
            listView.emptyView = emptyText

            cocktailList = RemoteDataCache.getFavorites()
            RecipeRequestHandler.getAndCacheRecipesForCocktails(cocktailList) {
                if (it != null) {
                    adapter = CocktailAdapter(cocktailList)
                    layout.cocktails.adapter = adapter
                } else {
                    Toast.makeText(
                        Cocktailor.applicationContext(),
                        R.string.unknown_error_recipe_search,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            cocktailList = RemoteDataCache.lastCocktailSearchResultList
        }
        adapter = CocktailAdapter(cocktailList)

        // sorting of displayed cocktails
        spinner?.adapter = ArrayAdapter(
            Cocktailor.applicationContext(),
            R.layout.support_simple_spinner_dropdown_item,
            types
        )
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                cocktailList.sortBy { it.strDrink }
                adapter.notifyDataSetChanged()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    SortingOptions.SORT_BY_NAME -> cocktailList.sortBy { it.strDrink }
                    SortingOptions.SORT_BY_MISSING_INGREDIENTS -> cocktailList.sortBy { it.missingIngredients }
                    SortingOptions.SORT_BY_AVAILABLE_INGREDIENTS -> cocktailList.sortByDescending { it.availableIngredients }
                }
                adapter.notifyDataSetChanged()
            }

        }

        // displaying recipe on click on cocktail
        layout.cocktails.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position)
            val recipeFragment = RecipeFragment(element.idDrink)
            (activity as MainActivity?)?.makeCurrentFragment(
                recipeFragment
            )
        }
        val listView: ListView = layout.cocktails as ListView
        val emptyText = layout.emptyFavorites as TextView
        listView.emptyView = emptyText
        layout.cocktails.adapter = adapter
        return layout
    }
}