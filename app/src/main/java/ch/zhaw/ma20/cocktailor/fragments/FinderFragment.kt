package ch.zhaw.ma20.cocktailor.fragments

import APIController
import ServiceVolley
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.MainActivity
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
import ch.zhaw.ma20.cocktailor.appconst.Connector
import ch.zhaw.ma20.cocktailor.model.*
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.fragment_finder.*
import kotlinx.android.synthetic.main.fragment_finder.view.*
import java.util.concurrent.atomic.AtomicInteger


class FinderFragment : Fragment() {
    var adapter: IngredientsSearchAdapter? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_finder, container, false)
        val selectedItems = RemoteDataCache?.selectedItemsSet
        adapter = IngredientsSearchAdapter(
            RemoteDataCache!!.ingredientsList
        )
        layout.ingredients_list.adapter = adapter

        // filter list items by to selected items only
        layout.filterIngredientsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val reducedList =
                    RemoteDataCache!!.ingredientsList.filter {
                        selectedItems.contains(it.strIngredient1.toString())
                    }
                adapter = IngredientsSearchAdapter(
                    reducedList as MutableList<Ingredient>
                )

            } else {
                // set default adapter
                adapter = IngredientsSearchAdapter(
                    RemoteDataCache!!.ingredientsList
                )
            }
            layout.ingredients_list.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }

        // filter list items by user input
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
                        RemoteDataCache!!.ingredientsList
                    )
                } else {
                    val reducedList =
                        RemoteDataCache!!.ingredientsList.filter {
                            it.strIngredient1.toUpperCase().contains(s.toString().toUpperCase())
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

        // handle search
        // TODO spaghetti spaghetti
        layout.startSearchButton.setOnClickListener(View.OnClickListener {
            var connector =
                if (layout.searchWithAllIngredientsSwitch.isChecked) Connector.AND else Connector.OR
            val service = ServiceVolley()
            val apiController = APIController(service)
            val cocktailResultCart =
                CocktailSearchResultCart(AtomicInteger(selectedItems.size), false)
            for (item in selectedItems) {
                val ingredient = item
                val path = "filter.php?i=$ingredient"
                val result = mutableMapOf<String, Cocktail>()

                apiController.get(path) { response ->
                    val cocktails = response?.let {
                        Klaxon().parse<CocktailSearchResult>(it)
                    }
                    if (cocktails != null) {
                        cocktailResultCart.addRequestResult(
                            cocktails.drinks.map { it.idDrink to it }.toMap().toMutableMap()
                        )
                    }
                    // all cocktail requests done
                    //TODO Error handling if request fails
                    if (cocktailResultCart.pendingRequests.decrementAndGet() == 0) {
                        var cocktailList = mutableListOf<Cocktail>()
                        if (connector == Connector.OR) {
                            cocktailList.addAll(cocktailResultCart.getCocktailsOR())
                        }
                        // AND
                        else {
                            cocktailList.addAll(cocktailResultCart.getCocktailsAND())
                        }
                        // cache cocktails
                        RemoteDataCache.addLastCocktailSearchResult(cocktailList)
                        if (cocktailList.isEmpty()) {
                            Toast.makeText(
                                Cocktailor.applicationContext(),
                                R.string.no_results,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        // get recipes for results in order to get missing ingredients
                        val recipeResultCart =
                            RecipeSearchResultCart(AtomicInteger(cocktailList.size), false)
                        for (item in cocktailList) {
                            val cocktailId = item.idDrink
                            val recipePath = "lookup.php?i=$cocktailId"
                            // get recipes
                            apiController.get(recipePath) { response ->
                                val recipes = response?.let {
                                    Klaxon().parse<RecipeSearchResult>(it)
                                }
                                if (recipes != null) {
                                    // recipe is always only 1 (unique id)
                                    val recipe = recipes?.drinks?.get(0)
                                    recipeResultCart.addQueryResult(recipe)
                                    // got all recipes -> get missing ingredients and cache recipe
                                    var ingredientsList = (recipe?.getIngredientsList()
                                        ?.map { it.ingredient })!!.toMutableList()
                                    item.setIngredientNumbers(ingredientsList)
                                }
                                if (recipeResultCart.pendingRequests.decrementAndGet() == 0) {
                                    var cocktailSearchResultFragment =
                                        CocktailSearchResultFragment()
                                    (activity as MainActivity?)?.makeCurrentFragment(
                                        cocktailSearchResultFragment
                                    )
                                    RemoteDataCache.addLastRecipeSearchResult(recipeResultCart.allRequestResults)
                                }
                            }
                            Log.i(
                                "COCKTAILRESULT",
                                item.idDrink + " " + item.strDrink + " " + item.strDrinkThumb
                            )
                        }
                    }
                }
            }
        })
        // handle selection of ingredients (https://stackoverflow.com/questions/2367936/listview-onitemclicklistener-not-responding)
        layout.ingredients_list.itemsCanFocus = true;
        return layout
    }

}