package com.example.floraleye.ui.dictionary

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter per la gestione del TabLayout contenente il dizionario e i preferiti.
 */
class DictionaryTabAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        lateinit var fragment : Fragment

        when (position) {
            0 -> fragment = DictionaryFragment()
            1 -> fragment = DictionaryFavourites()
        }

        return fragment
    }
}
