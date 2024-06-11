package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

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
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFile = "test/resources/xmlFile.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 String xmlFile = "test/resources/invalidXmlFile";
 abrir = new Abrir(form, xmlFile);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 String jawFile = "test/resources/validJawFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(jawFile);
 assertNotNull(form.getEntidades());
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 String jawFile = "test/resources/invalidJawFile";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(jawFile);
 fail("Should throw IOException");
 }

 @Test
 public void testAbrirWithOpenFileDialog() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File file = new File("test/resources/xmlFile.jaw.xml");
 JFileChooser chooser = new JFileChooser();
 chooser.setSelectedFile(file);
 chooser.showOpenDialog(null);
 abrir.abrir(chooser);
 assertNotNull(form.getEntidades());
 }

 @Test
 public void testAbrirWithInvalidFileType() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = new JFileChooser();
 chooser.showOpenDialog(null);
 chooser.setSelectedFile(new File("test/resources/invalidFileType"));
 abrir.abrir(chooser);
 fail("Should not open the file");
 }

}