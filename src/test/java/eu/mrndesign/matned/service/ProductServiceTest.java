package eu.mrndesign.matned.service;

import eu.mrndesign.matned.model.Product;
import eu.mrndesign.matned.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("Finding one product by id")
    public void findOneProductById() {
//        Given
        Product mockProd = new Product(1L, "Name", "Description", 2, 1);
        doReturn(mockProd).when(productRepository).findProductById(1L);
//        When
        Product foundProd = productService.findById(1L);
//        Then
        assertNotNull(foundProd);
    }

    @Test
    @DisplayName("Finding all products")
    public void findAllProducts() {
//        Given
        Product mockProd1 = new Product(1L, "Name1", "Description1", 2, 1);
        Product mockProd2 = new Product(2L, "Name2", "Description2", 4, 1);
        Product mockProd3 = new Product(3L, "Name3", "Description3", 6, 2);
        doReturn(Arrays.asList(mockProd1, mockProd2, mockProd3)).when(productRepository).findAll();
//        When
        Iterable<Product> allProds = productService.findAll();
//        Then
        assertEquals(3, ((Collection<?>) allProds).size(), "Products size should be 2");
    }

    @Test
    @DisplayName("Save test")
    public void saveTest() {
        Product mockProd1 = new Product(1L, "Name1", "Description1", 2, 1);
        doReturn(mockProd1).when(productRepository).save(mockProd1);

        Product product = productService.save(mockProd1);

        assertEquals("Name1", product.getName(), "Should equal 'Name1'");

    }


    @Test
    @DisplayName("Save test status 200")
    public void deleteTest() {
        Product mockProd1 = new Product(1L, "Name1", "Description1", 2, 1);
        doReturn(mockProd1).when(productRepository).save(mockProd1);
        Product product = productService.save(mockProd1);
        Assertions.assertNotNull(product, "should NOT be null");
        productService.deleteById(1L);
        product = productService.findById(1L);
        assertNull(product, "Should be null");
    }


    @Test
    @DisplayName("Update test status 200")
    public void updateTest() {
        Product mockProd1 = new Product(1L, "Name1", "Description1", 2, 1);
        doReturn(mockProd1).when(productRepository).save(mockProd1);
        Product product = productService.save(mockProd1);
        Assertions.assertNotNull(product, "should NOT be null");
        Product productData = productService.findById(1L);
        product = productService.findById(1L);
        assertNull(product, "Should be null");
    }


    @Test
    @DisplayName("Update test status 404")
    public void updateTestProductNotFound() {
        Product mockProd1 = new Product(1L, "Name1", "Description1", 2, 1);
        doReturn(mockProd1).when(productRepository).save(mockProd1);
        Product product = productService.save(mockProd1);
        Assertions.assertNotNull(product, "should NOT be null");
        productService.deleteById(1L);
        product = productService.findById(1L);
        assertNull(product, "Should be null");
    }




}