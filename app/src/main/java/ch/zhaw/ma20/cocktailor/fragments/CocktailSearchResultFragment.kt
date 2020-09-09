package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.MainActivity
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.CocktailSearchResultAdapter
import ch.zhaw.ma20.cocktailor.appconst.SortingOptions
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import kotlinx.android.synthetic.main.fragment_cocktails.view.*

class CocktailSearchResultFragment : Fragment() {
    val cockTailList = RemoteDataCache!!.lastCocktailSearchResultList
    val adapter = CocktailSearchResultAdapter(cockTailList)
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
        var layout = inflater.inflate(R.layout.fragment_cocktails, container, false)
        val types = arrayOf(
            SortingOptions.SORT_BY_NAME,
            SortingOptions.SORT_BY_MISSING_INGREDIENTS,
            SortingOptions.SORT_BY_AVAILABLE_INGREDIENTS
        )
        val spinner = layout.sortlist_spinner
        spinner?.adapter = ArrayAdapter(
            Cocktailor.applicationContext(),
            R.layout.support_simple_spinner_dropdown_item,
            types
        ) as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                cockTailList.sortBy{ it.strDrink }
                adapter!!.notifyDataSetChanged()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    SortingOptions.SORT_BY_NAME -> cockTailList.sortBy { it.strDrink }
                    SortingOptions.SORT_BY_MISSING_INGREDIENTS -> cockTailList.sortBy { it.missingIngredients }
                    SortingOptions.SORT_BY_AVAILABLE_INGREDIENTS -> cockTailList.sortByDescending { it.availableIngredients }
                }
                adapter!!.notifyDataSetChanged()
            }

        }
        layout.cocktails.setOnItemClickListener { parent, view, position, id ->
            val element = adapter!!.getItem(position)
            var recipeFragment = RecipeFragment(element.idDrink)
            (activity as MainActivity?)?.makeCurrentFragment(
                recipeFragment
            )
        }
        layout.cocktails.adapter = adapter
        adapter!!.notifyDataSetChanged()
        return layout
    }
}