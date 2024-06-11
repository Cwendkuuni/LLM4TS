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
 form = new ProcessarEntidades();
 abrir = new Abrir(form, "");
 }

 // Test case for default constructor
 @Test
 public void testDefaultConstructor() {
 assertEquals("", abrir.xml);
 assertNull(abrir.form);
 }

 // Test case for constructor with File object and XML extension
 @Test
 public void testConstructorWithXMLFile() {
 String xmlFile = "test/resources/xmlFileWithXMLextension.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertNotNull(abrir.xml);
 assertNotNull(abrir.form);
 }

 // Test case for constructor with File object and JAW extension
 @Test
 public void testConstructorWithJAWFile() {
 String jawFile = "test/resources/jawFileWithJAWextension.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 assertNotNull(abrir.xml);
 assertNotNull(abrir.form);
 }

 // Test case for abrirSerializado method with valid JAW file
 @Test
 public void testAbrirSerializadoWithValidJAWFile() {
 String jawFile = "test/resources/validJAWFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR_XML, jawFile);
 assertNotNull(abrir.form.getEntidades());
 }

 // Test case for abrirSerializado method with invalid JAW file
 @Test(expected = Exception.class)
 public void testAbrirSerializadoWithInvalidJAWFile() {
 String invalidJAWFile = "test/resources/invalidJAWFile.txt";
 abrir = new Abrir(form, Abrir.ABRIR_XML, invalidJAWFile);
 }

 // Test case for abrirXML method with valid XML file
 @Test
 public void testAbrirXMLWithValidXMLFile() {
 String xmlFile = "test/resources/validXMLFile.jaw.xml";
 abrir = new Abrir(form, Abrir.ABRIR, xmlFile);
 assertNotNull(abrir.form.getEntidades());
 }

 // Test case for abrirXML method with invalid XML file
 @Test(expected = Exception.class)
 public void testAbrirXMLWithInvalidXMLFile() {
 String invalidXMLFile = "test/resources/invalidXMLFile.txt";
 abrir = new Abrir(form, Abrir.ABRIR, invalidXMLFile);
 }

 // Test case for Filtro class
 @Test
 public void testFiltroClass() {
 File directory = new File("test/resources/testDirectory");
 File xmlFile = new File("test/resources/xmlFileWithXMLextension.jaw.xml");
 File jawFile = new File("test/resources/jawFileWithJAWextension.jaw");

 assertTrue(new Abrir.Filtro().accept(directory));
 assertTrue(new Abrir.Filtro().accept(xmlFile));
 assertFalse(new Abrir.Filtro().accept(jawFile));
 }

 // Test case for Filtro2 class
 @Test
 public void testFiltro2Class() {
 File directory = new File("test/resources/testDirectory");
 File xmlFile1 = new File("test/resources/xmlFile1WithXMLextension.jaw.xml");
 File xmlFile2 = new File("test/resources/xmlFile2WithXMLextension.jaw.xml");
 File jawFile = new File("test/resources/jawFileWithJAWextension.jaw");

 assertTrue(new Abrir.Filtro2().accept(directory));
 assertTrue(new Abrir.Filtro2().accept(xmlFile1));
 assertTrue(new Abrir.Filtro2().accept(xmlFile2));
 assertFalse(new Abrir.Filtro2().accept(jawFile));
 }
}