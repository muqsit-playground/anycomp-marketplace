package com.anycomp.controller;

import com.anycomp.MarketplaceMapper;
import com.anycomp.entity.Item;
import com.anycomp.entity.ItemRepository;
import com.anycomp.entity.SellerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ItemController{

	final private ItemRepository items;
	final private SellerRepository sellers;
	final private MarketplaceMapper map;

	@Operation(summary = "List all items", description = "Returns paginated list of all items in the marketplace.")
	@ApiResponse(responseCode = "200", description = "Items retrieved successfully")
	@GetMapping("/items")
	public PagedModel<EntityModel<Item.DTO>> all(
		@Parameter(hidden = true) Pageable pageable,
		PagedResourcesAssembler<Item.DTO> pagedAssembler
	){
		var page = items.findAll(pageable).map(map::toDto);
		return pagedAssembler.toModel(page);
	}

	@Operation(summary = "Get an item by ID", description = "Returns a single item if it exists.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Item found"),
		@ApiResponse(responseCode = "404", description = "Item not found")
	})
	@GetMapping("/items/{id}")
	public Item.DTO get(@Parameter(description = "Item ID", example = "1") @PathVariable("id") Long id){
		var item = items.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
		return map.toDto(item);
	}

	@Operation(summary = "List items for a seller", description = "Returns paginated list of items sold by a specific seller.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Items listed"),
		@ApiResponse(responseCode = "404", description = "Seller not found")
	})
	@GetMapping("/sellers/{sellerId}/items")
	public Page<Item.DTO> bySeller(@PathVariable("sellerId") Long sellerId, Pageable pageable){
		sellers.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		return items.findBySellerId(sellerId, pageable).map(map::toDto);
	}

	@Operation(summary = "Add new item to seller", description = "Creates a new item under the specified seller.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Item created"),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "404", description = "Seller not found")
	})
	@PostMapping("/sellers/{sellerId}/items")
	public Item.DTO addToSeller(@Parameter(description = "Seller ID", example = "1") @PathVariable("sellerId") Long sellerId, @RequestBody @Valid Item.DTO dto){
		var seller = sellers.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		var entity = map.toEntity(dto);
		entity.setSeller(seller);
		return map.toDto(items.save(entity));
	}

	@Operation(summary = "Update an item", description = "Modifies an existing item's details.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Item updated"),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "404", description = "Item not found")
	})
	@PutMapping("/items/{id}")
	public Item.DTO update(@Parameter(description = "Item ID", example = "1") @PathVariable("id") Long id, @RequestBody @Valid Item.DTO dto){
		var item = items.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
		item.setName(dto.name());
		item.setDescription(dto.description());
		item.setPrice(dto.price());
		item.setQuantity(dto.quantity());
		return map.toDto(items.save(item));
	}

	@Operation(summary = "Delete an item", description = "Removes an item from the marketplace.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Item deleted"),
		@ApiResponse(responseCode = "404", description = "Item not found")
	})
	@DeleteMapping("/items/{id}")
	public void delete(@Parameter(description = "Item ID", example = "1") @PathVariable("id") Long id){
		items.deleteById(id);
	}
}
