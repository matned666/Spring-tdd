package eu.mrndesign.matned.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.mrndesign.matned.model.Product;
import org.junit.jupiter.api.Assertions;
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

    @Test
    @DisplayName("Testing if not existing product is null")
    public  void  testProductNotFoundIsNull() throws IOException {
        //given
        File dataJson = Paths.get("src", "test", "resources","products.json").toFile();
        Product[] prods = new ObjectMapper().readValue(dataJson, Product[].class);

        Arrays.stream(prods).forEach(productRepository::save);

        //when
        Product obtainedProduct = productRepository.findProductById(10000);
        //then
        Assertions.assertNull(obtainedProduct);
    }

    @Test
    @DisplayName("Testing if existing product is ok")
    public  void  testProductNotFoundIsCorrect() throws IOException {
        //given
        File dataJson = Paths.get("src", "test", "resources","products.json").toFile();
        Product[] prods = new ObjectMapper().readValue(dataJson, Product[].class);

        Arrays.stream(prods).forEach(productRepository::save);

        //when
        Product obtainedProduct = productRepository.findProductById(1);
        //then
        Assertions.assertNotNull(obtainedProduct);
    }

}