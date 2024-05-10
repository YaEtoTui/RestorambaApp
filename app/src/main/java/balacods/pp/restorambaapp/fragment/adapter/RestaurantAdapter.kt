package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.enum.StatusRequest
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.databinding.ItemListRestaurantsBinding

class RestaurantAdapter : ListAdapter<RestaurantData, RestaurantAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: RestaurantAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListRestaurantsBinding.bind(view)

        fun bind(
            restaurantData: RestaurantData,
            onButtonClickListener: RestaurantAdapter.OnButtonClickListener
        ) = with(binding) {

            binding.tvTitleRestaurant.text = restaurantData.restaurantName

            binding.cView.setOnClickListener {
                onButtonClickListener.onClick(StatusRequest.LIST_RESTAURANTS.statusRequest)
            }

            binding.idBtRandom.setOnClickListener {
                onButtonClickListener.onClick(StatusRequest.DISH.statusRequest)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<RestaurantData>() {
        override fun areItemsTheSame(oldItem: RestaurantData, newItem: RestaurantData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RestaurantData, newItem: RestaurantData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_restaurants, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onButtonClickListener)
    }

    interface OnButtonClickListener {
        fun onClick(text: String)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }
}