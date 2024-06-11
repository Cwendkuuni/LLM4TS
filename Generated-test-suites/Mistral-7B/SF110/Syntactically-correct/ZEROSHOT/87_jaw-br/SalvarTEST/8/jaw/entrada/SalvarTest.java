package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private Entidade entidade1;
 private Entidade entidade2;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidade1 = new Entidade("Entidade1");
 entidade2 = new Entidade("Entidade2");
 form.setEntidades(new ArrayList<Entidade>() {{
 add(entidade1);
 add(entidade2);
 }});
 }

 @Test
 public void testSalvarXML() {
 Salvar salvarXML = new Salvar(form, Salvar.SALVAR_XML);
 SalvarExpected salvarExpectedXML = new SalvarExpected(entidade1, entidade2, "Entidade1.jaw.xml", "xmlData1", "xmlData2");

 assertEquals(salvarExpectedXML, salvar);
 }

 @Test
 public void testSalvarComoXML() {
 Salvar salvarComoXML = new Salvar(form, Salvar.SALVAR_COMO + Salvar.SALVAR_XML);
 SalvarExpected salvarExpectedXML = new SalvarExpected(entidade1, entidade2, "Entidade1.jaw.xml", "xmlData1", "xmlData2");

 assertEquals(salvarExpectedXML, salvar);
 }

 @Test
 public void testSalvarComoJaw() {
 Salvar salvarComoJaw = new Salvar(form, Salvar.SALVAR);
 SalvarExpected salvarExpectedJaw = new SalvarExpected(entidade1, entidade2, "Entidade1.jaw", "data1", "data2");

 assertEquals(salvarExpectedJaw, salvar);
 }

 @Test
 public void testSalvarComoJawComNomeArquivoSalvo() {
 Form formWithName = new Form() {{
 setNomeArquivoSalvo("Entidade1.jaw");
 }};
 Salvar salvarComoJaw = new Salvar(formWithName, Salvar.SALVAR);
 SalvarExpected salvarExpectedJaw = new SalvarExpected(entidade1, entidade2, "Entidade1.jaw", "data1", "data2");

 assertEquals(salvarExpectedJaw, salvar);
 }

 @Test
 public void testSalvarSemArquivo() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 assertFalse(salvar.getVela());
 assertEmpty(salvar.getArquivo());
 }

 @Test
 public void testSalvarComNovoProjeto() {
 Form formWithName = new Form() {{
 setNomeArquivoSalvo("Novo Projeto");
 }};
 Salvar salvar = new Salvar(form, Salvar.SALVAR_COMO + Salvar.SALVAR_XML);
 SalvarExpected salvarExpected = new SalvarExpected(entidade1, entidade2, "Novo Projeto.jaw.xml", "xmlData1", "xmlData2");

 assertEquals(salvarExpected, salvar);
 }

 @Test(expected = Exception.class)
 public void testSalvarComErro() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 throw new Exception("Test Error");
 }

 @Test
 public void testSerializacaoSalvarXML() throws Exception {
 Salvar salvarXML = new Salvar(form, Salvar.SALVAR_XML);
 String xmlExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<id>1</id>\n<nome>Entidade1</nome>\n</entidade>\n<entidade>\n<id>2</id>\n<nome>Entidade2</nome>\n</entidade>\n</entidades>";
 String xmlActual = salvar.getXml();
 assertEquals(xmlExpected, xmlActual);
 }

 @Test
 public void testSerializacaoSalvarJaw() throws Exception {
 File tempFile = File.createTempFile("SalvarTest", ".jaw");
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.salvar(form.getEntidades(), tempFile.getAbsolutePath());
 String expected = "data1\ndata2";
 String actual = new String(Files.readAllBytes(tempFile.toPath()));
 assertEquals(expected, actual);
 tempFile.delete();
 }

 @Test
 public void testFileChooserSaveDialog() {
 JFileChooser fileChooser = spy(new JFileChooser());
 doReturn(fileChooser).when(form).getFileChooser();
 doReturn(true).when(fileChooser).showSaveDialog(any());

 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 assertTrue(salvar.getVela());
 assertNotNull(salvar.getArquivo());
 }

 @Test
 public void testFileChooserSaveDialogNull() {
 JFileChooser fileChooser = spy(new JFileChooser());
 doReturn(fileChooser).when(form).getFileChooser();
 doReturn(null).when(fileChooser).getSelectedFile();

 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 assertFalse(salvar.getVela());
 assertEmpty(salvar.getArquivo());
 }
}

 class SalvarExpected {
 private boolean vela;
 private String arquivo;
 private String xml;
 private List<Entidade> entidade;
 private String xmlData1;
 private String xmlData2;

 SalvarExpected(Entidade entidade1, Entidade entidade2, String arquivo, String xmlData1, String xmlData2) {
 this.vela = false;
 this.arquivo = arquivo;
 this.xml = xml;
 this.entidade = new ArrayList<Entidade>() {{
 add(entidade1);
 add(entidade2);
 }};
 this.xmlData1 = xmlData1;
 this.xmlData2 = xmlData2;
 }

 public boolean getVela() {
 return vela;
 }

 public String getArquivo() {
 return arquivo;
 }

 public String getXml() {
 return xml;
 }

 public List<Entidade> getEntidade() {
 return entidade;
 }

 public String getXmlData1() {
 return xmlData1;
 }

 public String getXmlData2() {
 return xmlData2;
 }
 }