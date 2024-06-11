package jaw.entrada;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jaw.gui.ProcessarEntidades;
import jaw.util.Serializacao;
import jaw.util.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXML() {
 String xmlFilePath = "test/test.jaw.xml";
 File xmlFile = new File(xmlFilePath);
 assertFalse(xmlFile.exists()); // Test file not exists before test

 abrir = new Abrir(form, xmlFilePath);
 assertTrue(xmlFile.exists()); // Test file should be created

 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLFileNotFound() {
 abrir = new Abrir(form, "test/nonexistent.xml");
 fail("AbrirXML should have thrown FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLIOException() {
 abrir = new Abrir(form, "test/ioexception.xml");
 fail("AbrirXML should have thrown IOException");
 }

 @Test(expected = Exception.class)
 public void testAbrirXMLException() {
 abrir = new Abrir(form, "test/exception.xml");
 fail("AbrirXML should have thrown Exception");
 }

 @Test
 public void testAbrirSerializado() {
 String serializadoFilePath = "test/test.jaw";
 File serializadoFile = new File(serializadoFilePath);
 assertFalse(serializadoFile.exists()); // Test file not exists before test

 abrir = new Abrir(form, Abrir.ABRIR, serializadoFilePath);
 assertTrue(serializadoFile.exists()); // Test file should be created

 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = HeadlessException.class)
 public void testAbrirJFileChooserHeadlessException() {
 System.setProperty("java.awt.headless", "true");
 abrir = new Abrir(form, Abrir.ABRIR, "test/test.jaw");
 fail("Abrir should have thrown HeadlessException");
 System.setProperty("java.awt.headless", "false");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirInvalidTipoAbrir() {
 abrir = new Abrir(form, -1);
 fail("Abrir should have thrown IllegalArgumentException");
 }
}