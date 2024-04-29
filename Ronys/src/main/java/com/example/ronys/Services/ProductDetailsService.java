package com.example.ronys.Services;

import com.example.ronys.Model.LowStockProductDTO;
import com.example.ronys.Model.ProductDetails;
import com.example.ronys.Repository.ProductDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductDetailsService {
    private final ProductDetailsRepository productDetailsRepository;

    public ProductDetailsService(ProductDetailsRepository productDetailsRepository) {
        this.productDetailsRepository = productDetailsRepository;
    }

    public void addDetails(ProductDetails details) {
        productDetailsRepository.save(details);
    }
    public List<ProductDetails> getAllProductDetails() {
        return productDetailsRepository.findAll();
    }

    public List<ProductDetails> getProductDetailsByProductId(Long productId) {
        return productDetailsRepository.findByProductId(productId);
    }
    public List<ProductDetails> getLowStockProductDetails() {
        List<ProductDetails> allProductDetails = productDetailsRepository.findAll();

        // Filter product details with low stock (any color quantity less than 3)
        return allProductDetails.stream()
                .filter(productDetails -> productDetails.getQuantity() < 3)
                .collect(Collectors.toList());
    }

}
