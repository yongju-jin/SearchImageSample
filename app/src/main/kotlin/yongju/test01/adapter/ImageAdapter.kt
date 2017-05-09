package yongju.test01.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import yongju.test01.adapter.holder.ImageViewHolder
import yongju.test01.data.ImageInfo

/**
 * Created by yongju on 2017. 4. 6..
 */
class ImageAdapter(private val context: Context)
    : RecyclerView.Adapter<ImageViewHolder>(),
        ImageViewAdapterContract.View, ImageViewAdapterContract.Model {

    val imageUrlList: MutableList<ImageInfo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageViewHolder {
        return ImageViewHolder(context, parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder?, position: Int) {
        holder?.bindView(getItem(position))
    }

    override fun getItemCount() = imageUrlList.size

    private fun getItem(position: Int) = imageUrlList[position]

    override fun reload() {
        notifyDataSetChanged()
    }

    override fun addItem(imageInfo: ImageInfo) {
        imageUrlList.add(imageInfo)
    }

    override fun clearImage() {
        imageUrlList.clear()
        reload()
    }
}