package com.learn.simple_product_service.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3ProductRequestDTO {
	
	private UUID productId;
	
	private String fileName;

    private String contentType;
}
