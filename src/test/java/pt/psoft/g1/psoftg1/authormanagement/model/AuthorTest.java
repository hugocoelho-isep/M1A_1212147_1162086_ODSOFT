package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);

    private static class EntityWithPhotoImpl extends EntityWithPhoto { }
    @BeforeEach
    void setUp() {
    }

    @Test
    void ensureNameNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(null,validBio, null));
    }

    @Test
    void ensureBioNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(validName,null, null));
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        final var subject = new Author(validName,validBio, null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testCreateAuthorRequestWithPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(request.getName(), request.getBio(), "photoTest.jpg");
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, null);
        Author author = new Author(request.getName(), request.getBio(), null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = new Author(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Author author = new Author(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }

    @Test
    public void testApplyPatch_Success() {
        // arrange
        Author author = AuthorFactory.createAuthor("Author Name", "Author Bio", null);
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setBio("Updated Bio");
        long currentVersion = author.getVersion();

        // act
        author.applyPatch(currentVersion, updateRequest);

        // assert
        assertEquals("Updated Name", author.getName());
        assertEquals("Updated Bio", author.getBio());
    }

    @Test
    public void testApplyPatch_ThrowsStaleObjectStateException() {
        // arrange
        Author author = AuthorFactory.createAuthor("Author Name", "Author Bio", null);
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setBio("Updated Bio");
        long incorrectVersion = author.getVersion() + 1;

        // act + assert
        assertThrows(StaleObjectStateException.class, () -> author.applyPatch(incorrectVersion, updateRequest),
                "Expected applyPatch to throw StaleObjectStateException for version mismatch");
    }

    @Test
    public void testRemovePhoto_Success() {
        // Arrange: Use the correct version for removal
        Author author = AuthorFactory.createAuthor("Author Name", "Author Bio", null);
        long currentVersion = author.getVersion();

        // Act
        author.removePhoto(currentVersion);

        // Assert: Photo URI should be null after successful removal
        assertNull(author.getPhoto(), "Photo URI should be null after removal");
    }

    @Test
    public void testRemovePhoto_ThrowsConflictException() {
        // Arrange: Set an incorrect version for removal
        Author author = AuthorFactory.createAuthor("Author Name", "Author Bio", null);
        long incorrectVersion = author.getVersion() + 1;

        // Act & Assert: Expect ConflictException due to version mismatch
        assertThrows(ConflictException.class, () -> author.removePhoto(incorrectVersion),
                "Expected removePhoto to throw ConflictException for version mismatch");
    }

}

