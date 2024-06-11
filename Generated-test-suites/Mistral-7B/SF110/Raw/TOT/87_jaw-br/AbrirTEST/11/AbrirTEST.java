import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
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
 public void testAbrirXMLWithValidFile() {
 String xmlFilePath = "testFiles/validXMLFile.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String serializadoFilePath = "testFiles/validFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = new JFileChooser();
 chooser.setSelectedFile(new File(serializadoFilePath));
 chooser.showOpenDialog(null);
 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = Exception.class)
 public void testAbrirXMLWithInvalidFile() {
 String xmlFilePath = "testFiles/invalidXMLFile.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 fail("Expected an Exception to be thrown but none was");
 }

 @Test(expected = Exception.class)
 public void testAbrirSerializadoWithInvalidFile() {
 String serializadoFilePath = "testFiles/invalidFile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = new JFileChooser();
 chooser.setSelectedFile(new File(serializadoFilePath));
 chooser.showOpenDialog(null);
 fail("Expected an Exception to be thrown but none was");
 }

 @Test
 public void testFileChooserFilter() {
 JFileChooser chooser = new Abrir(form, Abrir.ABRIR).getFileChooser();
 File[] expectedFiles = new File[]{"testFiles/validFile.jaw", "testFiles/validXMLFile.jaw.xml"};
 File[] files = chooser.getFiles();
 assertArrayEquals(expectedFiles, files);
 }

 @Test
 public void testFileChooserDescription() {
 JFileChooser chooser = new Abrir(form, Abrir.ABRIR).getFileChooser();
 String expectedDescription = "JAW files";
 assertEquals(expectedDescription, chooser.getFileFilter().getDescription());
 }
}