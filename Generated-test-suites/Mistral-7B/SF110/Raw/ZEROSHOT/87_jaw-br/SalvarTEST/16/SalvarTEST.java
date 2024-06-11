package jaw.entrada;

import static org.junit.Assert.*;
import jaw.metamodelo.*;
import jaw.gui.*;
import java.io.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private Entidade entidade;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidade = new Entidade();
 entidades = new ArrayList<>();
 entidades.add(entidade);
 salvar = new Salvar(form, 20);
 }

 @Test
 public void testSalvarComArquivoValido() {
 // Prepare
 Salvar expected = new Salvar(form, 20);
 JFileChooser fileChooserSpy = new JFileChooser() {
 @Override
 public int showSaveDialog(Component parent) {
 return JFileChooser.APPROVE_OPTION;
 }
 };
 expected.setVelas(true);
 File arquivo = new File("Teste.jaw");
 expected.setArquivo(arquivo.getAbsolutePath());
 Serializacao serializacao = new Serializacao();
 when(serializacao.salvar(eq(entidades), eq(arquivo.getAbsolutePath())))
 .thenReturn(true);
 Salvar salvarSpy = spy(salvar);
 doReturn(fileChooserSpy).when(salvarSpy).criarEscolhedorDeArquivos();
 doReturn(expected).when(salvarSpy).newSalvar();

 // Test
 salvar.salvar(entidades, "Teste.jaw");

 // Verify
 verify(salvarSpy).criarEscolhedorDeArquivos();
 verify(salvarSpy).newSalvar();
 verify(serializacao).salvar(eq(entidades), eq("Teste.jaw"));
 }

 @Test
 public void testSalvarSemArquivoValidoComNomeVazio() {
 // Prepare
 Salvar expected = new Salvar(form, 20);
 JFileChooser fileChooserSpy = new JFileChooser();
 expected.setVelas(false);
 Serializacao serializacao = new Serializacao();
 when(serializacao.salvar(eq(entidades), eq(anyString())))
 .thenReturn(true);
 Salvar salvarSpy = spy(salvar);
 doReturn(fileChooserSpy).when(salvarSpy).criarEscolhedorDeArquivos();
 doReturn(expected).when(salvarSpy).newSalvar();

 // Test
 salvar.salvar(entidades, "");

 // Verify
 verifyNoInteractions(serializacao);
 }

 @Test(expected = Exception.class)
 public void testSalvarSemArquivoValidoComNomeArquivoInvalido() {
 // Prepare
 Salvar expected = new Salvar(form, 20);
 JFileChooser fileChooserSpy = new JFileChooser() {
 @Override
 public int showSaveDialog(Component parent) {
 return JFileChooser.APPROVE_OPTION;
 }
 };
 expected.setVelas(true);
 File arquivo = new File("Teste.jaw.xml");
 expected.setArquivo(arquivo.getAbsolutePath());
 Serializacao serializacao = new Serializacao();
 when(serializacao.salvar(eq(entidades), eq(arquivo.getAbsolutePath())))
 .thenThrow(new Exception());
 Salvar salvarSpy = spy(salvar);
 doReturn(fileChooserSpy).when(salvarSpy).criarEscolhedorDeArquivos();
 doReturn(expected).when(salvarSpy).newSalvar();

 // Test
 salvar.salvar(entidades, arquivo.getAbsolutePath());

 // Verify
 verify(salvarSpy).criarEscolhedorDeArquivos();
 verify(salvarSpy).newSalvar();
 verifyThrows(Exception.class, () -> serializacao.salvar(entidades, arquivo.getAbsolutePath()));
 }

 @Test
 public void testSalvarArquivoXMLValido() {
 // Prepare
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidade>\n <id>1</id>\n <nome>Entidade 1</nome>\n <descricao>Descrição da Entidade 1</descricao>\n</entidade>";
 File arquivo = new File("Teste.jaw.xml");
 Salvar salvar = new Salvar(form, 10);
 when(salvar.salvarArquivoXML(entidades)).thenReturn(xml);
 FileWriter escrevedor = mock(FileWriter.class);
 when(escrevedor.write(anyString()))
 .thenReturn(xml.length());
 when(escrevedor.flush())
 .thenReturn(true);
 when(escrevedor.close())
 .thenReturn(true);
 FileWriter escrevedorSpy = spy(escrevedor);
 Salvar salvarSpy = spy(salvar);
 doReturn(escrevedorSpy).when(salvarSpy).obterEscritorDeArquivo(eq("Teste.jaw.xml"));

 // Test
 salvar.salvarArquivoXML(entidades, "Teste.jaw.xml");

 // Verify
 verify(salvarSpy).obterEscritorDeArquivo(eq("Teste.jaw.xml"));
 }

 @Test(expected = Exception.class)
 public void testSalvarArquivoXMLInvalido() {
 // Prepare
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidade>\n <id>1</id>\n <nome>Entidade 1</nome>\n <descricao>Descrição da Entidade 1</descricao>\n</entidade>";
 File arquivo = new File("Teste.xml");
 Salvar salvar = new Salvar(form, 10);
 when(salvar.salvarArquivoXML(entidades)).thenThrow(new Exception());
 FileWriter escrevedor = mock(FileWriter.class);
 Salvar salvarSpy = spy(salvar);
 doThrow(IOException.class).when(escrevedor).write(anyString());
 doThrow(IOException.class).when(escrevedor).flush();
 doThrow(IOException.class).when(escrevedor).close();
 doReturn(escrevedor).when(salvarSpy).obterEscritorDeArquivo(eq("Teste.xml"));

 // Test
 salvar.salvarArquivoXML(entidades, "Teste.xml");

 // Verify
 verifyThrows(Exception.class, () -> serializacaoXML.entidades2XML(entidades));
 }
}