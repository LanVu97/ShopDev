package com.example.shopDev.Controllers;

import com.example.shopDev.Config.RoleShop;
import com.example.shopDev.Config.StatusShop;
import com.example.shopDev.DTO.LoginDto;
import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.DTO.SignUpDto;
import com.example.shopDev.DTO.SuccessResponse;
import com.example.shopDev.Models.ApiKey;
import com.example.shopDev.Models.KeyToken;
import com.example.shopDev.Models.Shops;
import com.example.shopDev.Repositories.KeyTokenRepository;
import com.example.shopDev.Security.JsonWebToken;
import com.example.shopDev.Services.AccessService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;


@RestController
@Transactional
@RequestMapping("/v1/api/shop/")
public class AccessController {
    @Autowired
    private AccessService accessService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KeyTokenRepository keyTokenRepository;

    @GetMapping("/create-api-key")
    public SuccessResponse CreateAPIKey() {

        ApiKey apikey = accessService.CreateAPIKey();


        return  SuccessResponse.builder()
                .code(201)
                .message("Create api key success")
                .metadata(apikey)
                .build();
    }

    @PostMapping("/signup")
    public SuccessResponse Signup(@RequestBody SignUpDto shopDto) {
        // convert DTO to entity
        Shops shopEntity = modelMapper.map(shopDto, Shops.class);

        Shops shop = accessService.SignUp(shopEntity);
        // convert entity to DTO
        ShopDto shopResponse = modelMapper.map(shop, ShopDto.class);

        return  SuccessResponse.builder()
                .code(201)
                .message("Register success")
                .metadata(shopResponse)
                .build();
    }

    @PostMapping("/login")
    public SuccessResponse Login(@RequestBody LoginDto shopDto)  {

        // convert DTO to entity
        Shops shopEntity = modelMapper.map(shopDto, Shops.class);

        Map<String, Object> map = accessService.Login(shopEntity);
        Shops shop = (Shops) map.get("shop");

        // convert entity to DTO
        ShopDto shopResponse = modelMapper.map(shop, ShopDto.class);

        map.replace("shop", shopResponse);
        return  SuccessResponse.builder()
                .code(200)
                .message("Login success")
                .metadata(map)
                .build();
    }

//    @GetMapping("/verifyToken")
//    public SuccessResponse Token(@PathParam("token") String token, @PathParam("shopId") String shopId)  {
//
//        //
////        String token = map.get("token");
////        String publicKey = map.get("publicKey");
//        KeyToken keyToken = keyTokenRepository.findByShopId(shopId);
//
//        Shops shop = JsonWebToken.verifyToken(token, keyToken);
//        return  SuccessResponse.builder()
//                .code(200)
//                .message("Login success")
//                .metadata(shop)
//                .build();
//    }


    @PostMapping("/refreshToken")
    public SuccessResponse HandleRefreshToken(@RequestBody Map<String, String> refreshToken)  {

        Map<String, Object> map = accessService.HandleRefreshToken(refreshToken.get("refreshToken"));
        Shops shop = (Shops) map.get("shop");

        // convert entity to DTO
        ShopDto shopResponse = modelMapper.map(shop, ShopDto.class);

        map.replace("shop", shopResponse);

        return  SuccessResponse.builder()
                .code(200)
                .message("Login success")
                .metadata(map)
                .build();
    }

    @PostMapping("/logout")
    public SuccessResponse Logout(@RequestAttribute("shop") String shop){

        String shopId =  accessService.Logout(shop);

        return  SuccessResponse.builder()
                .code(200)
                .message("Logout success")
                .metadata(shopId)
                .build();
    }
}
