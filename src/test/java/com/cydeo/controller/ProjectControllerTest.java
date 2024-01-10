package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest and @AutoConfigureMockMvc create all the beans in the application context
// @WebMVCTest(ProjectController.class) // creates only one bean from ProjectController class
@SpringBootTest
@AutoConfigureMockMvc //
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static UserDTO userDTO;
    static ProjectDTO projectDTO;
    static String token;

    @BeforeAll
    static void setUp() {
        userDTO = UserDTO.builder()
                .id(1L)
                .firstName("mike")
                .lastName("can")
                .userName("mike")
                .passWord("Abc1")
                .confirmPassWord("Abc1")
                .role(new RoleDTO(2L, "Manager"))
                .gender(Gender.MALE)
                .build();

        projectDTO = ProjectDTO.builder()
                .projectCode("API01")
                .projectName("Api-ozzy")
                .assignedManager(userDTO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .projectDetail("testing api")
                .projectStatus(Status.OPEN)
                .build();


        token = "Bearer " + getToken(userDTO.getUserName(),userDTO.getPassWord());



    }

    // get all project without token
    @Test
    void getAllProject_givenNoToken_test() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .get("/project"))
                .andExpect(status().is4xxClientError());
    }


    // with token
    @Test
    void getAllProjects_givenToken_test() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .get("/project")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].projectCode").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].assignedManager").isNotEmpty());
    }


    @Test
    void createProject_givenToken_test() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(projectDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateProject_givenToken() throws Exception {

        projectDTO.setProjectName("Api-cydeo zouliga");

        mvc.perform(MockMvcRequestBuilders
                        .put("/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Project updated successfully"));
    }

    @Test
    public void deleteProject_givenToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/project/" + projectDTO.getProjectCode())
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }



    private static String toJsonString(Object object) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(object);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static String getToken(String username, String password) {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>() {{
            add("grant_type", "password");
            add("client_id", "ticketing-app");
            add("client_secret", "E3gavXCR4sOcU4I1ILjpXU9GyLk4f6zf");
            add("username", username);
            add("password", password);
            add("scope", "openid");
        }};

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> entry = new HttpEntity<>(map, headers);

        ResponseEntity<HashMap> response = restTemplate.exchange(
                "http://localhost:8080/auth/realms/cydeo-dev/protocol/openid-connect/token",
                HttpMethod.POST,
                entry,
                HashMap.class
        );

        return (String) Objects.requireNonNull(response.getBody()).get("access_token");

    }


}















