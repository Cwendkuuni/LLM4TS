package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXML() {
 String xmlFilePath = "src/test/resources/jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertNotNull(abrir.getXml());
 assertEquals(xmlFilePath, form.getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithWrongFileType() {
 String xmlFilePath = "src/test/resources/wrongFileType.txt";
 abrir = new Abrir(form, xmlFilePath);
 fail("An IllegalArgumentException should be thrown when trying to open a non-XML file.");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithMissingFile() {
 String xmlFilePath = "src/test/resources/missingFile.xml";
 abrir = new Abrir(form, xmlFilePath);
 fail("A FileNotFoundException should be thrown when trying to open a missing file.");
 }

 @Test
 public void testAbrirSerializado() {
 String serializadoFilePath = "src/test/resources/serializado.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.showOpenDialog(null);
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 assertTrue(selectedFile.exists());
 assertFalse(selectedFile.isDirectory());

 fileChooser.setSelectedFile(selectedFile);
 abrir.abrirSerializado(fileChooser.getSelectedFile().getAbsolutePath());
 assertNotNull(abrir.getXml());
 assertEquals(selectedFile.getAbsolutePath(), form.getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithWrongFileType() {
 String serializadoFilePath = "src/test/resources/wrongFileType.txt";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.showOpenDialog(null);
 fileChooser.setSelectedFile(new File(serializadoFilePath));
 abrir.abrirSerializado(fileChooser.getSelectedFile().getAbsolutePath());
 fail("An IllegalArgumentException should be thrown when trying to open a non-JAW file.");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithMissingFile() {
 String serializadoFilePath = "src/test/resources/missingFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.showOpenDialog(null);
 fileChooser.setSelectedFile(new File(serializadoFilePath));
 abrir.abrirSerializado(fileChooser.getSelectedFile().getAbsolutePath());
 fail("A FileNotFoundException should be thrown when trying to open a missing file.");
 }

 @Test
 public void testOpenFileChooserWithXMLFilter() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser fileChooser = new Abrir().new Abrir(form, Abrir.ABRIR_XML);
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.showOpenDialog(null);
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 assertTrue(selectedFile.exists());
 assertFalse(selectedFile.isDirectory());
 assertFalse(selectedFile.getName().toLowerCase().endsWith(".xml"));
 selectedFile = new File(selectedFile.getParentFile(), "test.xml");
 fileChooser.setSelectedFile(selectedFile);
 abrir.abrirXML(selectedFile.getAbsolutePath());
 assertNotNull(abrir.getXml());
 assertEquals(selectedFile.getAbsolutePath(), form.getNomeArquivoSalvo());
 }

 @Test
 public void testOpenFileChooserWithJawFilter() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser fileChooser = new Abrir().new Abrir(form, Abrir.ABRIR);
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.showOpenDialog(null);
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 assertTrue(selectedFile.exists());
 assertFalse(selectedFile.isDirectory());
 assertFalse(selectedFile.getName().toLowerCase().endsWith(".xml"));
 assertFalse(selectedFile.getName().toLowerCase().endsWith(".jaw.xml"));
 selectedFile = new File(selectedFile.getParentFile(), "test.jaw");
 fileChooser.setSelectedFile(selectedFile);
 abrir.abrirSerializado(fileChooser.getSelectedFile().getAbsolutePath());
 assertNotNull(abrir.getXml());
 assertEquals(selectedFile.getAbsolutePath(), form.getNomeArquivoSalvo());
 }
}