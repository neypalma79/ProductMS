package com.angular.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product", catalog = "angular")
@Getter
@Setter
@ToString
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name="brand")
	private String brand;
	@Column(name="description")
	private String description;
	@Column(name="unit_price")
	private BigDecimal unitPrice;
	@Column(name="quantity")
	private Integer quantity;
	@Column(name="status")
	private Character status;
}
