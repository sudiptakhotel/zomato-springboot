package com.majorproject.zomato.ZomatoApp.strategy.strategyManager;

import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import com.majorproject.zomato.ZomatoApp.strategy.PaymentStrategy;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.CODPaymentStrategy;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final CODPaymentStrategy codPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod) {

        if (paymentMethod.equals(PaymentMethod.CASH))
            return codPaymentStrategy;
        else
            return walletPaymentStrategy;
    }
}
