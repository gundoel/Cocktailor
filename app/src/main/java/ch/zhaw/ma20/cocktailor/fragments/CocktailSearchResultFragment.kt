package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ch.zhaw.ma20.cocktailor.MainActivity
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.CocktailSearchResultAdapter
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.fragment_cocktailresults.view.*

class CocktailSearchResultFragment : Fragment() {

    var adapter: CocktailSearchResultAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val manager: FragmentManager = activity!!.supportFragmentManager
                if (manager.backStackEntryCount > 0) {
                    manager.popBackStackImmediate()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_cocktailresults, container, false)
        adapter = CocktailSearchResultAdapter(
            RemoteDataCache!!.lastCocktailSearchResultList
        )
        layout.cocktail_search_results.setOnItemClickListener { parent, view, position, id ->
            val element = adapter!!.getItem(position)
            var recipeFragment = RecipeFragment(element.idDrink)
            (activity as MainActivity?)?.makeCurrentFragment(
                recipeFragment
            )
        }
        layout.cocktail_search_results.adapter = adapter
        return layout
    }
}