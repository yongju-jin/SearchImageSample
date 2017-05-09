package yongju.test01.network.module

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by yongju on 2017. 4. 15..
 */

@Module
class HostModule {
    @Provides @Singleton
    fun provideBaseUrl() = "https://apis.daum.net/"
}