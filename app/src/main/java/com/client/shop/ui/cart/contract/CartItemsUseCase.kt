package com.client.shop.ui.cart.contract

import com.client.shop.ui.base.contract.ObservableUseCase
import com.domain.entity.CartProduct
import com.repository.CartRepository
import io.reactivex.Observable
import javax.inject.Inject

class CartItemsUseCase @Inject constructor(private val cartRepository: CartRepository) :
        ObservableUseCase<List<CartProduct>, Unit>() {

    override fun buildUseCaseObservable(params: Unit): Observable<List<CartProduct>> {
        return cartRepository.getCartProductList()
    }
}