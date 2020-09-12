package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.MainActivity
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.CocktailAdapter
import ch.zhaw.ma20.cocktailor.appconst.SortingOptions
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import ch.zhaw.ma20.cocktailor.remote.RecipeRequestHandler
import kotlinx.android.synthetic.main.fragment_cocktails.view.*

class CocktailFragment : BaseFragment() {
    var cocktailList = mutableListOf<Cocktail>()
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
        val type: String? = arguments?.getString("type")
        /* Fragment is being to display favorites and to display search results. Loading recipes is only needed with favorites,
        since recipes are already in cache when displaying search results */
        if (type == "favorites") {
            cocktailList = RemoteDataCache.favoriteCocktailsList
            RecipeRequestHandler.getRecipesForCocktails(cocktailList) {
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
            adapter = CocktailAdapter(cocktailList)
        }
        val adapter = CocktailAdapter(cocktailList)
        val spinner = layout.sortlist_spinner
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
        layout.cocktails.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position)
            val recipeFragment = RecipeFragment(element.idDrink)
            (activity as MainActivity?)?.makeCurrentFragment(
                recipeFragment
            )
        }
        layout.cocktails.adapter = adapter
        adapter.notifyDataSetChanged()
        return layout
    }
}