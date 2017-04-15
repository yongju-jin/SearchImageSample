package yongju.lezhin.presenter

import yongju.lezhin.adapter.ImageViewAdapterContract
import yongju.lezhin.data.model.ImageSource

/**
 * Created by yongju on 2017. 4. 5..
 */
interface ImageSearchContract {
    interface View {
        fun onFailSearchImage()
    }

    interface Presenter {
        var view: View?
        var imageSource: ImageSource?
        var imageAdapterModel: ImageViewAdapterContract.Model?
        var imageAdapterView: ImageViewAdapterContract.View?

        fun searchImage(keyword: String)
        fun searchMoreImage()
    }
}