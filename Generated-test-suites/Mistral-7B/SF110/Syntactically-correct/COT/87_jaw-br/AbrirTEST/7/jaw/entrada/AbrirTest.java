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
 public void setup() {
 form = new ProcessorEntidades(); // Assuming ProcessorEntidades has a no-args constructor
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFile = "test.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFileType() {
 String invalidFile = "test.txt";
 abrir = new Abrir(form, invalidFile);
 assertNotNull(abrir.getXml()); // This should not happen as it should throw an exception
 fail("Expected IllegalArgumentException but got none");
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String serializadoFile = "test.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(serializadoFile);
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithNonExistentFile() {
 String nonExistentFile = "nonexistentfile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(nonExistentFile);
 fail("Expected FileNotFoundException but got none");
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 String invalidXmlFile = "test.xml.jaw";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrirXML(invalidXmlFile);
 fail("Expected IOException but got none");
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFile = "test.jaw.xml";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrirXML(xmlFile);
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = FileFilter.FileTypeNotFoundException.class)
 public void testOpenFileChooserWithInvalidFilter() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = new JFileChooser();
 chooser.setFileFilter(new Abrir().new Filtro3()); // Assuming Filtro3 is an invalid filter
 int retValue = chooser.showOpenDialog(null);
 fail("Expected FileFilter.FileTypeNotFoundException but got none");
 }

 @Test
 public void testOpenFileChooserWithValidFilter() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = new JFileChooser();
 int retValue = chooser.showOpenDialog(null);
 assertTrue(retValue == JFileChooser.APPROVE_OPTION);
 }

 @Test
 public void testAbrirXMLWithEmptyFile() {
 String emptyXmlFile = "testEmpty.jaw.xml";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrirXML(emptyXmlFile);
 assertNotNull(abrir.getForm().getEntidades());
 assertTrue(abrir.getForm().getEntidades().isEmpty());
 }

}