package com.learn.simple_product_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3PresignedUrlResponseDTO {
	
	private String uploadUrl;

    private String objectKey;
}
