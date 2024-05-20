package com.example.floraleye.ui.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.floraleye.R
import com.example.floraleye.databinding.ItemDictionaryFlowerBinding
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.utils.RegexManager
import com.example.floraleye.viewmodels.DictionaryViewModel

/**
 * Adapter per la gestione del dizionario.
 */
class DictionaryAdapter(
    private val viewModel: DictionaryViewModel,
    private val listener: FlowersListener
) :
    RecyclerView.Adapter<DictionaryAdapter.FlowersViewHolder>() {

    private lateinit var mBinding: ItemDictionaryFlowerBinding
    private var list: MutableList<DictionaryFlower>

    init {
        list = viewModel.getDictionaryList().value as MutableList<DictionaryFlower>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowersViewHolder {
        mBinding = ItemDictionaryFlowerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return FlowersViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: FlowersViewHolder, position: Int) {
        var flower = list[position]

        if (!viewModel.currentFilters.isEmpty()) {
            flower = (list.filter {
                it.areFilterMatched(viewModel.currentFilters)
            } as MutableList<DictionaryFlower>)[position]
        }

        if (flower != null){
            holder.bindFlower(flower, listener)
        }
    }

    override fun getItemCount(): Int {
        if (!viewModel.currentFilters.isEmpty()) {
            return list.count {
                it.areFilterMatched(viewModel.currentFilters)
            }
        }

        return list.size
    }

    /**
     * Metodo per filtrare la lista del dizionario.
     */
    fun filter(text: String) {

        val filteredList: ArrayList<DictionaryFlower> = ArrayList()
        val items = viewModel.getDictionaryList().value

        if (text.isEmpty()){
            list = items as MutableList<DictionaryFlower>
            this.notifyDataSetChanged()
            return
        }

        if (!RegexManager.isDictionarySearchValid(text)){
            list = filteredList
            this.notifyDataSetChanged()
            return
        }

        if (items != null) {
            for (item in items) {
                if (item.commonName.lowercase().contains(text.lowercase())) {
                    filteredList.add(item)
                }
            }
        }

        if (filteredList.isNotEmpty()){
            list = filteredList
            notifyDataSetChanged()
        }
        else
        {
            list = viewModel.getDictionaryList().value as MutableList<DictionaryFlower>
            notifyDataSetChanged()
        }
    }

    /**
     * Metodo per effettuare il clean del Dizionario e notificarlo alla recyclerView.
     */
    fun cleanAndNotify() {
        if (!viewModel.isLoadingDictionary()) {
            this.notifyDataSetChanged()
        }
    }

    /**
     * ViewHolder per l'inizializzazione dei dati del dizionario.
     */
    class FlowersViewHolder(
        private val mBinding: ItemDictionaryFlowerBinding
        ): RecyclerView.ViewHolder(mBinding.root) {

        /**
         * Metodo per l'inizializzazione di ogni singolo item Fiore nel dizionario.
         * @param flower Il fiore nel dizionario da inizializzare.
         * @param listener Listener per la gestione del click su un fiore.
         */
        fun bindFlower(flower: DictionaryFlower, listener: FlowersListener) {

            Glide
                .with(mBinding.flowerImage.context)
                .load(flower.imageURL)
                .centerCrop()
                .placeholder(R.drawable.ic_dictionary_image_placeholder_128dp)
                .into(mBinding.flowerImage)

            mBinding.flower = flower

            itemView.setOnClickListener {
                listener.onClick(flower)
            }

            mBinding.starDictionary.setOnClickListener {
                listener.onClickFav(flower)
            }
        }
    }
}

/**
 * Interfaccia per la gestione dei dettagli del Dizionario.
 */
interface FlowersListener {

    /**
     * Funzione per il click su in fiore.
     */
    fun onClick(flower: DictionaryFlower)

    /**
     * Funzione per la gestione dei preferiti.
     */
    fun onClickFav(flower: DictionaryFlower)
}
