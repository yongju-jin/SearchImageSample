package yongju.lezhin.component

import dagger.Component
import yongju.lezhin.data.module.DataModule
import yongju.lezhin.network.module.RetrofitModule
import yongju.lezhin.view.MainActivity
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