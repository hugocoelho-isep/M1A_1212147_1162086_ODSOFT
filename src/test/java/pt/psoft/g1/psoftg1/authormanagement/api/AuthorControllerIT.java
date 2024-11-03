package pt.psoft.g1.psoftg1.authormanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorFactory;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.bootstrapping.Bootstrapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Based on https://www.baeldung.com/spring-boot-testing
 * <p>Adaptations to Junit 5 with ChatGPT
 */
@ExtendWith(SpringExtension.class)
//@WebMvcTest(AuthorController.class)
@SpringBootTest
@AutoConfigureMockMvc

public class AuthorControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    public void testCreateAuthor_Success() throws Exception {
        // Arrange
        CreateAuthorRequest createRequest = new CreateAuthorRequest("Jack Daniels", "Author of renowned thriller books", null, null);

        // Act & Assert
        mockMvc.perform(post("/api/authors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().exists(HttpHeaders.ETAG))
                .andExpect(jsonPath("$.name").value("Jack Daniels"))
                .andExpect(jsonPath("$.bio").value("Author of renowned thriller books"));
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/authordata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAuthorById_Success() throws Exception {
        // Arrange
        Long authorId = 1L; // Use an existing ID for a meaningful test

        // Act & Assert
        mockMvc.perform(get("/api/authors/{authorId}", authorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorNumber").value(authorId))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.bio").value("Author of renowned books"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/authordata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateAuthor_Success() throws Exception {
        // Arrange
        Long authorId = 1L;
        long version = 1L;
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest();
        updateRequest.setName("Jane Doe");
        updateRequest.setBio("Updated bio");

        // Act & Assert
        mockMvc.perform(patch("/api/authors/{authorId}", authorId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, version)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.ETAG))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.bio").value("Updated bio"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/authordata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAuthorsByNameFilter_Success() throws Exception {
        mockMvc.perform(get("/api/authors?name=Jane"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].name").value("Jane Doe"))
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"READER"})
    @DirtiesContext
    @Sql(scripts = "/authordata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAuthorBooks_Success() throws Exception {
        Long authorId = 1L;

        mockMvc.perform(get("/api/authors/{authorId}/books", authorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$.items[1].isbn").value("1234567891"))
                .andExpect(jsonPath("$.items.length()").value(2));
    }
}
