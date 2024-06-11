import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidades = new ArrayList<>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarMethodWithValidArguments() {
 // Test the Salvar method with valid arguments
 salvar.salvar(entidades, new File("test/output/testFile.jaw").getAbsolutePath());
 assertTrue(new File("test/output/testFile.jaw").exists());
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarMethodWithNullEntidades() {
 // Test the Salvar method with null entidades
 salvar.salvar(null, new File("test/output/testFile.jaw").getAbsolutePath());
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarMethodWithNullFile() {
 // Test the Salvar method with null file
 salvar.salvar(entidades, null);
 }

 @Test
 public void testSalvarMethodWithInvalidFileType() {
 // Test the Salvar method with invalid file type
 salvar.salvar(entidades, new File("test/output/testFile.txt").getAbsolutePath());
 assertFalse(new File("test/output/testFile.txt").exists());
 }

 @Test
 public void testSalvarMethodWithFileAlreadyExists() {
 // Test the Salvar method with file already exists
 salvar.salvar(entidades, new File("test/output/testFile.jaw").getAbsolutePath());
 salvar.salvar(entidades, new File("test/output/testFile.jaw").getAbsolutePath());
 assertTrue(new File("test/output/testFile.jaw").exists());
 }

 @Test(expected = IOException.class)
 public void testSerializacaoSalvarMethodWithException() {
 // Test the Serializacao.salvar method with exception
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.entidade = new ArrayList<>();
 salvar.entidade.add(new Entidade("teste", "teste"));
 salvar.xml = "";
 try {
 salvar.salvar(salvar.entidade, new File("test/output/testFile.jaw").getAbsolutePath());
 fail();
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testSerializacaoXMLMethodWithValidArguments() {
 // Test the SerializacaoXML.entidades2XML method with valid arguments
 entidades.add(new Entidade("teste1", "teste1"));
 entidades.add(new Entidade("teste2", "teste2"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 String xml = salvar.xml = salvar.entidades2XML(entidades);
 String expectedXml = "<entidades>\n" +
 "<entidade>\n" +
 "<nome>teste1</nome>\n" +
 "<descricao>teste1</descricao>\n" +
 "</entidade>\n" +
 "<entidade>\n" +
 "<nome>teste2</nome>\n" +
 "<descricao>teste2</descricao>\n" +
 "</entidade>\n" +
 "</entidades>";
 assertEquals(expectedXml, xml);
 }

 @Test(expected = NullPointerException.class)
 public void testSerializacaoXMLMethodWithNullEntidades() {
 // Test the SerializacaoXML.entidades2XML method with null entidades
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.xml = "";
 salvar.entidade = null;
 String xml = salvar.xml;
 fail();
 }

 @Test
 public void testSaveFileWithJFileChooser() {
 // Test the save file with JFileChooser
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos().toString()));
 escolhedor.setFileFilter(new Salvar().new Filtro2());
 File selectedFile = new File("test/output/testFile.jaw.xml");
 escolhedor.setSelectedFile(selectedFile);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.entidade = form.getEntidades();
 salvar.vela = true;
 int result = escolhedor.showSaveDialog(form);
 if (result == JFileChooser.APPROVE_OPTION) {
 assertEquals(selectedFile.getAbsolutePath(), salvar.arquivo);
 }
 }

 @After
 public void tearDown() {
 if (new File("test/output/testFile.jaw").exists()) {
 new File("test/output/testFile.jaw").delete();
 }
 if (new File("test/output/testFile.jaw.xml").exists()) {
 new File("test/output/testFile.jaw.xml").delete();
 }
 }

}