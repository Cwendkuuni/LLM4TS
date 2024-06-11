import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import java.io.File;

public class AbrirTest {
    
    
    @Test
    public void testAbrirXML() {
        String xmlFilePath = "test_files/valid_xml_file.jaw.xml";
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, xmlFilePath);
        assertFalse(abrir.xml.isEmpty());
        assertTrue(form.getEntidades() instanceof ArrayList);
        // Add more assertions based on the structure and content of Entidades
    }

    @Test(expected = FileNotFoundException.class)
    public void testAbrirXML_FileNotFound() {
        String xmlFilePath = "test_files/invalid_xml_file.jaw.xml";
        ProcessorEntidades form = new ProcessorEntidades();
        Abrir abrir = new Abrir(form, xmlFilePath);
        fail("Expected FileNotFoundException but got none");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirXML_InvalidFileExtension() {
        String xmlFilePath = "test_files/invalid_file.txt";
        ProcessorEntidades form = new ProcessorEntidades();
        Abrir abrir = new Abrir(form, xmlFilePath);
        fail("Expected IllegalArgumentException but got none");
    }

    
}