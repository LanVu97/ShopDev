package com.example.shopDev.Helper;

import com.example.shopDev.Exception.BadRequestError;
import com.example.shopDev.Models.product.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;
import java.util.Set;

public class JsonHelper {

    private static final Set<String> NOT_ALLOW_UPDATE = Set.of("id", "isDraft", "isPublished", "createdAt");

    public static <T> T updateJson(T existProduct, Class<T> typeParameterClass, Map<String, Object> fields)  {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ObjectNode node = objectMapper.convertValue(existProduct, ObjectNode.class);
            fields.forEach((key, value) ->{
                if(value == null || NOT_ALLOW_UPDATE.contains(key)){
                    return;
                }
                if(value != null){
                    if(value instanceof Map){
                        Map<String, String> map = (Map<String, String>) value;
                        // update product attirubte
                        map.forEach((k, v) ->{
                            node.with(key).put(k, v);
                        });

                    }else{
                        // update product
                        node.put(key, String.valueOf(value));

                        if("productName".equals(key)){
                            node.put("productSlug", Utils.toSlug(String.valueOf(value)));
                        }
                    }
                }
            });
            return objectMapper.treeToValue(node, typeParameterClass);
        }catch(Exception exp){
            throw new BadRequestError("convert" + exp);
        }
    }
}
