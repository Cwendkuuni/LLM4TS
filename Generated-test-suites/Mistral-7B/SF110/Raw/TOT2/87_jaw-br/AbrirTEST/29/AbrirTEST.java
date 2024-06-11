import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import jaw.entrada.Abrir;

public class AbrirTest {

    @Test
    public void testAbrirWithValidXmlFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        Assertions.assertNotNull(abrir.getXml());
    }

    @Test
    public void testAbrirWithInvalidXmlFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.txt";
        Abrir abrir = new Abrir(form, arquivo);
        Assertions.assertNull(abrir.getXml());
    }

    @Test
    public void testAbrirWithNullForm() {
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(null, arquivo);
        Assertions.assertThrows(NullPointerException.class, () -> abrir.getXml());
    }

    @Test
    public void testAbrirWithNullArquivo() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, null);
        Assertions.assertNull(abrir.getXml());
    }
}