package eu.mrndesign.matned.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.mrndesign.matned.model.Product;
import eu.mrndesign.matned.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @WithMockUser(value = "admin")
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

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("GET /product/100 test - product not found 404")
    public void testGETProdNOTFnd() throws Exception {

        doReturn(null).when(productService).findById(100);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 100))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("GET /product/1 test - products found 200")
    public void testGETProdsFnd() throws Exception {

        Product mockedProduct1 = new Product(1L, "Name1", "Desc", 2, 1);
        Product mockedProduct2 = new Product(2L, "Name2", "Desc", 2, 1);
        Product mockedProduct3 = new Product(3L, "Name3", "Desc", 2, 1);
        doReturn(Arrays.asList(mockedProduct1, mockedProduct2, mockedProduct3)).when(productService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[2].id", is(3)));

    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("POST /product test - posted product status 202")
    public void testPostProductOk() throws Exception {
        Product mockedProduct = new Product(1L, "Name12", "Dsc", 2, 2);
        doReturn(mockedProduct).when(productService).save(mockedProduct);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockedProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("DELETE /product/1 test - products found 200")
    public void testDeleteProd() throws Exception {
        doReturn(true).when(productService).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/product/{id}", 1))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("DELETE /product/122 test - product not found 404")
    public void testDeleteNotExistingProd() throws Exception {
        doReturn(false).when(productService).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/product/{id}", 1))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(value = "admin")
    @Test
    @DisplayName("PUT /product/1 test - product found 200")
    public void testPutProd() throws Exception {
        Product mockedProduct1 = new Product(1L, "Name1", "Desc", 2, 1);
        doReturn(mockedProduct1).when(productService).update(1L, mockedProduct1);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/product/{id}", 1L)
                .content(asJsonString(mockedProduct1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("PUT /product/1 test - product not found 404")
    public void testPutNotFoundProd() throws Exception {
        Product mockedProduct1 = new Product();
        doReturn(null).when(productService).update(1L, mockedProduct1);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/product/{id}", 1L)
                .content(asJsonString(mockedProduct1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}