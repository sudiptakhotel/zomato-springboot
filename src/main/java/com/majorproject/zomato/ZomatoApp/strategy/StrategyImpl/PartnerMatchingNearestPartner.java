package com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl;

import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;
import com.majorproject.zomato.ZomatoApp.entity.PartnerEntity;
import com.majorproject.zomato.ZomatoApp.repository.PartnerRepository;
import com.majorproject.zomato.ZomatoApp.strategy.PartnerMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerMatchingNearestPartner implements PartnerMatchingStrategy {

    private final PartnerRepository partnerRepository;

    @Override
    public List<PartnerEntity> findAllMatchingPartner(OrderRequestEntity orderRequest) {

        return partnerRepository.findTenNearestPartner(orderRequest.getRestaurant().getRestaurantLocation());
    }
}
