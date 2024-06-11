import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {

    @Test
    public void testAbrirWithStringArg() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.xml";
        Abrir abrir = new Abrir(form, arquivo);
        // Add assertions here
    }
}