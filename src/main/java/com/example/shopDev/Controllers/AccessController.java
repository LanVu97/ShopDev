package com.example.shopDev.Controllers;

import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.DTO.SignUpDto;
import com.example.shopDev.Models.GroceryItem;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.AccessRepository;
import com.example.shopDev.Repositories.ItemRepository;
import com.example.shopDev.Services.AccessService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, Object> addUser(@RequestBody SignUpDto shopDto) throws Exception {
        // convert DTO to entity
        Shops shopEntity = modelMapper.map(shopDto, Shops.class);

        Map<String, Object> map = accessService.SignUp(shopEntity);
        Shops shop = (Shops) map.get("shop");

        // convert entity to DTO
        ShopDto shopResponse = modelMapper.map(shop, ShopDto.class);

        map.replace("shop", shopResponse);
        return map;
    }
}
