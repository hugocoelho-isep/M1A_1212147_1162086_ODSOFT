package pt.psoft.g1.psoftg1.bookmanagement.model.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.model.TitleFactory;

class TitleTest {

    @Test
    void ensureTitleMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Title(null));
    }

    @Test
    void ensureTitleMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Title(""));
    }

    @Test
    void ensureTitleCantStartWithWhitespace() {
        final var title = new Title(" Some title");
        assertEquals("Some title", title.toString());
    }

    @Test
    void ensureTitleCantEndWithWhitespace() {
        final var title = new Title("Some title ");
        assertEquals("Some title", title.toString());
    }


    /**
     * Text from <a href="https://www.lipsum.com/">Lorem Ipsum</a> generator.
     */
    @Test
    void ensureTitleMustNotBeOversize() {
        assertThrows(IllegalArgumentException.class, () -> new Title("\n" +
                "\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam venenatis semper nisl, eget condimentum felis tempus vitae. Morbi tempus turpis a felis luctus, ut feugiat tortor mattis. Duis gravida nunc sed augue ultricies tempor. Phasellus ultrices in dolor id viverra. Sed vitae odio ut est vestibulum lacinia sed sed neque. Mauris commodo, leo in tincidunt porta, justo mi commodo arcu, non ultricies ipsum dolor a mauris. Pellentesque convallis vulputate nisl, vel commodo felis ornare nec. Aliquam tristique diam dignissim hendrerit auctor. Mauris nec dolor hendrerit, dignissim urna non, pharetra quam. Sed diam est, convallis nec efficitur eu, sollicitudin ac nibh. In orci leo, dapibus ut eleifend et, suscipit sit amet felis. Integer lectus quam, tristique posuere vulputate sed, tristique eget sem.\n" +
                "\n" +
                "Mauris ac neque porttitor, faucibus velit vel, congue augue. Vestibulum porttitor ipsum eu sem facilisis sagittis. Mauris dapibus tincidunt elit. Phasellus porttitor massa nulla, quis dictum lorem aliquet in. Integer sed turpis in mauris auctor viverra. Suspendisse faucibus tempus tellus, in faucibus urna dapibus at. Nullam dolor quam, molestie nec efficitur nec, bibendum a nunc.\n" +
                "\n" +
                "Maecenas quam arcu, euismod sit amet congue non, venenatis nec ipsum. Cras at posuere metus. Quisque facilisis, sem sit amet vestibulum porta, augue quam semper nulla, eu auctor orci purus vel felis. Fusce ultricies tristique tellus, sed rhoncus elit venenatis id. Aenean in lacus quis ipsum eleifend viverra at at lacus. Nulla finibus, risus ut venenatis posuere, lacus magna eleifend arcu, ut bibendum magna turpis eu lorem. Mauris sed quam eget libero vulputate pretium in in purus. Morbi nec faucibus mi, sit amet pretium tellus. Duis suscipit, tellus id fermentum ultricies, tellus elit malesuada odio, vitae tempor dui purus at ligula. Nam turpis leo, dignissim tristique mauris at, rutrum scelerisque est. Curabitur sed odio sit amet nisi molestie accumsan. Ut vulputate auctor tortor vel ultrices. Nam ut volutpat orci. Etiam faucibus aliquam iaculis.\n" +
                "\n" +
                "Mauris malesuada rhoncus ex nec consequat. Etiam non molestie libero. Phasellus rutrum elementum malesuada. Pellentesque et quam id metus iaculis hendrerit. Fusce molestie commodo tortor ac varius. Etiam ac justo ut lacus semper pretium. Curabitur felis mauris, malesuada accumsan pellentesque vitae, posuere non lacus. Donec sit amet dui finibus, dapibus quam quis, tristique massa. Phasellus velit ipsum, facilisis vel nisi eu, interdum vehicula ante. Nulla eget luctus nunc, nec ullamcorper lectus.\n" +
                "\n" +
                "Curabitur et nisi nisi. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur ultrices ultrices ante eu vestibulum. Phasellus imperdiet non ex sed rutrum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec consequat mauris sed pulvinar sodales. Quisque a iaculis eros. Donec non tellus eget ligula eleifend posuere. Sed tincidunt, purus id eleifend fringilla, tellus erat tristique urna, at ullamcorper purus turpis ac risus. Maecenas non finibus diam. Aliquam erat volutpat. Morbi ultrices blandit arcu eu dignissim. Duis ac dapibus libero. Ut pretium libero sit amet velit viverra semper. Suspendisse vitae augue dui.\n" +
                "\n" +
                "Aliquam aliquam justo porttitor sapien faucibus sollicitudin. Sed iaculis accumsan urna, id elementum est rhoncus vitae. Maecenas rhoncus ultrices arcu eu semper. Integer pulvinar ultricies purus, sit amet scelerisque dui vehicula vel. Phasellus quis urna ac neque auctor scelerisque eget eget arcu. Sed convallis, neque consectetur venenatis ornare, nibh lorem mollis magna, vel vulputate libero ligula egestas ligula. Curabitur iaculis nisl nisi, ac ornare urna lacinia non. Cras sagittis risus sit amet interdum porta. Nam dictum, neque ut blandit feugiat, tortor libero hendrerit enim, at tempor justo velit scelerisque odio. Fusce a ipsum sit amet ligula maximus pharetra. Suspendisse rhoncus leo dolor, vulputate blandit mi ullamcorper ut. Etiam consequat non mi eu porta. Sed mattis metus fringilla purus auctor aliquam.\n" +
                "\n" +
                "Vestibulum quis mi at lorem laoreet bibendum eu porta magna. Etiam vitae metus a sapien sagittis dapibus et et ex. Vivamus sed vestibulum nibh. Etiam euismod odio massa, ac feugiat urna congue ac. Phasellus leo quam, lacinia at elementum vitae, viverra quis ligula. Quisque ultricies tellus nunc, id ultrices risus accumsan in. Vestibulum orci magna, mollis et vehicula non, bibendum et magna. Pellentesque ut nibh quis risus dignissim lacinia sed non elit. Morbi eleifend ipsum posuere velit sollicitudin, quis auctor urna ullamcorper. Praesent pellentesque non lacus eu scelerisque. Praesent quis eros sed orci tincidunt maximus. Quisque imperdiet interdum massa a luctus. Phasellus eget nisi leo.\n" +
                "\n" +
                "Nunc porta nisi eu dui maximus hendrerit eu quis est. Cras molestie lacus placerat, maximus libero hendrerit, eleifend nisi. Suspendisse potenti. Praesent nec mi ut turpis pharetra pharetra. Phasellus pharetra. "));
    }

    @Test
    void ensureTitleIsSet() {
        final var title = new Title("Some title");
        assertEquals("Some title", title.toString());
    }

    @Test
    void ensureTitleIsChanged() {
        final var title = new Title("Some title");
        title.setTitle("Some other title");
        assertEquals("Some other title", title.toString());
    }

 //novos
 @Test
 void ensureTitleRemovesMultipleWhitespacesbeforeandafter() {
     final var title = new Title("   Some title   ");
     assertEquals("Some title", title.toString());
 }

 @Test
 void ensureTitleAllowsSpecialCharacters() {
        final var title = new Title("Title with !@#$%^&*() special chars");
        assertEquals("Title with !@#$%^&*() special chars", title.toString());
    }

    @Test
    void ensureTitleWithMaxLength() {
        String maxLengthTitle = "A".repeat(128);
        final var title = new Title(maxLengthTitle);
        assertEquals(maxLengthTitle, title.toString());
    }

    @Test
    void ensureTitleExceedingMaxLengthThrowsException() {
        String exceedingTitle = "A".repeat(129);
        assertThrows(IllegalArgumentException.class, () -> new Title(exceedingTitle));
    }

    @Test
    void testSetTitleWithValidValue() {
        Title title = new Title("Initial Title");
        title.setTitle("Updated Title");
        assertEquals("Updated Title", title.getTitle());
    }


    @Test
    void testValidateTitleCalledOnValidTitle() {
    Title title = TitleFactory.createTitle("Some initial title");
    Title spyTitle = Mockito.spy(title);

    // Call the setTitle method
    spyTitle.setTitle("Effective Java");

    // Verify that setTitle is called when setting a valid title
    verify(spyTitle).setTitle("Effective Java");
    assertEquals("Effective Java", spyTitle.getTitle());
}

    @Test
    void testSetTitleCalledWithNullTitleThrowsException() {
        Title title = new Title("Initial Title");
        Title spyTitle = Mockito.spy(title);

        // Tenta definir um título nulo e espera uma exceção
        assertThrows(IllegalArgumentException.class, () -> spyTitle.setTitle(null));

        // Verifica se setTitle foi chamado com null
        verify(spyTitle).setTitle(null);
    }

    @Test
    void testSetTitleCalledWithBlankTitleThrowsException() {
        Title title = new Title("Initial Title");
        Title spyTitle = Mockito.spy(title);

        // Tenta definir um título em branco e espera uma exceção
        assertThrows(IllegalArgumentException.class, () -> spyTitle.setTitle(""));

        // Verifica se setTitle foi chamado com uma string em branco
        verify(spyTitle).setTitle("");
    }

    @Test
    void testSetTitleCalledWithExceedingLengthTitleThrowsException() {
        String longTitle = "A".repeat(129); // Título que excede o limite
        Title title = new Title("Initial Title");
        Title spyTitle = Mockito.spy(title);

        // Tenta definir um título muito longo e espera uma exceção
        assertThrows(IllegalArgumentException.class, () -> spyTitle.setTitle(longTitle));

        // Verifica se setTitle foi chamado com o título longo
        verify(spyTitle).setTitle(longTitle);
    }

    @Test
    void testSetTitleTrimsWhitespace() {
        Title title = new Title("Some initial title");
        Title spyTitle = Mockito.spy(title);

        // Define um título com espaços em branco no início e fim
        spyTitle.setTitle("  Trimmed Title  ");

        // Verifica se setTitle foi chamado com o título ajustado
        verify(spyTitle).setTitle("  Trimmed Title  ");
        assertEquals("Trimmed Title", spyTitle.getTitle());
    }

    @Test
    void testSetTitleCalledOnlyOnceForSameTitle() {
        Title title = new Title("Duplicate Title");
        Title spyTitle = Mockito.spy(title);

        // Define o mesmo título novamente
        spyTitle.setTitle("Duplicate Title");

        // Verifica que setTitle foi chamado apenas uma vez
        verify(spyTitle).setTitle("Duplicate Title");
        verifyNoMoreInteractions(spyTitle);
    }







}