package yongju.lezhin.presenter

import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import yongju.lezhin.adapter.ImageViewAdapterContract
import yongju.lezhin.data.ImageInfo
import yongju.lezhin.data.model.ImageSource
import yongju.lezhin.view.ImageLoadInterface
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by yongju on 2017. 4. 5..
 */
class ImageSearchPresenter : ImageSearchContract.Presenter, ImageLoadInterface,
        ImageLoadInterface.ImageLoadingCalbacks {
    companion object {
        private val TAG = "ImageSearchPresenter"
        private val MAX_PAGE = 3
    }

    override var view: ImageSearchContract.View? = null

    override var imageSource: ImageSource? = null

    override var imageAdapterModel: ImageViewAdapterContract.Model? = null

    override var imageAdapterView: ImageViewAdapterContract.View? = null

    private var page = 0

    private var lastKeyword: String? = null

    private var loadingCount: AtomicInteger? = null

    private var isSearchingImage = false

    private var imageSearchDisposable: Disposable? = null

    override fun searchImage(keyword: String) {
        Log.d(TAG, "[searchImage] keyword: $keyword")
        page = 1
        loadingCount = AtomicInteger(0)
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
        isSearchingImage = true

        imageSearchDisposable = imageSource?.searchImage(keyword, page)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    it.channel.item.forEach {
                        imageAdapterModel?.addItem(ImageInfo(it.image, it.width.toFloat(),
                                it.height.toFloat()))
                        loadingCount?.incrementAndGet()
                    }
                    imageAdapterView?.reload()

                    isSearchingImage = false
                }, {
                    Log.e(TAG, "[ImageLoadErrorConsumer] it: $it", it)
                    isSearchingImage = false
                    view?.onFailSearchImage()
                }, {
                    Log.d(TAG, "[ImageLoadErrorConsumer] complete")
                    disposeImageSearchDisposable()
                })
    }

    private fun disposeImageSearchDisposable() {
        imageSearchDisposable?.dispose()
    }

    override fun isImageLoading(): Boolean {
        return isSearchingImage || (loadingCount?.get() ?: 0 > 0)
    }

    override fun completeImageLoad() {
        loadingCount?.decrementAndGet()
    }
}
