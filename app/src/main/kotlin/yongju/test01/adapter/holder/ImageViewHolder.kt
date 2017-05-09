package yongju.test01.adapter.holder

import android.content.Context
import android.graphics.drawable.Animatable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import kotlinx.android.synthetic.main.image_view.view.*
import yongju.test01.R
import yongju.test01.data.ImageInfo

/**
 * Created by yongju on 2017. 4. 6..
 */
class ImageViewHolder(context: Context, parent: ViewGroup?) :
        RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_view, parent, false)) {
    companion object {
        private val TAG = "ImageViewHolder"
    }

    fun bindView(imageInfo: ImageInfo) {
        itemView?.let {
            with(it) {
                image_view.controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(object : BaseControllerListener<com.facebook.imagepipeline.image.ImageInfo>() {
                        override fun onFinalImageSet(id: String?, imageInfo: com.facebook.imagepipeline.image.ImageInfo?, animatable: Animatable?) {
                            Log.d(TAG, "[onFinalImageSet] id: $id, imageInfo: $imageInfo, anitable: $$animatable")
                            super.onFinalImageSet(id, imageInfo, animatable)
                        }

                        override fun onFailure(id: String?, throwable: Throwable?) {
                            Log.e(TAG, "[onFailure] id: $id", throwable)
                            image_view.visibility = View.GONE
                        }
                    })
                    .setUri(imageInfo.imageUrl)
                    .build()
                image_view.aspectRatio = imageInfo.width / imageInfo.height
            }
        }
    }
}