package jaw.entrada.test;

import static org.junit.Assert.*;

import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLValidFile() {
 String xmlFile = "test/resources/valid.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLInvalidFile() {
 String xmlFile = "test/resources/invalid.txt";
 abrir = new Abrir(form, xmlFile);
 assertNull(form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoValidFile() {
 String serialFile = "test/resources/valid.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(serialFile);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoInvalidFile() {
 String serialFile = "test/resources/invalid.txt";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(serialFile);
 assertNull(form.getEntidades());
 }

 @Test
 public void testOpenFileDialogXML() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser escolhedor = new JFileChooser();
 assertSame(escolhedor, abrir.form.getFileChooser());
 }

 @Test
 public void testOpenFileDialogSerializado() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser escolhedor = new JFileChooser();
 assertNotSame(escolhedor, abrir.form.getFileChooser());
 }

 @Test
 public void testOpenXMLFileDialogReturnsNull() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File selectedFile = new File("test/resources/empty.txt");
 abrir.form.setFileChooser(new JFileChooser());
 abrir.form.getFileChooser().setSelectedFile(selectedFile);
 abrir.form.getFileChooser().showOpenDialog(null);
 assertNull(abrir.xml);
 }

 @Test
 public void testOpenSerializadoFileDialogReturnsNull() {
 abrir = new Abrir(form, Abrir.ABRIR);
 File selectedFile = new File("test/resources/empty.txt");
 abrir.form.setFileChooser(new JFileChooser());
 abrir.form.getFileChooser().setSelectedFile(selectedFile);
 abrir.form.getFileChooser().showOpenDialog(null);
 assertNull(abrir.xml);
 }

 @Test
 public void testOpenXMLFileDialogReturnsValidFile() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File selectedFile = new File("test/resources/valid.jaw.xml");
 abrir.form.setFileChooser(new JFileChooser());
 abrir.form.getFileChooser().setSelectedFile(selectedFile);
 abrir.form.getFileChooser().showOpenDialog(null);
 assertEquals(selectedFile.getAbsolutePath(), abrir.xml);
 }

 @Test
 public void testOpenSerializadoFileDialogReturnsValidFile() {
 abrir = new Abrir(form, Abrir.ABRIR);
 File selectedFile = new File("test/resources/valid.jaw");
 abrir.form.setFileChooser(new JFileChooser());
 abrir.form.getFileChooser().setSelectedFile(selectedFile);
 abrir.form.getFileChooser().showOpenDialog(null);
 assertEquals(selectedFile.getAbsolutePath(), abrir.xml);
 }
}