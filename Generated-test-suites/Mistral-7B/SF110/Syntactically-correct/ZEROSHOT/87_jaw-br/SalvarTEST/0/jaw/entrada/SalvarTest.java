package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

import jaw.metamodelo.Entidade;
import jaw.metamodelo.Metamodelo;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 Metamodelo metamodelo = new Metamodelo();
 this.form = new ProcessarEntidades(metamodelo);
 this.salvar = new Salvar(form, Salvar.SALVAR_XML);
 this.entidades = new ArrayList<Entidade>();
 }

 @Test
 public void testSalvarXmlEmptyArquivo() {
 salvar.salvar(entidades, "");
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlNullArquivo() {
 salvar.salvar(entidades, null);
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlNoFileChooser() {
 form.setName("");
 salvar.salvar(entidades, form);
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlEmptyFileChooser() {
 FileFilter filter = new FileFilter() {
 @Override
 public boolean accept(File f) {
 return true;
 }

 @Override
 public String getDescription() {
 return "Test Filter";
 }
 };
 form.setFileFilter(filter);
 salvar.salvar(entidades, form);
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlValidArquivo() {
 File file = new File("test.jaw.xml");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertEquals("test.jaw.xml", salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlInvalidArquivoFormat() {
 File file = new File("test.xml");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlArquivoWithExtension() {
 File file = new File("test.jaw.xml");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertEquals("test.jaw.xml", salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlArquivoWithoutExtension() {
 File file = new File("test");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertEquals("test.jaw.xml", salvar.getArquivo());
 }

 @Test
 public void testSalvarComValidArquivo() {
 File file = new File("test.jaw");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertEquals("test.jaw", salvar.getArquivo());
 }

 @Test
 public void testSalvarComInvalidArquivoFormat() {
 File file = new File("test.xml");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarComArquivoWithExtension() {
 File file = new File("test.jaw");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertEquals("test.jaw", salvar.getArquivo());
 }

 @Test
 public void testSalvarComArquivoWithoutExtension() {
 File file = new File("test");
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 form.setSelectedFile(file);
 salvar.salvar(entidades, form);
 assertEquals("test.jaw", salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlWithValidFileAndEntidades() {
 final String testXml = "<entidades>\n"
 + "  <entidade>\n"
 + "    <nome>Entidade1</nome>\n"
 + "    <atributos>\n"
 + "      <atributo>\n"
 + "        <nome>Atributo1</nome>\n"
 + "        <valor>Valor1</valor>\n"
 + "      </atributo>\n"
 + "    </atributos>\n"
 + "  </entidade>\n"
 + "</entidades>";
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 });
 Entidade entidade1 = new Entidade();
 entidade1.setNome("Entidade1");
 this.entidades.add(entidade1);
 entidade1.setAtributos(new ArrayList<Atributo>());
 Atributo atributo1 = new Atributo();
 atributo1.setNome("Atributo1");
 atributo1.setValue("Valor1");
 entidade1.getAtributos().add(atributo1);
 salvar.salvarXml(entidades, "");
 String resultXml = salvar.getXml();
 assertEquals(testXml, resultXml);
 }

 @Test
 public void testSalvarComWithValidFileAndEntidades() {
 final String testXml = "<entidades>\n"
 + "  <entidade>\n"
 + "    <nome>Entidade1</nome>\n"
 + "    <atributos>\n"
 + "      <atributo>\n"
 + "        <nome>Atributo1</nome>\n"
 + "        <valor>Valor1</valor>\n"
 + "      </atributo>\n"
 + "    </atributos>\n"
 + "  </entidade>\n"
 + "</entidades>";
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 Entidade entidade1 = new Entidade();
 entidade1.setNome("Entidade1");
 this.entidades.add(entidade1);
 entidade1.setAtributos(new ArrayList<Atributo>());
 Atributo atributo1 = new Atributo();
 atributo1.setNome("Atributo1");
 atributo1.setValue("Valor1");
 entidade1.getAtributos().add(atributo1);
 salvar.salvarCom(entidades, "");
 String resultXml = salvar.getXml();
 assertEquals(testXml, resultXml);
 }

 @Test
 public void testSalvarXmlWithValidFileAndEmptyEntidades() {
 final String testXml = "<entidades></entidades>";
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 });
 salvar.salvarXml(entidades, "");
 String resultXml = salvar.getXml();
 assertEquals(testXml, resultXml);
 }

 @Test
 public void testSalvarComWithValidFileAndEmptyEntidades() {
 final String testXml = "<entidades></entidades>";
 form.setFileFilter(new FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 });
 salvar.salvarCom(entidades, "");
 String resultXml = salvar.getXml();
 assertEquals(testXml, resultXml);
 }
}