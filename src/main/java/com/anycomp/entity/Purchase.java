package com.anycomp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="purchases")
public class Purchase{
	public record DTO(Long id, Long buyerId, Long itemId, int quantity, Instant purchaseDate){}

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name="buyer_id")
	private Buyer buyer;

	@ManyToOne
	@JoinColumn(name="item_id")
	private Item item;

	private int quantity;
	private Instant purchaseDate = Instant.now();
}
