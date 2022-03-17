package com.inetum.warehouse.controller;

import com.inetum.warehouse.model.Product;
import com.inetum.warehouse.service.ProductService;
import com.inetum.warehouse.utils.TestJsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
class ProductControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    protected ProductService productService;

    private MockMvc mockMvc;

    private static final String URL = "/product";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void create_correctProduct_returnsStatus201() throws Exception {
        //given
        String givenJson = "{\"name\": \"ABC\" ,\"description\": \"ABC\"}";

        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(givenJson))
                .andExpect(status().isCreated());
    }

    @Test
    void create_emptyProduct_returnsStatus400() throws Exception {
        //given
        List<String> listOfGivenJson = List.of(
                "{}",
                "{\"name\": \"ABC\" ,\"description\": \"\"   }",
                "{\"name\": \"\"   ,\"description\": \"ABC\"}",
                "{\"name\": \"\"    ,\"description\": \"\"   }"
        );

        //when
        String responseAsString1 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(0)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString1).contains("Values should not be empty!");

        String responseAsString2 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(1)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString2).contains("Values should not be empty!");

        String responseAsString3 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(2)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString3).contains("Values should not be empty!");

        String responseAsString4 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(3)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString4).contains("Values should not be empty!");
    }

    @Test
    void create_giveIncorrectJson_returnsStatus400() throws Exception {
        //given
        List<String> listOfGivenJson = List.of(
                "{\"name\": 1   ,\"description\":   }",
                "{\"name\":     ,\"description\": 1 }",
                "{\"name\":     ,\"description\":   }",
                "{\"name\": 'a' ,\"description\":   }"
        );
        //when
        String responseAsString1 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(0)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString1).contains("Give the correct values!");

        String responseAsString2 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(1)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString2).contains("Give the correct values!");

        String responseAsString3 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(2)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString3).contains("Give the correct values!");

        String responseAsString4 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(listOfGivenJson.get(3)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString4).contains("Give the correct values!");
    }

    @Test
    void update_productWithGivenIdNotFound_returnsStatus400() throws Exception {
        //given

        when(productService.update(anyLong(), any(Product.class)))
                .thenThrow(new EntityNotFoundException("Not found product with code: 1"));

        String todoAsJson = TestJsonUtils.convertObjectToJson(
                new Product(1L, "name", "desc"));
        //when
        mockMvc.perform(put(URL + "/" + 1)
                        .contentType(APPLICATION_JSON)
                        .content(todoAsJson))
                //then
                .andExpect(jsonPath("$", is("Not found product with code: 1")))
                .andExpect(status().isNotFound());
        verify(productService, times(1)).update(anyLong(), any(Product.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    void delete_productWithGivenIdNotFound_returnsStatus400() throws Exception {
        //given

        doThrow(EntityNotFoundException.class)
                .when(productService).delete(anyLong());

        //when
        mockMvc.perform(delete(URL + "/" + 1)
                        .contentType(APPLICATION_JSON)
                        .content(""))
                //then
                .andExpect(jsonPath("$", is("Not found product with code: 1")))
                .andExpect(status().isNotFound());
        verify(productService, times(1)).delete(anyLong());
        verifyNoMoreInteractions(productService);
    }

    @Test
    void delete_productWithGivenIdExist_returnsStatus204() throws Exception {
        //given
        doNothing().when(productService).delete(anyLong());
        //when
        mockMvc.perform(delete(URL + "/" + 1)
                        .contentType(APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent());
        verify(productService, times(1)).delete(anyLong());
        verifyNoMoreInteractions(productService);
    }


}