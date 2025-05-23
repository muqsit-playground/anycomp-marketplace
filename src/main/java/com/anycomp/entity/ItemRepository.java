package com.anycomp.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>{
	Page<Item> findBySellerId(Long sellerId, Pageable pageable);
}
