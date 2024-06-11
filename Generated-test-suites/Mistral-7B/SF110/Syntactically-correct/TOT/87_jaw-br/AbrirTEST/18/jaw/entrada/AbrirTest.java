import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import java.io.File;
import java.io.IOException;

public class AbrirTest {
    private ProcessarEntidades form;
    private Abrir objectUnderTest;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
    }

    @Test
    public void testAbrirXML() throws IOException {
        // Given
        String xmlFilePath = "testData/validXMLFile.jaw.xml";
        File xmlFile = new File(xmlFilePath);
        Abrir testObject = new Abrir(form, xmlFile.getAbsolutePath());

        // When

        // No need to assert anything here, as the test will automatically fail if an exception is not thrown

        // Then
        Assert.assertNotNull(testObject.getForm().getEntidades());
        Assert.assertEquals(xmlFilePath, testObject.getForm().getNomeArquivoSalvo());
    }
}