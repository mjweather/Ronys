package com.example.ronys.Repository;

import com.example.ronys.Model.Employee;
import com.example.ronys.Model.SellItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SellItemRepository extends JpaRepository<SellItem, Long> {

    List<SellItem> findAllByEmployee(Employee employee);

    List<SellItem> findAllByEmployeeAndTransactionDateBetween(Employee employee, LocalDateTime startDate, LocalDateTime endDate);
    List<SellItem> findAllByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
