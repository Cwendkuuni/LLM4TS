package jaw.entrada; // Make sure to import necessary packages
import org.junit.Test;
import static org.junit.Assert.*; // For assertions

public class AbrirTest {
    @Test
    public void testAbrirXML() {
        // Given
        ProcessarEntidades form = new ProcessarEntidades();
        String xmlFilePath = "test/resources/validXMLFile.jaw.xml"; // Replace with valid XML file path

        // When
        new Abrir(form, xmlFilePath);

        // Then
        assertNotNull(form.getEntidades()); // Check if entidades were set correctly
        // Add more assertions based on expected behavior of the 'form' object
    }

    @Test
    public void testAbrirInvalidXML() {
        // Given
        ProcessarEntidades form = new ProcessarEntidades();
        String invalidXmlFilePath = "test/resources/invalidXMLFile.jaw.xml"; // Replace with invalid XML file path

        // When
        new Abrir(form, invalidXmlFilePath);

        // Then
        assertTrue(form.getEntidades() == null); // Check if entidades were not set
        // Add more assertions based on expected error messages
    }

    @Test
    public void testAbrirSerializado() {
        // Given
        ProcessarEntidades form = new ProcessarEntidades();
        String serializadoFilePath = "test/resources/validJawFile.jaw"; // Replace with valid JAW file path

        // When
        new Abrir(form, Abrir.ABRIR);
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(serializoFilePath));
        chooser.showOpenDialog(null);

        // Then
        assertNotNull(form.getEntidades()); // Check if entidades were set correctly
        // Add more assertions based on expected behavior of the 'form' object
    }

    @Test
    public void testAbrirInvalidSerializado() {
        // Given
        ProcessarEntidades form = new ProcessarEntidades();
        String invalidSerializadoFilePath = "test/resources/invalidJawFile.jaw"; // Replace with invalid JAW file path

        // When
        new Abrir(form, Abrir.ABRIR);
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(invalidSerializoFilePath));
        chooser.showOpenDialog(null);

        // Then
        assertTrue(form.getEntidades() == null); // Check if entidades were not set
        // Add more assertions based on expected error messages
    }
}