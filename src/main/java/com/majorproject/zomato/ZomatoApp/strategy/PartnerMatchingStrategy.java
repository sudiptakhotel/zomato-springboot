package com.majorproject.zomato.ZomatoApp.strategy;

import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;
import com.majorproject.zomato.ZomatoApp.entity.PartnerEntity;

import java.util.List;

public interface PartnerMatchingStrategy {

    List<PartnerEntity> findAllMatchingPartner(OrderRequestEntity orderRequest);
}
