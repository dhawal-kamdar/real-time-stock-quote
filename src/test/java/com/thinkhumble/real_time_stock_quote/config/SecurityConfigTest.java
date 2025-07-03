package com.thinkhumble.real_time_stock_quote.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Test
    void whenAccessingApiWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/quote/AAPL"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAccessingApiWithAuth_thenOk() throws Exception {
        mockMvc.perform(get("/api/quote/AAPL")
                        .with(httpBasic(username, password)))
                .andExpect(status().isOk());
    }

    @Test
    void whenAccessingSwaggerWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAccessingSwaggerWithAuth_thenOk() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")
                        .with(httpBasic(username, password)))
                .andExpect(status().isOk());
    }

}