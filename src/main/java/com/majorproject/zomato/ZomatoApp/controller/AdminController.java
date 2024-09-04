package com.majorproject.zomato.ZomatoApp.controller;

import com.majorproject.zomato.ZomatoApp.dto.AdminPromoDTO;
import com.majorproject.zomato.ZomatoApp.dto.PartnerDTO;
import com.majorproject.zomato.ZomatoApp.dto.PointDTO;
import com.majorproject.zomato.ZomatoApp.dto.UserDTO;
import com.majorproject.zomato.ZomatoApp.service.AuthService;
import com.majorproject.zomato.ZomatoApp.service.PromoService;
import com.majorproject.zomato.ZomatoApp.service.RestaurantWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
//@Secured({"ROLE_ADMIN"})
public class AdminController {

    private final AuthService authService;
    private final PromoService promoService;

    @PostMapping(path = "/onBoardAdmin/{userId}")
    public ResponseEntity<UserDTO> onBoardAdmin(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.onBoardAdmin(userId));
    }

    @PostMapping(path = "/onBoardPartner/{userId}")
    public ResponseEntity<PartnerDTO> onBoardPartner(@PathVariable Long userId ,
                                                     @RequestBody PointDTO pointDTO) {

        return ResponseEntity.ok(authService.onBoardPartner(userId , pointDTO));
    }

    @GetMapping(path = "/getAllExpiredPromos")
    public ResponseEntity<List<AdminPromoDTO>> getAllExpiredPromos() {
        return ResponseEntity.ok(promoService.getAllExpiredPromos());
    }
}
