package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.service.IProductService;
import com.ecommerce.product_service.util.ApiResponseUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponseUtil<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Lista de productos obtenida correctamente", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseUtil<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Producto obtenido correctamente", product));
    }

    @PostMapping
    public ResponseEntity<ApiResponseUtil<ProductResponse>> saveProduct(
            @RequestBody @Validated ProductRequest request) {
        ProductResponse saved = productService.saveProduct(request);
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Producto guardado correctamente", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseUtil<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestBody @Validated ProductRequest request) {
        ProductResponse updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Producto actualizado correctamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseUtil<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Producto eliminado correctamente", null));
    }

}
