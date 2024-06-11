import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;

public class AbrirTest {
    @Test
    public void testAbrirWithStringArgument() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.xml);
    }
}