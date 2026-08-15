package com.learn.simple_product_service.service;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learn.simple_product_service.entity.Product;
import com.learn.simple_product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DummyDataInitializer {
	
	private final ProductRepository productRepository;
	
	@Bean
	public ApplicationRunner initializeDummyData() {
		return args -> {
			if (productRepository.count() == 0) {
                
                productRepository.save(Product.builder()
                        .sku("SKU-IPH15PRO")
                        .productName("iPhone 15 Pro Max")
                        .productCategory("Electronics")
                        .qty(10)
                        .price(new BigDecimal("19999000"))
                        .fromApproval(true)
                        .build());

                productRepository.save(Product.builder()
                        .sku("SKU-SAMG24U")
                        .productName("Samsung Galaxy S24 Ultra")
                        .productCategory("Electronics")
                        .qty(5)
                        .price(new BigDecimal("21499000"))
                        .build()); // qty default=0, fromApproval default=false

                productRepository.save(Product.builder()
                        .sku("SKU-MACM3AIR")
                        .productName("MacBook Air M3")
                        .productCategory("Electronics")
                        .qty(3)
                        .price(new BigDecimal("18500000"))
                        .fromApproval(true)
                        .build());

                productRepository.save(Product.builder()
                        .sku("SKU-SONYWH1K")
                        .productName("Sony WH-1000XM5")
                        .productCategory("Accessories")
                        .qty(15)
                        .price(new BigDecimal("4999000"))
                        .build());

                productRepository.save(Product.builder()
                        .sku("SKU-LOGIG502")
                        .productName("Logitech G502 X Plus")
                        .productCategory("Accessories")
                        .qty(25)
                        .price(new BigDecimal("2399000"))
                        .build());
            }
		};
	}
}
