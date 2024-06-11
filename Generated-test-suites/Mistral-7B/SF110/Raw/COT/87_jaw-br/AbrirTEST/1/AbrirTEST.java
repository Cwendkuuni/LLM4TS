package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, "test.jaw.xml"); // Testing xml file
 }

 @Test
 public void testAbrirXML() {
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 abrir = new Abrir(form, "test.txt"); // Testing invalid file
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirSerializado() {
 abrir = new Abrir(form, 20); // Testing serializado method
 assertNotNull(abrir.xml);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 abrir = new Abrir(form, 20, "test.txt"); // Testing invalid file with serializado method
 fail("Should throw FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithIOException() {
 abrir = new Abrir(form, 20, "test.jaw"); // Testing IOException in serializado method
 fail("Should throw IOException");
 }

 @Test
 public void testAbrirWithFileChooser() {
 abrir = new Abrir(form, 20); // Testing file chooser method
 assertNotNull(abrir.xml);
 }

 @Test(expected = NullPointerException.class)
 public void testAbrirWithNullFileChooser() {
 abrir = new Abrir(form, 20, null); // Testing null file chooser
 fail("Should throw NullPointerException");
 }

 @Test
 public void testAbrirXMLWithEmptyFile() {
 abrir = new Abrir(form, "testEmpty.jaw.xml"); // Testing empty xml file
 assertTrue(abrir.xml.length() == 0);
 }

 @Test
 public void testFiltro2AcceptsXmlFile() {
 File file = new File("test.jaw.xml");
 assertTrue(new Abrir().new Filtro2().accept(file));
 }

 @Test
 public void testFiltro2AcceptsDirectory() {
 File directory = new File("testDirectory");
 assertTrue(new Abrir().new Filtro2().accept(directory));
 }

 @Test
 public void testFiltroAcceptsJawFile() {
 File file = new File("test.jaw");
 assertTrue(new Abrir().new Filtro().accept(file));
 }

 @Test
 public void testFiltroAcceptsDirectoryJawFile() {
 File directory = new File("testDirectory");
 File jawFile = new File(directory, "test.jaw");
 assertTrue(new Abrir().new Filtro().accept(jawFile));
 }

}