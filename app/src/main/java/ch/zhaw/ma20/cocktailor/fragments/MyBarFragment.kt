package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
import ch.zhaw.ma20.cocktailor.adapters.MyBarListAdapter
import ch.zhaw.ma20.cocktailor.model.IngredientListItem
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.example.cocktailor.R
import kotlinx.android.synthetic.main.fragment_finder.view.*
import kotlinx.android.synthetic.main.fragment_my_bar.view.*

class MyBarFragment : Fragment() {
    var adapter: MyBarListAdapter? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_my_bar, container, false)
        adapter = MyBarListAdapter(RemoteDataCache!!.ingredientsList, requireContext())
        layout.listViewMyBarSearch.adapter = adapter
        return layout
    }
}