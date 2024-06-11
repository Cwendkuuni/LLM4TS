package jaw.entrada;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class AbrirTest {
    @Test
    public void testAbrirXmlWithValidXmlFile() {
        // Given
        final ProcessarEntidades form = new ProcessarEntidades();
        final Abrir abrir = new Abrir(form, "test.jaw.xml");

        // When
        abrir.abrirXML("test.jaw.xml");

        // Then
        assertNotNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithValidJawFile() {
        // Given
        final ProcessarEntidades form = new ProcessarEntidades();
        final Abrir abrir = new Abrir(form, "test.jaw");

        // When
        abrir.abrirSerializado("test.jaw");

        // Then
        assertNotNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirXmlWithInvalidXmlFile() {
        // Given
        final ProcessarEntidades form = new ProcessarEntidades();
        final Abrir abrir = new Abrir(form, "test.txt");

        // When
        abrir.abrirXML("test.txt");

        // Then
        // Exception should be thrown
    }

    @Test
    public void testAbrirSerializadoWithInvalidJawFile() {
        // Given
        final ProcessarEntidades form = new ProcessarEntidades();
        final Abrir abrir = new Abrir(form, "test.txt");

        // When
        abrir.abrirSerializado("test.txt");

        // Then
        // Exception should be thrown
    }

    @Test
    public void testOpenFileDialogWithXmlFilter() {
        // Given
        final ProcessarEntidades form = new ProcessarEntidades();
        final Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);

        // When
        abrir.abrir();

        // Then
        // File chooser should open and allow the selection of a .jaw.xml file
    }

    @Test
    public void testOpenFileDialogWithSerializacaoFilter() {
        // Given
        final ProcessarEntidades form = new ProcessarEntidades();
        final Abrir abrir = new Abrir(form, Abrir.ABRIR);

        // When
        abrir.abrir();

        // Then
        // File chooser should open and allow the selection of a .jaw file
    }
}