package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

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
 public void testAbrirXML() throws IOException {
 String xmlFilePath = "test.jaw.xml";
 File xmlFile = new File(xmlFilePath);
 abrir = new Abrir(form, xmlFile.getAbsolutePath());
 assertNotNull(abrir.xml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFileExtension() {
 File invalidXmlFile = new File("test.txt");
 abrir = new Abrir(form, invalidXmlFile.getAbsolutePath());
 }

 @Test
 public void testAbrirSerializado() throws IOException {
 String jawFilePath = "test.jaw";
 File jawFile = new File(jawFilePath);
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = spy(new JFileChooser());
 when(chooser.showOpenDialog(anyInt(), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 doReturn(chooser).when(abrir).escolhedor;
 when(chooser.getSelectedFile()).thenReturn(new File(jawFilePath));
 abrir.abrirSerializado(jawFilePath);
 assertNotNull(abrir.xml);
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidFileExtension() {
 File invalidJawFile = new File("test.txt");
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = spy(new JFileChooser());
 when(chooser.showOpenDialog(anyInt(), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(new File(invalidJawFile.getAbsolutePath()));
 abrir.abrirSerializado(invalidJawFile.getAbsolutePath());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithMissingFile() throws IOException {
 String missingJawFilePath = "test.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = spy(new JFileChooser());
 when(chooser.showOpenDialog(anyInt(), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(new File(missingJawFilePath));
 when(new Serializacao().abrir(missingJawFilePath)).thenThrow(new FileNotFoundException());
 abrir.abrirSerializado(missingJawFilePath);
 }

 @Test(expected = Exception.class)
 public void testAbrirXMLWithInvalidXML() throws IOException {
 String invalidXmlFilePath = "test.jaw.xml";
 File invalidXmlFile = new File(invalidXmlFilePath);
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = spy(new JFileChooser());
 when(chooser.showOpenDialog(anyInt(), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(new File(invalidXmlFilePath));
 abrir.abrirXML(invalidXmlFilePath);
 }

 @Test(expected = Exception.class)
 public void testAbrirXMLWithMissingXMLFile() throws IOException {
 String missingXmlFilePath = "test.jaw.xml";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = spy(new JFileChooser());
 when(chooser.showOpenDialog(anyInt(), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(new File(missingXmlFilePath));
 when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenThrow(new IOException());
 abrir.abrirXML(missingXmlFilePath);
 }
}