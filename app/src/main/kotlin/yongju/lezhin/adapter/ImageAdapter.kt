package yongju.lezhin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import yongju.lezhin.adapter.holder.ImageViewHolder
import yongju.lezhin.data.ImageInfo
import yongju.lezhin.view.ImageLoadInterface

/**
 * Created by yongju on 2017. 4. 6..
 */
class ImageAdapter(private val context: Context, private val imageLoadingCalbacks: ImageLoadInterface.ImageLoadingCalbacks)
    : RecyclerView.Adapter<ImageViewHolder>(), ImageViewAdapterContract.View, ImageViewAdapterContract.Model {

    val imageUrlList: MutableList<ImageInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageViewHolder {
        return ImageViewHolder(context, parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder?, position: Int) {
        holder?.bindView(getItem(position), imageLoadingCalbacks)
    }

    override fun getItemCount() = imageUrlList.size

    private fun getItem(position: Int) = imageUrlList[position]

    override fun reload() {
        notifyDataSetChanged()
    }

    override fun addItem(imageInfo: ImageInfo) {
        imageUrlList.add(imageInfo)
    }

    fun clearImage() {
//        notifyItemRangeRemoved(0, itemCount)
        imageUrlList.clear()
        reload()
    }
}