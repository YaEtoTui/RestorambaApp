package balacods.pp.restorambaapp.retrofit.domain.dto

import com.google.gson.annotations.SerializedName

data class RestaurantData(
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("restaurant_name")
    val restaurantName: String,
    @SerializedName("restaurant_location")
    val restaurantLocation: String,
    @SerializedName("restaurant_coordinate_x")
    val restaurantCoordinateX: Float,
    @SerializedName("restaurant_coordinate_y")
    val restaurantCoordinateY: Float,
    @SerializedName("restaurant_description")
    val restaurantDescription: String,
    @SerializedName("restaurant_contact_phone")
    val restaurantContactPhone: String,
    @SerializedName("restaurant_rating")
    val restaurantRating: Float,
    @SerializedName("restaurant_type")
    val restaurantType: String
)
