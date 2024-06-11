package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.jflex.main.Main;
import org.jflex.gui.ProcessarEntidades;
import org.jflex.serializacao.Serializacao;
import org.jflex.serializacao.SerializacaoXML;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirWithXmlFile() {
 String arquivo = "test.jaw.xml";
 abrir = new Abrir(form, arquivo);
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithInvalidXmlFile() {
 String arquivo = "test.jaw";
 abrir = new Abrir(form, arquivo);
 fail("Should throw IllegalArgumentException with invalid file type");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirWithInvalidXmlFileLocation() {
 String arquivo = "test.jaw.xmlx";
 abrir = new Abrir(form, arquivo);
 fail("Should throw FileNotFoundException with invalid file location");
 }

 @Test
 public void testAbrirWithSerializadoFile() {
 String arquivo = "test.jaw";
 abrir = new Abrir(form, 20, arquivo);
 assertTrue(abrir.xml.length() == 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithInvalidSerializadoFileType() {
 String arquivo = "test.txt";
 abrir = new Abrir(form, 20, arquivo);
 fail("Should throw IllegalArgumentException with invalid file type");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirWithInvalidSerializadoFileLocation() {
 String arquivo = "test.jawx";
 abrir = new Abrir(form, 20, arquivo);
 fail("Should throw FileNotFoundException with invalid file location");
 }

 @Test
 public void testAbrirWithInvalidFileDialog() {
 FileChooserUtils.setFileDialogThrowsException(true);
 abrir = new Abrir(form, 20);
 fail("Should throw FileNotFoundException with no selected file");
 }

 @Test
 public void testAbrirWithValidXmlFileDialog() throws IOException {
 File file = new File("test.jaw.xml");
 abrir = new Abrir(form, 10, file);
 assertTrue(abrir.xml.length() > 0);
 }

 @Test
 public void testAbrirWithValidSerializadoFileDialog() throws IOException {
 File file = new File("test.jaw");
 abrir = new Abrir(form, 20, file);
 assertTrue(abrir.xml.length() == 0);
 }

}