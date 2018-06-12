package com.shopapp.ui.product

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.View
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.divider.GridSpaceDecoration
import com.shopapp.ui.const.Constant.GRID_SPAN_COUNT
import com.shopapp.ui.product.adapter.ProductListAdapter
import com.shopapp.ui.product.contract.ProductListPresenter
import com.shopapp.ui.product.contract.ProductListView
import com.shopapp.ui.product.router.ProductRouter
import kotlinx.android.synthetic.main.fragment_popular.*
import javax.inject.Inject

class ProductPopularFragment :
    BaseLceFragment<List<Product>, ProductListView, ProductListPresenter>(),
    ProductListView,
    OnItemClickListener {

    companion object {
        private const val MAX_ITEMS = 4
    }

    @Inject
    lateinit var productListPresenter: ProductListPresenter

    @Inject
    lateinit var router: ProductRouter

    private val productList = mutableListOf<Product>()
    private val sortType = SortType.RELEVANT
    private lateinit var adapter: ProductListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        loadData(true)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachProductComponent().inject(this)
    }

    override fun getContentView() = R.layout.fragment_popular

    override fun createPresenter() = productListPresenter

    //SETUP

    private fun setupRecycler() {
        val layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
        val size = resources.getDimensionPixelSize(R.dimen.product_horizontal_item_size)
        adapter = ProductListAdapter(size, size, productList, this)
        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(
            GridSpaceDecoration(
                resources.getDimensionPixelSize(R.dimen.recycler_divider_space), GRID_SPAN_COUNT
            )
        )
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(sortType, MAX_ITEMS)
    }

    override fun showContent(data: List<Product>) {
        super.showContent(data)
        fragmentVisibilityListener?.changeVisibility(data.isNotEmpty())
        if (data.isNotEmpty()) {
            productList.clear()
            if (data.size >= MAX_ITEMS) {
                productList.addAll(data.subList(0, MAX_ITEMS))
            } else {
                productList.addAll(data)
            }
            adapter.notifyDataSetChanged()
        }
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        productList.getOrNull(position)?.let { router.showProduct(context, it.id) }
    }

}