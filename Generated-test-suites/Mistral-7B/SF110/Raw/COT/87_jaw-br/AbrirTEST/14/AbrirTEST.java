package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirXML() {
 String arquivoXml = "teste.jaw.xml";
 abrir = new Abrir(form, arquivoXml);
 assertEquals("teste.jaw.xml", form.getNomeArquivoSalvo());
 // Add assertions to check if form.getEntidades() is correctly set
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 String arquivoXml = "teste.txt";
 abrir = new Abrir(form, arquivoXml);
 // No need to add assertions as an IllegalArgumentException is expected
 }

 @Test
 public void testAbrirSerializado() {
 String arquivoSerializado = "teste.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog fd = new FileDialog(new Frame(), "Select a file", FileDialog.OPEN);
 fd.setFile(".", null);
 fd.addFileFilter(new FileFilter() {
 @Override
 public boolean accept(File file) {
 return file.isDirectory() || file.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 fd.show();
 File selectedFile = fd.getFile();
 assertNotNull(selectedFile);
 String filePath = selectedFile.getAbsolutePath();
 abrir = new Abrir(form, filePath);
 assertEquals(filePath, form.getNomeArquivoSalvo());
 // Add assertions to check if form.getEntidades() is correctly set
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 String arquivoSerializado = "teste.jaw.invalid";
 abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog fd = new FileDialog(new Frame(), "Select a file", FileDialog.OPEN);
 fd.setFile(".", null);
 fd.addFileFilter(new FileFilter() {
 @Override
 public boolean accept(File file) {
 return file.isDirectory() || file.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 fd.show();
 File selectedFile = fd.getFile();
 assertNull(selectedFile);
 abrir = new Abrir(form, selectedFile.getAbsolutePath());
 // No need to add assertions as a FileNotFoundException is expected
 }

 @Test
 public void testAbrirXMLWithNullForm() {
 assertNull(new Abrir(null, "teste.jaw.xml"));
 }

 @Test
 public void testAbrirSerializadoWithNullForm() {
 assertNull(new Abrir(null, Abrir.ABRIR, new File("teste.jaw")));
 }

}