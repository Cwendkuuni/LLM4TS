package jaw.entrada.test;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.io.*;

import jaw.entrada.*;
import jaw.gui.*;

public class AbrirTest {

private Abrir abrir;
private ProcessarEntidades form;

@Before
public void setUp() {
form = new ProcessarEntidades();
abrir = new Abrir(form, null);
}

@Test
public void testAbrirXML() throws Exception {
String xmlFile = "test.jaw.xml";
File xmlFileLocation = new File(xmlFile);
abrir = new Abrir(form, xmlFile);
assertTrue(abrir.xml.length() > 0);
}

@Test(expected = Exception.class)
public void testAbrirInvalidXML() throws Exception {
String invalidXmlFile = "test.jaw";
File invalidXmlFileLocation = new File(invalidXmlFile);
abrir = new Abrir(form, invalidXmlFile);
fail("Exception should be thrown when opening invalid XML file");
}

@Test(expected = Exception.class)
public void testAbrirInvalidXMLFormat() throws Exception {
String invalidXmlFormatFile = "test.invalidFormat";
File invalidXmlFormatFileLocation = new File(invalidXmlFormatFile);
abrir = new Abrir(form, invalidXmlFormatFile);
fail("Exception should be thrown when opening invalid XML format file");
}

@Test
public void testAbrirSerializado() throws Exception {
String serializedFile = "test.jaw";
File serializedFileLocation = new File(serializedFile);
abrir = new Abrir(form, 20, serializedFile);
assertTrue(abrir.xml.length() > 0);
}

@Test(expected = Exception.class)
public void testAbrirSerializedInvalidFile() throws Exception {
String invalidSerializedFile = "test.jawX";
File invalidSerializedFileLocation = new File(invalidSerializedFile);
abrir = new Abrir(form, 20, invalidSerializedFile);
fail("Exception should be thrown when opening invalid serialized file");
}

@Test
public void testFileChooserFilterXML() {
JFileChooser fileChooser = new JFileChooser();
FileFilter filter = new Abrir().new Filtro2();
fileChooser.setFileFilter(filter);
File testXmlFile = new File("test.jaw.xml");
assertTrue(fileChooser.accept(testXmlFile));
}

@Test
public void testFileChooserFilterSerialized() {
JFileChooser fileChooser = new JFileChooser();
FileFilter filter = new Abrir().new Filtro();
fileChooser.setFileFilter(filter);
File testSerializedFile = new File("test.jaw");
assertTrue(fileChooser.accept(testSerializedFile));
}

@Test
public void testFileChooserFilterInvalidXML() {
JFileChooser fileChooser = new JFileChooser();
FileFilter filter = new Abrir().new Filtro2();
fileChooser.setFileFilter(filter);
File testInvalidXmlFile = new File("test.jawX");
assertFalse(fileChooser.accept(testInvalidXmlFile));
}

@Test
public void testFileChooserFilterInvalidSerialized() {
JFileChooser fileChooser = new JFileChooser();
FileFilter filter = new Abrir().new Filtro();
fileChooser.setFileFilter(filter);
File testInvalidSerializedFile = new File("test.jawX");
assertFalse(fileChooser.accept(testInvalidSerializedFile));
}
}