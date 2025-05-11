package com.anycomp.controller;

import com.anycomp.MarketplaceService;
import com.anycomp.entity.Purchase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
public class PurchaseController{
	public record Request(Long buyerId, Long itemId, @Positive(message = "Quantity must be greater than 0") int quantity){}

	final private MarketplaceService service;

	@PostMapping("/purchase")
	@ResponseStatus(HttpStatus.CREATED)
	public Purchase.DTO purchase(@Valid @RequestBody Request req){
		return service.buyItem(req.buyerId(), req.itemId(), req.quantity());
	}
}
