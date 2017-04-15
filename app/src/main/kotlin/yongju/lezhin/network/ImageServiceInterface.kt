package yongju.lezhin.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import yongju.lezhin.BuildConfig
import yongju.lezhin.data.ImageResponse

/**
 * Created by yongju on 2017. 4. 6..
 */
interface ImageServiceInterface {

    @GET("search/image?result=20&output=json&apikey=" + BuildConfig.DAUM_API_KEY)
    fun searchImage(@Query("q") keyword: String, @Query("pageno") page: Int): Observable<ImageResponse>
}