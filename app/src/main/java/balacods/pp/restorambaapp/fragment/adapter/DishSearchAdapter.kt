package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.model.MenuAndNameRestaurantData
import balacods.pp.restorambaapp.databinding.ItemListDishesSearchWhiteGreenBinding

class DishSearchAdapter : ListAdapter<MenuAndNameRestaurantData, DishSearchAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: DishSearchAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListDishesSearchWhiteGreenBinding.bind(view)

        fun bind(
            menuData: MenuAndNameRestaurantData,
            onButtonClickListener: DishSearchAdapter.OnButtonClickListener
        ) = with(binding) {

            tvTitleDish.text = menuData.dishName
            tvTitleRestaurant.text = menuData.restaurantName
            tvDesc.text = menuData.dishDescription
            idButtonAgree.text = String.format("%s руб", menuData.dishPrice)

            cView.setOnClickListener {
                onButtonClickListener.onClick(menuData.dishesId, menuData.restaurantId)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<MenuAndNameRestaurantData>() {
        override fun areItemsTheSame(oldItem: MenuAndNameRestaurantData, newItem: MenuAndNameRestaurantData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MenuAndNameRestaurantData, newItem: MenuAndNameRestaurantData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_dishes_search_white_green, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onButtonClickListener)
    }

    interface OnButtonClickListener {
        fun onClick(dishesId: Long, restaurantId: Long)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }
}