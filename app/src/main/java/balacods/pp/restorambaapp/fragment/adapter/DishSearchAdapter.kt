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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class DishSearchAdapter :
    ListAdapter<MenuAndNameRestaurantData, DishSearchAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: DishSearchAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListDishesSearchWhiteGreenBinding.bind(view)

        fun bind(
            menuData: MenuAndNameRestaurantData,
            onButtonClickListener: DishSearchAdapter.OnButtonClickListener
        ) = with(binding) {

            if (menuData.dishAndPhotoData.photo != null) {
                imPhotoIcon.visibility = View.GONE
                Glide.with(itemView.context)
                    .load(menuData.dishAndPhotoData.photo.link1)
                    .centerInside()
                    .transform(RoundedCorners(20))
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imPhoto)
            }

            if (menuData.dishAndPhotoData.dish.dishName.length < 30) {
                tvTitleDish.text = menuData.dishAndPhotoData.dish.dishName
            } else {
                tvTitleDish.text = String.format(
                    "%s...",
                    menuData.dishAndPhotoData.dish.dishName.substring(0, 30)
                )
            }

            tvTitleRestaurant.text = menuData.nameRestaurant

            if (menuData.dishAndPhotoData.dish.dishDescription.length < 100) {
                tvDesc.text = menuData.dishAndPhotoData.dish.dishDescription
            } else {
                tvDesc.text = String.format(
                    "%s...",
                    menuData.dishAndPhotoData.dish.dishDescription.substring(0, 100)
                )
            }

            idButtonAgree.text = String.format("%s руб", menuData.dishAndPhotoData.dish.dishPrice)

            cView.setOnClickListener {
                onButtonClickListener.onClick(
                    menuData.dishAndPhotoData.dish.dishesId,
                    menuData.dishAndPhotoData.dish.restaurantId
                )
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<MenuAndNameRestaurantData>() {
        override fun areItemsTheSame(
            oldItem: MenuAndNameRestaurantData,
            newItem: MenuAndNameRestaurantData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MenuAndNameRestaurantData,
            newItem: MenuAndNameRestaurantData
        ): Boolean {
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