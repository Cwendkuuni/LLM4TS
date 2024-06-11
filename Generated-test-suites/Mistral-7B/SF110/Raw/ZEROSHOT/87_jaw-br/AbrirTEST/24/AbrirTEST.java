package jaw.entrada.test;

import static org.junit.Assert.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private final String PROJECT_DIR = "project_directory";
 private final String XML_FILE = "test.xml";
 private final String JAW_FILE = "test.jaw";
 private final String INCORRECT_FILE = "test_incorrect.txt";

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 Main.configuracao = new Configuracao();
 Main.configuracao.setCaminhoDosProjetos(PROJECT_DIR);
 }

 @Test
 public void testAbrirXML() {
 String xmlFile = PROJECT_DIR + File.separator + XML_FILE;
 Abrir abrir = new Abrir(form, xmlFile);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirInvalidXML() {
 String xmlFile = PROJECT_DIR + File.separator + INCORRECT_FILE;
 Abrir abrir = new Abrir(form, xmlFile);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirJawFile() {
 String jawFile = PROJECT_DIR + File.separator + JAW_FILE;
 Abrir abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog dialog = new FileDialog(new Frame(), "Choose a file", FileDialog.OPEN);
 dialog.setDirectory(PROJECT_DIR);
 dialog.setFile("*.jaw");
 dialog.show();
 File selectedFile = dialog.getFile();
 assertNotNull(selectedFile);
 String selectedFilePath = selectedFile.getAbsolutePath();
 assertEquals(jawFile, selectedFilePath);
 try {
 abrir.abrirSerializado(selectedFilePath);
 } catch (Exception e) {
 fail("Should not throw exception");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirInvalidJawFile() {
 String jawFile = PROJECT_DIR + File.separator + INCORRECT_FILE;
 Abrir abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog dialog = new FileDialog(new Frame(), "Choose a file", FileDialog.OPEN);
 dialog.setDirectory(PROJECT_DIR);
 dialog.setFile("*.jaw");
 dialog.show();
 File selectedFile = dialog.getFile();
 assertNotNull(selectedFile);
 String selectedFilePath = selectedFile.getAbsolutePath();
 assertEquals(jawFile, selectedFilePath);
 try {
 abrir.abrirSerializado(selectedFilePath);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAbrirXMLFileDialog() {
 String xmlFile = PROJECT_DIR + File.separator + XML_FILE;
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser fileChooser = new JFileChooser();
 FileFilter xmlFilter = new FileFilter() {
 @Override
 public boolean accept(File file) {
 return file.isDirectory() || file.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 };
 fileChooser.setFileFilter(xmlFilter);
 int returnVal = fileChooser.showOpenDialog(null);
 assertEquals(JFileChooser.APPROVE_OPTION, returnVal);
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 String selectedFilePath = selectedFile.getAbsolutePath();
 assertEquals(xmlFile, selectedFilePath);
 try {
 abrir.abrirXML(selectedFilePath);
 } catch (Exception e) {
 fail("Should not throw exception");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirInvalidXMLFileDialog() {
 String xmlFile = PROJECT_DIR + File.separator + INCORRECT_FILE;
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser fileChooser = new JFileChooser();
 FileFilter xmlFilter = new FileFilter() {
 @Override
 public boolean accept(File file) {
 return file.isDirectory() || file.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 };
 fileChooser.setFileFilter(xmlFilter);
 int returnVal = fileChooser.showOpenDialog(null);
 assertEquals(JFileChooser.APPROVE_OPTION, returnVal);
 File selectedFile = fileChooser.getSelectedFile();
 assertNotNull(selectedFile);
 String selectedFilePath = selectedFile.getAbsolutePath();
 assertEquals(xmlFile, selectedFilePath);
 try {
 abrir.abrirXML(selectedFilePath);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFiltro() {
 FileFilter filtro = new Abrir().new Filtro();
 File file = new File("test_directory/test.jaw");
 assertTrue(filtro.accept(file));
 file = new File("test_directory/test.xml");
 assertTrue(filtro.accept(file));
 file = new File("test_directory/test.txt");
 assertFalse(filtro.accept(file));
 }

 @Test
 public void testFiltro2() {
 FileFilter filtro2 = new Abrir().new Filtro2();
 File file = new File("test_directory/test.jaw.xml");
 assertTrue(filtro2.accept(file));
 file = new File("test_directory/test.xml");
 assertTrue(filtro2.accept(file));
 file = new File("test_directory/test.jaw");
 assertTrue(filtro2.accept(file));
 file = new File("test_directory/test.txt");
 assertFalse(filtro2.accept(file));
 }
}