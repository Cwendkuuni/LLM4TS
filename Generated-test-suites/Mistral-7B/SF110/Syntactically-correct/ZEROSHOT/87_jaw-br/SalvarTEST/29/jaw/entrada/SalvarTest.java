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

import jaw.entidade.Entidade;
import jaw.metamodelo.ProcessadorMetaModelo;
import jaw.metamodelo.ProcessadorMetaModeloMock;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private Salvar salvar;
 private ProcessadorMetaModelo processadorMetaModelo;
 private ProcessadorMetaModeloMock processadorMetaModeloMock;

 @Before
 public void setUp() {
 processadorMetaModelo = new ProcessadorMetaModelo();
 processadorMetaModeloMock = new ProcessadorMetaModeloMock();
 salvar = new Salvar(processadorMetaModeloMock, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarArquivoXML() {
 List<Entidade> entidades = new ArrayList<Entidade>();
 Entidade entidade1 = new Entidade("Teste1", new MetaModelo(), new MetaModelo());
 Entidade entidade2 = new Entidade("Teste2", new MetaModelo(), new MetaModelo());
 entidades.add(entidade1);
 entidades.add(entidade2);
 salvar.setEntidade(entidades);

 String arquivo = "src/test/resources/output/salvarTest.xml";
 String expectedXml = "<entidades>\n" +
                      "<entidade>\n" +
                      "<nome>Teste1</name>\n" +
                      "</entidade>\n" +
                      "<entidade>\n" +
                      "<nome>Teste2</nome>\n" +
                      "</entidade>\n" +
                      "</entidades>";

 salvar.salvar();

 try {
 Document doc = new SAXBuilder().build(new File(arquivo));
 Element entidadesElement = doc.getRootElement();
 Element entidade1Element = entidadesElement.getChild(0);
 Element entidade2Element = entidadesElement.getChild(1);
 String xml = SerializacaoXML.xmlToString(doc);

 assertEquals(entidades.size(), entidadesElement.getChildren().size());
 assertEquals(entidade1.getNome(), entidade1Element.getChildTextTrim("nome"));
 assertEquals(entidade2.getNome(), entidade2Element.getChildTextTrim("nome"));
 assertEquals(expectedXml, xml);
 } catch (IOException | JDOMException e) {
 fail("Error reading file: " + e.getMessage());
 }
 }

 @Test
 public void testNaoPossalvarArquivoXML() {
 salvar.salvar();
 assertFalse(new File("src/test/resources/output/salvarTest.xml").exists());
 }

 @Test(expected = NullPointerException.class)
 public void testNaoHaverEntidades() {
 salvar = new Salvar(processadorMetaModelMock, Salvar.SALVAR_XML);
 salvar.salvar();
 }

 @Test
 public void testSalvarArquivoJaw() {
 List<Entidade> entidades = new ArrayList<Entidade>();
 Entidade entidade1 = new Entidade("Teste1", new MetaModelo(), new MetaModelo());
 Entidade entidade2 = new Entidade("Teste2", new MetaModelo(), new MetaModelo());
 entidades.add(entidade1);
 entidades.add(entidade2);
 salvar.setEntidade(entidades);

 String arquivo = "src/test/resources/output/salvarTest.jaw";

 salvar.salvar();

 assertTrue(new File(arquivo).exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNaoPossalvarArquivoJawSemEntidades() {
 salvar = new Salvar(processadorMetaModelMock, Salvar.SALVAR);
 salvar.salvar();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNaoPossalvarArquivoJawComNomeInvalido() {
 salvar = new Salvar(processadorMetaModelMock, 31);
 salvar.setArquivo("src/test/resources/output/salvarTest.jaw1");
 salvar.salvar();
 }

 @Test
 public void testNaoPossalvarArquivoJawComArquivoExistente() {
 List<Entidade> entidades = new ArrayList<Entidade>();
 Entidade entidade1 = new Entidade("Teste1", new MetaModelo(), new MetaModelo());
 Entidade entidade2 = new Entidade("Teste2", new MetaModelo(), new MetaModelo());
 entidades.add(entidade1);
 entidades.add(entidade2);
 salvar.setEntidade(entidades);
 salvar.setArquivo("src/test/resources/output/salvarTest.jaw");
 salvar.salvar();
 assertFalse(salvar.salvar());
 assertTrue(new File("src/test/resources/output/salvarTest.jaw").exists());
 }

}