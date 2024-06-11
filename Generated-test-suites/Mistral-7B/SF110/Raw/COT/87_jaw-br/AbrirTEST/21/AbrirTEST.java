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
import java.io.FileWriter;
import java.io.IOException;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXML() throws IOException {
 String arquivoXML = "teste.jaw.xml";
 abrir = new Abrir(form, arquivoXML);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLInvalidFile() {
 String arquivoXML = "teste.jaw";
 abrir = new Abrir(form, arquivoXML);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAbrirSerializado() throws IOException {
 String arquivoJaw = "teste.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File tempFile = File.createTempFile("test", ".jaw");
 FileWriter fileWriter = new FileWriter(tempFile);
 fileWriter.write("Test Data");
 fileWriter.close();
 abrir.abrirSerializado(tempFile.getAbsolutePath());
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoInvalidFile() {
 String arquivoJaw = "teste.jaw.xml";
 abrir = new Abrir(form, Abrir.ABRIR);
 abrir.abrirSerializado(arquivoJaw);
 fail("Should throw IOException");
 }

 @Test
 public void testAbrirXMLFileDialog() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 FileDialog fileDialog = new FileDialog(new Frame(), "Open Dialog", FileDialog.OPEN);
 fileDialog.setDirectory(new File(".").getAbsolutePath());
 fileDialog.addFileFilter(new Abrir.Filtro2());
 fileDialog.show();
 String arquivoXML = fileDialog.getFile();
 assertTrue(arquivoXML != null);
 abrir.abrirXML(arquivoXML);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLFileDialogInvalidFile() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 FileDialog fileDialog = new FileDialog(new Frame(), "Open Dialog", FileDialog.OPEN);
 fileDialog.setDirectory(new File(".").getAbsolutePath());
 fileDialog.addFileFilter(new Abrir.Filtro2());
 fileDialog.show();
 String arquivoXML = fileDialog.getFile();
 assertNull(arquivoXML);
 abrir.abrirXML(arquivoXML);
 fail("Should throw FileNotFoundException");
 }

 @Test
 public void testAbrirSerializadoFileDialog() {
 abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog fileDialog = new FileDialog(new Frame(), "Open Dialog", FileDialog.OPEN);
 fileDialog.setDirectory(new File(".").getAbsolutePath());
 fileDialog.addFileFilter(new Abrir.Filtro());
 fileDialog.show();
 String arquivoJaw = fileDialog.getFile();
 assertTrue(arquivoJaw != null);
 abrir.abrirSerializado(arquivoJaw);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoFileDialogInvalidFileType() {
 abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog fileDialog = new FileDialog(new Frame(), "Open Dialog", FileDialog.OPEN);
 fileDialog.setDirectory(new File(".").getAbsolutePath());
 fileDialog.addFileFilter(new Abrir.Filtro());
 fileDialog.show();
 String arquivoXML = fileDialog.getFile();
 assertNull(arquivoXML);
 abrir.abrirXML(arquivoXML);
 fail("Should throw FileNotFoundException");
 }
}