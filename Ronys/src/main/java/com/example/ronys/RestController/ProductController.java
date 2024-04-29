package com.example.ronys.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ronys.Model.ProductDetails;
import com.example.ronys.Repository.ProductDetailsRepository;

@RestController
public class ProductController {
    private final ProductDetailsRepository productDetailsRepository;

    public ProductController(ProductDetailsRepository productDetailsRepository) {
        this.productDetailsRepository = productDetailsRepository;
    }
//
    @GetMapping("/detailsByCompany/{company}")
    private List<ProductDetails> getDetailsByCompany(@PathVariable("company") String company){
        return this.productDetailsRepository.findAllByProduct_Company(company);
    }

    @GetMapping("/detailsByGroup/{group}")
    private List<ProductDetails> getDetailsByGroup(@PathVariable("group") String group){
        System.out.println(group);
        return this.productDetailsRepository.findAllByProduct_Group(group);
    }

    @GetMapping("/detailsByProductId/{productId}")
    private List<ProductDetails> getDetailsByProductId(@PathVariable("productId") Long productId){
        return this.productDetailsRepository.findAllByProduct_Id(productId);
    }

    @GetMapping("/allDetails")
    private List<ProductDetails> getAllDetails(){
        return this.productDetailsRepository.findAll();
    }
}
