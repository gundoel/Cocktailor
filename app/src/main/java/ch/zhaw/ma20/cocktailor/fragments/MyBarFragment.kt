package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.adapters.MyBarListAdapter
import ch.zhaw.ma20.cocktailor.model.IngredientListItem
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.example.cocktailor.R
import kotlinx.android.synthetic.main.fragment_my_bar.*
import kotlinx.android.synthetic.main.fragment_my_bar.view.*


class MyBarFragment  : Fragment() {
    var adapterArrayList: ArrayAdapter<String>? = null
    var adapterBarList : MyBarListAdapter? = null
    var myBarList : MutableList<IngredientListItem>? = null
    var list = arrayListOf("Apple", "Banana")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myBarList = RemoteDataCache.ingredientsList

        var layout = inflater.inflate(R.layout.fragment_my_bar, container, false)

        adapterArrayList = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list!!)
        layout.listViewMyBarSearch.adapter = adapterArrayList

        adapterBarList = MyBarListAdapter(myBarList!!, requireContext())
        layout.listViewMyBar.adapter = adapterBarList

        var searchView : SearchView = layout.searchForBar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
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