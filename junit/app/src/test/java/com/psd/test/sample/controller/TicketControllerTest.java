package com.psd.test.sample.controller;

import com.google.gson.Gson;
import com.psd.test.sample.model.request.TicketBuyRequest;
import com.psd.test.sample.model.result.ServerResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * TicketControllerTest
 *
 * @author Created by gold on 2021/4/16 17:17
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerTest {

    @Autowired
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBuyTicket() throws Exception {
        TicketBuyRequest request = new TicketBuyRequest();
        request.setUserId(1000L);
        request.setTicketId(1L);
        request.setNumber(1);

        String content = exec(post("/v1/ticket/buy", request));

        ServerResult<Long> result = fromJson(content, ServerResult.class, Long.class);

        assertThat(result.getResult())
                .isNotNull()
                .isGreaterThan(0L);
    }

    private MockHttpServletRequestBuilder post(String uri, Object request) {
        return MockMvcRequestBuilders.post(uri)
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private String exec(RequestBuilder builder) throws Exception {
        return mockMvc.perform(builder)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private <T> T fromJson(String json, Class<?> parametrized, Class<?>... parameterClasses) {
        return gson.fromJson(json, new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return parameterClasses;
            }

            @Override
            public Type getRawType() {
                return parametrized;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        });
    }

    private String toJson(Object value) {
        return gson.toJson(value);
    }
}
