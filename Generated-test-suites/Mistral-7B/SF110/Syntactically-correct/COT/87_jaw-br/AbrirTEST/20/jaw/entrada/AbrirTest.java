package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessorEntidades(); // Assuming ProcessorEntidades has a no-args constructor
 }

 // Testing constructor with String argument
 @Test
 public void testAbrirWithXmlFile() {
 String arquivo = "test.jaw.xml";
 abrir = new Abrir(form, arquivo);
 assertEquals("", abrir.xml); // xml should be initialized with an empty string
 assertTrue(form.getEntidades() != null); // form.entidades should not be null
 }

 // Testing constructor with String ending with .jaw.xml
 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithInvalidXmlFile() {
 String arquivo = "test.txt";
 abrir = new Abrir(form, arquivo);
 fail("An exception should be thrown when an invalid XML file is provided.");
 }

 // Testing constructor with String ending with .jaw
 @Test
 public void testAbrirWithJawFile() {
 String arquivo = "test.jaw";
 abrir = new Abrir(form, arquivo);
 assertEquals("", abrir.xml); // xml should be initialized with an empty string
 assertTrue(form.getEntidades() != null); // form.entidades should not be null
 }

 // Testing constructor with FileFilter.ABRIR_XML
 @Test
 public void testAbrirWithXmlFileChooser() {
 File file = new File("test.jaw.xml");
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser escolhedor = new JFileChooser();
 File selectedFile = escolhedor.showOpenDialog(null);
 assertEquals(file, selectedFile); // Selected file should be "test.jaw.xml"
 assertTrue(form.getEntidades() != null); // form.entidades should not be null
 }

 // Testing constructor with FileFilter.ABRIR
 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithInvalidFileChooser() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showOpenDialog(null);
 abrir = new Abrir(form, Abrir.ABRIR);
 fail("An exception should be thrown when an invalid file is selected using the file chooser.");
 }

 // Testing abrirSerializado method with valid jaw file
 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 String arquivo = "test.jaw";
 abrir = new Abrir(form, arquivo);
 assertNotNull(form.getEntidades()); // form.entidades should not be null
 }

 // Testing abrirSerializado method with invalid jaw file
 @Test(expected = java.lang.Exception.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 String arquivo = "test.txt";
 abrir = new Abrir(form, arquivo);
 fail("An exception should be thrown when an invalid JAW file is provided.");
 }

 // Testing abrirXML method with valid jaw.xml file
 @Test
 public void testAbrirXMLWithValidJawXmlFile() {
 String arquivo = "test.jaw.xml";
 abrir = new Abrir(form, arquivo);
 assertNotNull(form.getEntidades()); // form.entidades should not be null
 }

 // Testing abrirXML method with invalid jaw.xml file
 @Test(expected = java.lang.Exception.class)
 public void testAbrirXMLWithInvalidJawXmlFile() {
 String arquivo = "test.txt";
 abrir = new Abrir(form, arquivo);
 fail("An exception should be thrown when an invalid JAW XML file is provided.");
 }

}