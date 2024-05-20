package com.example.floraleye.ui.dictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.floraleye.R
import com.example.floraleye.databinding.ItemDictionaryFlowerBinding
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.viewmodels.FavouritesViewModel

/**
 * Adapter per la gestione del dizionario.
 */
class DictionaryFavouritesAdapter(
    private val favouritesViewModel: FavouritesViewModel,
    private val listener: FlowersListener,
) :
    RecyclerView.Adapter<DictionaryFavouritesAdapter.FlowersFavouritesViewHolder>() {

    private lateinit var mBinding: ItemDictionaryFlowerBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowersFavouritesViewHolder {
        mBinding = ItemDictionaryFlowerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return FlowersFavouritesViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: FlowersFavouritesViewHolder, position: Int) {

        val flowerList = favouritesViewModel.getFavourites().value

        if (flowerList != null){
            holder.bindFlower(flowerList[position], listener)
        }
    }

    override fun getItemCount(): Int {

        val currentList = favouritesViewModel.getFavourites().value

        if (currentList != null){
           return currentList.size
        }

        return 0
    }

    /**
     * ViewHolder per l'inizializzazione dei dati del dizionario.
     */
    class FlowersFavouritesViewHolder(mBinding: ItemDictionaryFlowerBinding):
        RecyclerView.ViewHolder(mBinding.root) {

        private val mBinding : ItemDictionaryFlowerBinding = mBinding

        /**
         * Metodo per l'inizializzazione di ogni singolo item Fiore nel dizionario.
         * @param flower Il fiore nel dizionario da inizializzare.
         * @param listener Listener per la gestione del click su un fiore.
         */
        fun bindFlower(flower: DictionaryFlower, listener: FlowersListener) {

            if (flower.isFavourite) {
                Glide
                    .with(mBinding.flowerImage.context)
                    .load(flower.imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.ic_dictionary_image_placeholder_128dp)
                    .into(mBinding.flowerImage)

                mBinding.flower = flower
                mBinding.starDictionary.visibility = View.INVISIBLE

                itemView.setOnClickListener {
                    listener.onClick(flower)
                }
            }
        }
    }
}
