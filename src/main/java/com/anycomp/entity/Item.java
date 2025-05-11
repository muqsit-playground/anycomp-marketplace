package com.anycomp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
@Setter
@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item{
	public record DTO(Long id, @NotBlank String name, String description, @Positive double price, @Min(0) int quantity, Long sellerId){}

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String description;
	private double price;
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "seller_id")
	private Seller seller;
}
