package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;

import jaw.entida.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.metamodelo.Processo;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setup() {
 MetamodeloFactory mFactory = new MetamodeloFactory();
 Processo processo = mFactory.createProcesso();
 Entidade entidade1 = mFactory.createEntidade();
 Entidade entidade2 = mFactory.createEntidade();
 entidade1.setNome("Entidade1");
 entidade2.setNome("Entidade2");
 entidades = new ArrayList<Entidade>();
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = new ProcessarEntidades(processo, entidades);
 }

 @Test
 public void testSalvarComTipoXML() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 String expectedArquivo = "path/to/your/project/folder/Entidade1Entidade2.jaw.xml";
 String actualArquivo = salvar.getArquivo();
 assertEquals(expectedArquivo, actualArquivo);
 }

 @Test
 public void testSalvarComTipoJaw() {
 salvar = new Salvar(form, Salvar.SALVAR);
 String expectedArquivo = "path/to/your/project/folder/Entidade1Entidade2.jaw";
 String actualArquivo = salvar.getArquivo();
 assertEquals(expectedArquivo, actualArquivo);
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarComArquivoNulo() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setArquivo(null);
 salvar.salvar();
 }

 @Test(expected = IOException.class)
 public void testSalvarComErroEscritaArquivoXML() throws IOException {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 File file = new File("path/to/your/project/folder/Entidade1Entidade2.jaw.xml");
 file.createNewFile();
 FileWriter writer = new FileWriter(file);
 writer.write("Your XML data here");
 writer.close();
 salvar.setArquivo(file.getAbsolutePath());
 try {
 salvar.salvarXML(entidades);
 } catch (JDOMException e) {
 fail("Unexpected JDOMException");
 }
 }

 @Test(expected = IOException.class)
 public void testSalvarComErroEscritaArquivoJAW() throws IOException {
 salvar = new Salvar(form, Salvar.SALVAR);
 File file = new File("path/to/your/project/folder/Entidade1Entidade2.jaw");
 file.createNewFile();
 FileWriter writer = new FileWriter(file);
 writer.write("Your JAW data here");
 writer.close();
 salvar.setArquivo(file.getAbsolutePath());
 try {
 salvar.salvar(entidades, "");
 } catch (Exception e) {
 fail("Unexpected Exception");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarComArquivoInvalidoXML() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setArquivo("path/to/your/project/folder/Entidade1Entidade2.jaw.txt");
 salvar.salvarXML(entidades);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarComArquivoInvalidoJAW() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setArquivo("path/to/your/project/folder/Entidade1Entidade2.jaw.txt");
 salvar.salvar(entidades, "");
 }

 @Test
 public void testSalvarComTipoXMLeNomeDeArquivoVazio() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvarXML(entidades, "");
 }

 @Test
 public void testSalvarComTipoJAWEsenCriarArquivo() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.salvar(entidades, "");
 }

 @Test
 public void testSalvarComTipoXMLeCriarArquivo() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvarXML(entidades);
 }

 @Test
 public void testSalvarComTipoJAWEscreveArquivo() throws IOException {
 salvar = new Salvar(form, Salvar.SALVAR);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<processo>\n<entidade>\n<nome>Entidade1</nome>\n<atributos>\n<atributo>\n<nome>Atributo1</nome>\n<valor>Valor1</valor>\n</atributo>\n<atributo>\n<nome>Atributo2</nome>\n<valor>Valor2</valor>\n</atributo>\n</atributos>\n</entidade>\n<entidade>\n<nome>Entidade2</nome>\n<atributos>\n<atributo>\n<nome>Atributo1</nome>\n<valor>Valor1</valor>\n</atributo>\n<atributo>\n<nome>Atributo2</nome>\n<valor>Valor2</valor>\n</atributo>\n</atributos>\n</entidade>\n</processo>";
 File file = new File("path/to/your/project/folder/Entidade1Entidade2.jaw");
 FileWriter writer = new FileWriter(file);
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setArquivo(file.getAbsolutePath());
 salvar.salvar(entidades, "");
 XMLOutputter xmlOutput = new XMLOutputter();
 String actualXml = xmlOutput.outputString(DocumentHelper.parseText(salvar.getXml()));
 assertEquals(expectedXml, actualXml);
 }

}