package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point

data class RestaurantAndPhotoDTO(
    @SerializedName("Restaurants")
    val restaurant: RestaurantData,
    @SerializedName("PhotoRestaurants")
    val photo: PhotoRestaurantData,
    val point: Point
)