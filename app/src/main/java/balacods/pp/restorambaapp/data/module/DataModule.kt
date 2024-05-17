package balacods.pp.restorambaapp.data.module

import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

const val BASE_URL = "https://2955699-qx36434.twc1.net" //API SERVERS

object Common {
//    val dataModule = module {
//        factory { provideRestorambaApiService(retrofit = get()) }
//        single { provideRetrofitInstance(client = get()) }
//        single { CachingController(context = get()) }
//    }

    val retrofitService: RestorambaApiService
        get() = RetrofitClient.getClient(BASE_URL).create(RestorambaApiService::class.java)
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

private fun provideRetrofitInstance(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .client(client)
    .build()

private fun provideRestorambaApiService(retrofit: Retrofit): RestorambaApiService =
    retrofit.create(RestorambaApiService::class.java)