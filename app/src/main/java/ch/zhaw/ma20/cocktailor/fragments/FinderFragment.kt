package ch.zhaw.ma20.cocktailor.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.*
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
import ch.zhaw.ma20.cocktailor.appconst.Connector
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.fragment_finder.*
import kotlinx.android.synthetic.main.fragment_finder.view.*


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
        layout.startSearchButton.setOnClickListener(View.OnClickListener {
            var connector =
                if (layout.searchWithAllIngredientsSwitch.isChecked) Connector.AND else Connector.OR
            // get CocktailList and afterwards recipes for search results
            CocktailSearchHandler.getCocktailsByIngredients(selectedItems, connector) {
                if (it != null) {
                    // TODO implement properly
                    //ThumbHandler.storeMultipleThumbs(it)
                    RecipeSearchHandler.getRecipesForCocktails(it) {
                        if (it != null) {
                            var cocktailSearchResultFragment =
                                CocktailSearchResultFragment()
                            (activity as MainActivity?)?.makeCurrentFragment(
                                cocktailSearchResultFragment
                            )
                        } else {
                            Toast.makeText(
                                Cocktailor.applicationContext(),
                                R.string.unknownErrorRecipeSearch,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        Cocktailor.applicationContext(),
                        R.string.unknownErrorCocktailSearch,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
        // handle selection of ingredients (https://stackoverflow.com/questions/2367936/listview-onitemclicklistener-not-responding)
        layout.ingredients_list.itemsCanFocus = true;
        return layout
    }

}