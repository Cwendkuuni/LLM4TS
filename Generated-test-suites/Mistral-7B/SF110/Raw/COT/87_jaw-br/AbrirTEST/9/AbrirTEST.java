package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFile = "test.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertTrue(abrir.xml.isEmpty());
 // Assuming that the form.setEntidades() method sets the expected entities
 // You may need to write an assertion to check the entities here
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 String jawFile = "test.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 File selectedFile = new File("test.jaw");
 escolhedor.setSelectedFile(selectedFile);
 escolhedor.showOpenDialog(null);
 when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
 abrir.abrir(escolhedor, 20);
 assertTrue(abrir.xml.isEmpty());
 // Assuming that the form.setEntidades() method sets the expected entities
 // You may need to write an assertion to check the entities here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 abrir = new Abrir(form, "test.txt");
 abrir.abrirXML("test.txt");
 fail("An exception should have been thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File("nonexistentpath"));
 escolhedor.showOpenDialog(null);
 when(escolhedor.getSelectedFile()).thenReturn(null);
 abrir.abrir(escolhedor, 20);
 fail("An exception should have been thrown");
 }

 @Test
 public void testAbrirXMLWithEmptyDirectory() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(""));
 escolhedor.showOpenDialog(null);
 when(escolhedor.getSelectedFile()).thenReturn(null);
 abrir.abrir(escolhedor, 10);
 assertTrue(abrir.xml.isEmpty());
 }

 @Test
 public void testAbrirXMLWithNullFileChooser() {
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrir(null, 10);
 assertTrue(abrir.xml.isEmpty());
 }
}