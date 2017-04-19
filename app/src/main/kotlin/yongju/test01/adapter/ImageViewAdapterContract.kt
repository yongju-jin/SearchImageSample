package yongju.lezhin.adapter

import yongju.lezhin.data.ImageInfo


/**
 * Created by yongju on 2017. 4. 6..
 */
interface ImageViewAdapterContract {
    interface View {
        fun reload()
    }

    interface Model {
        fun addItem(imageInfo: ImageInfo)
    }
}