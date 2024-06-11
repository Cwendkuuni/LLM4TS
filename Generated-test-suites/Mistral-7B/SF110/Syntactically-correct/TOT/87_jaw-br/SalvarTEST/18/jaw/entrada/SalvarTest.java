import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import java.io.File;
import java.util.ArrayList;

public class SalvarTest {
    private Salvar salvar;
    private ArrayList<Entidade> entidades;

    @Before
    public void setUp() {
        entidades = new ArrayList<>();
        entidades.add(new Entidade("Teste1", new String[]{"Teste1Attribute1", "Teste1Attribute2"}));
        entidades.add(new Entidade("Teste2", new String[]{"Teste2Attribute1", "Teste2Attribute2"}));
        salvar = new Salvar(new ProcessarEntidades() {}, Salvar.SALVAR);
    }

    @Test
    public void testConstructorSalvar() {
        // Test typical usage with valid input
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertNotNull(salvar.entidade);
        assertEquals(2, salvar.entidade.size());

        // Test error scenario with null entidades
        salvar = new Salvar(new ProcessarEntidades() {}, Salvar.SALVAR);
        salvar.entidade = null;
        assertEquals(0, salvar.entidade.size());

        // Test edge case with empty String for arquivo
        salvar.arquivo = "";
        salvar.vela = true;
        assertEquals("", salvar.arquivo);

        // Test edge case with null FileChooser dialog
        JFileChooser escolhedor = null;
        salvar = new Salvar(new ProcessarEntidades() {}, Salvar.SALVAR);
        salvar.vela = true;
        Assert.assertThrows(NullPointerException.class, () -> salvar.escolhedor.showSaveDialog(new JFrame()));
    }
}