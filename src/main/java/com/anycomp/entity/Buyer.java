package com.anycomp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="buyers")
public class Buyer{
	public record DTO(Long id, @NotBlank String name, @Email String email){}

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	@Column(unique = true) private String email;

	@OneToMany(mappedBy = "buyer")
	private List<Purchase> purchases = new ArrayList<>();
}
