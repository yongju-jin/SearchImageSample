package yongju.test01.adapter

import yongju.test01.data.ImageInfo


/**
 * Created by yongju on 2017. 4. 6..
 */
interface ImageViewAdapterContract {
    interface View {
        fun reload()
        fun clearImage()
    }

    interface Model {
        fun addItem(imageInfo: ImageInfo)
    }
}