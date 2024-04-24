package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.databinding.ItemListDishesSearchWhiteGreenBinding
import balacods.pp.restorambaapp.retrofit.domain.dto.MenuData

class DishSearchAdapter : ListAdapter<MenuData, DishSearchAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: DishSearchAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListDishesSearchWhiteGreenBinding.bind(view)

        fun bind(
            menuData: MenuData,
            onButtonClickListener: DishSearchAdapter.OnButtonClickListener
        ) = with(binding) {
            binding.cView.setOnClickListener {
                onButtonClickListener.onClick()
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
            .inflate(R.layout.item_list_dishes_search_white_green, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onButtonClickListener)
    }

    interface OnButtonClickListener {
        fun onClick()
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }
}