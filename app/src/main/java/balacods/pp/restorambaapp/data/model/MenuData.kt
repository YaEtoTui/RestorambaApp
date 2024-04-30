package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class MenuData(
    @SerializedName("dishes_id")
    val dishesId: Int,
    @SerializedName("restaurant_id")
    val restaurantId: Int,
    @SerializedName("dish_name")
    val dishName: String,
    @SerializedName("dish_description")
    val dishDescription: String,
    @SerializedName("dish_price")
    val dishPrice: String,
    @SerializedName("dish_weight")
    val dishWeight: Float,
    @SerializedName("dish_type")
    val dishType: String
)
