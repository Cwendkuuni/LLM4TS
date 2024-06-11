import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {
    @Test
    public void testAbrirWithXmlFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.xml);
    }
}