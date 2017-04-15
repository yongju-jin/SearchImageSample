package yongju.lezhin.view


import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import yongju.lezhin.R
import yongju.lezhin.adapter.ImageAdapter
import yongju.lezhin.data.model.ImageSource
import yongju.lezhin.presenter.ImageSearchContract
import yongju.lezhin.presenter.ImageSearchPresenter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), ImageSearchContract.View {
    companion object {
        private val TAG = "MainActivity"
        private val SEARCH_DELAY = 1L
    }

    private var menuItem: MenuItem? = null

    private val imageSearchPresenter by lazy {
        ImageSearchPresenter()
    }

    private val imageAdapter by lazy {
        ImageAdapter(this, imageSearchPresenter)
    }

    private var keywordDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this)

        setContentView(R.layout.activity_main)

        imageSearchPresenter.view = this
        imageSearchPresenter.imageSource = ImageSource

        imageSearchPresenter.imageAdapterModel = imageAdapter
        imageSearchPresenter.imageAdapterView = imageAdapter

        main_recyclverview.adapter = imageAdapter

        val linearLayoutManager = LinearLayoutManager(this)
        main_recyclverview.layoutManager = linearLayoutManager
        main_recyclverview.itemAnimator = DefaultItemAnimator()
        main_recyclverview.addOnScrollListener(
                object : InfiniteScollListener(linearLayoutManager, imageSearchPresenter) {
            override fun onLoadMoreImages() {
                imageSearchPresenter.searchMoreImage()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        keywordDisposable?.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "[onCreateOptionsMenu]")
        menuInflater.inflate(R.menu.menu_search, menu)

        menuItem = menu?.findItem(R.id.menu_search)

        val searchView = menuItem?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        keywordDisposable = RxSearchView.queryTextChangeEvents(searchView)
                .doOnNext { if (it.isSubmitted) {
                    menuItem?.collapseActionView()
                    changeActionBarTitle(it.queryText().toString())
                }}
                .filter { !TextUtils.isEmpty(it.queryText().toString()) }
                .debounce(SEARCH_DELAY, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keywordConsumer, keywordErrorConsumer)

        return true
    }

    private val keywordConsumer by lazy {
        Consumer<SearchViewQueryTextEvent> {
            val keyword = it.queryText().toString()
            Log.d(TAG, "[keywordConsumer] keyword: $keyword")
            hideKeyboard(it.view())
            closeSearchView(keyword)
            imageAdapter.clearImage()

            imageSearchPresenter.searchImage(keyword)
        }
    }

    private val keywordErrorConsumer by lazy {
        Consumer<Throwable> {
            Log.e(TAG, "[keywordErrorConsumer] error", it)
            onFailSearchImage()
        }
    }

    private fun closeSearchView(keyword: String) {
        Log.d(TAG, "[closeSearchView]")
        menuItem.let {
            it?.collapseActionView()
            changeActionBarTitle(keyword)
        }
    }

    private fun changeActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun openSearchView() {
        Log.d(TAG, "[openSearchView]")
        menuItem.let {
            it?.expandActionView()
        }
    }

    private fun hideKeyboard(view: SearchView) {
        Log.d(TAG, "[hideKeyboard]")
        view.clearFocus()
    }

    override fun onFailSearchImage() {
        Log.d(TAG, "[onFailSearchImage]")
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.image_search_err_msg)
                .setPositiveButton(android.R.string.ok, { _, _ ->
                    openSearchView()
                })
                .create().show()
    }
}
