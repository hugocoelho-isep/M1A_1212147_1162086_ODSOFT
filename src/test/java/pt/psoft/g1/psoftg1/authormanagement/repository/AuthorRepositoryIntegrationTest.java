package pt.psoft.g1.psoftg1.authormanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Based on https://www.baeldung.com/spring-boot-testing
 * <p>Adaptations to Junit 5 with ChatGPT
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AuthorRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuthorRepository authorRepository;

    private Author author1, author2, author3, author4, author5;

    @BeforeEach
    public void setUp() {
        // given
        author1 = new Author("Alex", "O Alex escreveu livros", null);
        author2 = new Author("Maria", "A Maria escreve livros infantis", null);
        author3 = new Author("José", "O José escreve livros de ficção", null);
        author4 = new Author("Albertina", "O Albertina escrev livros de romance", null);
        author5 = new Author("Alberto", "O Alberto escreve livros de informação", null);

        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(author3);
        entityManager.persist(author4);
        entityManager.persist(author5);
        entityManager.flush();
    }

    @Test
    public void whenFindByName_thenReturnAuthor() {
        // given
        Author alex = new Author("Alex", "O Alex escreveu livros", null);
        entityManager.persist(alex);
        entityManager.flush();

        // when
        List<Author> list = authorRepository.searchByNameName(alex.getName());

        // then
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getName())
                .isEqualTo(alex.getName());
    }

    @Test
    public void givenExistingAuthor_whenFindByAuthorNumber_thenReturnAuthor() {
        // given
        Long authorNumber = author1.getAuthorNumber();

        // when
        Optional<Author> found = authorRepository.findByAuthorNumber(authorNumber);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(author1.getName());
    }

    @Test
    public void givenNonExistingAuthorNumber_whenFindByAuthorNumber_thenReturnEmpty() {
        // given
        Long invalidAuthorNumber = 999L;

        // when
        Optional<Author> found = authorRepository.findByAuthorNumber(invalidAuthorNumber);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    public void givenAuthorsWithCommonPrefix_whenSearchByNameNameStartsWith_thenReturnMatchingAuthors() {
        // given
        String prefix = "Al";

        // when
        List<Author> authors = authorRepository.searchByNameNameStartsWith(prefix);

        // then
        assertThat(authors).containsExactlyInAnyOrder(author1, author4, author5);
    }

    @Test
    public void givenAuthors_whenSearchByName_thenReturnExactMatch() {
        // given
        String exactName = "Maria";

        // when
        List<Author> authors = authorRepository.searchByNameName(exactName);

        // then
        assertThat(authors).hasSize(1);
        assertThat(authors.get(0).getName()).isEqualTo(author2.getName());
    }

    @Test
    public void givenNonExistingName_whenSearchByName_thenReturnEmpty() {
        // given
        String nonExistentName = "NonExistentName";

        // when
        List<Author> authors = authorRepository.searchByNameName(nonExistentName);

        // then
        assertThat(authors).isEmpty();
    }

//    @Test
//    public void whenFindTopAuthorByLendings_thenReturnAuthorsSortedByLendings() {
//        // given
//        Pageable pageable = PageRequest.of(0, 2);
//
//        // when
//        var topAuthors = authorRepository.findTopAuthorByLendings(pageable);
//
//        // then
//        assertThat(topAuthors.getContent()).isNotEmpty(); // Customize with actual lending data
//        // Further assertions would depend on the number of lendings assigned to authors
//    }

    @Test
    public void whenDeleteAuthor_thenAuthorIsRemoved() {
        // given
        Long authorNumber = author1.getAuthorNumber();

        // when
        authorRepository.delete(author1);

        // then
        Optional<Author> found = authorRepository.findByAuthorNumber(authorNumber);
        assertThat(found).isEmpty();
    }

//    @Test
//    public void givenAuthorWithCoAuthors_whenFindCoAuthorsByAuthorNumber_thenReturnCoAuthors() {
//        // given
//        // Set up book and co-author relationships in the entityManager here
//        // Example: entityManager.persist(bookWithCoAuthors);
//        // Replace author1.getAuthorNumber() with actual author numbers involved in co-authorship
//
//        // when
//        List<Author> coAuthors = authorRepository.findCoAuthorsByAuthorNumber(author1.getAuthorNumber());
//
//        // then
//        assertThat(coAuthors).containsExactly(author2); // Adjust based on actual setup
//    }

}
