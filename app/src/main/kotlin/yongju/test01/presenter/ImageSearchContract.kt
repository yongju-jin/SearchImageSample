package yongju.test01.presenter

/**
 * Created by yongju on 2017. 4. 5..
 */
interface ImageSearchContract {
    interface View {
        // 이미지 검색 실패 시
        fun onFailSearchImage()
    }

    interface Presenter {
        // 이미지 첫 검색 시
        fun searchImage(keyword: String)
        // 이미지 더 검색 시
        fun searchMoreImage()
    }
}