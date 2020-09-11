package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.MainActivity
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.CocktailAdapter
import ch.zhaw.ma20.cocktailor.appconst.SortingOptions
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import ch.zhaw.ma20.cocktailor.remote.RecipeRequestHandler
import kotlinx.android.synthetic.main.fragment_cocktails.view.*

class FavoritesFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_cocktails, container, false)
        // cache recipes
        // TODO cache only if necessary
        // display empty Text if no favorites are in list
        val listView: ListView = layout.cocktails as ListView
        val emptyText = layout.emptyFavorites as TextView
        emptyText.setText(R.string.empty_favorites)
        listView.emptyView = emptyText
        val cockTailList = RemoteDataCache.favoriteCocktailsList
        val adapter = CocktailAdapter(cockTailList)
        RecipeRequestHandler.getRecipesForCocktails(cockTailList) {
            if (it != null) {
                layout.cocktails.adapter = adapter
            } else {
                Toast.makeText(
                    Cocktailor.applicationContext(),
                    R.string.unknown_error_recipe_search,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        val types = arrayOf(
            SortingOptions.SORT_BY_NAME,
            SortingOptions.SORT_BY_MISSING_INGREDIENTS,
            SortingOptions.SORT_BY_AVAILABLE_INGREDIENTS
        )
        val spinner = layout.sortlist_spinner
        spinner?.adapter = ArrayAdapter(
            Cocktailor.applicationContext(),
            R.layout.support_simple_spinner_dropdown_item,
            types
        )
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                cockTailList.sortBy { it.strDrink }
                adapter.notifyDataSetChanged()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    SortingOptions.SORT_BY_NAME -> cockTailList.sortBy { it.strDrink }
                    SortingOptions.SORT_BY_MISSING_INGREDIENTS -> cockTailList.sortBy { it.missingIngredients }
                    SortingOptions.SORT_BY_AVAILABLE_INGREDIENTS -> cockTailList.sortByDescending { it.availableIngredients }
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
        adapter.notifyDataSetChanged()
        return layout
    }
}