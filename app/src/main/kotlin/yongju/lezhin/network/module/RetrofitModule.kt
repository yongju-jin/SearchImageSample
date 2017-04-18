package yongju.lezhin.network.module

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import yongju.lezhin.network.ImageServiceInterface
import javax.inject.Singleton

/**
 * Created by yongju on 2017. 4. 6..
 */

@Module(includes = arrayOf(
        HostModule::class
))
class RetrofitModule {
    companion object {
        private val TAG = "RetrofitModule"
    }

    @Provides @Singleton
    fun provideImageServiceInterface(baseUrl: String): ImageServiceInterface {
        Log.d(TAG, "[provideImageServiceInterface] baseUrl: $baseUrl")
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(ImageServiceInterface::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        Log.d(TAG, "[createOkHttpClient]")
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }
}