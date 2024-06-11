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

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXmlWithValidXmlFile() throws IOException {
 File xmlFile = new File("src/test/resources/valid_jaw_xml.jaw.xml");
 abrir = new Abrir(form, xmlFile.getAbsolutePath());
 assertEquals("valid_jaw_xml.jaw.xml", form.getNomeArquivoSalvo());
 // Add assertions to verify the contents of the Entidades object
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXmlWithInvalidXmlFile() {
 abrir = new Abrir(form, "invalid_xml_file.txt");
 fail("Expected FileNotFoundException was not thrown");
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() throws Exception {
 File jawFile = new File("src/test/resources/valid_jaw_file.jaw");
 abrir = new Abrir(form, Abrir.ABRIR, jawFile.getAbsolutePath());
 assertEquals("valid_jaw_file.jaw", form.getNomeArquivoSalvo());
 // Add assertions to verify the contents of the Entidades object
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 abrir = new Abrir(form, Abrir.ABRIR, "invalid_jaw_file.txt");
 fail("Expected FileNotFoundException was not thrown");
 }

 @Test
 public void testAbrirWithValidXmlFileAndFilter() throws Exception {
 JFileChooser escolhedor = new JFileChooser();
 File xmlFile = new File("src/test/resources/valid_jaw_xml.jaw.xml");
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setSelectedFile(xmlFile);
 assertEquals("valid_jaw_xml.jaw.xml", form.getNomeArquivoSalvo());
 // Add assertions to verify the contents of the Entidades object
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirWithInvalidXmlFileAndFilter() {
 JFileChooser escolhedor = new JFileChooser();
 File xmlFile = new File("src/test/resources/invalid_xml_file.txt");
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setSelectedFile(xmlFile);
 fail("Expected FileNotFoundException was not thrown");
 }

 @Test
 public void testAbrirWithValidJawFileAndFilter() throws Exception {
 JFileChooser escolhedor = new JFileChooser();
 File jawFile = new File("src/test/resources/valid_jaw_file.jaw");
 abrir = new Abrir(form, Abrir.ABRIR);
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setSelectedFile(jawFile);
 assertEquals("valid_jaw_file.jaw", form.getNomeArquivoSalvo());
 // Add assertions to verify the contents of the Entidades object
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirWithInvalidJawFileAndFilter() {
 JFileChooser escolhedor = new JFileChooser();
 File jawFile = new File("src/test/resources/invalid_jaw_file.txt");
 abrir = new Abrir(form, Abrir.ABRIR);
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setSelectedFile(jawFile);
 fail("Expected FileNotFoundException was not thrown");
 }

 @After
 public void tearDown() {
 form = null;
 abrir = null;
 }

}