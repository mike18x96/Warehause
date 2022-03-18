package com.inetum.warehouse.controller;

import com.inetum.warehouse.model.Product;
import com.inetum.warehouse.service.ProductService;
import com.inetum.warehouse.utils.TestJsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("provideJsonOfEmptyValuesOfProduct")
    void create_givenEmptyValuesOfProduct_returnsStatus400(String jsonOfEmptyValues) throws Exception {
        //when
        String responseAsString1 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonOfEmptyValues))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString1).contains("Values should not be empty!");
    }

    private static Stream<Arguments> provideJsonOfEmptyValuesOfProduct() {
        return Stream.of(
                Arguments.of("{}"),
                Arguments.of("{\"name\": \"ABC\" ,\"description\": \"\"   }"),
                Arguments.of("{\"name\": \"\"   ,\"description\": \"ABC\"}"),
                Arguments.of("{\"name\": \"\"    ,\"description\": \"\"   }"));
    }

    @ParameterizedTest
    @MethodSource("provideIncorrectJson")
    void create_giveIncorrectJson_returnsStatus400(String incorrectJson) throws Exception {
        //when
        String responseAsString1 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(incorrectJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString1).contains("Give the correct values!");
    }

    private static Stream<Arguments> provideIncorrectJson() {
        return Stream.of(
                Arguments.of("{\"name\": 1   ,\"description\":   }"),
                Arguments.of("{\"name\":     ,\"description\": 1 }"),
                Arguments.of("{\"name\":     ,\"description\":   }"),
                Arguments.of("{\"name\": 'a' ,\"description\":   }"));
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