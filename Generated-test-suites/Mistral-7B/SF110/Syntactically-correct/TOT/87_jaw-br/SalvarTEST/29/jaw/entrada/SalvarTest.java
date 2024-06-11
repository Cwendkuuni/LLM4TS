import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidades = new ArrayList<>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarXml_ValidArgs() throws IOException {
 // given
 entidades.add(new Entidade("Teste1", 1));
 entidades.add(new Entidade("Teste2", 2));
 salvar.entidade = entidades;

 // when
 salvar.arquivo = "teste.jaw.xml";
 Salvar.SerializacaoXML serializaXML = new Salvar.SerializacaoXML();
 String xmlExpected = serializaXML.entidades2XML(entidades);
 String xmlResult = salvar.xml;

 // then
 assertTrue(xmlResult.contains(xmlExpected));
 }

 @Test
 public void testSalvarXml_InvalidArquivo() {
 // given
 salvar.arquivo = "teste";

 // when
 // then
 assertNull(salvar.xml);
 }

 @Test
 public void testSalvarXml_EmptyList() {
 // given
 salvar.entidade = new ArrayList<>();

 // when
 // then
 assertNull(salvar.xml);
 }

 @Test
 public void testSalvarXml_NullList() {
 // given
 salvar.entidade = null;

 // when
 // then
 assertNull(salvar.xml);
 }

 @Test
 public void testSalvar_ValidArgs() throws IOException {
 // given
 entidades.add(new Entidade("Teste1", 1));
 entidades.add(new Entidade("Teste2", 2));
 salvar.entidade = entidades;

 // when
 salvar.arquivo = "teste.jaw";
 Salvar.Serializacao serializacao = new Salvar.Serializacao();
 boolean saved = serializacao.salvar(salvar.entidade, salvar.arquivo);

 // then
 assertTrue(saved);
 File file = new File(salvar.arquivo);
 assertTrue(file.exists());
 }

 @Test
 public void testSalvar_InvalidArquivoFormat() throws IOException {
 // given
 salvar.arquivo = "teste.txt";
 Salvar.Serializacao serializacao = new Salvar.Serializacao();

 // when
 // then
 assertFalse(serializacao.salvar(salvar.entidade, salvar.arquivo));
 File file = new File(salvar.arquivo);
 assertFalse(file.exists());
 }

 @Test
 public void testSalvar_NullList() {
 // given
 salvar.entidade = null;

 // when
 Salvar.Serializacao serializacao = new Salvar.Serializacao();

 // then
 assertFalse(serializacao.salvar(salvar.entidade, salvar.arquivo));
 }

 @Test
 public void testSalvar_EmptyList() {
 // given
 salvar.entidade = new ArrayList<>();

 // when
 Salvar.Serializacao serializacao = new Salvar.Serializacao();

 // then
 assertFalse(serializacao.salvar(salvar.entidade, salvar.arquivo));
 }
}