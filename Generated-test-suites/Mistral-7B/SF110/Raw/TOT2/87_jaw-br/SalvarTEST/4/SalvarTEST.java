import static org.junit.Assert.assertNotNull;
import jaw.entrada.Salvar;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SalvarTest {
    @org.junit.Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
        assertNotNull(salvar);
    }
}