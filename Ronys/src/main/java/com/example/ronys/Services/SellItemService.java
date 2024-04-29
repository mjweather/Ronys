package com.example.ronys.Services;

import com.example.ronys.Model.SellItem;
import com.example.ronys.Repository.SellItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SellItemService {
    private final SellItemRepository sellItemRepository;

    @Autowired
    public SellItemService(SellItemRepository sellItemRepository) {
        this.sellItemRepository = sellItemRepository;
    }

    public SellItem addSell(SellItem sellItem) throws DataIntegrityViolationException {
        return sellItemRepository.save(sellItem);
    }
}
