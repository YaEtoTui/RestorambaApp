package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantAndPhotoData(
    @SerializedName("Restaurants")
    val restaurant: RestaurantData,
    @SerializedName("PhotoRestaurants")
    val photo: PhotoRestaurantData
)