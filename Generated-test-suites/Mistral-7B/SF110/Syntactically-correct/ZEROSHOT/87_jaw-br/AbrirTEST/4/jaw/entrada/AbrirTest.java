package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

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
 // Given
 String xmlFilePath = "test/jaw/entrada/resources/xmlTestFile.jaw.xml";
 File xmlFile = new File(xmlFilePath);
 // When
 abrir = new Abrir(form, xmlFile.getAbsolutePath());
 // Then
 assertNotNull(abrir.getXml());
 assertEquals("xmlTestFile", abrir.getForm().getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirInvalidXML() {
 // Given
 String invalidXMLFilePath = "test/jaw/entrada/resources/invalidXMLFile";
 // When
 abrir = new Abrir(form, invalidXMLFilePath);
 // Then
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirMissingXML() {
 // Given
 String missingXMLFilePath = "test/jaw/entrada/resources/missingXMLFile.jaw.xml";
 // When
 abrir = new Abrir(form, missingXMLFilePath);
 // Then
 fail("Expected FileNotFoundException");
 }

 @Test
 public void testAbrirSerializado() {
 // Given
 String serializadoFilePath = "test/jaw/entrada/resources/serializadoTestFile.jaw";
 // When
 abrir = new Abrir(form, Abrir.ABRIR, serializadoFilePath);
 // Then
 assertNotNull(abrir.getXml());
 assertEquals("serializadoTestFile", abrir.getForm().getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirInvalidFileType() {
 // Given
 String invalidFileTypeFilePath = "test/jaw/entrada/resources/invalidFileType";
 // When
 abrir = new Abrir(form, Abrir.ABRIR, invalidFileTypeFilePath);
 // Then
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirMissingFile() {
 // Given
 String missingFileFilePath = "test/jaw/entrada/resources/missingFile";
 // When
 abrir = new Abrir(form, Abrir.ABRIR, missingFileFilePath);
 // Then
 fail("Expected FileNotFoundException");
 }

 @Test
 public void testFileChooserXML() throws IOException {
 // Given
 Main.configuracao = new Configuracao();
 Main.configuracao.setCaminhoDosProjetos("test/jaw/entrada/resources");
 File xmlTestFile = new File("xmlTestFile.jaw.xml");
 // When
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 // Then
 assertEquals(JFileChooser.APPROVE_OPTION, abrir.getForm().getFileChooser().getDialogResult());
 assertEquals(xmlTestFile.getAbsolutePath(), abrir.getForm().getFileChooser().getSelectedFile().getAbsolutePath());
 }

 @Test
 public void testFileChooserSerializado() throws IOException {
 // Given
 Main.configuracao = new Configuracao();
 Main.configuracao.setCaminhoDosProjetos("test/jaw/entrada/resources");
 File serializadoTestFile = new File("serializadoTestFile.jaw");
 // When
 abrir = new Abrir(form, Abrir.ABRIR);
 // Then
 assertEquals(JFileChooser.APPROVE_OPTION, abrir.getForm().getFileChooser().getDialogResult());
 assertEquals(serializadoTestFile.getAbsolutePath(), abrir.getForm().getFileChooser().getSelectedFile().getAbsolutePath());
 }
}