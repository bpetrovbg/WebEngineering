package com.fdiba.webeng;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fdiba.webeng.controllers.CreateAccountController;
import com.fdiba.webeng.controllers.InterestController;
import com.fdiba.webeng.controllers.LoginController;
import com.fdiba.webeng.controllers.MainController;
import com.fdiba.webeng.models.Interest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebengApplicationTests {
    @Autowired
    private CreateAccountController createAccountController;
    @Autowired
    private InterestController interestController;
    @Autowired
    private LoginController loginController;
    @Autowired
    private MainController mainController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() throws Exception {
        //check if all Controllers are created
        assertThat(createAccountController).isNotNull();
        assertThat(interestController).isNotNull();
        assertThat(loginController).isNotNull();
        assertThat(mainController).isNotNull();
    }

    @Test
    public void testInterestControllerStoresInterests() throws Exception {
        //prepare object
        Interest interest = new Interest();
        interest.setInterestname("TestInterest");
        //object -> JSON string
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = jacksonMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(interest);
        //test request
        this.mockMvc.perform(post("/interest/add").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(content().string("Interest is already saved!"));
    }

    @Test
    public void testLoginController() throws Exception {
        //pass username and password
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("username", Collections.singletonList("bpetrov"));
        body.put("password", Collections.singletonList("Password1"));
        //expect successful login if true
        this.mockMvc.perform(post("/login").params(body).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(view().name("redirect:/main"));
    }
}
