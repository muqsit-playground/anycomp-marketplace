package com.anycomp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller{
	public record DTO(Long id, @NotBlank String name, @Email String email){}

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@Column(unique = true)
	private String email;

	@OneToMany(mappedBy = "seller")
	private List<Item> items = new ArrayList<>();
}
