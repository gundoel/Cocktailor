package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.MyBarListAdapter
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.fragment_my_bar.view.*


class MyBarFragment : BaseFragment() {
    private val myBarList = RemoteDataCache.getMyBarList()
    private var adapterBarList: MyBarListAdapter? = null
    var adapterArrayList: ArrayAdapter<String>? = null
    var searchList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // convert ingredients list to ArrayList
        for (ingredient in RemoteDataCache.ingredientsList) {
            if (!searchList.contains(ingredient.strIngredient1)) {
                searchList.add(ingredient.strIngredient1)
            }
        }

        val layout = inflater.inflate(R.layout.fragment_my_bar, container, false)

        adapterArrayList = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, searchList)
        layout.listViewMyBarSearch.adapter = adapterArrayList
        layout.listViewMyBarSearch.setOnItemClickListener { _, _, position, _ ->
            val stringElement: String? = adapterArrayList!!.getItem(position)
            val newIngredientListItem = stringElement?.let { Ingredient(it) }
            if (newIngredientListItem != null && !myBarList.contains(newIngredientListItem)) {
                myBarList.add(newIngredientListItem)
            }
            myBarList.sortBy { ingredientListItem -> ingredientListItem.strIngredient1 }
            adapterBarList?.notifyDataSetChanged()
        }

        adapterBarList = MyBarListAdapter(myBarList)
        val listView: ListView = layout.listViewMyBar as ListView
        val emptyText = layout.emptyBar as TextView
        listView.emptyView = emptyText
        layout.listViewMyBar.adapter = adapterBarList
        val searchView: SearchView = layout.searchForBar
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