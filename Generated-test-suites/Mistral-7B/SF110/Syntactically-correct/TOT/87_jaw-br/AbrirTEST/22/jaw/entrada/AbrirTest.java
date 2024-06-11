import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AbrirTest {

    @Test
    public void testAbrirXML() throws IOException {
        final String xmlFilePath = "testData/validXMLFile.jaw.xml";
        final File xmlFile = new File(xmlFilePath);
        final ProcessarEntidades form = new ProcessarEntidades();
        final Abrir abrir = new Abrir(form, xmlFilePath);

        // Check if the correct file was opened
        assertEquals(xmlFilePath, form.getNomeArquivoSalvo());

        // Check if the correct data was loaded
        assertNotNull(abrir.getForm().getEntidades());
        // Perform some verification on the loaded data if necessary
        // ...
    }
}