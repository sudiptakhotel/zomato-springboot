package com.majorproject.zomato.ZomatoApp.strategy.strategyManager;

import com.majorproject.zomato.ZomatoApp.strategy.PartnerMatchingStrategy;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.PartnerMatchingHighestRatedPartner;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.PartnerMatchingNearestPartner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartnerStrategyManager {

    private final PartnerMatchingNearestPartner nearestPartner;
    private final PartnerMatchingHighestRatedPartner highestRatedPartner;

    public PartnerMatchingStrategy getPartnerMatchingStrategy(Double customerRating) {
        if(customerRating >= 4.8)
            return highestRatedPartner;
        else
            return nearestPartner;
    }
}
