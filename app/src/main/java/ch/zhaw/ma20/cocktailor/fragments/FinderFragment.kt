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
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
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
        val selectedItems = RemoteDataCache?.selectedItems
        adapter = IngredientsSearchAdapter(
            RemoteDataCache!!.ingredientsList,
            Cocktailor.applicationContext()
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
                    reducedList as MutableList<IngredientListItem>,
                    Cocktailor.applicationContext()
                )

            } else {
                // set default adapter
                adapter = IngredientsSearchAdapter(
                    RemoteDataCache!!.ingredientsList,
                    requireContext()
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
                        RemoteDataCache!!.ingredientsList,
                        requireContext()
                    )
                } else {
                    val reducedList =
                        RemoteDataCache!!.ingredientsList.filter {
                            it.strIngredient1.toUpperCase().contains(s.toString().toUpperCase())
                        }
                    adapter = IngredientsSearchAdapter(
                        reducedList as MutableList<IngredientListItem>,
                        requireContext()
                    )
                    ingredients_list.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }

            }

            override fun afterTextChanged(s: Editable) {}
        })

        layout.startSearchButton.setOnClickListener(View.OnClickListener {
            var connector =
                if (layout.searchWithAllIngredientsSwitch.isChecked) Connector.AND else Connector.OR
            val service = ServiceVolley()
            val apiController = APIController(service)
            val resultCart = CocktailSearchResultCart(AtomicInteger(selectedItems.size), false)
            for ((index, item) in selectedItems.withIndex()) {
                val ingredient = item
                val path = "filter.php?i=$ingredient"
                val result = mutableMapOf<String, Cocktail>()

                apiController.get(path) { response ->
                    val cocktails = response?.let {
                        Klaxon().parse<CocktailSearchResult>(it)
                    }
                    if (cocktails != null) {
                        resultCart.addCocktailMap(
                            cocktails.drinks.map { it.idDrink to it }.toMap().toMutableMap()
                        )
                    }
                    // all Requests done
                    //TODO Error handling if request fails
                    //TODO display results in new fragment
                    if (resultCart.pendingRequests.decrementAndGet() == 0) {
                        if (connector == Connector.OR) {
                            for (item in resultCart.getCocktailsOR()) {
                                Log.i(
                                    "COCKTAILRESULT",
                                    item.idDrink + " " + item.strDrink + " " + item.strDrinkThumb
                                )
                            }
                        }
                        // AND
                        else {
                            for (item in resultCart.getCocktailsAND()) {
                                Log.i(
                                    "COCKTAILRESULT",
                                    item.idDrink + " " + item.strDrink + " " + item.strDrinkThumb
                                )
                            }
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