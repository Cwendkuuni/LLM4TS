import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.util.ArrayList;
import java.util.Arrays;

public class SalvarTest {

    @Test
    public void testSalvarMethod() {
        Salvar salvar = new Salvar(null, 0);
        List<Entidade> entidade = new ArrayList<>();
        salvar.setEntidade(entidade);
        salvar.salvar(entidade, "");
        // Add assertions here
    }

    @Test
    public void testGettersAndSetters() {
        Salvar salvar = new Salvar(null, 0);
        String arquivo = "test.jaw";
        salvar.setArquivo(arquivo);
        assertEquals(arquivo, salvar.getArquivo());
    }
}