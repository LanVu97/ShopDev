package com.example.shopDev.Repositories.Product;


import com.example.shopDev.Models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

Page<Product> findByIsDraftAndProductShop(boolean isDraft, String shopId, Pageable pageable);

    Product findByProductShopAndId(String productShop, String productId);

    Page<Product> findByIsPublishedAndProductShop(boolean isPublished, String shopId, Pageable paging);

    List<Product> findByIsPublished(TextCriteria criteria, boolean isPublished);

    Page<Product> findByIsPublished(Pageable paging, boolean isPublished);

    Product findByIdAndIsPublished(String productId, boolean isPublished);
}
