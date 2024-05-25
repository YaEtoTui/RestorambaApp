package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.model.DishAndPhotoData
import balacods.pp.restorambaapp.databinding.ItemListDishesBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class DishAdapter : ListAdapter<DishAndPhotoData, DishAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: DishAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListDishesBinding.bind(view)

        fun bind(
            menuData: DishAndPhotoData,
            onButtonClickListener: DishAdapter.OnButtonClickListener
        ) = with(binding) {

            if (menuData.photo != null) {
                imPhotoIcon.visibility = View.GONE
                Glide.with(itemView.context)
                    .load(menuData.photo.link1)
                    .centerInside()
                    .transform(RoundedCorners(20))
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imPhoto)
            }

            tvTitleRestaurant.text = menuData.dish.dishName
            tvDesc.text = menuData.dish.dishDescription
            idButtonAgree.text = String.format("%s руб", menuData.dish.dishPrice)

            binding.cView.setOnClickListener {
                onButtonClickListener.onClick(menuData.dish.dishesId, menuData.dish.restaurantId)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<DishAndPhotoData>() {
        override fun areItemsTheSame(
            oldItem: DishAndPhotoData,
            newItem: DishAndPhotoData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DishAndPhotoData,
            newItem: DishAndPhotoData
        ): Boolean {
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