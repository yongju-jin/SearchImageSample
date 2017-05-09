package yongju.test01.adapter

import yongju.test01.data.ImageInfo


/**
 * Created by yongju on 2017. 4. 6..
 */
interface ImageViewAdapterContract {
    // adapter 갱신
    interface View {
        fun reload()
        fun clearImage()
    }

    // adapter 에 data 전달
    interface Model {
        fun addItem(imageInfo: ImageInfo)
    }
}