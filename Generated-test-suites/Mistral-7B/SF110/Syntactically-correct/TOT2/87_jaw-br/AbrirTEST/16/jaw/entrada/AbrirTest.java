import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {
    @Test
    public void testAbrirWithXmlFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        // Add assertions here to check the state of the object after the method call
    }

    @Test
    public void testAbrirWithWrongFileType() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.txt";
        Abrir abrir = new Abrir(form, arquivo);
        // Add assertions here to check the expected behavior when opening a wrong file type
    }
}