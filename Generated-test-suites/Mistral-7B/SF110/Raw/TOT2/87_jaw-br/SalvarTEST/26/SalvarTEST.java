import static org.junit.Assert.*;
import jaw.entrada.Salvar;

public class SalvarTest {
   @Test
   public void testSalvarConstructor() {
      ProcessarEntidades form = new ProcessarEntidades(); //assuming this class is instantiated correctly
      Salvar salvar = new Salvar(form, Salvar.SALVAR);
      assertFalse(salvar.isVelas());
      assertNull(salvar.getArquivo());
      assertNull(salvar.getXml());
      assertNotNull(salvar.getEntidade());
      assertEquals(0, salvar.getEntidade().size());
      assertEquals(Salvar.SALVAR, salvar.getTipoSalvar());
   }
}