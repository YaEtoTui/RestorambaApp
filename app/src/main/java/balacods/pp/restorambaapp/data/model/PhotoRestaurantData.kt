package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class PhotoRestaurantData(
    val id: Long,
    @SerializedName("restaurant_id")
    val restaurantId: Long,
    @SerializedName("link_1")
    val link1: String,
    @SerializedName("link_2")
    val link2: String,
    @SerializedName("link_3")
    val link3: String
)
