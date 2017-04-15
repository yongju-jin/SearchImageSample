package yongju.lezhin.view

/**
 * Created by yongju on 2017. 4. 8..
 */
interface ImageLoadInterface {
    fun isImageLoading(): Boolean

    interface ImageLoadingCalbacks {
        fun completeImageLoad()
    }
}