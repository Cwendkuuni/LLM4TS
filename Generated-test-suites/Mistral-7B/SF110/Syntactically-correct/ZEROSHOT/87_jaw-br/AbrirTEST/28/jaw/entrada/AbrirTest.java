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

 private Abrir abrir;
 private ProcessarEntidades form;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXMLValidFile() throws IOException {
 String xmlFilePath = "src/test/resources/validXMLFile.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertFalse(abrir.xml.isEmpty());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLInvalidFile() {
 abrir = new Abrir(form, "invalidXMLFile.jaw.xml");
 fail("FileNotFoundException was expected but not thrown");
 }

 @Test(expected = FileFilter.FileFilterMatchError.class)
 public void testOpenFileDialogXMLFilter() {
 File file = new File("src/test/resources/validXMLFile.jaw");
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.setFileFilter(new Abrir().new Filtro2());
 fileChooser.showOpenDialog(null);
 assertFalse(fileChooser.isFileSelectionEmpty());
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 assertTrue(selectedFile.exists());
 assertTrue(selectedFile.isFile());
 assertTrue(selectedFile.getName().toLowerCase().endsWith(".jaw.xml"));
 }

 @Test(expected = FileNotFoundException.class)
 public void testOpenFileDialogXMLFilterInvalidFile() {
 File file = new File("src/test/resources/validXMLFile.xml");
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.setFileFilter(new Abrir().new Filtro2());
 fileChooser.showOpenDialog(null);
 assertFalse(fileChooser.isFileSelectionEmpty());
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 assertFalse(selectedFile.exists());
 }

 @Test
 public void testAbrirSerializadoValidFile() throws IOException {
 String serializadoFilePath = "src/test/resources/validSerializadoFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.setFileFilter(new Abrir().new Filtro());
 fileChooser.showOpenDialog(null);
 File selectedFile = fileChooser.getSelectedFile();
 assertFalse(selectedFile.isEmpty());
 abrir = new Abrir(form, selectedFile.getAbsolutePath());
 assertFalse(abrir.xml.isEmpty());
 }

 @Test(expected = FileNotFoundException.class)
 public void testOpenFileDialogSerializadoFilterInvalidFile() {
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.setFileFilter(new Abrir().new Filtro());
 fileChooser.showOpenDialog(null);
 assertFalse(fileChooser.isFileSelectionEmpty());
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 assertFalse(selectedFile.exists());
 }

 @Test
 public void testAbrirSerializadoInvalidFile() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.setFileFilter(new Abrir().new Filtro());
 fileChooser.showOpenDialog(null);
 File selectedFile = fileChooser.getSelectedFile();
 assertFalse(selectedFile.isEmpty());
 abrir = new Abrir(form, selectedFile.getAbsolutePath());
 assertEquals("", abrir.xml);
 }

}