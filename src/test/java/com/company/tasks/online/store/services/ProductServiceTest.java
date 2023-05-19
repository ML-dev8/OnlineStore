package com.company.tasks.online.store.services;

import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.helpers.TestDataProvider;
import com.company.tasks.online.store.repositories.ProductRepository;
import com.company.tasks.online.store.services.dto.ProductDto;
import com.company.tasks.online.store.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepo;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepo);
    }

    @Test
    void getAllProducts_when_repoReturnsAll() {
        //Given
        List<Product> expectedProducts = TestDataProvider.getProducts(1).stream().collect(Collectors.toList());

        //when
        when(productRepo.findAll()).thenReturn(expectedProducts);
        List<Product> actualProducts = productService.getAllProducts();

        //then
        verify(productRepo, times(1)).findAll();
        assertTrue(Objects.equals(expectedProducts, actualProducts));
    }

    @Test
    void saveProduct_when_newProductProvided() {
        //Given
        ProductDto productCreateDto = TestDataProvider.getProductDto();
        Product expectedProduct = productCreateDto.convertToProduct();

        //when
        when(productRepo.save(any(Product.class))).thenReturn(expectedProduct);
        Product actualProduct = productService.saveProduct(productCreateDto);

        //then
        verify(productRepo, times(1)).save(any(Product.class));
        assertTrue(Objects.equals(expectedProduct, actualProduct));
    }

    @Test
    void saveProduct_when_newProductIsNull() {
        //Given
        ProductDto productDto = null;

        //then
        assertThrows(NullPointerException.class, () -> productService.saveProduct(productDto));
    }

    @Test
    void updateProduct_when_productNotFound() {
        //when
        when(productRepo.findById(anyInt())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(mock(ProductDto.class)));
        verify(productRepo, times(1)).findById(anyInt());
    }

    @Test
    void updateProduct_when_productIsNull() {
        //then
        assertThrows(NullPointerException.class, () -> productService.updateProduct(null));
    }


    @Test
    void updateProduct_when_productProvided() throws EntityNotFoundException {
        //Given
        int productId = 1;
        ProductDto productUpdateDto = TestDataProvider.getProductDto(productId);
        Product productToUpdate = TestDataProvider.getProduct(productId);
        Product expectedUpdatedProduct = productUpdateDto.convertToProduct();

        //when
        when(productRepo.findById(productId)).thenReturn(Optional.of(productToUpdate));
        when(productRepo.save(productToUpdate)).thenReturn(expectedUpdatedProduct);
        Product actualUpdatedProduct = productService.updateProduct(productUpdateDto);

        //then
        assertEquals(expectedUpdatedProduct, actualUpdatedProduct);
        verify(productRepo, times(1)).findById(anyInt());
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProductById_when_productNotFound() {
        //when
        when(productRepo.findById(anyInt())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> productService.deleteProductById(anyInt()));
        verify(productRepo, times(1)).findById(anyInt());
    }

    @Test
    void deleteProductById_when_productAssignedToAnyOrder() {
        //when
        when(productRepo.findById(anyInt())).thenReturn(Optional.of(mock(Product.class)));
        when(productRepo.isAssignedToAnyOrder(anyInt())).thenReturn(true);

        //then
        assertThrows(DBValidationException.class, () -> productService.deleteProductById(anyInt()));
        verify(productRepo, times(1)).findById(anyInt());
        verify(productRepo, times(1)).isAssignedToAnyOrder(anyInt());
    }

    @Test
    void deleteProductById_when_productNotAssignedToAnyOrder() throws DBValidationException, EntityNotFoundException {
        //given
        Product product = TestDataProvider.getProduct(1);

        //when
        when(productRepo.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepo.isAssignedToAnyOrder(product.getId())).thenReturn(false);
        doNothing().when(productRepo).deleteById(product.getId());
        productService.deleteProductById(product.getId());

        //then
        verify(productRepo, times(1)).findById(anyInt());
        verify(productRepo, times(1)).isAssignedToAnyOrder(anyInt());
        verify(productRepo, times(1)).deleteById(anyInt());
    }
}