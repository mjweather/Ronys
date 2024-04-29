package com.example.ronys.Services;

import com.example.ronys.Model.Supplier;
import com.example.ronys.MyExceptions.MyCustomDuplicateSupplierException;
import com.example.ronys.Repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public void addSupplier(Supplier supplier) throws MyCustomDuplicateSupplierException {
        if (supplierRepository.existsByName(supplier.getName())) {
            throw new MyCustomDuplicateSupplierException("Supplier with name '" + supplier.getName() + "' already exists!");
        }
        supplierRepository.save(supplier);
    }
}
