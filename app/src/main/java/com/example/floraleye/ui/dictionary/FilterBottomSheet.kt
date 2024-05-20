package com.example.floraleye.ui.dictionary

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floraleye.databinding.BottomSheetDictionaryFilterBinding
import com.example.floraleye.models.Taxonomy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM_FILTERS = "arg_param_filters"

/**
 * Classe utilizzata per gestire il pop up per l'acquisizione dei filtri.
 */
class FilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetDictionaryFilterBinding

    private lateinit var currentFilters: Taxonomy

    companion object {

        private val TAG: String = FilterBottomSheet::class.java.simpleName

        /**
         * Metodo utilizzabile per ottenere una nuova istanza di FilterBottomSheet.
         * @param currentFilters filtri correnti
         * @return istanza di FilterBottomSheet settata con i filtri correnti
         */
        @JvmStatic
        fun newInstance(currentFilters: Taxonomy) =
            FilterBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM_FILTERS, currentFilters)
                }
            }
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let { arg ->
            currentFilters = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                 arg?.getParcelable(ARG_PARAM_FILTERS, Taxonomy::class.java)
                     ?: Taxonomy.getEmptyTaxonomy()
            } else {
                arg?.getParcelable(ARG_PARAM_FILTERS) ?: Taxonomy.getEmptyTaxonomy()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            BottomSheetDictionaryFilterBinding.inflate(inflater, container, false)

        val dialog = dialog
        if (dialog is BottomSheetDialog) {
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else Log.d(TAG, "onCreateView: dialog is not BottomSheetDialog.")

        mBinding.filters = currentFilters

        mBinding.applyFiltersButton.setOnClickListener {
            setNewFiltersAndDismiss(getDisplayedFilters())
        }

        mBinding.resetFiltersButton.setOnClickListener {
            setNewFiltersAndDismiss(Taxonomy.getEmptyTaxonomy())
        }

        return mBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        arguments?.putParcelable(ARG_PARAM_FILTERS, getDisplayedFilters())
    }

    private fun setNewFiltersAndDismiss(newFilters: Taxonomy) {
        dismiss()
        try{
            val parentFragment = requireParentFragment()
            if (parentFragment is DictionaryFragment) {
                parentFragment.onFilterBottomSheetDismissed(newFilters)
            } else Log.d(TAG, "setNewFiltersAndDismiss: " +
                    "parent fragment is not DictionaryFragment.")
        } catch(exception: IllegalStateException) {
            Log.d(TAG, "setNewFiltersAndDismiss: " +
                    "impossible to take parent fragment with exception ${exception.message}.")
        }
    }

    private fun getDisplayedFilters(): Taxonomy {
        return Taxonomy(
            kingdom = mBinding.kingdomSpinner.selectedItem.toString(),
            phylum = mBinding.phylumSpinner.selectedItem.toString(),
            genus = mBinding.genusEditText.text.toString(),
            family = mBinding.familyEditText.text.toString(),
            species = mBinding.speciesEditText.text.toString(),
            order = ""
        )
    }
}
