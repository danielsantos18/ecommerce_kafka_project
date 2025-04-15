package com.ecommerce.product_service.service;

import java.util.List;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;

public interface IProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse saveProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest updatedProduct);

    void deleteProduct(Long id);
}
