package com.anycomp;

import com.anycomp.entity.Buyer;
import com.anycomp.entity.Item;
import com.anycomp.entity.Purchase;
import com.anycomp.entity.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MarketplaceMapper{

	Buyer.DTO toDto(Buyer src);

	Buyer toEntity(Buyer.DTO src);

	Seller.DTO toDto(Seller src);

	Seller toEntity(Seller.DTO src);

	@Mapping(target = "sellerId", source = "seller.id")
	Item.DTO toDto(Item src);

	@Mapping(target = "seller.id", source = "sellerId")
	Item toEntity(Item.DTO src);

	@Mapping(target="buyerId", source="buyer.id")
	@Mapping(target="itemId", source="item.id")
	Purchase.DTO toDto(Purchase src);
}
