package com.inetum.warehouse.controller;

import com.inetum.warehouse.exception.EmptyObjectException;
import com.inetum.warehouse.model.AbstractPurchase;
import com.inetum.warehouse.service.PurchaseService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
class PurchaseControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PurchaseService purchaseService;

    private MockMvc mockMvc;

    private static final String URL = "/purchase";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
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
                Arguments.of("                    "),
                Arguments.of("{\"1\": 50, \"2\":  a}"),
                Arguments.of("{\"1\": 50, \"2\":   }"));
    }

    @Test
    void create_giveEmptyMapInJson_returnsStatus400() throws Exception {
        //given
        when(purchaseService.validateOrder(any())).thenThrow(EmptyObjectException.class);

        String givenJson = "{}";
        //when
        String responseAsString1 = mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(givenJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(responseAsString1).contains("Min one product in order!");
    }

    @Test
    void create_giveCorrectMapInJson_returnsStatus200() throws Exception {
        //given
        Map<String, Long> testMap = new HashMap<>();
        testMap.put("1", 50L);

        when(purchaseService.validateOrder(testMap)).thenReturn(any(AbstractPurchase.class));

        String givenJson = "{\"1\": 50, \"2\":  50}";

        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(givenJson))
                .andExpect(status().isCreated());
    }
}