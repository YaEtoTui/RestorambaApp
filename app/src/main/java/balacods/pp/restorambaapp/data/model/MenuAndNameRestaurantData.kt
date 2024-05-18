package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class MenuAndNameRestaurantData(
    @SerializedName("id")
    val dishesId: Long,
    @SerializedName("restaurant_id")
    val restaurantId: Long,
    @SerializedName("name")
    val restaurantName: String,
    @SerializedName("name")
    val dishName: String,
    @SerializedName("description")
    val dishDescription: String,
    @SerializedName("price")
    val dishPrice: String,
    @SerializedName("weight")
    val dishWeight: Float,
    @SerializedName("type")
    val dishType: String
)
