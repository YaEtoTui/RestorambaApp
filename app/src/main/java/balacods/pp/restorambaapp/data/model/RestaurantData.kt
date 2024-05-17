package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantData(
    @SerializedName("id")
    val customerId: Long,
    @SerializedName("name")
    val restaurantName: String,
    @SerializedName("location")
    val restaurantLocation: String,
    @SerializedName("coordinate_X")
    val restaurantCoordinateX: Float,
    @SerializedName("coordinate_Y")
    val restaurantCoordinateY: Float,
    @SerializedName("description")
    val restaurantDescription: String,
    @SerializedName("contact_phone")
    val restaurantContactPhone: String,
    @SerializedName("rating")
    val restaurantRating: Float,
    @SerializedName("type")
    val restaurantType: String
)
