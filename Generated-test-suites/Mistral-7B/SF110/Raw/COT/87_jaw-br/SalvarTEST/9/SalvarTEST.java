import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private static ProcessarEntidades form;
 private static Salvar salvar;

 @BeforeClass
 public static void setup() {
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 // Test for constructor with Salvar_XML argument
 @Test
 public void testSalvar_XML() {
 List<Entidade> entidade = new ArrayList<Entidade>();
 entidade.add(new Entidade());

 salvar.setEntidade(entidade);
 salvar.salvar();

 File file = new File(salvar.getArquivo());
 Assert.assertTrue(file.exists());
 }

 // Test for constructor with Salvar argument and file chooser dialog opened
 @Test
 public void testSalvar_FileDialog() {
 List<Entidade> entidade = new ArrayList<Entidade>();
 entidade.add(new Entidade());

 salvar.setEntidade(entidade);
 salvar.salvar();

 File file = new File(salvar.getArquivo());
 Assert.assertTrue(file.exists());
 }

 // Test for constructor with Salvar argument and file name provided
 @Test
 public void testSalvar_FileName() {
 List<Entidade> entidade = new ArrayList<Entidade>();
 entidade.add(new Entidade());

 salvar.setEntidade(entidade);
 salvar.setArquivo("test.jaw");
 salvar.salvar();

 File file = new File("test.jaw");
 Assert.assertTrue(file.exists());
 }

 // Test for constructor with Salvar argument and file name provided with .xml extension
 @Test
 public void testSalvarXML_FileName() {
 List<Entidade> entidade = new ArrayList<Entidade>();
 entidade.add(new Entidade());

 salvar.setEntidade(entidade);
 salvar.setArquivo("test.jaw.xml");
 salvar.salvarXML();

 File file = new File("test.jaw.xml");
 Assert.assertTrue(file.exists());
 }

 // Test for exceptions when constructor with Salvar argument is called with null entidade list
 @Test(expected = NullPointerException.class)
 public void testSalvar_NullEntidadeList() {
 salvar.salvar();
 }

 // Test for exceptions when constructor with Salvar argument is called with null file name
 @Test(expected = NullPointerException.class)
 public void testSalvar_NullFileName() {
 salvar.setArquivo(null);
 salvar.salvar();
 }

 // Test for exceptions when constructor with Salvar argument is called with null entidade list and null file name
 @Test(expected = NullPointerException.class)
 public void testSalvar_NullEntidadeListAndFileName() {
 salvar.salvar();
 }

 // Test for exceptions when constructor with Salvar_XML argument is called with null entidade list
 @Test(expected = NullPointerException.class)
 public void testSalvarXML_NullEntidadeList() {
 salvar.salvarXML();
 }

}