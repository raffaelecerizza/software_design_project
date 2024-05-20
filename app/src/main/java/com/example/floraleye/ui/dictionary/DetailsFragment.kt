package com.example.floraleye.ui.dictionary

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.floraleye.MainActivity
import com.example.floraleye.databinding.FragmentDetailsBinding
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.utils.Constants.FLOWER_KEY


/**
 * Fragment per la gestione dei dettagli di un Fiore.
 */
class DetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentDetailsBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(this){
            parentFragmentManager.popBackStackImmediate()
        }

        val arguments = arguments

        if (arguments != null){

            @Suppress("DEPRECATION")
            val flower = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments.getParcelable(FLOWER_KEY, DictionaryFlower::class.java)
            }
            else
            {
                arguments.getParcelable(FLOWER_KEY)
            }

            mBinding.flower = flower

            Glide
                .with(mBinding.imageView.context)
                .load(flower?.imageURL)
                .centerCrop()
                .into(mBinding.imageView)
        }

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        val currentActivity = activity as MainActivity
        currentActivity.supportActionBar?.title = mBinding.flower?.commonName
    }
}
