package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.databinding.ItemListDishesBinding

class DishAdapter : ListAdapter<MenuData, DishAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: DishAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListDishesBinding.bind(view)

        fun bind(
            menuData: MenuData,
            onButtonClickListener: DishAdapter.OnButtonClickListener
        ) = with(binding) {
            tvTitleRestaurant.text = menuData.dishName
            tvDesc.text = menuData.dishDescription
            idButtonAgree.text = String.format("%s руб", menuData.dishPrice)

            binding.cView.setOnClickListener {
                onButtonClickListener.onClick(menuData.dishesId, menuData.restaurantId)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<MenuData>() {
        override fun areItemsTheSame(oldItem: MenuData, newItem: MenuData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MenuData, newItem: MenuData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_dishes, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onButtonClickListener)
    }

    interface OnButtonClickListener {
        fun onClick(dishId: Long, restaurantId: Long)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }
}