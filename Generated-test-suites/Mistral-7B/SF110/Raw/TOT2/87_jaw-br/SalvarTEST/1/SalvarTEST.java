import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {
    @Test
    public void testSalvar() {
        Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(new Entidade());
        salvar.entidade = entidades;
        String expectedXml = "<entidades><entidade></entidade></entidades>";
        salvar.salvar(salvar.entidade, "test.jaw.xml");
        assertNotNull(salvar.xml);
        assertEquals(expectedXml, salvar.xml);
    }
}