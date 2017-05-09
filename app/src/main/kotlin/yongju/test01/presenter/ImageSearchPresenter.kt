package yongju.test01.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import yongju.test01.adapter.ImageViewAdapterContract
import yongju.test01.data.ImageInfo
import yongju.test01.data.model.ImageSource
import javax.inject.Inject

/**
 * Created by yongju on 2017. 4. 5..
 */

class ImageSearchPresenter @Inject constructor(var view: ImageSearchContract.View,
                                               var imageSource: ImageSource,
                                               var imageAdapterModel: ImageViewAdapterContract.Model,
                                               var imageAdapterView: ImageViewAdapterContract.View)
    : ImageSearchContract.Presenter {
    companion object {
        private val TAG = "ImageSearchPresenter"
        private val MAX_PAGE = 3
    }

    private var page = 0
    private var lastKeyword: String? = null
    private var imageSearchDisposable: Disposable? = null

    override fun searchImage(keyword: String) {
        Log.d(TAG, "[searchImage] keyword: $keyword")
        page = 1
        lastKeyword = keyword

        searchImage(keyword, page)
    }

    override fun searchMoreImage() {
        Log.d(TAG, "[searchMoreImage] page: $page")
        if (page < MAX_PAGE) {
            lastKeyword?.let {
                page += 1
                searchImage(it, page)
            }
        }
    }

    private fun searchImage(keyword: String, page: Int) {
        imageSearchDisposable = imageSource.searchImage(keyword, page)
                // network 통신 thread
                .subscribeOn(Schedulers.io())
                // data setting & view 갱신 thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.channel.item.forEach {
                        imageAdapterModel.addItem(ImageInfo(it.image, it.width.toFloat(),
                                it.height.toFloat()))
                    }
                }, {
                    Log.e(TAG, "[ImageLoadErrorConsumer] it: $it", it)
                    view.onFailSearchImage()
                }, {
                    Log.d(TAG, "[ImageLoadErrorConsumer] complete")
                    imageAdapterView.reload()
                    disposeImageSearchDisposable()
                })
    }

    private fun disposeImageSearchDisposable() {
        imageSearchDisposable?.dispose()
    }
}
