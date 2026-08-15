package com.learn.simple_product_service.batch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.learn.simple_product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("batch")
@RequiredArgsConstructor
public class ProductBatchProcessor implements CommandLineRunner{
	
	private final ProductRepository productRepository;

	@Override
	public void run(String... args) throws Exception {
		log.info("==========================================");
        log.info("Starting Product Batch Processing");
        log.info("==========================================");

        var products = productRepository.findAll();

        log.info("Found {} products", products.size());

        for (var product : products) {

            log.info(
                "Processing product: id={}, name={}",
                product.getId(),
                product.getProductName()
            );

            // Simulate some processing
            log.info("Product {} processed successfully", product.getId());
        }

        log.info("==========================================");
        log.info("Product Batch Processing COMPLETED");
        log.info("==========================================");
	}
	
}
