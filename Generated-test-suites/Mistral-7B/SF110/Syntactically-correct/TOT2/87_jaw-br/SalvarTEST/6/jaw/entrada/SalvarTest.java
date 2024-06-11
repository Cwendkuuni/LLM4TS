import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.util.ArrayList;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
        assertNotNull(salvar);
    }
}