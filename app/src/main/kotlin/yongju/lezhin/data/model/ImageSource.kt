package yongju.lezhin.data.model

import yongju.lezhin.network.ImageServiceInterface
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by yongju on 2017. 4. 6..
 */

@Singleton
class ImageSource @Inject constructor(var imageServiceInterface: ImageServiceInterface) {

    fun searchImage(keyword: String, page: Int) = imageServiceInterface.searchImage(keyword, page)
}
