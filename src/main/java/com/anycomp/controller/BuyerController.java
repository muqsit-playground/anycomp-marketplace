package com.anycomp.controller;

import com.anycomp.MarketplaceMapper;
import com.anycomp.entity.Buyer;
import com.anycomp.entity.BuyerRepository;
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
@RequestMapping("/buyers")
@RequiredArgsConstructor
public class BuyerController{

	final private BuyerRepository repo;
	final private MarketplaceMapper map;

	// GET /buyers?page=0&size=20&sort=name,asc
	@Operation(summary = "List all buyers", description = "Paginated and sortable list of buyers.")
	@ApiResponse(responseCode = "200", description = "Buyer list retrieved successfully")
	@GetMapping
	public PagedModel<EntityModel<Buyer.DTO>> all(
		@Parameter(hidden = true) Pageable pageable,
		PagedResourcesAssembler<Buyer.DTO> pagedAssembler
	){
		var page = repo.findAll(pageable).map(map::toDto);
		return pagedAssembler.toModel(page);
	}

	@Operation(summary = "Get a buyer by ID", description = "Returns a single buyer if found.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Buyer found"),
		@ApiResponse(responseCode = "404", description = "Buyer not found")
	})
	@GetMapping("{id}")
	public Buyer.DTO get(@Parameter(description = "Buyer ID", example = "1") @PathVariable("id") Long id){
		var buyer = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found"));
		return map.toDto(buyer);
	}

	@Operation(summary = "Create new buyer", description = "Creates and returns the created buyer.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Buyer created"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@PostMapping
	public Buyer.DTO create(@RequestBody @Valid Buyer.DTO dto){
		var saved = repo.save(map.toEntity(dto));
		return map.toDto(saved);
	}

	@Operation(summary = "Update a buyer", description = "Updates the name and email of an existing buyer.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Buyer updated"),
		@ApiResponse(responseCode = "404", description = "Buyer not found"),
		@ApiResponse(responseCode = "400", description = "Invalid input")
	})
	@PutMapping("{id}")
	public Buyer.DTO update(
		@Parameter(description = "Buyer ID", example = "1") @PathVariable("id") Long id,
		@RequestBody @Valid Buyer.DTO dto
	){
		var existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found"));
		existing.setName(dto.name());
		existing.setEmail(dto.email());
		return map.toDto(repo.save(existing));
	}

	@Operation(summary = "Delete a buyer", description = "Deletes a buyer by ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Buyer deleted"),
		@ApiResponse(responseCode = "404", description = "Buyer not found")
	})
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@Parameter(description = "Buyer ID", example = "1") @PathVariable("id") Long id){
		repo.deleteById(id);
	}
}

