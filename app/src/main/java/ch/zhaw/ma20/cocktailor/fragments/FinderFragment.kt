package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.example.cocktailor.R
import kotlinx.android.synthetic.main.fragment_finder.view.*

class FinderFragment : Fragment() {
    var adapter: IngredientsSearchAdapter? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_finder, container, false)
        adapter = IngredientsSearchAdapter(RemoteDataCache!!.ingredientsList, requireContext())
        layout.ingredients_list.adapter = adapter
        return layout
    }
}