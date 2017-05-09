package yongju.test01.component

import dagger.Component
import yongju.test01.data.module.DataModule
import yongju.test01.network.module.RetrofitModule
import yongju.test01.view.MainActivity
import javax.inject.Singleton

/**
 * Created by yongju on 2017. 4. 15..
 */
@Singleton
@Component(modules = arrayOf(
        RetrofitModule::class,
        DataModule::class
))
interface ImageComponent {
    fun inject(mainActivity: MainActivity)
}