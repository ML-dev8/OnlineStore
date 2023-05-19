package com.company.tasks.online.store.services.impl;

import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.repositories.ProductRepository;
import com.company.tasks.online.store.services.ProductService;
import com.company.tasks.online.store.services.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;

    @Override
    public List<Product> getAllProducts() {
        return this.productRepo.findAll();
    }

    @Override
    public Product getProductById(int productId) throws EntityNotFoundException {
        return this.getProductEntityById(productId);
    }

    @Override
    public Product saveProduct(@NotNull final ProductDto productDto) {
        Product product = productDto.convertToProduct();
        return this.productRepo.save(product);
    }

    @Override
    public Product updateProduct(@NotNull final ProductDto productDto) throws EntityNotFoundException {
        Product productEntity = this.getProductEntityById(productDto.getId());
        productEntity.setName(productDto.getName());
        productEntity.setAmount(productDto.getAmount());
        productEntity.setPrice(productDto.getPrice());
        return this.productRepo.save(productEntity);
    }

    @Override
    @Transactional
    public void deleteProductById(int productId) throws DBValidationException, EntityNotFoundException {
        Product productEntity = this.getProductEntityById(productId);
        if (this.productRepo.isAssignedToAnyOrder(productId)) {
            throw new DBValidationException("Can't delete the entity as it is assigned to orders");
        }
        this.productRepo.deleteById(productEntity.getId());
    }

    private Product getProductEntityById(int productId) throws EntityNotFoundException {
        Optional<Product> productEntity = this.productRepo.findById(productId);
        if (productEntity.isEmpty()) {
            throw new EntityNotFoundException("Product not found");
        }
        return productEntity.get();
    }

}
