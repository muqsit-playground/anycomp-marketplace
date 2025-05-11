package com.anycomp.controller;

import com.anycomp.MarketplaceService;
import com.anycomp.entity.Purchase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class PurchaseController{
	public record Request(Long buyerId, Long itemId, @Positive(message = "Quantity must be greater than 0") int quantity){}

	final private MarketplaceService service;

	@Operation(
		summary = "Buyer purchases an item",
		description = "Checks stock availability, deducts quantity, and records the purchase."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Purchase created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input or insufficient stock"),
		@ApiResponse(responseCode = "404", description = "Buyer or item not found")
	})
	@PostMapping("/purchase")
	@ResponseStatus(HttpStatus.CREATED)
	public Purchase.DTO purchase(@Valid @RequestBody Request req){
		return service.buyItem(req.buyerId(), req.itemId(), req.quantity());
	}
}
