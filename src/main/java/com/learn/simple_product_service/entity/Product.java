package com.learn.simple_product_service.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tbl_product")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@org.hibernate.annotations.UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "sku", unique = true, nullable = false)
	private String sku;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "product_category", nullable = false)
	private String productCategory;
	
	@Builder.Default
	@Column(name = "qty", nullable = false)
	private int qty = 0;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Builder.Default
	@Column(name = "from_approval", nullable = false)
	private boolean fromApproval = false;
}
