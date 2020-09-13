package ch.zhaw.ma20.cocktailor.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.MainActivity
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
import ch.zhaw.ma20.cocktailor.appconst.Connector
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import ch.zhaw.ma20.cocktailor.remote.CocktailRequestHandler
import ch.zhaw.ma20.cocktailor.remote.RecipeRequestHandler
import kotlinx.android.synthetic.main.fragment_cocktails.view.*
import kotlinx.android.synthetic.main.fragment_finder.*
import kotlinx.android.synthetic.main.fragment_finder.view.*
import kotlinx.android.synthetic.main.fragment_my_bar.view.*
import java.util.*

/**
 * Fragment to select ingredients for cocktail search and start search.
 */
class FinderFragment : BaseFragment() {
    var adapter: IngredientsSearchAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_finder, container, false)
        val selectedItems = RemoteDataCache.selectedItemsSet

        adapter = IngredientsSearchAdapter(
            RemoteDataCache.ingredientsList
        )
        val listView: ListView = layout.ingredients_list as ListView
        val emptyText = layout.NoItemsInBar as TextView
        listView.emptyView = emptyText
        layout.ingredients_list.adapter = adapter

        /* filter list items to ingredients from my bar only when switch is checked.
           this is done by setting new adapter with ingredients from my bar.
        */
        layout.selectFromMyBarSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val reducedList = RemoteDataCache.getMyBarList()
                adapter = IngredientsSearchAdapter(
                    reducedList
                )
            } else {
                // set default adapter
                adapter = IngredientsSearchAdapter(
                    RemoteDataCache.ingredientsList
                )
            }
            layout.ingredients_list.adapter = adapter
        }

        /* filter list items to selected items only when switch is checked.
           this is done by setting new adapter with reduced list of selected items
        */
        layout.filterIngredientsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val reducedList =
                    RemoteDataCache.ingredientsList.filter {
                        selectedItems.contains(it.strIngredient1)
                    }
                adapter = IngredientsSearchAdapter(
                    reducedList as MutableList<Ingredient>
                )

            } else if(selectFromMyBarSwitch.isChecked) {
                // set my bar
                val reducedList = RemoteDataCache.getMyBarList()
                adapter = IngredientsSearchAdapter(
                    reducedList
                )
            }
            else {
                // set default adapter
                adapter = IngredientsSearchAdapter(
                    RemoteDataCache.ingredientsList
                )
            }
            layout.ingredients_list.adapter = adapter
        }

        /* filter provided list by user input in search field.
           this is done by setting new adapter with reduced list of selected items */
        layout.filterIngredientsSubstringEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    adapter = IngredientsSearchAdapter(
                        RemoteDataCache.ingredientsList
                    )
                } else {
                    val reducedList =
                        RemoteDataCache.ingredientsList.filter {
                            it.strIngredient1.toUpperCase(Locale.GERMANY)
                                .contains(s.toString().toUpperCase((Locale.GERMANY)))
                        }
                    adapter = IngredientsSearchAdapter(
                        reducedList as MutableList<Ingredient>
                    )
                    ingredients_list.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        /* Handle search and possible returns of null search results.
           Only technical errors are possible: search is executed with ingredients
           provided by API and cocktail IDs also provided by API. Therefore no false user input possible.
         */
        layout.startSearchButton.setOnClickListener {
            if (selectedItems.isEmpty()) {
                Toast.makeText(
                    Cocktailor.applicationContext(),
                    Cocktailor.applicationContext().getString(R.string.no_ingredients_selected),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val connector =
                    if (layout.searchWithAllIngredientsSwitch.isChecked) Connector.AND else Connector.OR
                // get CocktailList and afterwards recipes for search results
                CocktailRequestHandler.getAndCacheCocktailsByIngredients(
                    selectedItems,
                    connector
                ) { it: MutableList<Cocktail>? ->
                    if (it != null && it.size > 0) {
                        RecipeRequestHandler.getAndCacheRecipesForCocktails(it) {
                            if (it != null) {
                                val cocktailFragment =
                                    CocktailFragment()
                                val bundle: Bundle = bundleOf("type" to "search")
                                cocktailFragment.arguments = bundle
                                (activity as MainActivity?)?.makeCurrentFragment(
                                    cocktailFragment
                                )
                            } else {
                                Toast.makeText(
                                    Cocktailor.applicationContext(),
                                    R.string.unknown_error_recipe_search,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else if (it != null && it.size == 0) {
                        Toast.makeText(
                            Cocktailor.applicationContext(),
                            R.string.no_results,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            Cocktailor.applicationContext(),
                            R.string.unknown_error_cocktail_search,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        return layout
    }

}