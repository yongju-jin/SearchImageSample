package yongju.lezhin.network.module

import android.util.Log
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by yongju on 2017. 4. 15..
 */

@Module
class HostModule {
    companion object {
        private val TAG = "HostModule"
    }

    @Provides @Singleton
    fun provideBaseUrl(): String {
        Log.d(TAG, "[provideBaseUrl]")
        return "https://apis.daum.net/"
    }
}