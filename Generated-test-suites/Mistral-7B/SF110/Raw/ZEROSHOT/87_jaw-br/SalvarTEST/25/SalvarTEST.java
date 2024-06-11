package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.entiida.Entidade;
import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private Serializacao serializacao;
 private SerializacaoXML serializacaoXML;

 @Before
 public void setUp() {
 form = Mockito.mock(ProcessarEntidades.class);
 serializacao = Mockito.mock(Serializacao.class);
 serializacaoXML = Mockito.mock(SerializacaoXML.class);
 salvar = new Salvar(form, 20);
 }

 @Test
 public void testSalvarJawFile() throws IOException {
 List<Entidade> entidade = new ArrayList<>();
 entidade.add(new Entidade("teste1"));
 entidade.add(new Entidade("teste2"));
 when(form.getEntidades()).thenReturn(entidade);
 doReturn(true).when(serializacao).salvar(entidade, salvar.arquivo);
 salvar.salvar();
 verify(form, times(1)).setNomeArquivoSalvo(eq(salvar.arquivo));
 verify(serializacao, times(1)).salvar(entidade, eq(salvar.arquivo));
 }

 @Test(expected = FileNotFoundException.class)
 public void testSalvarJawFileWithNullFile() throws IOException {
 List<Entidade> entidade = new ArrayList<>();
 entidade.add(new Entidade("teste1"));
 entidade.add(new Entidade("teste2"));
 when(form.getEntidades()).thenReturn(entidade);
 doReturn(false).when(serializacao).salvar(entidade, salvar.arquivo);
 salvar.salvar();
 }

 @Test
 public void testSalvarXmlFile() {
 List<Entidade> entidade = new ArrayList<>();
 entidade.add(new Entidade("teste1"));
 entidade.add(new Entidade("teste2"));
 when(form.getEntidades()).thenReturn(entidade);
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jaw>\n<entidade>\n<nome>teste1</nome>\n</entidade>\n<entidade>\n<nome>teste2</nome>\n</entidade>\n</jaw>";
 doReturn(xml).when(serializacaoXML).entidades2XML(entidade);
 doReturn(true).when(serializacaoXML).salvarXml(xml, salvar.arquivo);
 salvar.salvarXml();
 verify(form, times(1)).setNomeArquivoSalvo(eq(salvar.arquivo + ".jaw.xml"));
 verify(serializacaoXML, times(1)).entidades2XML(entidade);
 verify(serializacaoXML, times(1)).salvarXml(eq(xml), eq(salvar.arquivo + ".jaw.xml"));
 }

 @Test(expected = FileNotFoundException.class)
 public void testSalvarXmlFileWithNullFile() {
 List<Entidade> entidade = new ArrayList<>();
 entidade.add(new Entidade("teste1"));
 entidade.add(new Entidade("teste2"));
 when(form.getEntidades()).thenReturn(entidade);
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jaw>\n<entidade>\n<nome>teste1</nome>\n</entidade>\n<entidade>\n<nome>teste2</nome>\n</entidade>\n</jaw>";
 doReturn(xml).when(serializacaoXML).entidades2XML(entidade);
 doReturn(false).when(serializacaoXML).salvarXml(xml, salvar.arquivo + ".jaw.xml");
 salvar.salvarXml();
 }
}