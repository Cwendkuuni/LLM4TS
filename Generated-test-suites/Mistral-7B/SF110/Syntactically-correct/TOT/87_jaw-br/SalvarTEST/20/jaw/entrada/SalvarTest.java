import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jaw.entrada.Entidade;
import jaw.entrada.ProcessarEntidades;
import jaw.metamodelo.*;
import jaw.gui.Main;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = Mockito.spy(new ProcessarEntidades());
 entidades = new ArrayList<>();
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 entidades.add(new Entidade("Teste2", "Teste2", "Teste2"));
 salvar = new Salvar(form, 20);
 }

 @Test
 public void testSalvarComSucesso() {
 Mockito.doReturn(entidades).when(form).getEntidades();
 Mockito.doReturn(new File("test.jaw")).when(form).getNomeArquivoSalvo();
 Salvar.Serializacao serializacao = Mockito.spy(new Salvar.Serializacao());
 Mockito.doNothing().when(serializacao).salvar(entidades, "test.jaw");
 salvar.salvar();
 Mockito.verify(serializacao, Mockito.times(1)).salvar(entidades, "test.jaw");
 }

 @Test(expected = Exception.class)
 public void testSalvarArquivoInvalido() {
 Mockito.doReturn(new File("test.txt")).when(form).getNomeArquivoSalvo();
 salvar.salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarEntidadesNull() {
 Mockito.doReturn(null).when(form).getEntidades();
 salvar.salvar();
 }

 @Test
 public void testSalvarXMLComSucesso() {
 Mockito.doReturn(entidades).when(form).getEntidades();
 Mockito.doReturn(new File("test.jaw.xml")).when(form).getNomeArquivoSalvo();
 String xmlExpected = "<entidades>\n" +
         "<entidade>\n" +
         "<nome>Teste1</nome>\n" +
         "<descricao>Teste1</descricao>\n" +
         "<nomeProjeto>Teste1</nomeProjeto>\n" +
         "</entidade>\n" +
         "<entidade>\n" +
         "<nome>Teste2</nome>\n" +
         "<descricao>Teste2</descricao>\n" +
         "<nomeProjeto>Teste2</nomeProjeto>\n" +
         "</entidade>\n" +
         "</entidades>\n";
 Salvar.SerializacaoXML serializacaoXML = Mockito.spy(new Salvar.SerializacaoXML());
 Mockito.doReturn(xmlExpected).when(serializacaoXML).entidades2XML(entidades);
 FileWriter escrevedor = Mockito.mock(FileWriter.class);
 Mockito.doNothing().when(serializacaoXML).escreverArquivo(xmlExpected, escrevedor);
 Salvar salvarXML = new Salvar(form, 10);
 salvarXML.salvarXML();
 Mockito.verify(serializacaoXML, Mockito.times(1)).entidades2XML(entidades);
 Mockito.verify(escrevedor, Mockito.times(1)).write(xmlExpected);
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLArquivoInvalido() {
 Mockito.doReturn(new File("test.txt")).when(form).getNomeArquivoSalvo();
 Salvar.SerializacaoXML serializacaoXML = Mockito.spy(new Salvar.SerializacaoXML());
 FileWriter escrevedor = Mockito.mock(FileWriter.class);
 salvar.salvarXML();
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLEntidadesNull() {
 Salvar.SerializacaoXML serializacaoXML = Mockito.spy(new Salvar.SerializacaoXML());
 FileWriter escrevedor = Mockito.mock(FileWriter.class);
 Salvar salvarXML = new Salvar(form, 10);
 salvarXML.salvarXML();
 }
}