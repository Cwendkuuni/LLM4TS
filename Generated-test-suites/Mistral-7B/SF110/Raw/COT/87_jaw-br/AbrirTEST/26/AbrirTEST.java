package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import jaw.Serializacao;
import jaw. SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLWithValidXMLFile() {
 String xmlFile = "test.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 abrir = new Abrir(form, "test.txt");
 fail("An IllegalArgumentException should be thrown when an invalid file is passed.");
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String serialFile = "test.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File file = new File(serialFile);
 try {
 file.createNewFile();
 abrir.abrirSerializado(file.getAbsolutePath());
 file.delete();
 } catch (IOException e) {
 e.printStackTrace();
 fail("An error occurred while creating a temporary file.");
 }
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado("test.txt");
 fail("An IllegalArgumentException should be thrown when an invalid file is passed.");
 }

 @Test
 public void testAbrirXMLWithFileFilter() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser fileChooser = new JFileChooser();
 File selectedFile = new File("test.jaw.xml");
 fileChooser.setSelectedFile(selectedFile);
 fileChooser.showOpenDialog(null);
 abrir.abrirXML(fileChooser.getSelectedFile().getAbsolutePath());
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = java.io.FileNotFoundException.class)
 public void testAbrirXMLWithNoFileSelected() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 fileChooser.showOpenDialog(null);
 fail("A FileNotFoundException should be thrown when no file is selected.");
 }

 @Test
 public void testFiltro2() {
 FileFilter filtro = new Abrir().new Filtro2();
 File testFile = new File("test.jaw.xml");
 assertTrue(filtro.accept(testFile));
 }

 @Test
 public void testFiltro() {
 FileFilter filtro = new Abrir().new Filtro();
 File testFile = new File("test.jaw");
 assertTrue(filtro.accept(testFile));
 }

 @After
 public void tearDown() {
 form = null;
 abrir = null;
 }
}