package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXmlWithValidXmlFile() throws IOException {
 String xmlFilePath = "src/test/resources/validxmlfile.jaw.xml";
 File xmlFile = new File(xmlFilePath);
 abrir = new Abrir(form, xmlFile);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXmlWithInvalidXmlFile() {
 String invalidXmlFilePath = "src/test/resources/invalidxmlfile";
 abrir = new Abrir(form, invalidXmlFilePath);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() throws Exception {
 String jawFilePath = "src/test/resources/validjawfile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR, jawFilePath);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 String invalidJawFilePath = "src/test/resources/invalidjawfile";
 abrir = new Abrir(form, Abrir.ABRIR, invalidJawFilePath);
 fail("Expected IOException, but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithoutFileChooserSelection() {
 abrir = new Abrir(form, Abrir.ABRIR);
 fail("Expected IllegalArgumentException, but no exception was thrown.");
 }

 @Test(expected = IllegalStateException.class)
 public void testAbrirSerializadoWithoutJFileChooser() {
 abrir = new Abrir(null, Abrir.ABRIR);
 fail("Expected IllegalStateException, but no exception was thrown.");
 }

 @Test
 public void testAbrirXmlWithValidXmlFileFromFileChooser() throws Exception {
 String xmlFilePath = "src/test/resources/validxmlfile.jaw.xml";
 File xmlFile = new File(xmlFilePath);
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = new JFileChooser();
 chooser.setSelectedFile(xmlFile);
 chooser.showOpenDialog(null);
 abrir.setArquivo(xmlFile.getAbsolutePath());
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXmlWithInvalidXmlFileFromFileChooser() throws Exception {
 String invalidXmlFilePath = "src/test/resources/invalidxmlfile";
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = new JFileChooser();
 chooser.setSelectedFile(new File(invalidXmlFilePath));
 chooser.showOpenDialog(null);
 fail("Expected FileNotFoundException, but no exception was thrown.");
 }

 @Test
 public void testAbrirSerializadoWithValidJawFileFromFileChooser() throws Exception {
 String jawFilePath = "src/test/resources/validjawfile.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 chooser.setSelectedFile(new File(jawFilePath));
 chooser.showOpenDialog(null);
 abrir.setArquivo(chooser.getSelectedFile().getAbsolutePath());
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidJawFileFromFileChooser() throws Exception {
 String invalidJawFilePath = "src/test/resources/invalidjawfile";
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 chooser.setSelectedFile(new File(invalidJawFilePath));
 chooser.showOpenDialog(null);
 fail("Expected FileNotFoundException, but no exception was thrown.");
 }

 @Test
 public void testFiltroXml() {
 File xmlFile = new File("src/test/resources/validxmlfile.jaw.xml");
 assertTrue(new Abrir.Filtro2().accept(xmlFile));
 }

 @Test
 public void testFiltroJaw() {
 File jawFile = new File("src/test/resources/validjawfile.jaw");
 assertTrue(new Abrir.Filtro().accept(jawFile));
 }

 @Test
 public void testFiltroXmlInvalid() {
 File invalidXmlFile = new File("src/test/resources/invalidxmlfile");
 assertFalse(new Abrir.Filtro2().accept(invalidXmlFile));
 }

 @Test
 public void testFiltroJawInvalid() {
 File invalidJawFile = new File("src/test/resources/invalidjawfile");
 assertFalse(new Abrir.Filtro().accept(invalidJawFile));
 }
}