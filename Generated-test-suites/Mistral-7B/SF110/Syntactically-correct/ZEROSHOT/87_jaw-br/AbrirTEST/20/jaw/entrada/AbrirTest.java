package jaw.entrada.test;

import static org.junit.Assert.*;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import org.junit.Before;
import org.junit.Test;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXML() {
 String xmlFilePath = "src/test/resources/jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLInvalidFile() {
 String invalidXmlFilePath = "src/test/resources/invalidXML.txt";
 abrir = new Abrir(form, invalidXmlFilePath);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testAbrirSerializado() {
 String jawFilePath = "src/test/resources/jaw.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(jawFilePath);
 assertNotNull(form.getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoInvalidFile() {
 String invalidJawFilePath = "src/test/resources/invalidJaw.txt";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(invalidJawFilePath);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testOpenFileDialogXMLFilter() {
 FileDialog fileDialog = new FileDialog(new Frame(), "Open", FileDialog.OPEN);
 FileFilter xmlFilter = new Abrir.Filtro2();
 fileDialog.setFileFilter(xmlFilter);
 File selectedFile = new File("src/test/resources/jaw.xml.xml");
 fileDialog.setSelectedFile(selectedFile);
 fileDialog.show();
 String filePath = fileDialog.getFile();
 assertTrue(filePath != null && filePath.equals(selectedFile.getAbsolutePath()));
 }

 @Test
 public void testOpenFileDialogSerializadoFilter() {
 FileDialog fileDialog = new FileDialog(new Frame(), "Open", FileDialog.OPEN);
 FileFilter jawFilter = new Abrir.Filtro();
 fileDialog.setFileFilter(jawFilter);
 File selectedFile = new File("src/test/resources/jaw.jaw");
 fileDialog.setSelectedFile(selectedFile);
 fileDialog.show();
 String filePath = fileDialog.getFile();
 assertTrue(filePath != null && filePath.equals(selectedFile.getAbsolutePath()));
 }

 @Test
 public void testReadXMLFile() throws IOException {
 String xmlFilePath = "src/test/resources/jaw.xml";
 Reader reader = new FileReader(xmlFilePath);
 assertNotNull(reader);
 }

 @Test(expected = FileNotFoundException.class)
 public void testReadInvalidXMLFile() throws IOException {
 String invalidXmlFilePath = "src/test/resources/invalidXML.txt";
 Reader reader = new FileReader(invalidXmlFilePath);
 fail("A FileNotFoundException should have been thrown.");
 }

 @Test
 public void testXml2Entidades() throws Exception {
 String xmlFilePath = "src/test/resources/jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 abrir.abrirXML(xmlFilePath);
 assertNotNull(form.getEntidades());
 }

 @Test(expected = IOException.class)
 public void testXml2EntidadesInvalidFile() throws Exception {
 String invalidXmlFilePath = "src/test/resources/invalidXML.txt";
 abrir = new Abrir(form, invalidXmlFilePath);
 fail("An IOException should have been thrown.");
 }
}