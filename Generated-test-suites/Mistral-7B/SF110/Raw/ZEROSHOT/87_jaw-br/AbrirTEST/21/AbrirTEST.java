package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.utils.Serializacao;
import jaw.utils.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXML() throws IOException {
 String xmlFile = "path/to/your/xml/file.jaw.xml";
 File xmlFileLocation = new File(xmlFile);
 abrir = new Abrir(form, xmlFile);
 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWrongFileType() throws IOException {
 String wrongFileType = "path/to/your/wrong/file.txt";
 abrir = new Abrir(form, wrongFileType);
 fail("An IllegalArgumentException should be thrown");
 }

 @Test
 public void testAbrirSerializado() throws IOException, Exception {
 String serializadoFile = "path/to/your/serializado/file.jaw";
 File serializadoFileLocation = new File(serializadoFile);
 abrir = new Abrir(form, 20, serializadoFile);
 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWrongFileType() throws IOException {
 String wrongFileType = "path/to/your/wrong/file.xml";
 abrir = new Abrir(form, 20, wrongFileType);
 fail("An IllegalArgumentException should be thrown");
 }

 @Test
 public void testOpenFileDialogXML() throws IOException, Exception {
 String xmlFilePath = "path/to/your/xml/file.jaw.xml";
 abrir = new Abrir(form, 10);
 File selectedFile = new File(xmlFilePath);
 abrir.getForm().getEscolherArquivo().setSelectedFile(selectedFile);
 abrir.getForm().getAbrir().doClick();
 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test
 public void testOpenFileDialogSerializado() throws IOException, Exception {
 String serializadoFilePath = "path/to/your/serializado/file.jaw";
 abrir = new Abrir(form, 20);
 File selectedFile = new File(serializadoFilePath);
 abrir.getForm().getEscolherArquivo().setSelectedFile(selectedFile);
 abrir.getForm().getAbrir().doClick();
 assertNotNull(abrir.getXml());
 assertNotNull(abrir.getForm().getEntidades());
 }

 @Test(expected = HeadlessException.class)
 public void testOpenFileDialogHeadless() throws IOException, Exception {
 abrir = new Abrir(form, 10);
 abrir.getForm().getEscolherArquivo().showOpenDialog(null);
 fail("A HeadlessException should be thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testOpenFileDialogNullSelectedFile() throws IOException, Exception {
 abrir = new Abrir(form, 10);
 abrir.getForm().getEscolherArquivo().setSelectedFile(null);
 abrir.getForm().getAbrir().doClick();
 fail("A NullPointerException should be thrown");
 }

}