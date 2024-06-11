package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidades = new ArrayList<>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @Test
 public void testSalvarMethodWithValidArguments() {
 // Given
 entidades.add(MetamodeloFactory.createEntidade("teste", "teste"));
 salvar.entidade = entidades;
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File("C:/test"));
 escolhedor.showSaveDialog(form);
 File file = escolhedor.getSelectedFile();
 String expectedAbsolutePath = "C:/test/test.jaw";
 // When
 salvar.arquivo = file.getAbsolutePath();
 salvar.vela = true;
 // Then
 assertTrue(salvar.salvar(entidades, arquivo));
 assertEquals(expectedAbsolutePath, salvar.arquivo);
 }

 @Test(expected = Exception.class)
 public void testSalvarMethodWithNullFileChooser() {
 // Given
 entidades.add(MetamodeloFactory.createEntidade("teste", "teste"));
 salvar.entidade = entidades;
 // When
 salvar.vela = true;
 // Then
 assertNull(salvar.arquivo);
 salvar.salvar(entidades, null);
 }

 @Test(expected = Exception.class)
 public void testSalvarMethodWithNullEntidadeList() {
 // Given
 salvar = new Salvar(form, Salvar.SALVAR);
 // When
 // Then
 assertNull(salvar.entidade);
 salvar.salvar(null, new File("test.jaw"));
 }

 @Test
 public void testSalvarMethodWithInvalidFileExtension() {
 // Given
 entidades.add(MetamodeloFactory.createEntidade("teste", "teste"));
 salvar.entidade = entidades;
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File("C:/test"));
 escolhedor.showSaveDialog(form);
 File file = escolhedor.getSelectedFile();
 file.setName(file.getName() + ".txt");
 String expectedAbsolutePath = "C:/test/test.txt";
 // When
 salvar.arquivo = file.getAbsolutePath();
 salvar.vela = true;
 // Then
 assertFalse(salvar.salvar(entidades, arquivo));
 assertEquals(expectedAbsolutePath, salvar.arquivo);
 }

 @Test
 public void testSalvarMethodWithNullEntidadeListAndInvalidFileExtension() {
 // Given
 salvar = new Salvar(form, Salvar.SALVAR);
 // When
 // Then
 assertNull(salvar.entidade);
 File file = new File("test.txt");
 salvar.arquivo = file.getAbsolutePath();
 salvar.vela = true;
 assertFalse(salvar.salvar(null, file));
 }

 @Test(expected = IOException.class)
 public void testSalvarMethodWithIOException() {
 // Given
 entidades.add(MetamodeloFactory.createEntidade("teste", "teste"));
 salvar.entidade = entidades;
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File("C:/test"));
 escolhedor.showSaveDialog(form);
 File file = escolhedor.getSelectedFile();
 String expectedAbsolutePath = "C:/test/test.jaw";
 // When
 salvar.arquivo = file.getAbsolutePath();
 salvar.vela = true;
 FileWriter writer = new FileWriter(file);
 writer.write("Teste");
 writer.close();
 // Then
 assertFalse(salvar.salvar(entidades, arquivo));
 assertEquals(expectedAbsolutePath, salvar.arquivo);
 }

 @Test
 public void testSalvarXMLMethodWithValidArguments() {
 // Given
 entidades.clear();
 entidades.add(MetamodeloFactory.createEntidade("teste", "teste"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 // When
 salvar.xml = "<Entidades>" + "<Entidade>" + "<Nome>teste</Nome>" + "</Entidade>" + "</Entidades>"; // Serialize to XML String
 String expectedAbsolutePath = "C:/test/test.jaw.xml";
 // Then
 assertTrue(salvar.salvarXML(entidades, "test.jaw.xml"));
 assertEquals(expectedAbsolutePath, salvar.arquivo);
 }

 @Test(expected = IOException.class)
 public void testSalvarXMLMethodWithIOException() {
 // Given
 entidades.clear();
 entidades.add(MetamodeloFactory.createEntidade("teste", "teste"));
salvar = new Salvar(form, Salvar.SALVAR_XML);
 // When
 String xml = "<Entidades>" + "<Entidade>" + "<Nome>teste</Nome>" + "</Entidade>" + "</Entidades>"; // Serialize to XML String
 File file = new File("test.jaw.xml");
 // Then
 assertFalse(salvar.salvarXML(entidades, file));
 }

 @Test
 public void testSalvarXMLMethodWithValidArgumentsAndFileWriter() {
 // Given
 entidades.clear();
 entidades.add(MetamodeloFactory.createEntidade("teste", "teste"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 // When
 String xml = "<Entidades>" + "<Entidade>" + "<Nome>teste</Nome>" + "</Entidade>" + "</Entidades>"; // Serialize to XML String
 try {
 FileWriter writer = new FileWriter("test.jaw.xml");
 salvar.xml = xml;
 assertTrue(salvar.salvarXML(entidades, "test.jaw.xml"));
 } catch (IOException e) {
 fail("IOException occurred while writing to file.");
 }
 // Then
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Entidades>\n<Entidade>\n<Nome>teste</Nome>\n</Entidade>\n</Entidades>", salvar.xml);
 }

}