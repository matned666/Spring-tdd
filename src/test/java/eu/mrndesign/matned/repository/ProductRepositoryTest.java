package eu.mrndesign.matned.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.mrndesign.matned.model.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    File DATA_JSON = Paths.get("src", "test", "resources", "products.json").toFile();
    @BeforeEach
    public void setup() throws IOException {
        // Deserialize products from JSON file to Product array
        Product[] products = new ObjectMapper().readValue(DATA_JSON, Product[].class);
        // Save each product to database
        Arrays.stream(products).forEach(productRepository::save);
    }

    @AfterEach
    public void cleanup(){
        // Cleanup the database after each test
        productRepository.deleteAll();
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
        List<Product> list = (List<Product>) productRepository.findAll();
        Long idToCheck = list.get(list.size()-1).getId();
        //when
        Product obtainedProduct = productRepository.findProductById(idToCheck);
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
        Assertions.assertEquals(expectedProduct.getName(), savedProduct.getName(), "Product names should equal");
    }

    @Test
    @DisplayName("Testing if prod updated successfully")
    public  void  testProductCanBeUpdated() throws IOException {
        //given
        Product productToUpdate = new Product(1L , "Updated product", "Description", 2, 1);
        //when
        Product updatedProduct = productRepository.save(productToUpdate);
        //then
        Assertions.assertEquals(productToUpdate.getName(), updatedProduct.getName(), "Should be the same names in both products");

    }

    @Test
    @DisplayName("Testing if finds all products")
    public  void  testProductsListFound() throws IOException {
        //given
        //when
        Iterable<Product> prodList = productRepository.findAll();
        //then
        Assertions.assertEquals(((Collection<?>)prodList).size(), 2, "size should be 3");
    }


    @Test
    @DisplayName("Testing delete product")
    public void testProductsDelete() throws IOException {
        //given
        Product product = productRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(product, "should NOT be null");
        productRepository.deleteById(1L);
        //when
        product = productRepository.findById(1L).orElse(null);
        //then
        Assertions.assertNull(product, "should be null");
    }



}