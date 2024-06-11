package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFilePath = "test/resources/validXMLFile.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 String xmlFilePath = "test/resources/invalidXMLFile";
 abrir = new Abrir(form, xmlFilePath);
 fail("Should throw IllegalArgumentException with message 'Arquivo xml corrompido ou incompatível com aplicação!'");
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 String jawFilePath = "test/resources/validJawFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR, jawFilePath);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 String jawFilePath = "test/resources/invalidJawFile";
 abrir = new Abrir(form, Abrir.ABRIR, jawFilePath);
 fail("Should throw FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLWithIOException() {
 String xmlFilePath = "test/resources/ioExceptionXMLFile.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 fail("Should throw IOException");
 }

 @Test(expected = ClassCastException.class)
 public void testAbrirXMLWithInvalidXmlFormat() {
 String xmlFilePath = "test/resources/invalidXmlFormat.xml";
 abrir = new Abrir(form, xmlFilePath);
 fail("Should throw ClassCastException");
 }

 @Test
 public void testOpenFileDialogWithValidFileFilter() {
 File expectedFile = new File("test/resources/validXMLFile.jaw.xml");
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File chosenFile = abrir.getFileChooser().getSelectedFile();
 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testOpenFileDialogWithInvalidFileFilter() {
 File expectedFile = new File("test/resources/validJawFile.jaw");
 abrir = new Abrir(form, Abrir.ABRIR_SERIALIZADO);
 File chosenFile = abrir.getFileChooser().getSelectedFile();
 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testAbrirSerializadoWithInvalidFileFormat() {
 String jawFilePath = "test/resources/invalidJawFile.txt";
 abrir = new Abrir(form, Abrir.ABRIR, jawFilePath);
 assertNotNull(abrir.getXml()); // This should not be null, but the test should fail
 fail("Should throw 'Arquivo jaw corrompido ou incompatível com aplicação!'");
 }

 @Test
 public void testAbrirXMLWithEmptyFile() {
 String xmlFilePath = "test/resources/emptyXMLFile.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertNotNull(abrir.getXml()); // This should not be null, but the test should fail
 fail("Should throw 'Arquivo xml corrompido ou incompatível com aplicação!'");
 }

}