import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import java.util.ArrayList;

public class SalvarTest {
    @Test
    public void testSalvar() {
        Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
        ArrayList<Entidade> entidades = new ArrayList<>();
        Entidade entidade1 = new Entidade("Teste1");
        Entidade entidade2 = new Entidade("Teste2");
        entidades.add(entidade1);
        entidades.add(entidade2);

        salvar.setEntidades(entidades);
        String expectedArquivo = "test.jaw";
        File expectedFile = new File("test.jaw");
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setSelectedFile(expectedFile);

        salvar.Salvar(escolhedor, null);

        assertTrue(salvar.isVela());
        assertEquals(expectedArquivo, salvar.getArquivo());
    }
}