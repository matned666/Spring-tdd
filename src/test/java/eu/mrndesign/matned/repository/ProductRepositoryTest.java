package eu.mrndesign.matned.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.mrndesign.matned.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() throws IOException {
        File dataJson = Paths.get("src", "test", "resources","products.json").toFile();
        Product[] prods = new ObjectMapper().readValue(dataJson, Product[].class);

        Arrays.stream(prods).forEach(productRepository::save);
    }

    @Test
    @DisplayName("Testing if not existing product is null")
    public  void  testProductNotFoundIsNull() throws IOException {
        //given

        //when
        Product obtainedProduct = productRepository.findProductById(10000L);
        //then
        Assertions.assertNull(obtainedProduct);
    }

    @Test
    @DisplayName("Testing if existing product is ok")
    public  void  testProductNotFoundIsCorrect() throws IOException {
        //given

        //when
        Product obtainedProduct = productRepository.findProductById(1L);
        //then
        Assertions.assertNotNull(obtainedProduct);
    }

    @Test
    @DisplayName("Testing if prod added successfully")
    public  void  testProductCanBeAdded() throws IOException {
        //given
        Product expectedProduct = new Product("Name", "Description", 2, 1);
        //when
        Product savedProduct = productRepository.save(expectedProduct);
        //then
        Assertions.assertNotNull(savedProduct, "Product should be saved");
        Assertions.assertNotNull(savedProduct.getId(), "Product Id should NOT be null");
        Assertions.assertNotNull(savedProduct.getName() == expectedProduct.getName(), "Product Id should NOT be null");
    }

    @Test
    @DisplayName("Testing if prod updated successfully")
    public  void  testProductCanBeUpdated() throws IOException {
        //given

        //when

        //then

    }

}