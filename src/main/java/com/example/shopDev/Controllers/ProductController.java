package com.example.shopDev.Controllers;

import com.example.shopDev.DTO.ProductDTO;
import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.DTO.SuccessResponse;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Services.AccessService;
import com.example.shopDev.Services.Product.ProductServiceFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequestMapping("/v1/api/product/")
public class ProductController {
    @Autowired
    private AccessService accessService;

    @Autowired
    private ProductServiceFactory productServiceFactory;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/createProduct")
    public SuccessResponse CreateProduct(@RequestAttribute("shop") String shop, @RequestBody ProductDTO product) {

        String shopId =  accessService.Logout(shop);
        Product pro = modelMapper.map(product, Product.class);
         pro = productServiceFactory
                .createProduct(product.getProductType(), pro, shopId);

        ProductDTO productResponse = modelMapper.map(pro, ProductDTO.class);

        return  SuccessResponse.builder()
                .code(201)
                .message("Create product success")
                .metadata(productResponse)
                .build();
    }
}
