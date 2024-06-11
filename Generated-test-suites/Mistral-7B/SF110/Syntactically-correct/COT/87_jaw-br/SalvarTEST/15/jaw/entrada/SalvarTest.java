package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.ProcessarEntidades;
import jaw.entrada.Salvar;
import jaw.gui.Main;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 // Initialize test data
 form = new ProcessarEntidades();
 entidades = new ArrayList<Entidade>();
 entidades.add(new Entidade("Test", "Test", "Test", new int[5]));
 }

 // Test constructor
 @Test
 public void testSalvarConstructor() {
 salvar = new Salvar(form, Salvar.SALVAR);
 assertNotNull(salvar.getEntidade());
 }

 // Test getters and setters
 @Test
 public void testGettersAndSetters() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setArquivo("test.jaw");
 assertEquals("test.jaw", salvar.getArquivo());
 }

 // Test salvar method with valid inputs
 @Test
 public void testSalvarValidInputs() throws IOException {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidade(entidades);
 File file = new File("test.jaw");
 salvar.salvar(entidades, file.getAbsolutePath());
 assertTrue(file.exists());
 }

 // Test salvar method with invalid file extension
 @Test(expected = IllegalArgumentException.class)
 public void testSalvarInvalidFileExtension() throws IOException {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidade(entidades);
 File file = new File("test.xml");
 salvar.salvar(entidades, file.getAbsolutePath());
 }

 // Test salvar method with null entidade list
 @Test(expected = NullPointerException.class)
 public void testSalvarNullEntidadeList() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setArquivo("test.jaw");
 salvar.salvar(null, "test.jaw");
 }

 // Test salvarXML method with valid inputs
 @Test
 public void testSalvarXMLValidInputs() throws IOException {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidade(entidades);
 String xml = "<entidades>\n<entidade>\n<nome>Test</nome>\n<classe>Test</classe>\n<descricao>Test</descricao>\n<atributos>\n<atributo>\n<nome>Test</nome>\n<tipo>int</tipo>\n<tamanho>5</tamanho>\n</atributo>\n</atributos>\n</entidade>\n</entidades>";
 File file = new File("test.jaw.xml");
 salvar.salvarXML(entidades, file.getAbsolutePath());
 String actualXML = new SerializacaoXML().xml2entidades(new FileReader(file.getAbsolutePath()).readAllLines()[0]);
 assertEquals(xml, actualXML);
 }

 // Test salvarXML method with null entidade list
 @Test(expected = NullPointerException.class)
 public void testSalvarXMLNullEntidadeList() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 String xml = "<entidades>\n<entidade>\n<nome>Test</nome>\n<classe>Test</classe>\n<descricao>Test</descricao>\n<atributos>\n<atributo>\n<nome>Test</nome>\n<tipo>int</tipo>\n<tamanho>5</tamanho>\n</atributo>\n</atributos>\n</entidade>\n</entidades>";
 salvar.salvarXML(null, "test.jaw.xml");
 }

}