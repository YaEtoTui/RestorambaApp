package balacods.pp.restorambaapp.data.module

import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.okHTTP.CachingController
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

const val BASE_URL = "localhost:8080" //API SERVERS

val dataModule = module {
    factory { provideRestorambaApiService(retrofit = get()) }
    single { provideRetrofitInstance(client = get()) }
    single { CachingController(context = get()) }
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