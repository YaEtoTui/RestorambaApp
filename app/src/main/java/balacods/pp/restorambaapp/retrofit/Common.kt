package balacods.pp.restorambaapp.retrofit

import balacods.pp.restorambaapp.retrofit.api.MainApi

object Common {
    private val BASE_URL = "https://www.1506815-cq40245.tw1.ru" // НАДО МЕНЯТЬ :3
    val retrofitService: MainApi
        get() = RetrofitClient.getClient(BASE_URL).create(MainApi::class.java)
}