package com.anycomp;

import com.anycomp.entity.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class MarketplaceService{

	final private BuyerRepository buyers;
	final private ItemRepository items;
	final private PurchaseRepository purchases;
	final private MarketplaceMapper map;

	public Purchase.DTO buyItem(long buyerId, long itemId, int quantity){
		var buyer = buyers.findById(buyerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found"));
		var item = items.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
		if(item.getQuantity() < quantity) throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock");
		item.setQuantity(item.getQuantity() - quantity);
		var purchase = purchases.save(new Purchase(null, buyer, item, quantity, Instant.now()));
		return map.toDto(purchase);
	}
}
