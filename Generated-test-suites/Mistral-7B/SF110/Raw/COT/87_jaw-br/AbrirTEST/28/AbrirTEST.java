package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;
 @Rule
 public ExpectedException exception = ExpectedException.none();

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 }

 // Testing constructor with String argument and XML file
 @Test
 public void testAbrirXML() {
 String arquivo = "test.jaw.xml";
 abrir = new Abrir(form, arquivo);
 assertNotNull(abrir.getXml());
 }

 // Testing constructor with String argument and non-XML file
 @Test(expected = Exception.class)
 public void testAbrirNonXML() {
 String arquivo = "test.txt";
 abrir = new Abrir(form, arquivo);
 fail("Expected an Exception to be thrown but no exception was thrown");
 }

 // Testing constructor with int argument and XML file filter
 @Test
 public void testAbrirXMLFilter() throws IOException {
 JFileChooser escolhedor = new JFileChooser();
 File testFile = new File("test.jaw.xml");
 escolhedor.setSelectedFile(testFile);
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 assertNotNull(abrir.getXml());
 }

 // Testing constructor with int argument and non-XML file filter
 @Test(expected = Exception.class)
 public void testAbrirNonXMLFilter() throws IOException {
 JFileChooser escolhedor = new JFileChooser();
 File testFile = new File("test.txt");
 escolhedor.setSelectedFile(testFile);
 abrir = new Abrir(form, Abrir.ABRIR);
 fail("Expected an Exception to be thrown but no exception was thrown");
 }

 // Testing abrirSerializado method with valid JAW file
 @Test
 public void testAbrirSerializado() throws IOException, Exception {
 String arquivo = "test.jaw";
 abrir = new Abrir(form, arquivo);
 assertNotNull(abrir.getForm().getEntidades());
 }

 // Testing abrirSerializado method with invalid JAW file
 @Test(expected = Exception.class)
 public void testAbrirSerializadoInvalidFile() throws IOException, Exception {
 String arquivo = "test.invalid";
 abrir = new Abrir(form, arquivo);
 fail("Expected an Exception to be thrown but no exception was thrown");
 }

 // Testing abrirXML method with valid XML file
 @Test
 public void testAbrirXMLValidFile() throws IOException, Exception {
 String arquivo = "test.jaw.xml";
 abrir = new Abrir(form, arquivo);
 assertNotNull(abrir.getForm().getEntidades());
 }

 // Testing abrirXML method with invalid XML file
 @Test(expected = Exception.class)
 public void testAbrirXMLInvalidFile() throws IOException, Exception {
 String arquivo = "test.invalid.xml";
 abrir = new Abrir(form, arquivo);
 fail("Expected an Exception to be thrown but no exception was thrown");
 }

 // Testing exception handling when no file is chosen
 @Test
 public void testNoFileChosen() {
 abrir = new Abrir(form, Abrir.ABRIR);
 assertEquals("", abrir.getXml());
 }

 // Testing exception handling when wrong file type is chosen
 @Test(expected = Exception.class)
 public void testWrongFileType() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 exception.expect(Exception.class);
 abrir.abrirSerializado("test.txt");
 }
}