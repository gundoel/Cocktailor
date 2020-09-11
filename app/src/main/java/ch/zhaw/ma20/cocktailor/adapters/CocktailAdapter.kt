package ch.zhaw.ma20.cocktailor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.model.Cocktail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cocktail_selectable_item.view.*
import kotlinx.android.synthetic.main.fragment_recipe.view.*

class CocktailAdapter(
    var cocktails: MutableList<Cocktail>

) : BaseAdapter() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(Cocktailor.applicationContext())

    override fun getCount(): Int {
        return cocktails.size
    }

    override fun getItem(index: Int): Cocktail {
        return cocktails.get(index)
    }

    override fun getItemId(index: Int): Long {
        return 0
    }

    override fun getView(
        index: Int, oldView: View?,
        viewGroup: ViewGroup?
    ): View {
        var view: View
        //check if we get a view to recycle
        if (oldView == null) {
            view = layoutInflater.inflate(R.layout.cocktail_selectable_item, null)
        } else {
            view = oldView
        }
        val entry = getItem(index)
        var imageView : ImageView = view.cocktailThumb
        Picasso.get().load(entry.strDrinkThumb).into(imageView);
        view.cocktailName.text = entry.strDrink
        view.missingIngredients.text = entry.missingIngredients.toString()
        view.availableIngredients.text = entry.availableIngredients.toString()
        return view
    }
}