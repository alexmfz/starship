package com.space.starship.Controller;

import com.space.starship.Model.Starship;
import com.space.starship.Service.StarshipRepository;
import com.space.starship.Service.StarshipService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SpringJUnitConfig
@AutoConfigureTestDatabase
public class StarshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    StarshipRepository starshipRepository_2;

    @InjectMocks
    private LoginController loginController;

    @MockBean
    @Autowired
    private StarshipService starshipService;

    @Test
    public void getStarshipsTestWhenNoLoginProvided() throws Exception {
        // Set the expected behaviour
        List<Starship> starshipServiceList = new ArrayList<>();
        Mockito.when(starshipService.getStarships(Mockito.anyString(), Mockito.anyString())).thenReturn(starshipServiceList);

        //Make http request >> will fail because there is no authorization
        MvcResult result = mockMvc.perform(get("/spacecrafts/starship/getStarships")
                .contextPath("/spacecrafts"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void getStarshipsTestWhenLoginProvided() throws Exception {
        // Call to the login to get the token
        String token = mockMvc.perform(post("/spacecrafts/auth/login")
                        .contextPath("/spacecrafts")
                .param("user", "ADMIN")
                .param("password", "ADMIN")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Set the expected behaviour
        List<Starship> starshipServiceList = new ArrayList<>();
        Mockito.when(starshipService.getStarships(Mockito.anyString(), Mockito.anyString())).thenReturn(starshipServiceList);

        //Make http request with auth
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        MvcResult result = mockMvc.perform(get("/spacecrafts/starship/getStarships")
                        .contextPath("/spacecrafts")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNo", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void getStarshipsByNameTestWhenLoginProvided() throws Exception {
        // Call to the login to get the token
        String token = mockMvc.perform(post("/spacecrafts/auth/login")
                        .contextPath("/spacecrafts")
                        .param("user", "ADMIN")
                        .param("password", "ADMIN")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Set the expected behaviour
        List<Starship> starshipServiceList = new ArrayList<>();
        Mockito.when(starshipService.getStarshipByName("Odissey")).thenReturn(starshipServiceList);

        //Make http request with auth
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        MvcResult result = mockMvc.perform(get("/spacecrafts/starship/getStarshipByName")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("Name", "Odissey")
                        .contextPath("/spacecrafts"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createStarshipTestWhenLoginProvided() throws Exception {
        // Call to the login to get the token
        String token = mockMvc.perform(post("/spacecrafts/auth/login")
                        .contextPath("/spacecrafts")
                        .param("user", "ADMIN")
                        .param("password", "ADMIN"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Set the expected behaviour
        String response="A new starship was successfully created!";
        Starship starship = new Starship(1L, "Mars", "Fire");
        String requestBody = "{\"id\": 1, \"name\": \"Odissey\", \"weapons\": \"Fire\"}";
        Mockito.when(starshipService.createStarship(starship)).thenReturn(response);

        //Make http request with auth
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        MvcResult result = mockMvc.perform(post("/spacecrafts/starship/createStarship")
                        .content(requestBody)
                        .contextPath("/spacecrafts")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void modifyStarshipByNameTestWhenLoginProvided() throws Exception {
        // Call to the login to get the token
        String token = mockMvc.perform(post("/spacecrafts/auth/login")
                        .contextPath("/spacecrafts")
                        .param("user", "ADMIN")
                        .param("password", "ADMIN")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Set the expected behaviour
        String name = "Odissey";
        String weapon = "Laser";
        String response= "The starship " + name + " was modified. New Weapon: " + weapon;
        Mockito.when(starshipService.modifyStarshipByName(name, weapon)).thenReturn(response);

        //Make http request with auth
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        MvcResult result = mockMvc.perform(put("/spacecrafts/starship/modifyStarshipByName")
                        .contextPath("/spacecrafts")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("Name", name)
                        .param("Weapon", weapon))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteStarshipByNameTestWhenLoginProvided() throws Exception {
        // Call to the login to get the token
        String token = mockMvc.perform(post("/spacecrafts/auth/login")
                        .contextPath("/spacecrafts")
                        .param("user", "ADMIN")
                        .param("password", "ADMIN")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Set the expected behaviour
        String name="Odissey";
        String response =  "The starship " + name + " was successfully removed!";
        Mockito.when(starshipService.deleteStarshipByName(name)).thenReturn(response);

        //Make http request with auth
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        MvcResult result = mockMvc.perform(delete("/spacecrafts/starship/deleteStarshipByName")
                        .headers(headers)
                        .contextPath("/spacecrafts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("Name", name))
                .andExpect(status().isOk())
                .andReturn();
    }
}