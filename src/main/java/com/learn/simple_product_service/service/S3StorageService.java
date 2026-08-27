package com.learn.simple_product_service.service;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learn.simple_product_service.entity.Product;
import com.learn.simple_product_service.repository.ProductRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class S3StorageService {
	
	private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    private final String bucketName;
    private final Duration presignedUrlDuration;
    private final ProductRepository productRepository;
    
    
    public S3StorageService(
            S3Client s3Client,
            S3Presigner s3Presigner,
            ProductRepository productRepository,
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.s3.presigned-url-duration}") Duration presignedUrlDuration) {

        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.productRepository = productRepository;
        this.presignedUrlDuration = presignedUrlDuration;
    }
    
    public String upload(
            UUID productId,
            MultipartFile file) throws Exception {

        // 1. Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + productId
                        )
                );

        // 2. Generate S3 object key
        String folder = "products/" + productId + "/images";

        String objectKey = buildObjectKey(
                folder,
                file.getOriginalFilename()
        );

        // 3. Upload file to S3
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(file.getBytes())
        );

        // 4. Build stable S3 object URL
        String imageUrl =
                "https://" +
                bucketName +
                ".s3." +
                s3Client.serviceClientConfiguration()
                        .region()
                        .id() +
                ".amazonaws.com/" +
                objectKey;

        // 5. Add image URL to Product
        product.getImageUrls().add(imageUrl);

        // 6. Save Product
        productRepository.save(product);

        // 7. Return URL
        return imageUrl;
    }
    
    public String generatePreviewUrl(String imageUrl) {

        String objectKey = extractObjectKey(imageUrl);

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(presignedUrlDuration)
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }
    
    private String extractObjectKey(String imageUrl) {

        try {

            URI uri = URI.create(imageUrl);

            String objectKey = uri.getPath();

            if (objectKey.startsWith("/")) {
                objectKey = objectKey.substring(1);
            }

            return URLDecoder.decode(
                    objectKey,
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid S3 image URL: " + imageUrl,
                    e
            );
        }
    }
    
    public String generateDownloadUrl(String imageUrl) {

        String objectKey = extractObjectKey(imageUrl);

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .responseContentDisposition(
                                "attachment; filename=\"product-image\""
                        )
                        .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(presignedUrlDuration)
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }
    
    public void deleteImage(UUID productId, String imageUrl) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + productId
                        )
                );

        // Make sure this image actually belongs to this product
        if (!product.getImageUrls().contains(imageUrl)) {
            throw new RuntimeException(
                    "Image does not belong to product: " + productId
            );
        }

        // Convert S3 URL -> object key
        String objectKey = extractObjectKey(imageUrl);

        // Delete from S3
        DeleteObjectRequest deleteRequest =
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();

        s3Client.deleteObject(deleteRequest);

        // Remove URL from database
        product.getImageUrls().remove(imageUrl);

        productRepository.save(product);
    }

    private String buildObjectKey(
            String folder,
            String originalFilename) {

        String extension = "";

        if (originalFilename != null &&
                originalFilename.contains(".")) {

            extension =
                    originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );
        }

        return folder + "/" +
                UUID.randomUUID() +
                extension;
    }
}
