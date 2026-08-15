package com.learn.simple_product_service.mapper;

import org.springframework.stereotype.Component;

import com.learn.simple_product_service.entity.Product;
import com.learn.simple_product_service.model.ProductNewRequestDto;


@Component
public class ProductMapper {
	
	public Product productNewDtoToProductEntity(ProductNewRequestDto productNewRequest) {
		return Product.builder()
				.sku(productNewRequest.getSku())
				.productName(productNewRequest.getProductName())
				.productCategory(productNewRequest.getProductCategory())
				.qty(productNewRequest.getQty())
				.price(productNewRequest.getPrice())
				.fromApproval(productNewRequest.isFromApproval())
				.build();
	}
}
