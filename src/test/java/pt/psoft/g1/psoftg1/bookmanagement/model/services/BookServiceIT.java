package pt.psoft.g1.psoftg1.bookmanagement.model.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;

import static org.junit.Assert.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class BookServiceIT {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private CreateBookRequest createBookRequest;

    @BeforeEach
    public void setUp() {
        Genre validGenre = new Genre("Fantasia");
        ArrayList<Author> authors = new ArrayList<>();
        ArrayList<Long> authorlist = new ArrayList<>();
        authorlist.add(1L);
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        authors.add(alex);
        createBookRequest = new CreateBookRequest();
        createBookRequest.setTitle("Sample Book");
        createBookRequest.setDescription("A short description of the book");
        createBookRequest.setGenre(validGenre.getGenre());
        createBookRequest.setAuthors(authorlist);


    }

    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/bookdataput.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateBook() {
        Book savedBook = bookService.create(createBookRequest,"9782826012092");

        assertNotNull(savedBook);
        assertNotNull(savedBook.getIsbn());
        assertEquals("Sample Book", savedBook.getTitle().toString());
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/bookdataput.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFindBookByIsbn() {

        Book savedBook = bookService.create(createBookRequest,"9782826012092");

        Book foundBook = bookService.findByIsbn(savedBook.getIsbn());

        assertEquals(savedBook.getIsbn(), foundBook.getIsbn());
        assertEquals("Sample Book", foundBook.getTitle().getTitle().toString());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/bookdata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateBook() {

        UpdateBookRequest updatedBookRequest = new UpdateBookRequest();
        updatedBookRequest.setDescription("An updated description of the book");
        updatedBookRequest.setIsbn("9782826012092");

        Book updatedBook = bookService.update(updatedBookRequest,"1");

        assertNotNull(updatedBook);
        assertEquals(updatedBook.getIsbn(), updatedBook.getIsbn());
        assertEquals("An updated description of the book", updatedBook.getDescription().toString());
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/bookdataput.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFindByGenre() {

        Book savedBook = bookService.create(createBookRequest,"9782826012092");
        ArrayList<Book> foundBooks = (ArrayList<Book>) bookService.findByGenre("Fantasia");

        assertEquals(1, foundBooks.size());
        assertEquals("Sample Book", foundBooks.get(0).getTitle().getTitle().toString());
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"LIBRARIAN"})
    @DirtiesContext
    @Sql(scripts = "/bookdataput.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFindByTitle() {
        Book savedBook = bookService.create(createBookRequest,"9782826012092");
        ArrayList<Book> foundBooks = (ArrayList<Book>) bookService.findByTitle("Sample Book");

        assertEquals(1, foundBooks.size());
        assertEquals("Sample Book", foundBooks.get(0).getTitle().getTitle().toString());
    }






}
