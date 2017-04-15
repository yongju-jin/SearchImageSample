package yongju.lezhin.data.model

import yongju.lezhin.network.ImageServiceInterface
import yongju.lezhin.network.createRetrofit

/**
 * Created by yongju on 2017. 4. 6..
 */
object ImageSource {
    val DAUM_API_URL = "https://apis.daum.net/"

    private val imageServiceInterface: ImageServiceInterface
    init {
        imageServiceInterface = createRetrofit(ImageServiceInterface::class.java, DAUM_API_URL)
    }

    fun searchImage(keyword: String, page: Int) = imageServiceInterface.searchImage(keyword, page)
}
