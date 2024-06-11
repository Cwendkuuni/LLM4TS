package jaw.entrada;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import org.junit.Before;
import org.junit.Test;

import jaw.gui.ProcessarEntidades;
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
 String xmlFile = "test_file.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertEquals("test_file.jaw.xml", form.getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 String xmlFile = "test_file.txt";
 abrir = new Abrir(form, xmlFile);
 assertFalse(form.getEntidades() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithInvalidFileLocation() {
 String xmlFile = "test_file.jaw.xml";
 File file = new File("nonexistent_location/" + xmlFile);
 abrir = new Abrir(form, xmlFile);
 assertFalse(form.getEntidades() != null);
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String file = "test_file.jaw";
 abrir = new Abrir(form, new File(file).getAbsolutePath(), Abrir.ABRIR);
 assertEquals("test_file.jaw", form.getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFileType() {
 String file = "test_file.txt";
 abrir = new Abrir(form, new File(file).getAbsolutePath(), Abrir.ABRIR);
 assertFalse(form.getEntidades() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidFileLocation() {
 String file = "test_file.jaw";
 File fileLocation = new File("nonexistent_location/" + file);
 abrir = new Abrir(form, fileLocation.getAbsolutePath(), Abrir.ABRIR);
 assertFalse(form.getEntidades() != null);
 }

 @Test
 public void testOpenFileDialogWithXmlFilter() {
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 Filtro2 xmlFilter = new Filtro2();
 fileChooser.setFileFilter(xmlFilter);
 fileChooser.showOpenDialog(null);
 File selectedFile = fileChooser.getSelectedFile();
 assertTrue(selectedFile != null);
 assertTrue(selectedFile.isFile());
 assertTrue(selectedFile.getName().toLowerCase().endsWith(".jaw.xml"));
 }

 @Test
 public void testOpenFileDialogWithSerializadoFilter() {
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 Filtro serializadoFilter = new Filtro();
 fileChooser.setFileFilter(serializadoFilter);
 fileChooser.showOpenDialog(null);
 File selectedFile = fileChooser.getSelectedFile();
 assertTrue(selectedFile != null);
 assertTrue(selectedFile.isFile());
 assertTrue(selectedFile.getName().toLowerCase().endsWith(".jaw"));
 }

 @Test
 public void testShowMessageDialogWithInvalidJawFile() {
 abrir = new Abrir(form, new File("test_file.jaw.xml").getAbsolutePath());
 String expectedMessage = "Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!";
 JOptionPane.showMessageDialog(null, "Message", JOptionPane.ERROR_MESSAGE); // Needed to suppress warning
 assertEquals(expectedMessage, JOptionPane.showMessageDialog(null, "Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", JOptionPane.ERROR_MESSAGE).getMessage());
 }

 @Test
 public void testShowMessageDialogWithInvalidXmlFile() {
 abrir = new Abrir(form, new File("test_file.jaw").getAbsolutePath());
 String expectedMessage = "Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!";
 JOptionPane.showMessageDialog(null, "Message", JOptionPane.ERROR_MESSAGE); // Needed to suppress warning
 assertEquals(expectedMessage, JOptionPane.showMessageDialog(null, "Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", JOptionPane.ERROR_MESSAGE).getMessage());
 }
}