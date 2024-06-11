package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private Abrir abrir;
 private ProcessarEntidades form;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, "test.jaw");
 }

 @Test
 public void testAbrirXMLWithValidFile() {
 String expectedXml = "Expected XML data";
 File xmlFile = new File("test.jaw.xml");
 abrir = new Abrir(form, xmlFile);
 assertEquals(expectedXml, abrir.xml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 abrir = new Abrir(form, new File("test.txt"));
 fail("Should throw IllegalArgumentException with invalid XML file");
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 abrir = new Abrir(form, new File("test.jaw"));
 assertNotNull(abrir.form.getEntidades());
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 abrir = new Abrir(form, new File("test.txt"));
 fail("Should throw IOException with invalid JAW file");
 }

 @Test
 public void testOpenFileDialogWithXmlFilter() {
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File("path/to/projects"));
 chooser.setFileFilter(new Abrir.Filtro2());
 chooser.showOpenDialog(null);
 File selectedFile = chooser.getSelectedFile();
 assertTrue(selectedFile.getName().toLowerCase().endsWith(".jaw.xml"));
 }

 @Test
 public void testOpenFileDialogWithJawFilter() {
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File("path/to/projects"));
 chooser.setFileFilter(new Abrir.Filtro());
 chooser.showOpenDialog(null);
 File selectedFile = chooser.getSelectedFile();
 assertTrue(selectedFile.getName().toLowerCase().endsWith(".jaw"));
 }

 @Test
 public void testOpenFileDialogWithNoFileSelected() {
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File("path/to/projects"));
 chooser.showOpenDialog(null);
 assertNull(chooser.getSelectedFile());
 }
}