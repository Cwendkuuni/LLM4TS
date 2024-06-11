package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessorEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXML() {
 String xmlFile = "test.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertTrue(abrir.xml.length() > 0);
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String serialFile = "test.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File("test.jaw");
 when(file.exists()).thenReturn(true);
 abrir.abrirSerializado(file.getAbsolutePath());
 assertNotNull(abrir.form.getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 String invalidFile = "test.txt";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(invalidFile);
 when(file.exists()).thenReturn(true);
 abrir.abrirSerializado(file.getAbsolutePath());
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testAbrirXMLWithInvalidFile() {
 String xmlInvalidFile = "test.txt";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File file = new File(xmlInvalidFile);
 when(file.exists()).thenReturn(true);
 abrir.abrirXML(file.getAbsolutePath());
 fail("IllegalArgumentException expected");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithNonExistentFile() {
 String serialNonExistentFile = "test.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(serialNonExistentFile);
 when(file.exists()).thenReturn(false);
 abrir.abrirSerializado(file.getAbsolutePath());
 fail("FileNotFoundException expected");
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLWithInvalidFile() {
 String xmlInvalidFile = "test.txt";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File file = new File(xmlInvalidFile);
 when(file.exists()).thenReturn(true);
 when(new FileReader(file).readAllLines()).thenThrow(new IOException());
 abrir.abrirXML(file.getAbsolutePath());
 fail("IOException expected");
 }

 @Test
 public void testChooseFileWithJawExtension() {
 FileFilter jawFilter = new Abrir().new Filtro();
 File file = new File("test.jaw");
 assertTrue(jawFilter.accept(file));
 }

 @Test
 public void testChooseFileWithXmlExtension() {
 FileFilter xmlFilter = new Abrir().new Filtro2();
 File file = new File("test.jaw.xml");
 assertTrue(xmlFilter.accept(file));
 }

 @After
 public void tearDown() {
 form = null;
 abrir = null;
 }
}