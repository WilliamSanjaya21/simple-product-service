package com.learn.simple_product_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.simple_product_service.entity.Product;
import com.learn.simple_product_service.model.ProductNewRequestDto;
import com.learn.simple_product_service.model.ProductNewResponseDto;
import com.learn.simple_product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@GetMapping("/all")
	public ResponseEntity<List<Product>> findAll() {
		return ResponseEntity.ok().body(productService.findAllProducts());
	}
	
	@GetMapping
	public ResponseEntity<Product> findBySku(@RequestParam("sku") String sku) {
		return ResponseEntity.ok().body(productService.findBySku(sku));
	}
	
	@PostMapping("/save-new")
	public ResponseEntity<ProductNewResponseDto> saveNewProduct(
			@RequestBody ProductNewRequestDto productRequestDto) {
		return ResponseEntity.ok().body(productService.saveNewProduct(productRequestDto));
	}
}
