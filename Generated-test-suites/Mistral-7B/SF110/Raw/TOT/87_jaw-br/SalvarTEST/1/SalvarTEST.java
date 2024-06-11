import org.junit.Assert;
import org.junit.Test;
import jaw.entrada.Salvar;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {

 @Test
 public void testSalvarJawFileWithValidData() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
 List<Entidade> entidades = new ArrayList<>();
 entidades.add(new Entidade());
 salvar.entidade = entidades;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.showSaveDialog(new JFrame()); // Assume a valid file is selected
 salvar.arquivo = "test.jaw";
 Assert.assertTrue(salvar.salvar(entidades, salvar.arquivo));
 }

 @Test
 public void testSalvarJawFileWithNullData() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
 salvar.entidade = null;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.showSaveDialog(new JFrame()); // Assume a valid file is selected
 salvar.arquivo = "test.jaw";
 Assert.assertFalse(salvar.salvar(null, salvar.arquivo));
 }

 @Test
 public void testSalvarXmlFileWithValidData() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
 List<Entidade> entidades = new ArrayList<>();
 entidades.add(new Entidade());
 salvar.entidade = entidades;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.showSaveDialog(new JFrame()); // Assume a valid file is selected
 salvar.arquivo = "test.jaw.xml";
 String xml = new SerializacaoXML().entidades2XML(entidades);
 Assert.assertEquals(xml, salvor.salvar(entidades, salvor.arquivo));
 }

 @Test(expected = Exception.class)
 public void testSalvarXmlFileWithNullData() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
 salvar.entidade = null;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.showSaveDialog(new JFrame()); // Assume a valid file is selected
 salvor.arquivo = "test.jaw.xml";
 salvar.salvar(null, salvor.arquivo);
 }
}