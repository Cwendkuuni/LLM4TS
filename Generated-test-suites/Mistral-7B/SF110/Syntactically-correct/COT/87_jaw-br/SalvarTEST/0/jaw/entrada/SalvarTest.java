package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import org.junit.Test;
import jaw.metamodelo.*;
import jaw.gui.*;
import jaw.entidade.*;
import jaw.Salvar;
import jaw.Serializacao;
import jaw.SerializacaoXML;

public class SalvarTest {

 private ProcessarEntidades form;
 private List<Entidade> entidades;
 private Entidade entidade;
 private Salvar salvar;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<>();
 entidade = new Entidade("1", "Teste", new MetaClass("TesteClass"), new MetaAttribute("TesteAttribute", "TesteValue"));
 entidades.add(entidade);
 }

 // Testing constructor
 @Test
 public void testSalvarConstructor() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 assertNotNull(salvar);
 }

 // Testing getEntidades method
 @Test
 public void testGetEntidades() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidade(entidades);
 List<Entidade> expected = entidades;
 List<Entidade> actual = salvar.getEntidade();
 assertEquals(expected, actual);
 }

 // Testing Salvar as JAW file (normal case)
 @Test
 public void testSalvarAsJawFile() throws Exception {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidade(entidades);
 salvar.salvar();
 File expectedFile = new File(Main.configuracao.getCaminhoDosProjetos() + File.separator + "Teste.jaw");
 File actualFile = new File(salvar.getArquivo());
 assertTrue(actualFile.exists());
 assertFalse(expectedFile.exists());
 File tempFile = File.createTempFile("temp", null);
 FileUtils.copyFile(actualFile, tempFile);
 String expectedContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
 "<entidades version=\"1.0\">" +
 "<entidade id=\"1\">" +
 "<nome>Teste</nome>" +
 "<classe>" +
 "<nome>TesteClass</nome>" +
 "</classe>" +
 "<atributos>" +
 "<atributo>" +
 "<nome>TesteAttribute</nome>" +
 "<valor>TesteValue</valor>" +
 "</atributo>" +
 "</atributos>" +
 "</entidade>" +
 "</entidades>";
 String actualContent = new String(Files.readAllBytes(tempFile.toPath()));
 assertEquals(expectedContent, actualContent);
 Files.delete(tempFile);
 }

 // Testing Salvar as JAW file (null arquivo)
 @Test(expected = IllegalArgumentException.class)
 public void testSalvarAsJawFileNullArquivo() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.salvar();
 }

 // Testing Salvar as XML file (normal case)
 @Test
 public void testSalvarAsXMLFile() throws Exception {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidade(entidades);
 salvar.salvarAsXML();
 File expectedFile = new File(Main.configuracao.getCaminhoDosProjetos() + File.separator + "Teste.jaw.xml");
 File actualFile = new File(salvar.getArquivo());
 assertTrue(actualFile.exists());
 assertFalse(expectedFile.exists());
 String expectedContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
 "<entidades version=\"1.0\">" +
 "<entidade id=\"1\">" +
 "<nome>Teste</nome>" +
 "<classe>" +
 "<nome>TesteClass</nome>" +
 "</classe>" +
 "<atributos>" +
 "<atributo>" +
 "<nome>TesteAttribute</nome>" +
 "<valor>TesteValue</valor>" +
 "</atributo>" +
 "</atributos>" +
 "</entidade>" +
 "</entidades>";
 String actualContent = new String(Files.readAllBytes(actualFile.toPath()));
 assertEquals(expectedContent, actualContent);
 }

 // Testing Salvar as XML file (null arquivo)
 @Test(expected = IllegalArgumentException.class)
 public void testSalvarAsXMLFileNullArquivo() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvarAsXML();
 }

 // Testing Salvar with null entidades
 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithNullEntidades() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidade(null);
 }

 // Testing Salvar with empty entidades
 @Test
 public void testSalvarWithEmptyEntidades() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidade(Collections.emptyList());
 salvar.salvar();
 File expectedFile = new File(Main.configuracao.getCaminhoDosProjetos() + File.separator + "Teste.jaw");
 File actualFile = new File(salvar.getArquivo());
 assertFalse(actualFile.exists());
 }

 // Testing Salvar with invalid file filter (XML)
 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithInvalidFileFilterXML() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar();
 }

 // Testing Salvar with invalid file filter (JAW)
 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithInvalidFileFilterJAW() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.salvar();
 }

 // Testing Salvar with null form
 @Test(expected = NullPointerException.class)
 public void testSalvarWithNullForm() {
 Salvar salvar = new Salvar(null, Salvar.SALVAR);
 }

 // Testing Salvar with null form.getEntidades()
 @Test(expected = NullPointerException.class)
 public void testSalvarWithNullFormGetEntidades() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 form.setEntidades(null);
 }

}