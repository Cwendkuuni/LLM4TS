package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import org.junit.Before;
import org.junit.Test;

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
 }

 @Test
 public void testAbrirXML() {
 File xmlFile = new File("src/test/java/jaw/entrada/test/test.xml");
 abrir = new Abrir(form, xmlFile.getAbsolutePath());
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirNonXMLFile() {
 File file = new File("src/test/java/jaw/entrada/test/test.txt");
 abrir = new Abrir(form, file.getAbsolutePath());
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testXMLFileReadError() {
 File xmlFile = new File("src/test/java/jaw/entrada/test/inexistent.xml");
 assertThrows(IOException.class, () -> new Abrir(form, xmlFile.getAbsolutePath()));
 }

 @Test
 public void testAbrirSerializado() {
 File serializedFile = new File("src/test/java/jaw/entrada/test/test.jaw");
 try {
 Serializacao.salvar(new Object(), serializedFile);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to serialize test object");
 }
 abrir = new Abrir(form, serializedFile.getAbsolutePath());
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirNonJawFile() {
 File file = new File("src/test/java/jaw/entrada/test/test.txt");
 abrir = new Abrir(form, file.getAbsolutePath());
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test(expected = IOException.class)
 public void testSerializadoFileReadError() {
 File serializedFile = new File("src/test/java/jaw/entrada/test/inexistent.jaw");
 assertThrows(IOException.class, () -> new Abrir(form, serializedFile.getAbsolutePath()));
 }

 @Test
 public void testFileChooserXML() throws IOException {
 File xmlFile = new File("src/test/java/jaw/entrada/test/test.xml");
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File selectedFile = new File("src/test/java/jaw/entrada/test/test.xml");
 assertTrue(abrir.getSelectedFile().equals(selectedFile));
 abrir.abrirXML(selectedFile.getAbsolutePath());
 assertNotNull(abrir.getXml());
 }

 @Test
 public void testFileChooserSerializado() throws IOException {
 File serializedFile = new File("src/test/java/jaw/entrada/test/test.jaw");
 abrir = new Abrir(form, Abrir.ABRIR);
 File selectedFile = new File("src/test/java/jaw/entrada/test/test.jaw");
 assertTrue(abrir.getSelectedFile().equals(selectedFile));
 abrir.abrirSerializado(selectedFile.getAbsolutePath());
 assertNotNull(abrir.getXml());
 }

 @Test
 public void testFileChooserNullFile() {
 abrir = new Abrir(form, Abrir.ABRIR);
 assertNull(abrir.getSelectedFile());
 }
}