package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @SuppressWarnings("static-access")
 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFilePath = "test.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertEquals("test.jaw.xml", form.getNomeArquivoSalvo());
 // Add assertions to check the contents of form.getEntidades()
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFileType() {
 String invalidFileType = "test.txt";
 abrir = new Abrir(form, invalidFileType);
 fail("An exception should have been thrown when an invalid file type was passed.");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithInvalidFile() {
 String invalidFile = "test.jaw.xml1234";
 abrir = new Abrir(form, invalidFile);
 fail("An exception should have been thrown when an invalid file was passed.");
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String serializadoFilePath = "test.jaw";
 abrir = new Abrir(form, 20, new File(serializadoFilePath));
 assertEquals("test.jaw", form.getNomeArquivoSalvo());
 // Add assertions to check the contents of form.getEntidades()
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidTipoAbrir() {
 abrir = new Abrir(form, 30, null);
 fail("An exception should have been thrown when an invalid tipoAbrir was passed.");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidFile2() {
 abrir = new Abrir(form, 20, new File("test.jaw1234"));
 fail("An exception should have been thrown when an invalid file was passed.");
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLWithIOException() {
 String xmlFilePathWithIOException = "test.jaw.xmlIOException";
 try {
 new Abrir(form, xmlFilePathWithIOException);
 fail("An IOException should have been thrown when reading the XML file.");
 } catch (IOException e) {
 // Add assertions to check the error message
 }
 }

 @Test(expected = ClassNotFoundException.class)
 public void testAbrirXMLWithClassNotFoundException() {
 String xmlFilePathWithClassNotFoundException = "test.jaw.xmlClassNotFoundException";
 try {
 new Abrir(form, xmlFilePathWithClassNotFoundException);
 fail("A ClassNotFoundException should have been thrown when deserializing the XML file.");
 } catch (ClassNotFoundException e) {
 // Add assertions to check the error message
 }
 }

}