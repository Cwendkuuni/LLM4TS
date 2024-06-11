package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.Filtro;
import jaw.entrada.Filtro2;
import jaw.entrada.ProcessadorEntidadesMock;
import jaw.entrada.Salvar;
import jaw.metamodelo.EntidadeMock;
import jaw.metamodelo.MetamodeloFactory;
import jaw.metamodelo.MetamodeloPackage;

public class SalvarTest {

 private Salvar salvar;
 private ProcessadorEntidadesMock processadorEntidades;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 processadorEntidades = new ProcessadorEntidadesMock();
 entidades = new ArrayList<>();
 entidades.add(MetamodeloFactory.eINSTANCE.createEntidade("Teste1", MetamodeloPackage.ENTIDADE));
 entidades.add(MetamodeloFactory.eINSTANCE.createEntidade("Teste2", MetamodeloPackage.ENTIDADE));
 processadorEntidades.setEntidades(entidades);
 salvar = new Salvar(processadorEntidades, Salvar.SALVAR_XML);
 }

 @After
 public void tearDown() throws IOException {
 File file = new File("output.jaw.xml");
 if (file.exists()) {
 file.delete();
 }
 }

 @Test
 public void testSalvarXML() {
 salvar.salvar();
 File file = new File("output.jaw.xml");
 assertTrue(file.exists());
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLInvalidFormat() {
 salvar.salvar();
 salvar.setArquivo("output.jaw");
 salvar.salvar();
 }

 @Test
 public void testSalvarFileChooser() {
 File expectedFile = new File("output.jaw.xml");
 Salvar salvarExpected = new Salvar(processadorEntidades, Salvar.SALVAR_XML);
 JFileChooser escolhedor = salvarExpected.getEscolhedor();
 File selectedFile = escolhedor.showSaveDialog(processadorEntidades.getForm()) == JFileChooser.APPROVE_OPTION ? escolhedor.getSelectedFile() : null;
 assertEquals(expectedFile, selectedFile);
 }

 @Test(expected = Exception.class)
 public void testSalvarFileChooserCancel() {
 Salvar salvarExpected = new Salvar(processadorEntidades, Salvar.SALVAR_XML);
 salvarExpected.setVela(true);
 JFileChooser escolhedor = salvarExpected.getEscolhedor();
 escolhedor.setSelectedFile(null);
 escolhedor.showSaveDialog(processadorEntidades.getForm());
 }

 @Test
 public void testSalvarComoXML() {
 salvar = new Salvar(processadorEntidades, Salvar.SALVAR_COMO);
 salvar.setArquivo("output.jaw.xml");
 salvar.salvar();
 File file = new File("output.jaw.xml");
 assertTrue(file.exists());
 }

 @Test(expected = Exception.class)
 public void testSalvarComoXMLInvalidFormat() {
 salvar = new Salvar(processadorEntidades, Salvar.SALVAR_COMO);
 salvar.setArquivo("output.jaw");
 salvar.salvar();
 }

 @Test
 public void testSalvarArquivoNaoEncontrado() {
 salvar = new Salvar(processadorEntidades, Salvar.SALVAR);
 salvar.setArquivo("output.jaw");
 salvar.salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarArquivoExistente() {
 salvar = new Salvar(processadorEntidades, Salvar.SALVAR);
 salvar.setArquivo("output.jaw");
 File file = new File("output.jaw");
 file.createNewFile();
 salvar.salvar();
 }

 @Test
 public void testSalvarXMLFileFilter() {
 Salvar salvarExpected = new Salvar(processadorEntidades, Salvar.SALVAR_XML);
 JFileChooser escolhedor = salvarExpected.getEscolhedor();
 Filtro filtro = salvarExpected.getFiltro();
 assertEquals("XML JAW files", filtro.getDescription());
 assertTrue(filtro.accept(new File("output.jaw.xml")));
 }

 @Test
 public void testSalvarFileFilter() {
 Salvar salvarExpected = new Salvar(processadorEntidades, Salvar.SALVAR);
 JFileChooser escolhedor = salvarExpected.getEscolhedor();
 Filtro filtro = salvarExpected.getFiltro();
 assertEquals("JAW files", filtro.getDescription());
 assertTrue(filtro.accept(new File("output.jaw")));
 }

}