package jaw.entrada.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jaw.entida.Entidade;
import jaw.entida.ProcessarEntidades;
import jaw.gui.Main;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 form.setEntidades(new ArrayList<Entidade>());
 salvar = new Salvar(form, 20);
 }

 @Test
 public void testSalvarWithValidParametersAndValidFileChooserSelection() {
 // Arrange
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2");
 form.getEntidades().add(entidade1);
 form.getEntidades().add(entidade2);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(new File("path/to/valid/file.jaw"));
 salvar.vela = true;

 // Act
 salvar.Salvar(form, fileChooser);

 // Assert
 assertTrue(salvar.entidade.size() == 2);
 assertFalse(salvar.vela);
 File savedFile = new File(salvar.arquivo);
 assertTrue(savedFile.exists());
 }

 @Test
 public void testSalvarWithValidParametersAndEmptyFileChooserSelection() {
 // Arrange
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2");
 form.getEntidades().add(entidade1);
 form.getEntidades().add(entidade2);
 JFileChooser fileChooser = new JFileChooser();

 // Act
 salvar.Salvar(form, fileChooser);

 // Assert
 assertTrue(salvar.entidade.size() == 2);
 assertFalse(salvar.vela);
 assertNull(salvar.arquivo);
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidParametersAndNullEntidadesList() {
 // Arrange
 JFileChooser fileChooser = new JFileChooser();
 salvar.vela = true;

 // Act and Assert
 salvar.Salvar(form, fileChooser);
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidParametersAndNullFileFilter() {
 // Arrange
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2");
 form.getEntidades().add(entidade1);
 form.getEntidades().add(entidade2);
 JFileChooser fileChooser = new JFileChooser();
 salvar.vela = false;

 // Act and Assert
 salvar.Salvar(form, fileChooser);
 }

 @Test
 public void testExportarComValidParametersAndValidFileChooserSelectionXml() {
 // Arrange
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2");
 form.getEntidades().add(entidade1);
 form.getEntidades().add(entidade2);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jaw>\n<entidade>\n<id>1</id>\n<nome>Teste1</nome>\n<descricao>Teste1</descricao>\n<tipo>Teste1</tipo>\n</entidade>\n<entidade>\n<id>2</id>\n<nome>Teste2</nome>\n<descricao>Teste2</descricao>\n<tipo>Teste2</tipo>\n</entidade>\n</jaw>";
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(new File("path/to/valid/file.jaw.xml"));
 salvar.vela = true;

 // Act
 salvar.exportarXml(form, fileChooser);

 // Assert
 assertTrue(salvar.entidade.size() == 2);
 assertFalse(salvar.vela);
 File savedFile = new File(salvar.arquivo);
 assertTrue(savedFile.exists());
 try {
 FileReader reader = new FileReader(savedFile);
 String actualXml = new String(new byte[reader.available()]);
 reader.close();
 assertEquals(expectedXml, actualXml);
 } catch (IOException ex) {
 // Ignore
 }
 }

 @Test(expected = IOException.class)
 public void testExportarComInvalidParametersAndEmptyFileChooserSelectionXml() {
 // Arrange
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2");
 form.getEntidades().add(entidade1);
 form.getEntidades().add(entidade2);
 JFileChooser fileChooser = new JFileChooser();

 // Act and Assert
 salvar.exportarXml(form, fileChooser);
 }

 @Test
 public void testSalvarComValidParametersAndValidFileChooserSelectionWithoutFileExtension() {
 // Arrange
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1");
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2");
 form.getEntidades().add(entidade1);
 form.getEntidades().add(entidade2);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(new File("path/to/valid/file"));
 salvar.vela = true;

 // Act
 salvar.salvar(form, fileChooser);

 // Assert
 assertTrue(salvar.entidade.size() == 2);
 assertFalse(salvar.vela);
 File savedFile = new File(salvar.arquivo);
 assertTrue(savedFile.exists());
 assertTrue(savedFile.getName().endsWith(".jaw"));
 }
}