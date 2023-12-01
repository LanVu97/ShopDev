package com.example.shopDev;

import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Helper.JsonHelper;
import com.example.shopDev.Models.product.Product;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ProductTest {

    @Test
    void patchUpdate_Normal(){
        Product product = Product.builder()
                .id("6537d0fa932f3a1f4f1550be")
                .productName("product_name")
                .isDraft(true)
                .isPublished(false)
                .productPrice(100.0)
                .productRatingAverage(5.0)
                .build();

        Map<String, Object> fields = new HashMap<>();

        fields.put("productName", "update_product_name");
        fields.put("id", "update-id"); // not update
        fields.put("isPublished", true); // not update

        Product updateProduct = JsonHelper.updateJson(product,Product.class, fields);

        // expected
        Product expectedEntity = Product.builder()
                .id("6537d0fa932f3a1f4f1550be")
                .productName("update_product_name")
                .isDraft(true)
                .isPublished(false)
                .productSlug("update_product_name")
                .productPrice(100.0)
                .productRatingAverage(5.0)
                .build();
        Assertions.assertEquals(updateProduct, expectedEntity);
    }

    @Test
    void patchUpdate_Normal_Typecast(){

        Map<String, String> attributes = new HashMap<>();
        attributes.put("manufacture", "Philips");
        attributes.put("color", "RED");

        Product product = Product.builder()
                .id("6537d0fa932f3a1f4f1550be")
                .productName("product_name")
                .isDraft(true)
                .isPublished(false)
                .productPrice(100.0)
                .productQuantity(10)
                .productRatingAverage(5.0)
                .productAttributes(attributes)
                .build();


        Map<String, String> attributeUpdate = new HashMap<>();
        attributeUpdate.put("manufacture", "Philips-update");

        Map<String, Object> fields = new HashMap<>();
        fields.put("productName", "update_product_name");
        fields.put("id", "update-id"); // not update
        fields.put("productRatingAverage", 4.3); // jackson will map to Double, but in ProductEntity, it's float type
        fields.put("productQuantity", "10"); // should be integer, but we support parsing from String to int

        fields.put("productAttributes", attributeUpdate);

        Product updatedProduct = JsonHelper.updateJson(product,Product.class, fields);

        // expected
        Map<String, Object> expectedAttributes = new HashMap<>();
        expectedAttributes.put("manufacture", "Philips-update");
        expectedAttributes.put("color", "RED");

        Product expectedEntity = Product.builder()
                .id("6537d0fa932f3a1f4f1550be")
                .productName("update_product_name")
                .isDraft(true)
                .isPublished(false)
                .productPrice(100.0)
                .productSlug("update_product_name")
                .productQuantity(10)
                .productRatingAverage(4.3)
                .productAttributes(expectedAttributes)
                .build();

        Assertions.assertEquals(updatedProduct, expectedEntity);
    }

    @Test
    void patchUpdate_WrongType(){
        Product product = Product.builder()
                .id("6537d0fa932f3a1f4f1550be")
                .productName("product_name")
                .isDraft(true)
                .isPublished(false)
                .productPrice(100.0)
                .productRatingAverage(5.0)
                .build();

        Map<String, Object> inputData = new HashMap<>();
        inputData.put("productName", "update_product_name");
        inputData.put("id", "update-id"); // not update
        inputData.put("productRatingAverage", "true"); // wrong type

        Assertions.assertThrows(BadRequestError.class, () -> JsonHelper.updateJson(product,Product.class, inputData));
    }
}
