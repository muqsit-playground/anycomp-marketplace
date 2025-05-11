package com.anycomp.controller;

import com.anycomp.MarketplaceMapper;
import com.anycomp.entity.Item;
import com.anycomp.entity.ItemRepository;
import com.anycomp.entity.SellerRepository;
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

	@GetMapping("/items")
	public PagedModel<EntityModel<Item.DTO>> all(Pageable pageable, PagedResourcesAssembler<Item.DTO> pagedAssembler){
		var page = items.findAll(pageable).map(map::toDto);
		return pagedAssembler.toModel(page);
	}

	@GetMapping("/items/{id}")
	public Item.DTO get(@PathVariable("id") Long id){
		var item = items.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
		return map.toDto(item);
	}

	@GetMapping("/sellers/{sellerId}/items")
	public Page<Item.DTO> bySeller(@PathVariable("sellerId") Long sellerId, Pageable pageable){
		sellers.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		return items.findBySellerId(sellerId, pageable).map(map::toDto);
	}

	@PostMapping("/sellers/{sellerId}/items")
	public Item.DTO addToSeller(@PathVariable("sellerId") Long sellerId, @RequestBody @Valid Item.DTO dto){
		var seller = sellers.findById(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		var entity = map.toEntity(dto);
		entity.setSeller(seller);
		return map.toDto(items.save(entity));
	}

	@PutMapping("/items/{id}")
	public Item.DTO update(@PathVariable("id") Long id, @RequestBody @Valid Item.DTO dto){
		var item = items.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
		item.setName(dto.name());
		item.setDescription(dto.description());
		item.setPrice(dto.price());
		item.setQuantity(dto.quantity());
		return map.toDto(items.save(item));
	}

	@DeleteMapping("/items/{id}")
	public void delete(@PathVariable("id") Long id){
		items.deleteById(id);
	}
}
