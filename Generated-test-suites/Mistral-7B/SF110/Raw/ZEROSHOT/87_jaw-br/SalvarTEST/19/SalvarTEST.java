package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<>();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarNaoInicializaArquivo() {
 assertFalse(salvar.isVela());
 assertNull(salvar.getArquivo());
 assertNull(salvar.getXml());
 assertNotNull(salvar.getEntidade());
 assertFalse(salvar.getEntidade().isEmpty());
 }

 @Test
 public void testSalvarComArquivoValido() throws IOException {
 File tempFile = File.createTempFile("test", ".jaw");
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(tempFile);
 escolhedor.showSaveDialog(form);
 salvar.setVela(true);
 salvar.setArquivo(tempFile.getAbsolutePath());
 List<Entidade> novasEntidades = new ArrayList<>();
 novasEntidades.add(new Entidade("Teste", 1, 1.0));
 salvar.setEntidade(novasEntidades);
 Serializacao serializacao = new Serializacao();
 assertTrue(serializacao.salvar(salvar.getEntidade(), salvar.getArquivo()));
 File arquivoSalvo = new File(salvar.getArquivo());
 assertTrue(arquivoSalvo.exists());
 FileWriter writer = new FileWriter(arquivoSalvo);
 writer.write("Teste");
 writer.close();
 String conteudoArquivo = new String(Files.readAllBytes(arquivoSalvo.toPath()));
 assertEquals("Teste", conteudoArquivo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarComArquivoInvalido() {
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showSaveDialog(form);
 salvar.setVelas(true);
 salvar.setArquivo("teste.txt");
 List<Entidade> novasEntidades = new ArrayList<>();
 novasEntidades.add(new Entidade("Teste", 1, 1.0));
 salvar.setEntidade(novasEntidades);
 Serializacao serializacao = new Serializacao();
 serializacao.salvar(salvar.getEntidade(), salvar.getArquivo());
 fail("Deve lançar exceção quando o arquivo não tem o formato .jaw");
 }

 @Test
 public void testSalvarComArquivoInvalidoNaoLancaExcecao() {
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showSaveDialog(form);
 salvar.setVela(true);
 salvar.setArquivo("teste.txt");
 List<Entidade> novasEntidades = new ArrayList<>();
 novasEntidades.add(new Entidade("Teste", 1, 1.0));
 salvar.setEntidade(novasEntidades);
 Serializacao serializacao = new Serializacao();
 serializacao.salvar(salvar.getEntidade(), salvar.getArquivo());
 }

 @Test
 public void testSalvarXmlNaoInicializaArquivo() {
 assertFalse(salvar.isVela());
 assertNull(salvar.getArquivo());
 assertNull(salvar.getXml());
 assertNotNull(salvar.getEntidade());
 assertFalse(salvar.getEntidade().isEmpty());
 }

 @Test
 public void testSalvarXmlComArquivoValid() throws IOException {
 String xmlExpected = "<entidades>\n<entidade>\n<nome>Teste</nome>\n<id>1</id>\n<valor>1.0</valor>\n</entidade>\n</entidades>";
 File tempFile = File.createTempFile("test", ".jaw.xml");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(tempFile);
 escolhedor.showSaveDialog(form);
 salvar.setVela(true);
 salvar.setArquivo(tempFile.getAbsolutePath());List<Entidade> novasEntidades = new ArrayList<>();
 novasEntidades.add(new Entidade("Teste", 1, 1.0));
 salvar.setEntidade(novasEntidades);
 SerializacaoXML serializaXML = new SerializacaoXML();
 String xml = serializaXML.entidades2XML(salvar.getEntidade());
 salvar.setXml(xml);
 FileWriter writer = new FileWriter(tempFile);
 writer.write(xmlExpected);
 writer.close();
 String conteudoArquivo = new String(Files.readAllBytes(tempFile.toPath()));
 assertEquals(xmlExpected, conteudoArquivo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXmlComArquivoInvalido() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showSaveDialog(form);
 salvar.setVela(true);
 salvar.setArquivo("teste.txt");
 List<Entidade> novasEntidades = new ArrayList<>();
 novasEntidades.add(new Entidade("Teste", 1, 1.0));
 salvar.setEntidade(novasEntidades);
 SerializacaoXML serializaXML = new SerializacaoXML();
 serializaXML.entidades2XML(salvar.getEntidade());
 fail("Deve lançar exceção quando o arquivo não tem o formato .jaw.xml");
 }

 @Test
 public void testSalvarXmlComArquivoInvalidoNaoLancaExcecao() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showSaveDialog(form);
 salvar.setVela(true);
 salvar.setArquivo("teste.txt");
 List<Entidade> novasEntidades = new ArrayList<>();
 novasEntidades.add(new Entidade("Teste", 1, 1.0));
 salvar.setEntidade(novasEntidades);
 SerializacaoXML serializaXML = new SerializacaoXML();
 serializaXML.entidades2XML(salvar.getEntidade());
 }
}