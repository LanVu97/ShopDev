package com.example.shopDev.Controllers;

import com.example.shopDev.DTO.ProductDTO;
import com.example.shopDev.DTO.ShopDto;
import com.example.shopDev.DTO.SuccessResponse;
import com.example.shopDev.Models.product.Product;
import com.example.shopDev.Services.AccessService;
import com.example.shopDev.Services.Product.ProductServiceFactory;
import com.github.fge.jsonpatch.JsonPatch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/shop/createProduct")
    public SuccessResponse CreateProduct(@RequestAttribute("shop") String shopId, @RequestBody ProductDTO product) {

//        String shopId =  shopService.findById(shop);
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

    @GetMapping("/draft/all")
    public SuccessResponse GetAllDraftForShop(@RequestAttribute("shop") String shopId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {

        List<Product> productResponse = productServiceFactory.GetAllDraftForShop(shopId, size, page);

        return  SuccessResponse.builder()
                .code(200)
                .message("Get Draft product success")
                .metadata(productResponse)
                .build();
    }

    @PostMapping("/publish/{id}")
    public SuccessResponse PublishProductByShop(@RequestAttribute("shop") String shopId, @PathVariable("id") String id) {

        Product productResponse = productServiceFactory.publishProductByShop(shopId,id);

        return  SuccessResponse.builder()
                .code(200)
                .message("Publish product success")
                .metadata(productResponse)
                .build();
    }

    @PostMapping("/unpublish/{id}")
    public SuccessResponse UnPublishProductByShop(@RequestAttribute("shop") String shopId, @PathVariable("id") String id) {

        Product productResponse = productServiceFactory.unPublishProductByShop(shopId,id);

        return  SuccessResponse.builder()
                .code(200)
                .message("Publish product success")
                .metadata(productResponse)
                .build();
    }

    @GetMapping("/publish/all")
    public SuccessResponse GetAllPublishForShop(@RequestAttribute("shop") String shopId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {

        List<Product> productResponse = productServiceFactory.GetAllPublishForShop(shopId, size, page);

        return  SuccessResponse.builder()
                .code(200)
                .message("Get published product of shop success")
                .metadata(productResponse)
                .build();
    }

    //searchProductByUser

    @GetMapping("/search/{keySearch}")
    public SuccessResponse searchProductByKeyword(@PathVariable("keySearch") String keySearch,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "3") int size) {

        List<Product> productResponse = productServiceFactory.searchProductByUser(keySearch, size, page);

        return  SuccessResponse.builder()
                .code(200)
                .message("Search published product success")
                .metadata(productResponse)
                .build();
    }

    //findAllProducts
    @GetMapping("/all")
    public SuccessResponse findAllProducts(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "3") int size) {

        List<Product> productResponse = productServiceFactory.findAllProducts(size, page);

        return  SuccessResponse.builder()
                .code(200)
                .message("Get All published product success")
                .metadata(productResponse)
                .build();
    }

    @GetMapping("/{id}")
    public SuccessResponse findAllProducts(@PathVariable("id") String id) {

        Product pro = productServiceFactory.findProductDetail(id);
        ProductDTO productResponse = modelMapper.map(pro, ProductDTO.class);

        return  SuccessResponse.builder()
                .code(200)
                .message("Get published product detail success")
                .metadata(productResponse)
                .build();
    }

    @PatchMapping("/{id}")
    public SuccessResponse UpdatePaticalProduct(@PathVariable("id") String id, @RequestBody Map<String, Object> fields, @RequestAttribute("shop") String shopId) {

        productServiceFactory.updateProduct(id,fields, shopId);

        return  SuccessResponse.builder()
                .code(200)
                .message("Update product success")
                .metadata("")
                .build();
    }


    // update path use ReflectionUtils
//    @PatchMapping(path = "/{product_id}")
//    public SuccessResponse patchProduct(@PathVariable("product_id") String productId, @RequestBody Map<String, Object> body, @RequestAttribute("shop") String shopId) {
//
//
//        Product entity =  productServiceFactory.patchUpdate(shopId, productId, body);
//
//        return  SuccessResponse.builder()
//                .code(200)
//                .message("Update product success")
//                .metadata(entity)
//                .build();
//    }
}
