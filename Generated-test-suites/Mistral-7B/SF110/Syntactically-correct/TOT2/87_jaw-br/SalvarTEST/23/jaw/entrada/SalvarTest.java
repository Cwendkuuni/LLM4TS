import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import jaw.entrada.Salvar;

public class SalvarTest {
    @Test
    public void testSalvarConstructor() {
        Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
        assertNotNull(salvar);
    }
}