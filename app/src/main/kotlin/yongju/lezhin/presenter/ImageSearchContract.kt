package yongju.lezhin.presenter

/**
 * Created by yongju on 2017. 4. 5..
 */
interface ImageSearchContract {
    interface View {
        fun onFailSearchImage()
    }

    interface Presenter {
        fun searchImage(keyword: String)
        fun searchMoreImage()
    }
}