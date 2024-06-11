package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 form = mock(ProcessarEntidades.class);
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXMLWithValidFile() {
 String xmlFile = "test.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 verify(form).setNomeArquivoSalvo(xmlFile);
 try {
 FileReader reader = new FileReader(xmlFile);
 String expectedXml = "expectedXmlContent";
 when(new SerializacaoXML().xml2Entidades(reader)).thenReturn(new Entidades(expectedXml));
 assertEquals(expectedXml, abrir.getXml());
 } catch (FileNotFoundException e) {
 fail("File should exist for testing");
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithInvalidFile() {
 String xmlFile = "test.txt";
 abrir = new Abrir(form, xmlFile);
 try {
 FileReader reader = new FileReader(xmlFile);
 fail("FileReader should throw FileNotFoundException");
 } catch (FileNotFoundException e) {
 // expected
 }
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String arquivo = "test.jaw";
 abrir = new Abrir(form, arquivo);
 verify(form).setNomeArquivoSalvo(arquivo);
 try {
 assertEquals(new Entidades("serializedData"), abrir.getForm().getEntidades());
 } catch (Exception e) {
 fail("Should not throw exception for valid serialized file");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 String arquivo = "test.txt";
 abrir = new Abrir(form, arquivo);
 try {
 fail("IllegalArgumentException should be thrown for invalid file type");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testOpenFileDialogWithXMLFilter() {
 FileDialog fileDialog = mock(FileDialog.class);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(eq(null))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw.xml"));
 when(fileChooser.getFileFilter()).thenReturn(new Filtro2());
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 abrir.abrir(fileChooser, fileDialog);
 verify(fileChooser).setFileFilter(isA(Filtro2.class));
 verify(fileDialog).setDialogTitle("Importa XML");
 }

 @Test
 public void testOpenFileDialogWithSerializadoFilter() {
 FileDialog fileDialog = mock(FileDialog.class);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(eq(null))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw"));
 when(fileChooser.getFileFilter()).thenReturn(new Filtro());
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrir(fileChooser, fileDialog);
 verify(fileChooser).setFileFilter(isA(Filtro.class));
 verify(fileDialog).setDialogTitle("Abrir");
 }
}