package com.anycomp.controller;

import com.anycomp.MarketplaceMapper;
import com.anycomp.entity.Buyer;
import com.anycomp.entity.BuyerRepository;
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
	@GetMapping
	public PagedModel<EntityModel<Buyer.DTO>> all(Pageable pageable, PagedResourcesAssembler<Buyer.DTO> pagedAssembler){
		var page = repo.findAll(pageable).map(map::toDto);
		return pagedAssembler.toModel(page);
	}

	@GetMapping("{id}")
	public Buyer.DTO get(@PathVariable("id") Long id){
		var buyer = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found"));
		return map.toDto(buyer);
	}

	@PostMapping
	public Buyer.DTO create(@RequestBody @Valid Buyer.DTO dto){
		var saved = repo.save(map.toEntity(dto));
		return map.toDto(saved);
	}

	@PutMapping("{id}")
	public Buyer.DTO update(@PathVariable("id") Long id, @RequestBody @Valid Buyer.DTO dto){
		var existing = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found"));
		existing.setName(dto.name());
		existing.setEmail(dto.email());
		return map.toDto(repo.save(existing));
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id){
		repo.deleteById(id);
	}
}

