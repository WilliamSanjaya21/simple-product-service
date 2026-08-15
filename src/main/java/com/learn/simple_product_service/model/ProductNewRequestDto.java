package com.learn.simple_product_service.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductNewRequestDto {
	
	private String sku;
	private String productName;
	private String productCategory;
	private int qty;
	private BigDecimal price;
	private boolean fromApproval;
}
