package com.shopify.ui.payment.card.contract

import com.domain.entity.Address
import com.domain.entity.Card
import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import io.reactivex.Single
import javax.inject.Inject

interface CardPaymentView : BaseLceView<Boolean>

class CardPaymentPresenter constructor(private val cardPaymentUseCase: CardPaymentUseCase) :
        BaseLcePresenter<Boolean, CardPaymentView>(cardPaymentUseCase) {

    fun pay(checkout: Checkout, card: Card, address: Address) {
        cardPaymentUseCase.execute(
                { view?.showContent(it) },
                { it.printStackTrace() },
                CardPaymentUseCase.Params(checkout, card, address)
        )
    }
}

class CardPaymentUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<Boolean, CardPaymentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Boolean> =
            checkoutRepository.payByCard(params.card)
                    .flatMap { checkoutRepository.completeCheckoutByCard(params.checkout, params.address, it) }

    class Params(
            val checkout: Checkout,
            val card: Card,
            val address: Address
    )
}