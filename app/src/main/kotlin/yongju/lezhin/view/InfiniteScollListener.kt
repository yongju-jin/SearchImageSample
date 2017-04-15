package yongju.lezhin.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by yongju on 2017. 4. 7..
 */
abstract class InfiniteScollListener(val layoutManager: LinearLayoutManager,
                                     val imageLoadInterface: ImageLoadInterface) :
        RecyclerView.OnScrollListener() {
    companion object {
        private val VISIBLE_THRESHOLD = 3
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        if (dy < 0 || imageLoadInterface.isImageLoading()) return

        val visibleItemCount = recyclerView?.childCount ?: 0
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            onLoadMoreImages()
        }
    }

    abstract fun onLoadMoreImages()
}