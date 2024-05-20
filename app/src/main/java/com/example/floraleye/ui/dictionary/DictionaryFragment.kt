package com.example.floraleye.ui.dictionary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.OnAttachStateChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentDictionaryBinding
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.models.Taxonomy
import com.example.floraleye.utils.Constants.FLOWER_KEY
import com.example.floraleye.utils.DictionaryManager
import com.example.floraleye.utils.GeneralUtils
import com.example.floraleye.viewmodels.DictionaryViewModel
import com.example.floraleye.viewmodels.FavouritesViewModel


private const val FILTER_BOTTOM_SHEET_TAG = "FilterBottomSheetTag"

/**
 * Fragment per la gestione del Dizionario.
 */
class DictionaryFragment : Fragment(), FlowersListener {

    private lateinit var mBinding: FragmentDictionaryBinding

    private val dictionaryViewModel: DictionaryViewModel by viewModels({ requireParentFragment() })

    private val favouritesViewModel: FavouritesViewModel by viewModels({ requireParentFragment() })

    private lateinit var manager: DictionaryManager

    private var adapter: DictionaryAdapter? = null

    private var mSearchView: SearchView? = null

    private var mSearchString: String? = null

    private var mAlertDialog: AlertDialog? = null

    companion object {

        private val TAG: String = DictionaryFragment::class.java.simpleName

        private const val SEARCH_KEY = "search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState != null) {
            mSearchString = savedInstanceState.getString(SEARCH_KEY, null)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mSearchString = mSearchView?.query.toString()
        outState.putString(SEARCH_KEY, mSearchString)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDictionaryBinding.inflate(inflater, container, false)

        manager = DictionaryManager(
            dictionaryViewModel = dictionaryViewModel,
            favouritesViewModel = favouritesViewModel,
            fragment = this,
            mBinding = mBinding,
            context = context
        )

        manager.initDictionary()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dictionaryViewModel.bIsFlowerListInitialized.observe(this){
            setMenuVisibility(mBinding.isDictionaryLoaded == true)

            if (mBinding.isDictionaryLoaded == true){
                mBinding.rvFlowerList.adapter = DictionaryAdapter(
                    dictionaryViewModel,
                    this@DictionaryFragment
                )

                adapter = mBinding.rvFlowerList.adapter as DictionaryAdapter

                mBinding.dictionaryEmptyTextView.visibility = if ((adapter?.itemCount ?: 0) == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        mBinding.swipeRefreshDictionary.setOnRefreshListener {
            mAlertDialog = GeneralUtils.showGenericMessageInDialog(
                context = requireContext(),
                title = R.string.title_dictionary,
                message = R.string.str_refresh_message,
                onNegativeButtonClicked = {
                    mBinding.swipeRefreshDictionary.isRefreshing = false
                },
                onPositiveButtonClicked = {
                    adapter?.cleanAndNotify()
                    dictionaryViewModel.clean(requireContext())
                    manager.initDictionary()
                    mBinding.swipeRefreshDictionary.isRefreshing = false
                }
            )

            if (dictionaryViewModel.isLoadingDictionary() ||
                dictionaryViewModel.bIsFlowerListInitialized.value == true
            ){
                mBinding.swipeRefreshDictionary.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_dictionary, menu)

        val searchMenuItem = menu.findItem(R.id.dictionarySearch)

        mSearchView = searchMenuItem.actionView as? SearchView

        mSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return if (adapter != null) {
                    adapter?.filter(newText)
                    false
                } else {
                    Log.d(TAG, "onQueryTextChange: adapter is null.")
                    false
                }
            }
        })

        mSearchView?.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(p0: View) {
                mSearchString = ""
            }

            override fun onViewAttachedToWindow(p0: View) {
                //non implementato
            }
        })

        if (mSearchString != null &&
            mSearchString?.isNotEmpty() == true &&
            mSearchString != "null") {
            searchMenuItem.expandActionView()
            mSearchView?.setQuery(mSearchString, false)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_filter_dictionary -> {
                FilterBottomSheet.newInstance(dictionaryViewModel.currentFilters)
                    .show(childFragmentManager, FILTER_BOTTOM_SHEET_TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setMenuVisibility(mBinding.isDictionaryLoaded == true)
        val currentActivity = activity as MainActivity
        currentActivity.supportActionBar?.setTitle(R.string.title_dictionary)
    }

    override fun onClick(flower: DictionaryFlower) {

        val detailsFragment = DetailsFragment()
        val args = Bundle()
        args.putParcelable(FLOWER_KEY, flower)
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

    override fun onDestroy() {
        super.onDestroy()
        if (mAlertDialog != null &&
            mAlertDialog?.isShowing == true
        ){
            mAlertDialog?.dismiss()
        }
    }

    override fun onClickFav(flower: DictionaryFlower) {

        flower.isFavourite = !flower.isFavourite

        if (flower.isFavourite){
            favouritesViewModel.addFlowerToFavourites(flower)
            dictionaryViewModel.updateFavourites(flower)
        }
        else {
            favouritesViewModel.removeFlowerToFavourites(flower)
            dictionaryViewModel.updateFavourites(flower)
        }
    }

    /**
     * Metodo invocato automaticamente quando viene dismisso il pop up per il settaggio dei filtri.
     * Questo metodo si occupa dell'ottenimento dei nuovi filtri e della loro applicazione.
     */
    fun onFilterBottomSheetDismissed(newFilters: Taxonomy) {
        dictionaryViewModel.setNewFilters(newFilters)
        adapter?.notifyDataSetChanged() ?: Log.d(TAG, "onFilterBottomSheetDismissed: " +
                "adapter is null.")
        mBinding.dictionaryEmptyTextView.visibility = if ((adapter?.itemCount ?: 0) == 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
