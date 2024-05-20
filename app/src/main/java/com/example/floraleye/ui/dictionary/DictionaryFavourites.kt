package com.example.floraleye.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentFavouritesBinding
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.utils.Constants
import com.example.floraleye.viewmodels.FavouritesViewModel

/**
 * Fragment per la gestione dei fiori preferiti.
 */
class DictionaryFavourites : Fragment(), FlowersListener {

    private lateinit var mBinding: FragmentFavouritesBinding
    private val favouritesViewModel: FavouritesViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentFavouritesBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvFavouriteFlowers.adapter = DictionaryFavouritesAdapter(
            favouritesViewModel,
            this@DictionaryFavourites
        )

        favouritesViewModel.getFavourites().observe(this){
            mBinding.rvFavouriteFlowers.adapter?.notifyDataSetChanged()

            if (it.size == 0){
                mBinding.dictionaryEmptyTextView.visibility = View.VISIBLE
            }
            else {
                mBinding.dictionaryEmptyTextView.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentActivity = activity as MainActivity
        currentActivity.supportActionBar?.setTitle(R.string.title_dictionary)
    }

    override fun onClickFav(flower: DictionaryFlower) {
        //non implementato
    }

    override fun onClick(flower: DictionaryFlower) {

        val detailsFragment = DetailsFragment()
        val args = Bundle()
        args.putParcelable(Constants.FLOWER_KEY, flower)
        detailsFragment.arguments = args

        val activity = activity as MainActivity
        val currentFragment = activity.getCurrentFragment()
        val parentFragmentManager = currentFragment?.parentFragmentManager

        parentFragmentManager?.commit {
            this.setCustomAnimations(
                androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom,
                androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom,
                androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom,
                androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom,
            )

            this.replace(parentFragmentManager.fragments[0].id, detailsFragment)
            this.setReorderingAllowed(true)
            this.addToBackStack(null)
        }
    }
}
