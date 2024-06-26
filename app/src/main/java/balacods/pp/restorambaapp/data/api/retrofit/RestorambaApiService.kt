package balacods.pp.restorambaapp.data.api.retrofit

import balacods.pp.restorambaapp.data.model.DishAndPhotoData
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RestorambaApiService {

    @GET("api/v1/restaurants")
    suspend fun getListRestaurants(): Response<List<RestaurantAndPhotoData>>

    @GET("api/v1/restaurants/{restaurant_id}")
    suspend fun getRestaurantById(@Path("restaurant_id") restaurantId: Long): Response<RestaurantAndPhotoData>

    @GET("api/v1/restaurants/{restaurant_id}/dishes")
    suspend fun getDishesByRestaurantId(@Path("restaurant_id") restaurantId: Long): Response<List<DishAndPhotoData>>

    @GET("api/v1/restaurants/{restaurant_id}/dishes/{dish_id}")
    suspend fun getDishByRestaurantAndDishId(
        @Path("restaurant_id") restaurantId: Long,
        @Path("dish_id") dishId: Long
    ): Response<DishAndPhotoData>

    @GET("api/v1/dishes")
    suspend fun getAllDishes(): Response<List<DishAndPhotoData>>

    @GET("api/v1/dishes/random_dish")
    suspend fun getRandomDish(): Response<DishAndPhotoData>

    @GET("api/v1/dishes/random_dish/{restaurant_id}")
    suspend fun getRandomDishByRestaurantId(@Path("restaurant_id") restaurantId: Long): Response<DishAndPhotoData>
}