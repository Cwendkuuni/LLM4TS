package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloPackage;
import jaw.metamodelo.impl.MetamodeloPackageImpl;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;

 @Before
 public void setup() {
 MetamodeloPackage package1 = new MetamodeloPackageImpl();
 form = new ProcessarEntidades(package1);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarWithValidXMLFile() {
 // Test case for saving an XML file with valid data
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 entidades.add(entidade1);
 salvar.entidade = entidades;
 String expectedPath = "test.jaw.xml"; // Replace with valid test path
 salvar.arquivo = expectedPath;
 salvar.xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Metamodelo>\n<Entidade>\n<Nome>Teste1</Nome>\n<Descricao>Teste1</Descricao>\n<Tipo>Teste1</Tipo>\n</Entidade>\n</Metamodelo>";

 assertFalse(salvar.vela);
 salvar.salvar();
 assertTrue(new File(salvar.arquivo).exists());
 }

 @Test
 public void testSalvarWithInvalidXMLFile() {
 // Test case for saving an XML file with invalid data
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 entidades.add(entidade1);
 salvar.entidade = entidades;
 String expectedPath = "test.jaw.xml"; // Replace with valid test path
 salvar.arquivo = expectedPath;
 salvar.xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Metamodelo>\n<Entidade>\n<Nome>Teste1</Nome>\n<Descricao>Teste1\n<Tipo>Teste1\n</Entidade>\n</Metamodelo>";

 assertFalse(salvar.vela);
 assertThrows(IOException.class, () -> salvar.salvar());
 assertNull(new File(salvar.arquivo).getAbsoluteFile());
 }

 @Test
 public void testSalvarWithValidJawFile() {
 // Test case for saving a Jaw file with valid data
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 entidades.add(entidade1);
 salvar.entidade = entidades;
 String expectedPath = "test.jaw"; // Replace with valid test path
 salvar.arquivo = expectedPath;

 assertFalse(salvar.vela);
 salvar.salvar();
 assertTrue(new File(salvar.arquivo).exists());
 }

 @Test(expected = IOException.class)
 public void testSalvarWithInvalidJawFile() {
 // Test case for saving a Jaw file with invalid data
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 entidades.add(entidade1);
 salvar.entidade = entidades;
 String expectedPath = "test.jaw"; // Replace with valid test path
 salvar.arquivo = expectedPath;
 salvar.vela = true;

 assertThrows(IOException.class, () -> salvar.salvar());
 assertNull(new File(salvar.arquivo).getAbsoluteFile());
 }

 @Test
 public void testSalvarWithNoFileChosen() {
 // Test case for saving a file with no file chosen
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 entidades.add(entidade1);
 salvar.entidade = entidades;
 String expectedPath = "";
 salvar.arquivo = expectedPath;
 salvar.vela = true;

 assertFalse(salvar.vela);
 salvar.salvar();
 assertFalse(new File(salvar.arquivo).exists());
 }

 @Test
 public void testSalvarWithNullFileChosen() {
 // Test case for saving a file with a null file chosen
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 entidades.add(entidade1);
 salvar.entidade = entidades;
 String expectedPath = null;
 salvar.arquivo = expectedPath;
 salvar.vela = true;

 assertFalse(salvar.vela);
 assertThrows(NullPointerException.class, () -> salvar.salvar());
 assertNull(new File(salvar.arquivo).getAbsoluteFile());
 }

 @Test
 public void testSalvarWithNoEntidades() {
 // Test case for saving a file when there are no Entidades
 salvar.entidade = null;
 String expectedPath = "";
 salvar.arquivo = expectedPath;
 salvar.vela = true;

 assertFalse(salvar.vela);
 salvar.salvar();
 assertFalse(new File(salvar.arquivo).exists());
 }

}