package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.gui.ProcessarEntidades;
import jaw.Salvar;
import jaw.Serializacao;
import jaw.SerializacaoXML;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades processarEntidades;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 MetamodeloFactory metamodeloFactory = new MetamodeloFactory();
 entidades = new ArrayList<Entidade>();
 processarEntidades = new ProcessarEntidades(metamodeloFactory);
 salvar = new Salvar(processarEntidades, Salvar.SALVAR);
 }

 @Test
 public void testSalvar() {
 List<Entidade> entidadesSalvas = salvar.getEntidade();
 assertEquals(entidades.size(), entidadesSalvas.size());
 for (Entidade entidade : entidades) {
 assertEquals(entidade, entidadesSalvas.get(entidadesSalvas.indexOf(entidade)));
 }
 File arquivo = new File("teste.jaw");
 assertTrue(arquivo.exists());
 arquivo.delete();
 }

 @Test(expected = Exception.class)
 public void testSalvarWithoutFile() {
 salvar.setArquivo("");
 salvar.salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidFileType() {
 File arquivo = new File("teste.txt");
 salvar.setArquivo(arquivo);
 salvar.salvar();
 }

 @Test
 public void testSalvarXML() {
 List<Entidade> entidadesXML = new ArrayList<Entidade>();
 Entidade entidadeXML = MetamodeloFactory.createEntidade("TesteXML");
 entidadeXML.setNome("TesteXML");
 entidadeXML.setTipo("TesteXML");
 entidadesXML.add(entidadeXML);
 salvar = new Salvar(processarEntidades, Salvar.SALVAR_XML);
 salvar.setEntidade(entidadesXML);
 String xml = salvar.getXml();
 assertNotNull(xml);
 File arquivoXML = new File("teste.jaw.xml");
 assertTrue(arquivoXML.exists());
 arquivoXML.delete();
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithoutFile() {
 salvar.setXml("");
 salvar.salvarXML();
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithInvalidFileType() {
 salvar.setXml("");
 File arquivoXML = new File("teste.txt");
 salvar.setArquivo(arquivoXML);
 salvar.salvarXML();
 }
}