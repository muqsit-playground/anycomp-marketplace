package com.anycomp.controller;

import com.anycomp.MarketplaceMapper;
import com.anycomp.entity.Seller;
import com.anycomp.entity.SellerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController{

	final private SellerRepository repo;
	final private MarketplaceMapper map;

	@GetMapping
	public PagedModel<EntityModel<Seller.DTO>> all(Pageable pageable, PagedResourcesAssembler<Seller.DTO> pagedAssembler){
		var page = repo.findAll(pageable).map(map::toDto);
		return pagedAssembler.toModel(page);
	}

	@GetMapping("{id}")
	public Seller.DTO get(@PathVariable("id") Long id){
		var seller = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		return map.toDto(seller);
	}

	@PostMapping
	public Seller.DTO create(@RequestBody @Valid Seller.DTO dto){
		var saved = repo.save(map.toEntity(dto));
		return map.toDto(saved);
	}

	@PutMapping("{id}")
	public Seller.DTO update(@PathVariable("id") Long id, @RequestBody @Valid Seller.DTO dto){
		var seller = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		seller.setName(dto.name());
		seller.setEmail(dto.email());
		return map.toDto(repo.save(seller));
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id){
		repo.deleteById(id);
	}
}
