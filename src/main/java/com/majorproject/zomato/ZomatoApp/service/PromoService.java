package com.majorproject.zomato.ZomatoApp.service;


import com.majorproject.zomato.ZomatoApp.dto.AdminPromoDTO;
import com.majorproject.zomato.ZomatoApp.dto.CustomerPromoDTO;
import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.PromoEntity;

import java.util.List;

public interface PromoService {

    AdminPromoDTO createPromo(AdminPromoDTO adminPromoDTO);
    PromoEntity getPromoById(Long promoId);
    List<CustomerPromoDTO> getEligiblePromos(Double totalBill);
    Double applyPromo(Long promoId , Double totalBill);

    List<AdminPromoDTO> getAllExpiredPromos();
}
