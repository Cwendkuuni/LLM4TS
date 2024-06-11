package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.Form;
import jaw.entrada.Salvar;
import jaw.gui.JanelaPrincipal;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private Form form;
 private Salvar salvar;
 private List<Entidade> entidades;
 private String expectedPath;

 @Before
 public void setUp() {
 form = new Form();
 entidades = new ArrayList<>();
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, 20);
 expectedPath = "path/to/test/project/jaw.jaw";
 }

 @Test
 public void testSalvarJawFile() {
 Salvar expectedSalvar = new Salvar(form, 20);
 JFileChooser fileChooser = new JFileChooser();
 File selectedFile = new File("path/to/test/project/jaw.jaw");
 fileChooser.setSelectedFile(selectedFile);
 expectedSalvar.vela = true;

 // Test the Salvar constructor
 Salvar actualSalvar = new Salvar(form, 20);
 assertEquals(expectedSalvar.vela, actualSalvar.vela);
 assertEquals(expectedPath, actualSalvar.arquivo);

 // Test the salvar method
 Serializacao serialize = new Serializacao();
 try {
 serialize.salvar(entidades, actualSalvar.arquivo);
 }
 catch (Exception e) {
 fail("Failed to save file: " + e.getMessage());
 }
 File savedFile = new File(actualSalvar.arquivo);
 assertTrue(savedFile.exists());
 }

 @Test
 public void testSalvarXmlFile() {
 SerializacaoXML xmlSerializer = new SerializacaoXML();
 String xmlData = xmlSerializer.entidades2XML(entidades);
 String expectedXmlPath = "path/to/test/project/jaw.jaw.xml";

 // Test the Salvar constructor
 Salvar expectedSalvar = new Salvar(form, 10);
 expectedSalvar.vela = false;

 // Test the salvar method
 String actualXmlPath = "";
 Salvar actualSalvar = new Salvar(form, 10);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(new File(expectedXmlPath));
 actualSalvar.vela = false;

 // Set the expected file path for the test
 expectedSalvar.arquivo = expectedXmlPath;

 // Test saving XML file
 String actualXmlData = "";
 try {
 actualXmlData = actualSalvar.xml = actualSalvar.salvarXML(entidades);
 }
 catch (Exception e) {
 fail("Failed to save XML file: " + e.getMessage());
 }
 assertEquals(xmlData, actualXmlData);

 // Test writing XML data to file
 try {
 FileWriter writer = new FileWriter(new File(expectedXmlPath));
 writer.write(xmlData);
 writer.close();
 }
 catch (IOException e) {
 fail("Failed to write XML data to file: " + e.getMessage());
 }

 // Test reading XML data from file
 String readXmlData = "";
 try {
 FileReader reader = new FileReader(new File(expectedXmlPath));
 int read = 0;
 char[] buffer = new char[1024];
 while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
 readXmlData += new String(buffer, 0, read);
 }
 reader.close();
 }
 catch (IOException e) {
 fail("Failed to read XML data from file: " + e.getMessage());
 }
 assertEquals(xmlData, readXmlData);
 }
}