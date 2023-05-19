package com.company.tasks.online.store.ws.controllers;

import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.services.ProductService;
import com.company.tasks.online.store.services.dto.ProductDto;
import com.company.tasks.online.store.ws.dto.ErrorResponse;
import com.company.tasks.online.store.ws.dto.ProductCreateRequest;
import com.company.tasks.online.store.ws.dto.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * GET request which returns list of products
     *
     * @return list of {@link Product} products
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllProducts() {
        try {
            log.debug("Getting products from db");
            return ResponseEntity.ok(this.productService.getAllProducts());
        } catch (Exception ex) {
            log.warn("Error while getting products from db, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while getting products"));
        }
    }

    /**
     * GET request which returns product by id
     *
     * @param productId - prodcut id
     * @return {@link Product} product
     */
    @GetMapping(value = "/{product_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getProductById(@PathVariable("product_id") @Positive(message = "productId must be positive") int productId) {
        try {
            log.debug("Getting product by id={} from db", productId);
            return ResponseEntity.ok(this.productService.getProductById(productId));
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while processing request"));
        }
    }

    /**
     * Post request which creates product
     *
     * @param productCreateRequest - new product
     * @return created new {@link Product} product
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        try {
            log.debug("Creating new product");
            Product savedProduct = this.productService.saveProduct(productCreateRequest.convertToProductDto());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while creating product"));
        }
    }

    /**
     * PUT request which updates product
     *
     * @param productUpdateRequest - product update params
     * @return updated {@link Product} product
     */
    @PutMapping(value = "/{product_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable("product_id") @Positive(message = "productId must be positive") int productId,
                                           @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        try {
            log.debug("Updating product");
            ProductDto productDto = productUpdateRequest.convertToProductDto(productId);
            this.productService.updateProduct(productDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while creating product"));
        }
    }

    /**
     * Delete request to delete product
     *
     * @param productId - product id
     * @return Operation {@link HttpStatus} status
     */
    @DeleteMapping(value = "/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("product_id") @Positive(message = "productId must be positive") int productId) {
        try {
            log.debug("Deleting product");
            this.productService.deleteProductById(productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DBValidationException ex) {
            log.warn("Db validation failed, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
        } catch (EntityNotFoundException ex) {
            log.warn("Entity not found, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
        } catch (Exception ex) {
            log.warn("General error while processing request, " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error while deleting product"));
        }
    }
}
