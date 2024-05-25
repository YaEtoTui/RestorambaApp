package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class DishAndPhotoData(
    @SerializedName("Dishes")
    val dish: DishData,
    @SerializedName("PhotoDishes")
    val photo: PhotoDishData
)
