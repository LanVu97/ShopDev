package com.example.shopDev.Controllers;

import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.DTO.SignUpDto;
import com.example.shopDev.DTO.SuccessResponse;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Services.AccessService;
import com.example.shopDev.Services.ShopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequestMapping("/v1/api/shop/")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public SuccessResponse GetShopDetail(@PathVariable String shopId) {
        // convert DTO to entity
//        Shops shopEntity = modelMapper.map(shopDto, Shops.class);

        Shops shop = shopService.findById(shopId);
        // convert entity to DTO
        ShopDto shopResponse = modelMapper.map(shop, ShopDto.class);

        return  SuccessResponse.builder()
                .code(200)
                .message("Get success")
                .metadata(shopResponse)
                .build();
    }
}
