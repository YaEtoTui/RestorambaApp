package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class MenuData(
    @SerializedName("id")
    val dishesId: Int,
    @SerializedName("restaurant_id")
    val restaurantId: Int,
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
