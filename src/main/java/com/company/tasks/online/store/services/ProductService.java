package com.company.tasks.online.store.services;

import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.services.dto.ProductDto;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(int productId) throws EntityNotFoundException;

    Product saveProduct(@NotNull ProductDto productDto);

    Product updateProduct(@NotNull ProductDto productDto) throws EntityNotFoundException;

    void deleteProductById(int productId) throws DBValidationException, EntityNotFoundException;
}
