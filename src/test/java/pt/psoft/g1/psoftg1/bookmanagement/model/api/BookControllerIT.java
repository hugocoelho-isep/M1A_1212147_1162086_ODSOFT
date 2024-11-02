package pt.psoft.g1.psoftg1.bookmanagement.model.api;

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
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc


public class BookControllerIT {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/bookdataput.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateBook_ValidRequest_ReturnsCreated() throws Exception {
        // Arrange: create a valid Book object
        ArrayList<Author> authors = new ArrayList<>();
        Genre validGenre = new Genre("Fantasia");
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        authors.add(alex);
        ArrayList<Long> authorlist = new ArrayList<>();
        authorlist.add(1L);
        CreateBookRequest createBookRequest = new CreateBookRequest();
        createBookRequest.setTitle("A Valid Book Title");
        createBookRequest.setDescription("A short description of the book");
        createBookRequest.setGenre(validGenre.getGenre());
        createBookRequest.setAuthors(authorlist);


        String ISBN = "9791234567896";
        // Act & Assert: perform the request and expect 201 status
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{isbn}", ISBN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION)) // Check if Location header is present
                .andExpect(jsonPath("$.title").value("A Valid Book Title"))
                .andExpect(jsonPath("$.isbn").value("9791234567896"))
                .andExpect(jsonPath("$.description").value("A short description of the book"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"READER"})
    @DirtiesContext
    @Sql(scripts = "/bookdata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetBookById_ExistingBook_ReturnsBookDetails() throws Exception {
        // Arrange: assuming a book with ID 1 exists in the test database
        String ISBN = "9782826012092";

        // Act & Assert: perform GET request and expect 200 status with book details
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{isbn}", ISBN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.isbn").value(ISBN))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.authors").exists());
    }






    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/bookdata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateBook_ValidRequest_ReturnsUpdatedBook() throws Exception {
        // Arrange: Assuming a book with ISBN 9782826012092 exists in the test database
        String ISBN = "9782826012092";
        long version = 1L;

        UpdateBookRequest updatedBookRequest = new UpdateBookRequest();
        updatedBookRequest.setDescription("An updated description of the book");




        // Act & Assert: Perform PUT request to update the book and expect 200 status with updated details
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/books/{isbn}", ISBN)
                        .header(HttpHeaders.IF_MATCH, version)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("An updated description of the book"));
    }

}
