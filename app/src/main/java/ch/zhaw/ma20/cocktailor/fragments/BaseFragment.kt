package ch.zhaw.ma20.cocktailor.fragments

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

open class BaseFragment : Fragment() {

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
}