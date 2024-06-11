package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.util.Serializacao;
import jaw.util.SerializacaoXML;

public class AbrirTest {

 private Abrir abrir;
 private ProcessarEntidades form;
 private Main configuracao;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 form = mock(ProcessarEntidades.class);
 configuracao = mock(Main.class);
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirWithXmlFile() {
 String xmlFilePath = "test.jaw.xml";
 when(configuracao.getCaminhoDosProjetos()).thenReturn(new File("testPath"));
 given(form.setEntidades(anyList())).willAnswer(invocation -> invocation.getArgument(0));
 abrir = new Abrir(form, Abrir.ABRIR_XML, new File(xmlFilePath));
 assertTrue(abrir.xml.isEmpty()); // xml should be empty before opening the file

 // Test opening xml file
 abrir.abrirXML(xmlFilePath);
 verify(form, times(1)).setEntidades(anyList()); // form should be set with the loaded entidades
 assertFalse(abrir.xml.isEmpty()); // xml should not be empty after opening the file
 }

 @Test
 public void testAbrirWithInvalidXmlFile() {
 String xmlFilePath = "test.xml";
 when(configuracao.getCaminhoDosProjetos()).thenReturn(new File("testPath"));
 abrir = new Abrir(form, Abrir.ABRIR_XML, new File(xmlFilePath));
 assertFalse(abrir.xml.isEmpty()); // xml should still be empty because the file is not valid

 // Test opening invalid xml file
 abrir.abrirXML(xmlFilePath);
 verify(form, never()).setEntidades(anyList()); // form should not be set with the loaded entidades
 assertEquals("Arquivo xml corrompido ou incompatível com aplicação!", JOptionPane.showMessageDialog(null, anyString(), anyString(), 0));
 }

 @Test
 public void testAbrirWithJawFile() {
 String jawFilePath = "test.jaw";
 when(configuracao.getCaminhoDosProjetos()).thenReturn(new File("testPath"));
 given(form.setEntidades(anyList())).willAnswer(invocation -> invocation.getArgument(0));
 abrir = new Abrir(form, Abrir.ABRIR, new File(jawFilePath));
 assertTrue(abrir.xml.isEmpty()); // xml should be empty before opening the file

 // Test opening jaw file
 abrir.abrirSerializado(jawFilePath);
 verify(form, times(1)).setEntidades(anyList()); // form should be set with the loaded entidades
 assertFalse(abrir.xml.isEmpty()); // xml should not be empty after opening the file
 }

 @Test(expected = IOException.class)
 public void testAbrirWithInvalidJawFile() {
 String jawFilePath = "test.jawx";
 when(configuracao.getCaminhoDosProjetos()).thenReturn(new File("testPath"));
 abrir = new Abrir(form, Abrir.ABRIR, new File(jawFilePath));
 assertTrue(abrir.xml.isEmpty()); // xml should still be empty because the file is not valid

 // Test opening invalid jaw file
 abrir.abrirSerializado(jawFilePath);
 verify(form, never()).setEntidades(anyList()); // form should not be set with the loaded entidades
 }

 @Test
 public void testOpenFileChooserWithXmlFilter() {
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(fileChooser);
 given(fileChooser.showOpenDialog(any())).willReturn(JFileChooser.APPROVE_OPTION);
 File xmlFile = new File("test.jaw.xml");
 when(fileChooser.getSelectedFile()).thenReturn(xmlFile);
 when(configuracao.getCaminhoDosProjetos()).thenReturn(new File("testPath"));
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 assertTrue(abrir.xml.isEmpty()); // xml should be empty before opening the file
// Test opening file chooser with xml filter
 abrir.abrirXML();
 verify(form, times(1)).setEntidades(anyList()); // form should be set with the loaded entidades
 assertFalse(abrir.xml.isEmpty()); // xml should not be empty after opening the file
 }

 @Test
 public void testOpenFileChooserWithInvalidFilter() {
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(fileChooser);
 given(fileChooser.showOpenDialog(any())).willReturn(JFileChooser.APPROVE_OPTION);
 File xmlFile = new File("test.jaw");
 when(fileChooser.getSelectedFile()).thenReturn(xmlFile);
 when(configuracao.getCaminhoDosProjetos()).thenReturn(new File("testPath"));
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 assertTrue(abrir.xml.isEmpty()); // xml should still be empty because the file is not valid
 // Test opening file chooser with invalid filter
 abrir.abrirXML();
 verify(form, never()).setEntidades(anyList()); // form should not be set with the loaded entidades
 assertEquals("Arquivo xml corrompido ou incompatível com aplicação!", JOptionPane.showMessageDialog(null, anyString(), anyString(), 0));
 }
}