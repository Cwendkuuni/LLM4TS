import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import jaw.entrada.Salvar;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        // Arrange
        List<Entidade> entidade = new ArrayList<>();
        Entidade entidade1 = new Entidade();
        Entidade entidade2 = new Entidade();
        entidade.add(entidade1);
        entidade.add(entidade2);
        form.setEntidades(entidade);
        String expectedArquivo = "test.jaw";
        // Act
        salvar.Salvar(form, Salvar.SALVAR);
        // Assert
        assertNotNull(salvar.getArquivo());
        assertEquals(expectedArquivo, salvar.getArquivo());
    }
}