package com.learn.simple_product_service.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learn.simple_product_service.model.S3ProductRequestDTO;
import com.learn.simple_product_service.service.ProductService;
import com.learn.simple_product_service.service.S3StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/s3")
@Slf4j
@RequiredArgsConstructor
public class S3StorageController {
	
	private final S3StorageService s3StorageService;
	
	@PostMapping("/upload/{productId}")
    public ResponseEntity<String> uploadS3(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file) {

        try {

            String imageUrl =
                    s3StorageService.upload(
                            productId,
                            file
                    );

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {

            log.error(
                    "Failed to upload image for product {}",
                    productId,
                    e
            );

            throw new RuntimeException(
                    "Failed to upload image: " + e.getMessage(),
                    e
            );
        }
    }
	
	@GetMapping("/preview")
    public ResponseEntity<Void> previewImage(
            @RequestParam("url") String imageUrl) {

        String presignedUrl =
                s3StorageService.generatePreviewUrl(imageUrl);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(presignedUrl))
                .build();
    }

    @GetMapping("/download")
    public ResponseEntity<Void> downloadImage(
            @RequestParam("url") String imageUrl) {

        String presignedUrl =
                s3StorageService.generateDownloadUrl(imageUrl);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(presignedUrl))
                .build();
    }
}
