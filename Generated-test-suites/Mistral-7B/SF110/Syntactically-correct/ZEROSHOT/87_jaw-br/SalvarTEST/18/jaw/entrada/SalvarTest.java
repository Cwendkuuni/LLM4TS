package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private List<Entidade> entidades;
 private static final String TEST_PROJECT_PATH = "test_project_path";
 private static final String TEST_XML_FILE = "test_file.xml";
 private static final String TEST_JAW_FILE = "test_file.jaw";

 @Before
 public void setup() {
 entidades = new ArrayList<Entidade>();
 salvar = new Salvar(null, 1);
 }

 @After
 public void tearDown() {
 File testProjectPath = new File(TEST_PROJECT_PATH);
 File testXmlFile = new File(TEST_PROJECT_PATH + File.separator + TEST_XML_FILE);
 File testJawFile = new File(TEST_PROJECT_PATH + File.separator + TEST_JAW_FILE);
 testProjectPath.deleteOnExit();
 testXmlFile.deleteOnExit();
 testJawFile.deleteOnExit();
 }

 @Test
 public void testSalvarXML() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1", new ArrayList<String>()));
 entidades.add(new Entidade("Teste2", "Teste2", "Teste2", new ArrayList<String>()));
 Salvar salvarXML = new Salvar(null, 10);
 salvarXML.entidade = entidades;
 salvarXML.salvar();
 File testXmlFile = new File(TEST_PROJECT_PATH + File.separator + TEST_XML_FILE);
 assertTrue(testXmlFile.exists());
 }

 @Test
 public void testSalvarJaw() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1", new ArrayList<String>()));
 entidades.add(new Entidade("Teste2", "Teste2", "Teste2", new ArrayList<String>()));
 Salvar salvarJaw = new Salvar(null, 20);
 salvarJaw.entidade = entidades;
 salvarJaw.salvar();
 File testJawFile = new File(TEST_PROJECT_PATH + File.separator + TEST_JAW_FILE);
 assertTrue(testJawFile.exists());
 }

 @Test(expected = Exception.class)
 public void testSalvarWithoutFileChooser() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1", new ArrayList<String>()));
 entidades.add(new Entidade("Teste2", "Teste2", "Teste2", new ArrayList<String>()));
 Salvar salvarXML = new Salvar(null, 10);
 salvarXML.entidade = entidades;
 salvarXML.salvar();
 }

 @Test
 public void testSalvarInvalidFileType() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1", new ArrayList<String>()));
 entidades.add(new Entidade("Teste2", "Teste2", "Teste2", new ArrayList<String>()));
 Salvar salvarInvalidFileType = new Salvar(null, 40);
 salvarInvalidFileType.entidade = entidades;
 salvarInvalidFileType.salvar();
 }

 @Test(expected = IOException.class)
 public void testSerializacaoXMLWriteFileException() {
 Salvar salvarXML = new Salvar(null, 10);
 salvarXML.salvar();
 }

 @Test(expected = IOException.class)
 public void testSerializacaoWriteFileException() {
 Salvar salvarJaw = new Salvar(null, 20);
 salvarJaw.salvar();
 }
}