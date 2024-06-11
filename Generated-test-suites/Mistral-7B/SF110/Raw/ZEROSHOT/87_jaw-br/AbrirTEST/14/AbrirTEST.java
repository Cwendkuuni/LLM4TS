package jaw.entrada.test;

import static org.junit.Assert.*;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import org.junit.Before;
import org.junit.Test;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXmlValidFile() {
 String xmlFile = "test/java/jaw/entrada/AbrirTest/test.jaw.xml";
 abrir = new Abrir(form, xmlFile, Abrir.ABRIR_XML);
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXmlInvalidFileType() {
 String xmlFile = "test/java/jaw/entrada/AbrirTest/test.txt";
 abrir = new Abrir(form, xmlFile, Abrir.ABRIR_XML);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoInvalidFileType() {
 String serialFile = "test/java/jaw/entrada/AbrirTest/test.txt";
 abrir = new Abrir(form, serialFile, Abrir.ABRIR);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirSerializadoValidFile() {
 String serialFile = "test/java/jaw/entrada/AbrirTest/test.jaw";
 abrir = new Abrir(form, serialFile, Abrir.ABRIR);
 assertNotNull(abrir.getXml());
 }

 @Test
 public void testOpenDialogXMLFilter() {
 FileDialog dialog = new FileDialog(new Frame(), "Open", FileDialog.OPEN);
 dialog.setFileFilter(new Abrir.Filtro2());
 File file = new File("test/java/jaw/entrada/AbrirTest/test.jaw.xml");
 dialog.setLocation(10, 10);
 dialog.setVisible(true);
 assertFalse(dialog.isCancelOption());
 assertEquals(dialog.getFile(), file.getAbsolutePath());
 }

 @Test
 public void testOpenDialogSerializadoFilter() {
 FileDialog dialog = new FileDialog(new Frame(), "Open", FileDialog.OPEN);
 dialog.setFileFilter(new Abrir.Filtro());
 File file = new File("test/java/jaw/entrada/AbrirTest/test.jaw");
 dialog.setLocation(10, 10);
 dialog.setVisible(true);
 assertFalse(dialog.isCancelOption());
 assertEquals(dialog.getFile(), file.getAbsolutePath());
 }

 @Test
 public void testAbrirXMLInvalidFile() {
 String xmlFile = "test/java/jaw/entrada/AbrirTest/testInvalid.jaw.xml";
 abrir = new Abrir(form, xmlFile, Abrir.ABRIR_XML);
 assertTrue(form.getEntidades().isEmpty());
 }

 @Test
 public void testAbrirSerializadoInvalidFile() {
 String serialFile = "test/java/jaw/entrada/AbrirTest/testInvalid.jaw";
 abrir = new Abrir(form, serialFile, Abrir.ABRIR);
 assertTrue(form.getEntidades().isEmpty());
 }
}