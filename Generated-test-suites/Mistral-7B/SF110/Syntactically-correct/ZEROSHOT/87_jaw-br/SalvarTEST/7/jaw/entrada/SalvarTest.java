package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.Filtro;
import jaw.entrada.Filtro2;
import jaw.entrada.ProcessarEntidades;
import jaw.entrada.Salvar;
import jaw.gui.Main;
import jaw.metamodelo.Classe;
import jaw.metamodelo.Atributo;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;
 private static final String TEST_XML_FILE = "src/test/java/jaw/entrada/test/SalvarTest-output.xml";
 private static final String TEST_JAW_FILE = "src/test/java/jaw/entrada/test/SalvarTest-output.jaw";

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<>();

 // Initialize Form with some Entities
 entidades.add(new Entidade("Teste", new Classe("TestClasse", new ArrayList<Atributo>() {{
 add(new Atributo("TestAtributo1", "java.lang.String", "teste1"));
 add(new Atributo("TestAtributo2", "java.lang.Integer", "teste2"));
 }})));
 entidades.add(new Entidade("Teste2", new Classe("Teste2Classe", new ArrayList<Atributo>() {{
 add(new Atributo("TestAtributo3", "java.lang.Double", "teste3"));
 add(new Atributo("TestAtributo4", "java.lang.Character", "teste4"));
 }})));
 try {
 form.setEntidades(entidades);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testSaveXMLFileWithValidFileChooser() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser fileChooser = new JFileChooser();
 File selectedFile = new File("src/test/java/jaw/entrada/test/output.xml");
 fileChooser.setSelectedFile(selectedFile);
 fileChooser.showSaveDialog(form);
 salvar.setArquivo(selectedFile.getAbsolutePath());
 salvar.salvar(entidades, arquivo);

 assertFalse(salvar.isVela());
 assertTrue(new File(arquivo).exists());

 // Validate XML file content
 Element root;
 try {
 Document document = new SAXBuilder().build(new File(TEST_XML_FILE));
 root = document.getRootElement();
 } catch (IOException | JDOMException e) {
 fail("Failed to read created XML file: " + e.getMessage());
 }

 assertEquals("JawProject", root.getName());
 List<Element> classes = root.getChildren("Class");
 assertEquals(2, classes.size());

 for (Element clazz : classes) {
 String name = clazz.getAttributeValue("name");
 List<Element> attributes = clazz.getChildren("Attribute");
 assertEquals(2, attributes.size());

 for (Element attribute : attributes) {
 String type = attribute.getChildText("type");
 String nameAttribute = attribute.getChildText("name");

 assertEquals("TestClasse", name.equals("Teste") ? "Teste" : "Teste2Classe");
 assertEquals("java.lang.String", nameAttribute.equals("TestAtributo1") ? type : "java.lang.Integer");
 assertEquals("teste1", nameAttribute.equals("TestAtributo1") ? attribute.getChildText("name") : "teste2");
 }
 }

 // Validate that XML file has been deleted
 assertFalse(new File(TEST_XML_FILE).exists());
 }

 @Test(expected = IllegalStateException.class)
 public void testSaveXMLFileWithInvalidFileChooser() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setArquivo("InvalidPath");
 salvar.salvar(entidades, arquivo);

 assertTrue(salvar.isVela());
 assertFalse(new File(arquivo).exists());
 }

 @Test
 public void testSaveJawFileWithValidFileChooser() {
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser fileChooser = new JFileChooser();
 File selectedFile = new File("src/test/java/jaw/entrada/test/output.jaw");
 fileChooser.setSelectedFile(selectedFile);
 fileChooser.showSaveDialog(form);
 salvar.setArquivo(selectedFile.getAbsolutePath());
 salvar.salvar(entidades, arquivo);

 assertFalse(salvar.isVela());
 assertTrue(new File(arquivo).exists());
 }

 @Test(expected = IllegalStateException.class)
 public void testSaveJawFileWithInvalidFileChooser() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setArquivo("InvalidPath");
 salvar.salvar(entidades, arquivo);

 assertTrue(salvar.isVela());
 assertFalse(new File(arquivo).exists());
 }

 @Test
 public void testSaveJawFileWithFormNameSalvoSet() {
 salvar = new Salvar(form, Salvar.SALVAR);
 form.setNomeArquivoSalvo("output.jaw");
 salvar.salvar(entidades, arquivo);

 assertFalse(salvar.isVela());
 assertTrue(new File(TEST_JAW_FILE).exists());
 }

}