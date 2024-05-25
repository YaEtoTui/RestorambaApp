package balacods.pp.restorambaapp.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDishData(
    val id: Long,
    @SerializedName("dish_id")
    val dishId: Long,
    @SerializedName("link_1")
    val link1: String,
    @SerializedName("link_2")
    val link2: String,
    @SerializedName("link_3")
    val link3: String
)