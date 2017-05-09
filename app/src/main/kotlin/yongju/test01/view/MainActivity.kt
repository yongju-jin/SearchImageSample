package yongju.test01.view


import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import com.facebook.drawee.backends.pipeline.Fresco
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import yongju.test01.R
import yongju.test01.adapter.ImageAdapter
import yongju.test01.adapter.ImageViewAdapterContract
import yongju.test01.component.DaggerImageComponent
import yongju.test01.data.module.DataModule
import yongju.test01.network.module.HostModule
import yongju.test01.presenter.ImageSearchContract
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ImageSearchContract.View {
    companion object {
        private val TAG = "MainActivity"
        private val SEARCH_DELAY = 1L
        private val VISIBLE_THRESHOLD = 5
    }

    private var menuItem: MenuItem? = null

    @Inject
    lateinit var imageSearchContractPresenter: ImageSearchContract.Presenter

    @Inject
    lateinit var imageViewAdapterContractView: ImageViewAdapterContract.View

    private var keywordDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this)

        setContentView(R.layout.activity_main)

        val imageAdapter = ImageAdapter(this)
        DaggerImageComponent.builder()
                .dataModule(DataModule(this, imageAdapter, imageAdapter))
                .hostModule(HostModule())
                .build().inject(this)

        main_recyclverview.adapter = imageAdapter

        val linearLayoutManager = LinearLayoutManager(this)
        main_recyclverview.layoutManager = linearLayoutManager
        main_recyclverview.itemAnimator = DefaultItemAnimator()

        RxRecyclerView.scrollEvents(main_recyclverview)
            .filter {
                it.dy() > 0
            }.map {
                it.view().layoutManager as LinearLayoutManager
            }.subscribe({
                if ((it.itemCount
                        - it.findFirstVisibleItemPosition()) <= VISIBLE_THRESHOLD) {
                    imageSearchContractPresenter.searchMoreImage()
                }
            }, Throwable::printStackTrace)
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
                .doOnNext {
                    if (it.isSubmitted) {
                        menuItem?.collapseActionView()
                        val queryText = it.queryText()
                        if (queryText.isNotEmpty()) {
                            changeActionBarTitle(queryText.toString())
                        }
                    }
                }
                .filter { it.queryText().isNotEmpty() }
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
            imageViewAdapterContractView.clearImage()

            imageSearchContractPresenter.searchImage(keyword)
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
        menuItem?.let {
            it.collapseActionView()
            changeActionBarTitle(keyword)
        }
    }

    private fun changeActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun openSearchView() {
        Log.d(TAG, "[openSearchView]")
        menuItem?.expandActionView()
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
