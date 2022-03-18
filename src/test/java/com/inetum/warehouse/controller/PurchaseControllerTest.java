package com.inetum.warehouse.controller;

import com.inetum.warehouse.model.AbstractPurchase;
import com.inetum.warehouse.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    void create_giveIncorrectJson_returnsStatus400() throws Exception {
        //given
        List<String> listOfGivenJson = List.of(
                "                    ",
                "{\"1\": 50, \"2\":  a}",
                "{\"1\": 50, \"2\":   }",
                "{\" \": 50, \"2\": 50}"
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
    void create_giveEmptyMapInJson_returnsStatus400() throws Exception {
        //given
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