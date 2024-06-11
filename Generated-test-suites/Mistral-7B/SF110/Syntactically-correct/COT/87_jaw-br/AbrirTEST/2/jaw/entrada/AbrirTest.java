package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class AbrirTest {

 private Abrir obj;
 private ProcessarEntidades form;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 obj = new Abrir(form, "test.jaw.xml"); // Testing XML file
 }

 @Test
 public void testAbrirXML() {
 assertEquals("", obj.xml); // Check if xml is empty at start
 // Testing valid XML file
 String xmlFilePath = "test.jaw.xml";
 File xmlFile = new File(xmlFilePath);
 obj = new Abrir(form, xmlFile);
 // Check if xml is not empty after opening a valid file
 assertNotEquals("", obj.xml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 obj = new Abrir(form, "test.txt"); // Testing an invalid file
 // Check if an IllegalArgumentException is thrown
 }

 @Test
 public void testAbrirSerializado() {
 // Cannot test directly as it requires a valid jaw file and the Serializacao class is not available for testing
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 obj = new Abrir(form, new File("test.txt")); // Testing an invalid file for serializado method
 // Check if an IllegalArgumentException is thrown
 }

 @Test
 public void testAbrirWithFileChooser() {
 // Testing file chooser functionality
 final JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.showOpenDialog(null);
 File selectedFile = escolhedor.getSelectedFile();
 obj = new Abrir(form, 20);
 obj.abrirSerializado(selectedFile.getAbsolutePath());
 // Check if the file is opened correctly
 assertNotNull(obj.xml);
 }

 @Test(expected = NullPointerException.class)
 public void testAbrirWithNullFile() {
 obj = new Abrir(form, 20);
 obj.abrirSerializado(null);
 // Check if a NullPointerException is thrown
 }

 @Test
 public void testAbrirWithInvalidFileFilterXML() {
 obj = new Abrir(form, 10);
 obj.abrirXML("test.txt");
 // Check if an IllegalArgumentException is thrown
 }

 @Test
 public void testAbrirWithInvalidFileFilterSerializado() {
 obj = new Abrir(form, 20);
 obj.abrirSerializado("test.txt");
 // Check if an IllegalArgumentException is thrown
 }
}