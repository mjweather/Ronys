package com.example.ronys.Repository;

import com.example.ronys.Model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);

    Supplier findByName(String supplierName);
}
