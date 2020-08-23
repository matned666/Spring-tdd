package eu.mrndesign.matned.controller;

import eu.mrndesign.matned.model.Product;
import eu.mrndesign.matned.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// JsonPath - wyszukiwanie w Jsonach =
//{
//name = xyz, desc=xcv
//}
//jsonPath == $.name   -  $ to jest root

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /product/1 test - product found 200")
    public void testGETProdFnd() throws Exception {

        Product mockedProduct = new Product(1L, "Name", "Desc", 2, 1);
        doReturn(mockedProduct).when(productService).findById(mockedProduct.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", mockedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("GET /product/1 test - products found 200")
    public void testGETProdsFnd() throws Exception {

        Product mockedProduct1 = new Product(1L, "Name1", "Desc", 2, 1);
        Product mockedProduct2 = new Product(2L, "Name2", "Desc", 2, 1);
        Product mockedProduct3 = new Product(3L, "Name3", "Desc", 2, 1);
        doReturn(Arrays.asList(mockedProduct1, mockedProduct2, mockedProduct3)).when(productService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/product" ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));

    }

}