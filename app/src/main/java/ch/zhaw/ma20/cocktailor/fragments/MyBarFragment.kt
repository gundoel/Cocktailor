package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.MyBarListAdapter
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.fragment_my_bar.*
import kotlinx.android.synthetic.main.fragment_my_bar.view.*


class MyBarFragment : Fragment() {
    val myBarList = RemoteDataCache.myBarList
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
        adapterArrayList =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, searchList)
        layout.listViewMyBarSearch.adapter = adapterArrayList
        layout.listViewMyBarSearch.setOnItemClickListener { parent, view, position, id ->
            val stringElement: String? = adapterArrayList!!.getItem(position)
            val newIngredientListItem = stringElement?.let { Ingredient(it) }
            if (newIngredientListItem != null && !myBarList.contains(newIngredientListItem)) {
                myBarList?.add(newIngredientListItem)
            }
            myBarList?.sortBy { ingredientListItem -> ingredientListItem.strIngredient1 }
            adapterBarList?.notifyDataSetChanged()
        }

        adapterBarList = MyBarListAdapter(myBarList!!, Cocktailor.applicationContext())
        // display empty Text if bar is empty. if not, use adapter to display items.
        val listView: ListView = layout.listViewMyBar as ListView
        val emptyText = layout.emptyBar as TextView
        listView.emptyView = emptyText
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
}