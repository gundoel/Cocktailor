package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.adapters.MyBarListAdapter
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache.emptyBarString
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache.myBarList
import kotlinx.android.synthetic.main.fragment_my_bar.view.*

class MyBarFragment : Fragment() {

    var adapterArrayList: ArrayAdapter<String>? = null
    var adapterBarList: MyBarListAdapter? = null
    var searchList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // convert ingredients list to ArrayList
        for (ingredient in RemoteDataCache.ingredientsList) {
            if (ingredient != null) searchList.add(ingredient.strIngredient1)
        }

        var layout = inflater.inflate(R.layout.fragment_my_bar, container, false)

        // search list
        adapterArrayList = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, searchList)
        layout.listViewMyBarSearch.adapter = adapterArrayList
        layout.listViewMyBarSearch.setOnItemClickListener { parent, view, position, id ->
            val stringElement: String? = adapterArrayList!!.getItem(position)
            val newIngredientListItem = stringElement?.let { Ingredient(it) }
            if (stringElement != null && !(checkDuplicateMyBarList(myBarList, stringElement))) {
                if (checkIfBarIsEmpty(myBarList)) newIngredientListItem?.let {
                    myBarList?.set(0,
                        it
                    )
                }
                else newIngredientListItem?.let { myBarList?.add(it) }
            }
            myBarList?.sortBy { ingredientListItem -> ingredientListItem.strIngredient1 }
            adapterBarList?.notifyDataSetChanged()
        }

        // my bar list
        adapterBarList = MyBarListAdapter(myBarList!!, requireContext())
        layout.listViewMyBar.adapter = adapterBarList

        var searchView: SearchView = layout.searchForBar

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (searchList.contains(query)) {
                    adapterArrayList?.filter?.filter(query)
                } else {
                    Toast.makeText(requireContext(), "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapterArrayList?.filter?.filter(newText)
                return false
            }
        })
        return layout
    }

    private fun checkDuplicateMyBarList(
        myBarList: MutableList<Ingredient>?,
        stringElement: String
    ): Boolean {
        var counter = 0
        var isDouble = false
        if (myBarList != null) {
            for (ingredientListItem in myBarList) {
                if (ingredientListItem.strIngredient1 == stringElement) counter++
            }
            if (counter > 0) isDouble = true
        }
        return isDouble
    }

    private fun checkIfBarIsEmpty(myBarList: MutableList<Ingredient>?): Boolean {
        var isBarEmpty = false
        if (myBarList?.get(0)?.strIngredient1 == emptyBarString) isBarEmpty = true
        return isBarEmpty
    }
}