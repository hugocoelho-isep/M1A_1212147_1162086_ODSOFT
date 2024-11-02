package pt.psoft.g1.psoftg1.bookmanagement.model.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BookRepositoryIT {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book sampleBook;

    @BeforeEach
    public void setUp() {
        Genre validGenre = new Genre("Fantasia");
        validGenre = genreRepository.save(validGenre); // Persist the genre

        Author author = new Author("Alex", "O Alex escreveu livros", null);
        author = authorRepository.save(author); // Persist the author

        ArrayList<Author> authors = new ArrayList<>();
        authors.add(author);

        sampleBook = new Book("9782826012092", "Sample Book", "A short description of the book", validGenre, authors, null);
    }

    @Test
    public void testSaveBook() {
        Book savedBook = bookRepository.save(sampleBook);
        assertNotNull(savedBook.getIsbn());
        assertEquals("Sample Book", savedBook.getTitle().getTitle());
    }

    @Test
    public void testFindBookByIsbn() {
        Book savedBook = bookRepository.save(sampleBook);
        Optional<Book> foundBook = bookRepository.findByIsbn(savedBook.getIsbn());
        assertTrue(foundBook.isPresent());
        assertEquals(savedBook.getIsbn(), foundBook.get().getIsbn());
    }



    @Test
    public void testDeleteBook() {
        Book savedBook = bookRepository.save(sampleBook);
        String isbn="9782826012092";
        bookRepository.delete(savedBook);
        Optional<Book> foundBook = bookRepository.findByIsbn(isbn);
        assertFalse(foundBook.isPresent());
    }

    @Test
    public void testFindBooksByGenre() {
        bookRepository.save(sampleBook);
        List<Book> books = bookRepository.findByGenre(sampleBook.getGenre().getGenre());
        assertFalse(books.isEmpty());
        assertEquals(sampleBook.getGenre().getGenre(), books.get(0).getGenre().getGenre());
    }

    @Test
    public void testFindBooksByAuthorName() {
        bookRepository.save(sampleBook);
        List<Book> books = bookRepository.findByAuthorName(sampleBook.getAuthors().get(0).getName());
        assertFalse(books.isEmpty());
        assertEquals(sampleBook.getAuthors().get(0).getName(), books.get(0).getAuthors().get(0).getName());
    }

    @Test
    public void testFindBooksByTitle() {
        bookRepository.save(sampleBook);
        List<Book> books = bookRepository.findByTitle(sampleBook.getTitle().getTitle());
        assertFalse(books.isEmpty());
        assertEquals(sampleBook.getTitle(), books.get(0).getTitle());
    }



    @Test
    public void testFindBooksByAuthorNumber() {
        Author author = sampleBook.getAuthors().get(0);
        bookRepository.save(sampleBook);
        List<Book> books = bookRepository.findBooksByAuthorNumber(author.getAuthorNumber());
        assertFalse(books.isEmpty());
        assertEquals(author.getAuthorNumber(), books.get(0).getAuthors().get(0).getAuthorNumber());
    }

}
