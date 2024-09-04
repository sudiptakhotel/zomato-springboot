package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.AdminPromoDTO;
import com.majorproject.zomato.ZomatoApp.dto.CartDTO;
import com.majorproject.zomato.ZomatoApp.dto.CustomerPromoDTO;
import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.PromoEntity;
import com.majorproject.zomato.ZomatoApp.exception.DuplicatePromoInsertionException;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.PromoRepository;
import com.majorproject.zomato.ZomatoApp.service.CartService;
import com.majorproject.zomato.ZomatoApp.service.PromoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromosServiceImpl implements PromoService {

    private final ModelMapper modelMapper;
    private final PromoRepository promoRepository;

    @Override
    public AdminPromoDTO createPromo(AdminPromoDTO adminPromoDTO) {

        //check if there is any promo available with same name
        PromoEntity promo = promoRepository.findByPromoName(adminPromoDTO.getPromoName())
                .orElseThrow(() -> new DuplicatePromoInsertionException("Promo is " +
                        "already present with name : " + adminPromoDTO.getPromoName()));

        modelMapper.map(adminPromoDTO , PromoEntity.class);

        //save the promo to database
        PromoEntity savedPromo = promoRepository.save(promo);

        return modelMapper.map(savedPromo , AdminPromoDTO.class);

    }

    @Override
    public PromoEntity getPromoById(Long promoId) {

        return promoRepository.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo " +
                        "not found with id : "+promoId));
    }

    @Override
    public List<CustomerPromoDTO> getEligiblePromos(Double totalBill) {

        //fetch promos whose threshold amount is met
        List<PromoEntity> eligiblePromoList = promoRepository
                .findByThresholdAmountLessThanEqualAndExpirationDateAfter(totalBill , LocalDate.now());

        return eligiblePromoList.stream()
                .map(promoEntity -> modelMapper.map(promoEntity , CustomerPromoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Double applyPromo(Long promoId, Double totalBill) {

        PromoEntity promo = promoRepository.findById(promoId)
                .orElseThrow(() -> new ResourceNotFoundException("Promo " +
                        "not found with id : "+promoId));

        Integer discount = promo.getDiscountPercentage();
        Double discountedPercentage = discount / 100.00;

        //calculate and return the bill
        Double updatedTotalBill = totalBill - (totalBill *  discountedPercentage);
        log.info("Update amount"+updatedTotalBill);
        return updatedTotalBill;
    }

    @Override
    public List<AdminPromoDTO> getAllExpiredPromos() {

        LocalDate currentDate = LocalDate.now();

        List<PromoEntity> promoEntityList = promoRepository.findByExpirationDateBefore(currentDate)
                .orElse(null);

        return promoEntityList.stream()
                .map(promoEntity -> modelMapper.map(promoEntity , AdminPromoDTO.class))
                .toList();
    }
}
