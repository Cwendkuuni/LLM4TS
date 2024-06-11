package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private File tempDirectory;
 private File outputFile;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 tempDirectory = new File(System.getProperty("java.io.tmpdir"));
 outputFile = new File(tempDirectory, "output.jaw");
 salvar = new Salvar(null, 20);
 entidades = Arrays.asList(new Entidade("teste1", "teste1"), new Entidade("teste2", "teste2"));
 }

 @After
 public void tearDown() throws IOException {
 tempDirectory.delete();
 }

 @Test
 public void testSalvar() {
 Salvar salvar = new Salvar(null, 20);
 salvar.salvar(entidades, outputFile);
 assertTrue(outputFile.exists());
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarWithNullEntidades() {
 Salvar salvar = new Salvar(null, 20);
 salvar.salvar(null, outputFile);
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarWithNullOutputFile() {
 Salvar salvar = new Salvar(null, 20);
 salvar.salvar(entidades, null);
 }

 @Test
 public void testSalvarWithValidFile() {
 Salvar salvar = new Salvar(null, 20);
 salvar.salvar(entidades, outputFile);
 assertEquals("teste1,teste1\nteste2,teste2", salvar.xml);
 }

 @Test(expected = IOException.class)
 public void testSalvarWithInvalidFile() {
 Salvar salvar = new Salvar(null, 20);
 final File invalidFile = new File("/nonexistentdirectory/output.jaw");
 salvar.salvar(entidades, invalidFile);
 }

 @Test
 public void testSalvarXml() {
 Salvar salvar = new Salvar(null, 10);
 final String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>teste1</nome>\n<descricao>teste1</descricao>\n</entidade>\n<entidade>\n<nome>teste2</nome>\n<descricao>teste2</descricao>\n</entidade>\n</entidades>";
 salvar.xml = salvar.entidades2XML(entidades);
 assertEquals(expectedXml, salvar.xml);
 }

 @Test
 public void testSaveWithValidation() {
 Salvar salvar = new Salvar(null, 20);
 assertFalse(salvar.salvar(null, outputFile));
 }
}