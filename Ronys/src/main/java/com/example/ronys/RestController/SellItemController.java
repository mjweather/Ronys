package com.example.ronys.RestController;

import com.example.ronys.Model.SellItem;
import com.example.ronys.Model.Transaction;
import com.example.ronys.Repository.SellItemRepository;
import com.example.ronys.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SellItemController {

    private final SellItemRepository sellItemRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public SellItemController(SellItemRepository sellItemRepository, TransactionRepository transactionRepository) {
        this.sellItemRepository = sellItemRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/totalSellAndProfitToday")
    public Map<String, Integer> countSellAndProfitToday(
            @RequestParam(required = false) LocalDate selectedDate) {
        System.out.println("today");
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }
        LocalDateTime startTimestamp = selectedDate.atTime(9, 0); // 9 am of selected date
        LocalDateTime endTimestamp = selectedDate.plusDays(1).atTime(9, 0); // 9 am of next day
        return calculateTotalSellAndProfit(startTimestamp, endTimestamp);
    }

    @GetMapping("/totalSellAndProfitMonthly")
    public Map<String, Integer> countSellAndProfitMonthly(
            @RequestParam int selectedMonth,
            @RequestParam int selectedYear) {
        LocalDateTime startTimestamp = LocalDate.of(selectedYear, selectedMonth, 1).atTime(9, 0); // 9 am of the first day of the month
        LocalDateTime endTimestamp = LocalDate.of(selectedYear, selectedMonth, 1).plusMonths(1).atTime(9, 0); // 9 am of the first day of next month
        return calculateTotalSellAndProfit(startTimestamp, endTimestamp);
    }

    @GetMapping("/totalSellAndProfitYearly")
    public Map<String, Integer> countSellAndProfitYearly(
            @RequestParam int selectedYear) {
        LocalDateTime startTimestamp = LocalDate.of(selectedYear, 1, 1).atTime(9, 0); // 9 am of the first day of the year
        LocalDateTime endTimestamp = LocalDate.of(selectedYear + 1, 1, 1).atTime(9, 0); // 9 am of the first day of next year
        return calculateTotalSellAndProfit(startTimestamp, endTimestamp);
    }

    @GetMapping("/transactionsForSelectedPeriod")
    public Map<String, Integer> fetchTransactionsForSelectedPeriod(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        System.out.println("selected");
        LocalDateTime adjustedStartDateTime = startDate.atTime(9, 0); // Set the start time to 9 am
        LocalDateTime adjustedEndDateTime = endDate.atTime(9, 0); // Set the end time to 9 am of the next day

        return calculateTotalSellAndProfit(adjustedStartDateTime, adjustedEndDateTime);
    }

    private Map<String, Integer> calculateTotalSellAndProfit(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(startTimestamp, endTimestamp);

        int totalSell = 0;
        int totalProfit = 0;

        for (Transaction transaction : transactions) {
            totalSell += transaction.getTotalPrice();
            totalProfit += transaction.getTotalProfit();
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("totalSell", totalSell);
        result.put("totalProfit", totalProfit);

        return result;
    }

    @GetMapping("/sellItemDaily")
    public List<SellItem> getSellItemsDaily(@RequestParam(required = false) LocalDate selectedDate) {
        try {
            if (selectedDate == null) {
                selectedDate = LocalDate.now();
            }

            LocalDateTime startDateTime = LocalDateTime.of(selectedDate, LocalTime.of(9, 0)); // Start of the selected date (inclusive)
            LocalDateTime endDateTime = LocalDateTime.of(selectedDate.plusDays(1), LocalTime.of(23, 59, 59, 999999999)); // Until the end of the selected day (inclusive)

            System.out.println("Fetching sell items for date: " + selectedDate);
            System.out.println("Start DateTime: " + startDateTime);
            System.out.println("End DateTime: " + endDateTime);

            List<SellItem> sellItems = sellItemRepository.findAllByTransactionDateBetween(startDateTime, endDateTime);
            System.out.println("Number of sell items found: " + sellItems.size());

            return sellItems;
        } catch (Exception e) {
            System.err.println("Error fetching sell items for date: " + selectedDate);
            e.printStackTrace();
            return Collections.emptyList(); // Return empty list in case of error
        }
    }


    @GetMapping("/sellItemPeriod")
    public List<SellItem> getSellBetweenPeriod(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return Collections.emptyList(); // Handle invalid input
        }
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0)); // Start of the start date (inclusive)
        LocalDateTime endDateTime = LocalDateTime.of(endDate.plusDays(1), LocalTime.MAX); // Until the end of the next day (inclusive)
        List<Transaction> transactions = this.transactionRepository.findByDateBetween(startDateTime, endDateTime);
        List<SellItem> sellItems = new ArrayList<>();

        for (Transaction transaction : transactions) {
            List<SellItem> sellItemsForTransaction = transaction.getSaleItems();
            // Filtering sell items based on transaction ID
            List<SellItem> sellItemsMatchingTransactionId = sellItemsForTransaction.stream()
                    .filter(sellItem -> sellItem.getTransaction().getId().equals(transaction))
                    .collect(Collectors.toList());
            sellItems.addAll(sellItemsMatchingTransactionId);
        }
        return sellItemRepository.findAllByTransactionDateBetween(startDateTime, endDateTime);
    }

    @GetMapping("/sellItemMonthly")
    public List<SellItem> sellItemListMonthly(
            @RequestParam int selectedMonth,
            @RequestParam int selectedYear) {
        LocalDateTime startDateTime = LocalDateTime.of(selectedYear, selectedMonth, 1, 0, 0); // Start of the selected month (inclusive)
        LocalDateTime endDateTime = startDateTime.plusMonths(1); // Until the start of the next month (exclusive)
        return sellItemRepository.findAllByTransactionDateBetween(startDateTime, endDateTime);
    }

    @GetMapping("/sellItemYearly")
    public List<SellItem> sellItemListYearly(
            @RequestParam int selectedYear) {
        LocalDateTime startDateTime = LocalDateTime.of(selectedYear, 1, 1, 0, 0); // Start of the selected year (inclusive)
        LocalDateTime endDateTime = startDateTime.plusYears(1); // Until the start of the next year (exclusive)
        return sellItemRepository.findAllByTransactionDateBetween(startDateTime, endDateTime);
    }
}
