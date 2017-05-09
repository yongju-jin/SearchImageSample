package yongju.test01.data.module

import dagger.Module
import dagger.Provides
import yongju.test01.adapter.ImageViewAdapterContract
import yongju.test01.presenter.ImageSearchContract
import yongju.test01.presenter.ImageSearchPresenter

/**
 * Created by yongju on 2017. 4. 18..
 */
@Module
class DataModule(val view: ImageSearchContract.View,
                 val imageAdapterModel: ImageViewAdapterContract.Model,
                 val imageAdapterView: ImageViewAdapterContract.View) {

    @Provides
    fun providesImageSearchContractView() = view

    @Provides
    fun providesImageViewAdapterContractModel() = imageAdapterModel

    @Provides
    fun providesImageViewAdapterContractView() = imageAdapterView

    @Provides
    fun providesImageSearchContractPresenter(imageSearchPresenter: ImageSearchPresenter): ImageSearchContract.Presenter =
            imageSearchPresenter
}