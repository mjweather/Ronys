package com.example.ronys.RestController;

import com.example.ronys.Model.*;
import com.example.ronys.Repository.EmployeeRepository;
import com.example.ronys.Repository.SupplierRepository;
import com.example.ronys.Services.ProductDetailsService;
import com.example.ronys.Services.ProductService;
import com.example.ronys.Services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class Controller {
    private final ProductService productService;
    private final SupplierService supplierService;
    private final SupplierRepository supplierRepository;
    private final ProductDetailsService productDetailsService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public Controller(ProductService productService, SupplierService supplierService, SupplierRepository supplierRepository, ProductDetailsService productDetailsService, EmployeeRepository employeeRepository) {
        this.productService = productService;
        this.supplierService = supplierService;
        this.supplierRepository = supplierRepository;
        this.productDetailsService = productDetailsService;
        this.employeeRepository = employeeRepository;
    }
    //add product details
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody Product product, @RequestParam(value = "supplierName", required = false) String supplierName) {
        System.out.println("Hi");
        try {
            if (supplierName != null && !supplierName.isEmpty()) {
                System.out.println(supplierName);
                Supplier supplier = supplierRepository.findByName(supplierName);
                if (supplier == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier with name '" + supplierName + "' not found");
                }
                product.setSupplier(supplier);
            }
            productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/addProductDetails")
    public ResponseEntity<String> addProduct(@RequestBody List<ProductDetails> productDetails, @RequestParam("productId") Long productId) {
        System.out.println("called");

        Optional<Product> product = this.productService.getProductById(Long.valueOf(productId));
        for (ProductDetails details: productDetails) {
            details.setBarCode(productId);
            details.setProduct(product.get());
            this.productDetailsService.addDetails(details);
        }
            return ResponseEntity.status(HttpStatus.CREATED).body("Product Details added successfully");
        }



    //supplier
    @PostMapping("/addSupplier")
    public ResponseEntity<String> addSupplier(@RequestBody Supplier supplier){
        try {
            supplierService.addSupplier(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body("Supplier added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add supplier: " + e.getMessage());
        }
    }
    @GetMapping("/getAllSupplier")
    public List<Supplier> getAllSupplier(){

        return this.supplierRepository.findAll();
    }
    @GetMapping("/getProduct/{productId}")
    public Optional<Product> getProduct(@PathVariable("productId") Long productId) {
        return productService.getProductById(productId);
    }
    @GetMapping("/getAllEmployee")
    public List<Employee> getAllEmployee(){
        System.out.println("emp");
        return this.employeeRepository.findAll();
    }
    @PostMapping("/addEmployee")
    public void addEmployee(@RequestBody Employee employee){
        this.employeeRepository.save(employee);
    }


}
