package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.Filtro;
import jaw.entrada.Filtro2;
import jaw.entrada.ProcessarEntidades;
import jaw.entrada.Salvar;
import jaw.metamodelo.Metamodelo;

public class SalvarTest {

 // Testing constructor
 @Test
 public void testSalvarConstructor() {
  ProcessarEntidades form = new ProcessarEntidades();
  Salvar salvar = new Salvar(form, 20);
  assertTrue(salvar.getEntidade().isEmpty());
 }

 // Testing entidade getter and setter
 @Test
 public void testEntidadeGetterAndSetter() {
  ProcessarEntidades form = new ProcessarEntidades();
  Salvar salvar = new Salvar(form, 20);
  List<Entidade> entidades = new ArrayList<Entidade>();
  Entidade entidade = new Entidade();
  entidades.add(entidade);
  salvar.setEntidade(entidades);
  List<Entidade> entidadesRetrieved = salvar.getEntidade();
  assertEquals(entidades, entidadesRetrieved);
 }

 // Testing arquivo getter and setter
 @Test
 public void testArquivoGetterAndSetter() {
  Salvar salvar = new Salvar(null, 20);
  String arquivo = "testFile.jaw";
  salvar.setArquivo(arquivo);
  assertEquals(arquivo, salvar.getArquivo());
 }

 // Testing xml getter and setter
 @Test
 public void testXmlGetterAndSetter() {
  Salvar salvar = new Salvar(null, 10);
  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<model>\n<entidade>\n<nome>testEntidade</nome>\n</entidade>\n</model>";
  salvar.setXml(xml);
  assertEquals(xml, salvar.getXml());
 }

 // Testing vela getter
 @Test
 public void testVelaGetter() {
  Salvar salvar = new Salvar(null, 20);
  assertFalse(salvar.isVela());
  salvar.setVela(true);
  assertTrue(salvar.isVela());
 }

 // Testing tipoSalvar getter and setter
 @Test
 public void testTipoSalvarGetterAndSetter() {
  Salvar salvar = new Salvar(null, 0);
  int tipoSalvar = 10;
  salvar.setTipoSalvar(tipoSalvar);
  assertEquals(tipoSalvar, salvar.getTipoSalvar());
 }

 // Testing getEntidades method
 @Test
 public void testGetEntidadesMethod() {
  ProcessarEntidades form = new ProcessarEntidades();
  Salvar salvar = new Salvar(form, 20);
  List<Entidade> entidades = new ArrayList<Entidade>();
  Entidade entidade = new Entidade();
  entidades.add(entidade);
  form.setEntidades(entidades);
  try {
   salvar.getEntidades();
  } catch (Exception e) {
   fail("Exception should not be thrown when getting entidades");
  }
  List<Entidade> retrievedEntidades = salvar.getEntidades();
  assertEquals(entidades, retrievedEntidades);
 }

 // Testing setEntidades method
 @Test
 public void testSetEntidadesMethod() {
  ProcessarEntidades form = new ProcessarEntidades();
  Salvar salvar = new Salvar(form, 20);
  List<Entidade> entidades = new ArrayList<Entidade>();
  Entidade entidade = new Entidade();
  entidades.add(entidade);
  salvar.setEntidades(entidades);
  List<Entidade> expectedEntidades = new ArrayList<Entidade>();
  Entidade newEntidade = new Entidade();
  expectedEntidades.add(newEntidade);
  salvar.setEntidades(expectedEntidades);
  List<Entidade> retrievedEntidades = salvar.getEntidades();
  assertEquals(expectedEntidades, retrievedEntidades);
 }

 // Testing salvar method for XML saving
 @Test
 public void testSalvarXML() throws Exception {
  ProcessarEntidades form = new ProcessarEntidades();
  Salvar salvar = new Salvar(form, 10);
  List<Entidade> entidades = new ArrayList<Entidade>();
  Entidade entidade = new Entidade();
  entidades.add(entidade);
  form.setEntidades(entidades);
  String expectedFilePath = "testFile.jaw.xml";
  salvar.salvar(form, expectedFilePath);
  File file = new File(expectedFilePath);
  assertTrue(file.exists());
  file.delete();
 }

 // Testing salvar method for file saving
 @Test
 public void testSalvarFile() throws Exception {
  ProcessarEntidades form = new ProcessarEntidades();
  Salvar salvar = new Salvar(form, 20);
  List<Entidade> entidades = new ArrayList<Entidade>();
  Entidade entidade = new Entidade();
  entidades.add(entidade);
  form.setEntidades(entidades);
  String expectedFilePath = "testFile.jaw";
  JFileChooser chooser = new JFileChooser();
  File selectedFile = new File(expectedFilePath);
  chooser.setSelectedFile(selectedFile);
  salvar.salvar(form, chooser);
  assertTrue(selectedFile.exists());
  selectedFile.delete();
 }

 // Testing salvar method for XML and file saving
 @Test
 public void testSalvarXMLAndFile() throws Exception {
  ProcessarEntidades form = new ProcessarEntidades();
  Salvar salvar = new Salvar(form, 30);
  List<Entidade> entidades = new ArrayList<Entidade>();
  Entidade entidade = new Entidade();
  entidades.add(entidade);
  form.setEntidades(entidades);
  String expectedFilePath = "testFile.jaw.xml";
  JFileChooser chooser = new JFileChooser();
  File selectedFile = new File(expectedFilePath + ".jaw");
  chooser.setSelectedFile(selectedFile);
  salvar.salvar(form, chooser);
  File xmlFile = new File(expectedFilePath + ".jaw.xml");
  assertTrue(xmlFile.exists());
  assertTrue(selectedFile.exists());
  xmlFile.delete();
  selectedFile.delete();
 }

 // Testing accept method for Filtro2
 @Test
 public void testAcceptFiltro2() {
  File file = new File("testFile.jaw.xml");
  Filtro2 filter = new Filtro2();
  assertTrue(filter.accept(file));
  File xmlFile = new File("testFile.xml");
  assertFalse(filter.accept(xmlFile));
 }

 // Testing accept method for Filtro
 @Test
 public void testAcceptFiltro() {
  File file = new File("testFile.jaw");
  Filtro filter = new Filtro();
  assertTrue(filter.accept(file));
  File xmlFile = new File("testFile.jaw.xml");
  assertFalse(filter.accept(xmlFile));
 }

 // Testing getDescription method for Filtro2
 @Test
 public void testGetDescriptionFiltro2() {
  Filtro2 filter = new Filtro2();
  assertEquals("XML JAW files", filter.getDescription());
 }

 // Testing getDescription method for Filtro
 @Test
 public void testGetDescriptionFiltro() {
  Filtro filter = new Filtro();
  assertEquals("JAW files", filter.getDescription());
 }
}