package com.inetum.warehouse.controller;

import com.inetum.warehouse.exception.EmptyObjectException;
import com.inetum.warehouse.model.Inventory;
import com.inetum.warehouse.service.InventoryService;
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

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
class InventoryControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    protected InventoryService inventoryService;

    private MockMvc mockMvc;

    private static final String URL = "/inventory";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void addProductToInventory_emptyInventory_returnsStatus400() throws Exception {
        //given
        when(inventoryService.addProductWithAmountToInventory(any(Inventory.class))).thenThrow(EmptyObjectException.class);
        String givenJson = "{}";
        //when
        String responseAsString = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(givenJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString).contains("The new Product in inventory can not be empty!");
    }

    @ParameterizedTest
    @MethodSource("provideIncorrectJson")
    void addProductToInventory_giveIncorrectJson_returnsStatus400(String incorrectJson) throws Exception {
        //when
        String responseAsString = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(incorrectJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString)
                .contains("Give the correct values!");
    }

    private static Stream<Arguments> provideIncorrectJson() {
        return Stream.of(
                Arguments.of("{\"id\": 1,\"count\": }"),
                Arguments.of("{\"id\": ,\"count\": 1}"),
                Arguments.of("{\"id\": ,\"count\": }"),
                Arguments.of("{\"id\": 'a',\"count\": }"));
    }

    @Test
    void addProductToInventory_giveCorrectInventory_returnsStatus201() throws Exception {
        //given
        String givenJson = "{\"id\": 1,\"count\": 1}";
        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(givenJson))
                .andExpect(status().isCreated());
    }
}