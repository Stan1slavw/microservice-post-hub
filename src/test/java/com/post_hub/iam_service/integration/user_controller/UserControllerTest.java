package com.post_hub.iam_service.integration.user_controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.post_hub.iam_service.model.dto.user.UserDTO;
import com.post_hub.iam_service.model.entity.User;
import com.post_hub.iam_service.model.exception.InvalidDataException;
import com.post_hub.iam_service.model.exception.NotFoundException;
import com.post_hub.iam_service.model.request.User.NewUserRequest;
import com.post_hub.iam_service.model.request.User.UpdateUserRequest;
import com.post_hub.iam_service.model.responce.IamResponse;
import com.post_hub.iam_service.repositories.UserRepository;
import com.post_hub.iam_service.security.encrypt.JwtTokenProvider;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = com.post_hub.iam_service.IamServiceApplication.class)
@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class UserControllerTest {

    @Autowired
    @Setter
    private MockMvc mockMvc;

    @Autowired
    @Setter
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    @Setter
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String adminJwt;
    private String userJwt;

    @BeforeAll
    @Transactional
    void authorize() {
        User admin = userRepository.findById(1).orElseThrow(() -> new InvalidDataException("Admin with id 1 not found"));
        Hibernate.initialize(admin.getRoles());
        this.adminJwt = "Bearer " + jwtTokenProvider.generateToken(admin);

        User user = userRepository.findById(3).orElseThrow(() -> new InvalidDataException("User with id 3 not found"));
        this.userJwt = jwtTokenProvider.generateToken(user);
    }

    @Test
    void getAllUsers_200_OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/all")
                        .header(HttpHeaders.AUTHORIZATION, adminJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllUsers_Unauthorized_401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/all"))
                        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    void create_User_AsAdmin_OK_200() throws Exception {
        NewUserRequest request = new NewUserRequest("newUserTest", "password123", "newusertest@gmail..com");

       MvcResult requestResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/create")
                .header(HttpHeaders.AUTHORIZATION, adminJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
               .andReturn();

       IamResponse<UserDTO> response = parseUserDTOResponse(requestResult.getResponse().getContentAsByteArray());

       UserDTO resultBody = Objects.nonNull(response.getPayload()) ? response.getPayload() : null;
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(resultBody);
        Assertions.assertEquals(request.getEmail(), resultBody.getEmail());
        Assertions.assertEquals(request.getUsername(), resultBody.getUsername());

    }

    @Test
    @Transactional
    void create_User_AsUnauthorized_401() throws Exception{

        NewUserRequest request = new NewUserRequest("newUserTest", "password123", "newusertest@gmail..com");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/create")
                        .header(HttpHeaders.AUTHORIZATION, userJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    void update_User_AsAdmin_OK_200() throws Exception{

        UpdateUserRequest request = new UpdateUserRequest("update_username", "newusertest@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/5")
                        .header(HttpHeaders.AUTHORIZATION, adminJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private IamResponse<UserDTO> parseUserDTOResponse(byte[] contentAsByteArray){
        try{
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(contentAsByteArray, new TypeReference<>() {});
        } catch (IOException e){
            throw new RuntimeException("Failed to parse UserDTO response", e);
        }
    }
}
