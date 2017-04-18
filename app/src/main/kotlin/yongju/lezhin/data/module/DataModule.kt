package yongju.lezhin.data.module

import dagger.Module
import dagger.Provides
import yongju.lezhin.adapter.ImageViewAdapterContract
import yongju.lezhin.presenter.ImageSearchContract
import yongju.lezhin.presenter.ImageSearchPresenter

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
    fun providesImageSearchPresenter(imageSearchPresenter: ImageSearchPresenter): ImageSearchContract.Presenter =
            imageSearchPresenter
}