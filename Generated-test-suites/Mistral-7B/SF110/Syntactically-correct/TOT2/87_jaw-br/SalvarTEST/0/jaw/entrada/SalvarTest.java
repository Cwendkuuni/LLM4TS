import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {
    
    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNotNull(salvar);
    }
}