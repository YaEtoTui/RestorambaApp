package balacods.pp.restorambaapp.data.api.retrofit

import balacods.pp.restorambaapp.data.model.RestaurantData
import retrofit2.http.GET

interface RestorambaApiService {

    @GET("api/v1/restaurants")
    suspend fun getListRestaurants() : List<RestaurantData>

}