package balacods.pp.restorambaapp.data.api.retrofit

import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.data.model.RestaurantData
import retrofit2.http.GET
import retrofit2.http.Path

interface RestorambaApiService {

    @GET("api/v1/restaurants")
    suspend fun getListRestaurants(): List<RestaurantData>

    @GET("api/v1/restaurants/{restaurant_id}")
    suspend fun getRestaurantById(@Path("restaurant_id") restaurantId: Long): RestaurantData

    @GET("api/v1/restaurants/{restaurant_id}/dishes")
    suspend fun getDishesByRestaurantId(@Path("restaurant_id") restaurantId: Long): List<MenuData>

    @GET("api/v1/restaurants/{restaurant_id}/dishes/{dish_id}")
    suspend fun getDishesByRestaurantId(
        @Path("restaurant_id") restaurantId: Long,
        @Path("dish_id") dishId: Long
    ): MenuData

    @GET("api/v1/dishes")
    suspend fun getAllDishes(): List<MenuData>

    @GET("api/v1/dishes/random_dish")
    suspend fun getRandomDish(): MenuData

    @GET("api/v1/dishes/random_dish/{restaurant_id}")
    suspend fun getRandomDishByRestaurantId(@Path("restaurant_id") restaurantId: Long): MenuData
}