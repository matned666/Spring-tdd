package eu.mrndesign.matned.integreation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import eu.mrndesign.matned.DBTestUtil;
import eu.mrndesign.matned.model.Product;
import eu.mrndesign.matned.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  //  <-------  could be used, but it's slow as hell
@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private Long id;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        DBTestUtil.resetAutoIncrementColumns(applicationContext, "product");
        File dataJson = Paths.get("src", "test", "resources", "products.json").toFile();
        Product[] products = new ObjectMapper().readValue(dataJson, Product[].class);
        Arrays.stream(products).forEach(productRepository::save);
        List<Product> list = (List<Product>) productRepository.findAll();
        id = list.get(list.size()-1).getId();
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }


    @WithMockUser(value = "admin")
    @Test
    @DisplayName("GET Test product found - GET /product/1")
    public void testGETProductFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }


    @WithMockUser(value = "admin")
    @Test
    @DisplayName("GET Test product NOT found - GET /product/100")
    public void testGETProductNOTFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 100))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(value = "admin")
    @Test
    @DisplayName("POST /product test - posted product status 202")
    public void testPostProductOk() throws Exception {
        Product mockedProduct = new Product(1L, "Name12", "Dsc", 2, 2);
        productRepository.save(mockedProduct);
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
    @DisplayName("Test products found ")
    public void testGetProdFnd() {
        RestTemplate restTemplate = new RestTemplate();
        WireMockServer wireMockServer = new WireMockServer(9090);
        wireMockServer.start();
        // configure response stub
        wireMockServer.stubFor(get(urlEqualTo("/product"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"First Product\",\n" +
                                "  \"description\": \"First Product Description\",\n" +
                                "  \"quantity\": 8,\n" +
                                "  \"version\": 1\n" +
                                "}")));
//        wireMockServer.stubFor(get(urlEqualTo("/product/2"))
//                .willReturn(aResponse().withStatus(404)));
//        wireMockServer.stubFor(post("/product")
//                // Actual Header sent by the RestTemplate is: application/json;charset=UTF-8
//                .withHeader("Content-Type", containing("application/json"))
//                .withRequestBody(containing("\"id\":3"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(200)
//                        .withBodyFile("json/supply-response-after-post.json")));
        Product product = restTemplate.getForObject("http://localhost:9090/product", Product.class);
        assertEquals(product.getId(), 1);
        wireMockServer.stop();
    }


    @WithMockUser(value = "admin")
    @Test
    @DisplayName("DELETE /product/1 test - products found 200")
    public void testDeleteProd() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/product/2"))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("DELETE /product/1 test - product not found 404")
    public void testDeleteNotExistingProd() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/product/566"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("PUT /product/1 test - product found 200")
    public void testPutProd() throws Exception {
        Product mockedProduct1 = new Product("TestName", "updated description", 20, 1);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/product/{id}", 1)
                .content(asJsonString(mockedProduct1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("TestName")))
                .andExpect(jsonPath("$.description", is("updated description")))
                .andExpect(jsonPath("$.quantity", is(20)));
    }

    @WithMockUser(value = "admin")
    @Test
    @DisplayName("PUT /product/100 test - product not found 404")
    public void testPutNotFoundProd() throws Exception {
        Product mockedProduct1 = new Product();
        mockMvc.perform(MockMvcRequestBuilders
                .put("/product/{id}", 100)
                .content(asJsonString(mockedProduct1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}