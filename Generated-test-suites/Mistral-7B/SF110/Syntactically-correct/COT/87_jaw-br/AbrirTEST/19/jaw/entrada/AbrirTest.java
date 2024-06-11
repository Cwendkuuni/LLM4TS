package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFile = "testData/valid.jaw.xml";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File file = new File(xmlFile);
 whenOpeningFileWithXmlFilter(file);
 assertEquals("Expected valid XML data", "Valid XML data", form.getEntidades().toString());
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 String jawFile = "testData/valid.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(jawFile);
 whenOpeningFileWithJawFilter(file);
 assertEquals("Expected valid JAW data", "Valid JAW data", form.getEntidades().toString());
 }

 @Test
 public void testAbrirXMLWithInvalidXmlFile() {
 String xmlFile = "testData/invalid.xml";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File file = new File(xmlFile);
 whenOpeningFileWithXmlFilter(file);
 assertNull("Expected null data", form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoWithInvalidJawFile() {
 String jawFile = "testData/invalid.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(jawFile);
 whenOpeningFileWithJawFilter(file);
 assertNull("Expected null data", form.getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithNullFile() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrirXML(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithNullFile() {
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(null);
 }

 @Test
 public void testOpenFileDialogWithXmlFilter() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File file = new File("testData/valid.jaw.xml");
 abrir.abrirXML(file);
 verify(form).setNomeArquivoSalvo(file.getAbsolutePath());
 }

 @Test
 public void testOpenFileDialogWithJawFilter() {
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File("testData/valid.jaw");
 abrir.abrirSerializado(file);
 verify(form).setNomeArquivoSalvo(file.getAbsolutePath());
 }

 @Test(expected = IllegalStateException.class)
 public void testOpeningFileDialogWithoutSelectingFile() {
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(null);
 }

 private void whenOpeningFileWithXmlFilter(File file) {
 try {
 abrir.abrirXML(file);
 } catch (IOException e) {
 fail("Unexpected exception when opening XML file: " + e.getMessage());
 }
 }

 private void whenOpeningFileWithJawFilter(File file) {
 try {
 abrir.abrirSerializado(file);
 } catch (IOException e) {
 fail("Unexpected exception when opening JAW file: " + e.getMessage());
 }
 }
}