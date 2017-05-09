package yongju.test01.data

/**
 * Created by yongju on 2017. 4. 6..
 */
data class ImageChannel (
        val result: String,
        val pageCount: String,
        val title: String,
        val totalCount: String,
        val description: String,
        val item: List<ImageItem>,
        val lastBuildDate: String,
        val link: String,
        val generator: String
)