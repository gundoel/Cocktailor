package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.CocktailSearchResultAdapter
import ch.zhaw.ma20.cocktailor.adapters.IngredientsSearchAdapter
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.fragment_cocktailresults.view.*
import kotlinx.android.synthetic.main.fragment_finder.view.*

class CocktailSearchResultFragment : Fragment() {

    var adapter: CocktailSearchResultAdapter? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_cocktailresults, container, false)
        adapter = CocktailSearchResultAdapter(
            RemoteDataCache!!.lastCocktailSearchResultList)
        layout.cocktail_search_results.adapter = adapter
        return layout
    }
}