package com.example.ronys.RestController;

import com.example.ronys.Model.*;
import com.example.ronys.Repository.EmployeeRepository;
import com.example.ronys.Repository.ProductDetailsRepository;
import com.example.ronys.Repository.SellItemRepository;
import com.example.ronys.Repository.TransactionRepository;
import com.example.ronys.Services.ProductDetailsService;
import com.example.ronys.Services.ProductService;
import com.example.ronys.Services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final SellItemRepository sellItemRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductDetailsService productDetailsService;
    private final ProductService productService;


    public AdminController(UserService userService, TransactionRepository transactionRepository, SellItemRepository sellItemRepository, EmployeeRepository employeeRepository, ProductDetailsService productDetailsService, ProductService productService) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.sellItemRepository = sellItemRepository;
        this.employeeRepository = employeeRepository;
        this.productDetailsService = productDetailsService;
        this.productService = productService;
    }
//    @GetMapping("/totalSellAndProfitToday")
//    public Map<String, Integer> countSellAndProfitToday(
//            @RequestParam(required = false) LocalDate selectedDate) {
//        System.out.println("today");
//        if (selectedDate == null) {
//            selectedDate = LocalDate.now();
//        }
//        LocalDateTime startTimestamp = selectedDate.atTime(9, 0); // 7 am of selected date
//        LocalDateTime endTimestamp = selectedDate.plusDays(1).atTime(9, 0); // 7 am of next day
//        return calculateTotalSellAndProfit(startTimestamp, endTimestamp);
//    }
//
//
//    @GetMapping("/totalSellAndProfitMonthly")
//    public Map<String, Integer> countSellAndProfitMonthly(
//            @RequestParam int selectedMonth,
//            @RequestParam int selectedYear) {
//        LocalDateTime startTimestamp = LocalDate.of(selectedYear, selectedMonth, 1).atTime(9, 0); // 7 am of the first day of the month
//        LocalDateTime endTimestamp = LocalDate.of(selectedYear, selectedMonth, 1).plusMonths(1).atTime(9, 0); // 7 am of the first day of next month
//        return calculateTotalSellAndProfit(startTimestamp, endTimestamp);
//    }
//
//    @GetMapping("/totalSellAndProfitYearly")
//    public Map<String, Integer> countSellAndProfitYearly(
//            @RequestParam int selectedYear) {
//        LocalDateTime startTimestamp = LocalDate.of(selectedYear, 1, 1).atTime(9, 0); // 7 am of the first day of the year
//        LocalDateTime endTimestamp = LocalDate.of(selectedYear + 1, 1, 1).atTime(9, 0); // 7 am of the first day of next year
//        return calculateTotalSellAndProfit(startTimestamp, endTimestamp);
//    }
//    @GetMapping("/transactionsForSelectedPeriod")
//    public Map<String, Integer> fetchTransactionsForSelectedPeriod(
//            @RequestParam(required = false) LocalDate startDate,
//            @RequestParam(required = false) LocalDate endDate) {
//        System.out.println("selected");
//        LocalDateTime adjustedStartDateTime = startDate.atTime(9, 0); // Set the start time to 9 am
//        LocalDateTime adjustedEndDateTime = endDate.atTime(9, 0); // Set the end time to 9 am of the next day
//
//        return calculateTotalSellAndProfit(adjustedStartDateTime, adjustedEndDateTime);
//    }
//
//
//    private Map<String, Integer> calculateTotalSellAndProfit(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
//        List<Transaction> transactions = transactionRepository.findByDateBetween(startTimestamp, endTimestamp);
//
//        int totalSell = 0;
//        int totalProfit = 0;
//
//        for (Transaction transaction : transactions) {
//            totalSell += transaction.getTotalPrice();
//            totalProfit += transaction.getTotalProfit();
//        }
//
//        Map<String, Integer> result = new HashMap<>();
//        result.put("totalSell", totalSell);
//        result.put("totalProfit", totalProfit);
//
//        return result;
//    }
//
//    @GetMapping("/getSellDetails")
//    public List<Transaction> todaySellDetails(
//            @RequestParam(required = false) LocalDate selectedDate) {
//        System.out.println("today");
//        if (selectedDate == null) {
//            selectedDate = LocalDate.now();
//        }
//        LocalDateTime startTimestamp = selectedDate.atTime(9, 0); // 7 am of selected date
//        LocalDateTime endTimestamp = selectedDate.plusDays(1).atTime(9, 0); // 7 am of next day
//        return this.transactionRepository.findByDateBetween(startTimestamp,endTimestamp);
//    }
//
//    @PostMapping("/addUser")
//    public void addUser(@RequestBody Users user) throws MyCustomDuplicateKeyException {
//        userService.createUser(user);
//    }
//
//@GetMapping("/sales")
//public Map<String, Integer> getEmployeeSales(
//        @RequestParam(required = false) LocalDate startDate,
//        @RequestParam(required = false) LocalDate endDate) {
//    System.out.println("Sales within time period: " + startDate + " to " + endDate);
//    List<Employee> employees = employeeRepository.findAll();
//    Map<String, Integer> employeeSalesMap = new HashMap<>();
//    // Set the start time to 9:00 AM of the selected date
//    LocalDateTime adjustedStartDateTime = startDate.atTime(9, 0);
//
//// Set the end time to 9:00 AM of the next date
//    LocalDateTime adjustedEndDateTime = endDate.plusDays(1).atTime(9, 0);
//    System.out.println("Sales within time period: " + adjustedStartDateTime + " to " + adjustedEndDateTime);
//    for (Employee employee : employees) {
//        int totalSales = calculateEmployeeSalesWithinPeriod(employee, adjustedStartDateTime, adjustedEndDateTime);
//        employeeSalesMap.put(employee.getName(), totalSales);
//    }
//
//    return employeeSalesMap;
//}
//
//    private int calculateEmployeeSalesWithinPeriod(Employee employee, LocalDateTime startDate, LocalDateTime endDate) {
//        List<SellItem> sellItems = sellItemRepository.findAllByEmployeeAndTransactionDateBetween(employee, startDate, endDate);
//        int totalSales = 0;
//        for (SellItem sellItem : sellItems) {
//            totalSales += (sellItem.getProductDetails().getProduct().getSellingPrice() - sellItem.getDiscount());
//        }
//        return totalSales;
//    }
//
//    @GetMapping("/sellItemDaily")
//    public List<SellItem> getSellItemsDaily(@RequestParam(required = false) LocalDate selectedDate) {
//        System.out.println("today sell items");
//        if (selectedDate == null) {
//            selectedDate = LocalDate.now();
//        }
//        LocalDateTime startTimestamp = selectedDate.atTime(9, 0); // 7 am of selected date
//        LocalDateTime endTimestamp = selectedDate.plusDays(1).atTime(9, 0); // 7 am of next day
//        return this.sellItemRepository.findAllByTransactionDateBetween(startTimestamp,endTimestamp);
//    }
//    @GetMapping("/sellItemPeriod")
//    public List<SellItem> getSellBetweenPeriod(
//            @RequestParam(required = false) LocalDate startDate,
//            @RequestParam(required = false) LocalDate endDate) {
//        System.out.println("selected");
//        LocalDateTime adjustedStartDateTime = startDate.atTime(9, 0); // Set the start time to 9 am
//        LocalDateTime adjustedEndDateTime = endDate.atTime(9, 0); // Set the end time to 9 am of the next day
//
//        return this.sellItemRepository.findAllByTransactionDateBetween(adjustedStartDateTime,adjustedEndDateTime);
//    }
//    @GetMapping("/sellItemMonthly")
//    public List<SellItem> sellItemListMonthly(
//            @RequestParam int selectedMonth,
//            @RequestParam int selectedYear) {
//        // Set the start date to the first day of the selected month at 9 AM
//        LocalDateTime startTimestamp = LocalDateTime.of(selectedYear, selectedMonth, 1, 9, 0);
//        // Set the end date to the first day of the next month at 9 AM
//        LocalDateTime endTimestamp = LocalDateTime.of(selectedYear, selectedMonth, 1, 9, 0).plusMonths(1);
//        return this.sellItemRepository.findAllByTransactionDateBetween(startTimestamp, endTimestamp);
//    }
//    @GetMapping("/sellItemYearly")
//    public List<SellItem> sellItemListYearly(
//            @RequestParam int selectedYear) {
//        LocalDateTime startTimestamp = LocalDate.of(selectedYear, 1, 1).atTime(9, 0); // 7 am of the first day of the year
//        LocalDateTime endTimestamp = LocalDate.of(selectedYear + 1, 1, 1).atTime(9, 0); // 7 am of the first day of next year
//        return this.sellItemRepository.findAllByTransactionDateBetween(startTimestamp,endTimestamp);
//    }

        @GetMapping("/sales")
    public Map<String, Integer> getEmployeeSales() {
        System.out.println("Sales");
        List<Employee> employees = employeeRepository.findAll();
        Map<String, Integer> employeeSalesMap = new HashMap<>();

        for (Employee employee : employees) {
            int totalSales = calculateEmployeeSales(employee);
            employeeSalesMap.put(employee.getName(), totalSales);
        }

        return employeeSalesMap;
    }

    private int calculateEmployeeSales(Employee employee) {
        List<SellItem> sellItems = sellItemRepository.findAllByEmployee(employee);
        int totalSales = 0;
        for (SellItem sellItem : sellItems) {
            totalSales += (sellItem.getProductDetails().getProduct().getSellingPrice() - sellItem.getDiscount());
        }
        return totalSales;
    }

    @GetMapping("/low-stock")
    public List<LowStockProductDTO> getLowStockProductDetails() {
        List<ProductDetails> lowStockProducts = productDetailsService.getLowStockProductDetails();

        // Aggregate product details by product ID and color
        Map<Long, Map<String, Integer>> aggregatedProducts = lowStockProducts.stream()
                .collect(Collectors.groupingBy(productDetails ->
                                productDetails.getProduct().getId(), // Group by product ID
                        Collectors.groupingBy(ProductDetails::getColor, // Then group by color
                                Collectors.summingInt(ProductDetails::getQuantity)))); // Sum the quantities

        // Map the aggregated products to DTOs
        return aggregatedProducts.entrySet().stream()
                .flatMap(productIdEntry -> productIdEntry.getValue().entrySet().stream()
                        .map(colorEntry -> new LowStockProductDTO(
                                productIdEntry.getKey(), // Product ID
                                colorEntry.getKey(), // Color
                                colorEntry.getValue() // Quantity
                        )))
                .collect(Collectors.toList());
    }
}
