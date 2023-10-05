package com.example.shopDev.Controllers;

import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.DTO.SignUpDto;
import com.example.shopDev.DTO.SuccessResponse;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Services.AccessService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v1/api/shop/")
public class AccessController {
    @Autowired
    private AccessService accessService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signup")
    @Transactional
    public SuccessResponse Signup(@RequestBody SignUpDto shopDto) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // convert DTO to entity
        Shops shopEntity = modelMapper.map(shopDto, Shops.class);

        Map<String, Object> map = accessService.SignUp(shopEntity);
        Shops shop = (Shops) map.get("shop");

        // convert entity to DTO
        ShopDto shopResponse = modelMapper.map(shop, ShopDto.class);

        map.replace("shop", shopResponse);

        SuccessResponse s =  SuccessResponse.builder()
                .code(201)
                .message("Register success")
                .metadata(map)
                .build();

        return s;
    }
}
