package com.anycomp.controller;

import com.anycomp.MarketplaceMapper;
import com.anycomp.entity.Seller;
import com.anycomp.entity.SellerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Operation(summary = "List all sellers", description = "Returns a paginated list of all registered sellers.")
	@ApiResponse(responseCode = "200", description = "Sellers listed successfully")
	@GetMapping
	public PagedModel<EntityModel<Seller.DTO>> all(@Parameter(hidden = true)Pageable pageable, PagedResourcesAssembler<Seller.DTO> pagedAssembler){
		var page = repo.findAll(pageable).map(map::toDto);
		return pagedAssembler.toModel(page);
	}

	@Operation(summary = "Get a seller by ID", description = "Fetches a seller by their unique ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Seller found"),
		@ApiResponse(responseCode = "404", description = "Seller not found")
	})
	@GetMapping("{id}")
	public Seller.DTO get(@Parameter(description = "Seller ID", example = "1") @PathVariable("id") Long id){
		var seller = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		return map.toDto(seller);
	}

	@Operation(summary = "Create a new seller", description = "Registers a new seller and returns the created entity.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Seller created"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@PostMapping
	public Seller.DTO create(@RequestBody @Valid Seller.DTO dto){
		var saved = repo.save(map.toEntity(dto));
		return map.toDto(saved);
	}

	@Operation(summary = "Update a seller", description = "Updates the name and email of an existing seller.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Seller updated"),
		@ApiResponse(responseCode = "404", description = "Seller not found"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@PutMapping("{id}")
	public Seller.DTO update(@Parameter(description = "Seller ID", example = "1") @PathVariable("id") Long id, @RequestBody @Valid Seller.DTO dto){
		var seller = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
		seller.setName(dto.name());
		seller.setEmail(dto.email());
		return map.toDto(repo.save(seller));
	}

	@Operation(summary = "Delete a seller", description = "Deletes the seller with the given ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Seller deleted"),
		@ApiResponse(responseCode = "404", description = "Seller not found")
	})
	@DeleteMapping("{id}")
	public void delete(@Parameter(description = "Seller ID", example = "1") @PathVariable("id") Long id){
		repo.deleteById(id);
	}
}
