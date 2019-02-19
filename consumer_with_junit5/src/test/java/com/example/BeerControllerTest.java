package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
//remove::start[]
// example of usage with fixed port
//@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-junit5:+:stubs:8090")
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-junit5")
//remove::end[]
@DirtiesContext
public class BeerControllerTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    BeerController beerController;

    //remove::start[]
    @StubRunnerPort("beer-api-producer-junit5")
    int producerPort;

    @BeforeEach
    public void setupPort() {
		this.beerController.port = this.producerPort;
    }

    //remove::end[]
    //tag::tests[]
    @Test
    public void should_give_me_a_beer_when_im_old_enough() throws Exception {
        //remove::start[]
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.json.write(new Person("marcin", 22)).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().string("THERE YOU GO"));
        //remove::end[]
    }

    @Test
    public void should_reject_a_beer_when_im_too_young() throws Exception {
        //remove::start[]
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.json.write(new Person("marcin", 17)).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().string("GET LOST"));
        //remove::end[]
    }
    //end::tests[]
}
