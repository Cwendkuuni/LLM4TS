package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.*;
import java.io.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private Entidade entidade;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidade = new Entidade();
 entidades = new ArrayList<>();
 entidades.add(entidade);
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @Test
 public void testSalvar() {
 // Test null case
 salvar.setEntidades(null);
 assertFalse(salvar.salvar(entidades, "test.jaw"));

 // Test empty case
 salvar.setEntidades(new ArrayList<>());
 assertFalse(salvar.salvar(entidades, "test.jaw"));

 // Test saving file with valid data
 salvar.setEntidades(entidades);
 File tempFile = new File("test.jaw");
 assertTrue(salvar.salvar(entidades, tempFile.getAbsolutePath()));
 assertFalse(tempFile.exists()); // File should be saved to project directory, not temporary directory

 // Test saving file with invalid extension
 salvar.setEntidades(entidades);
 File tempFileInvalid = new File("test.txt");
 assertFalse(salvar.salvar(entidades, tempFileInvalid.getAbsolutePath()));
 }

 @Test(expected = NullPointerException.class)
 public void testNullFileChooser() {
 salvar = new Salvar(form, Salvar.SALVAR_COMO);
 salvar.arquivo = null;
 salvar.salvar(entidades, null);
 }

 @Test
 public void testArquivoNull() {
 salvar = new Salvar(form, Salvar.SALVAR_COMO);
 salvar.arquivo = null;
 salvar.salvar(entidades, "");
 assertFalse(salvar.vela);
 }

 @Test
 public void testArquivoEmpty() {
 salvar = new Salvar(form, Salvar.SALVAR_COMO);
 salvar.arquivo = "";
 salvar.salvar(entidades, "");
 assertFalse(salvar.vela);
 }

 @Test
 public void testSaveWithInvalidExtension() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.arquivo = "test.txt";
 salvar.salvar(entidades, salvar.arquivo);
 assertFalse(salvar.vela);
 }

 @Test
 public void testSaveWithValidExtensionButNoFile() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.arquivo = "test.jaw";
 salvar.salvar(entidades, salvar.arquivo);
 assertFalse(salvar.vela);
 }

 @Test
 public void testSaveWithValidExtensionAndFile() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.arquivo = "test.jaw";
 salvar.salvar(entidades, salvar.arquivo);
 assertTrue(salvar.vela);
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileWriterException() throws FileNotFoundException {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 File file = new File("test.xml");
 FileWriter writer = new FileWriter(file);
 writer.write("test");
 writer.close();
 salvar.xml = "test";
 salvar.saveXML(salvar.xml, file.getAbsolutePath());
 }

 @Test
 public void testSaveXML() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 File tempFile = new File("test.xml");
 salvar.xml = "<Test></Test>";
 salvar.saveXML(salvar.xml, tempFile.getAbsolutePath());
 assertTrue(tempFile.exists());
 }

 @Test
 public void testSaveXMLWithEmptyXML() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.xml = "";
 salvar.saveXML(salvar.xml, "test.xml");
 assertFalse(new File("test.xml").exists());
 }

}