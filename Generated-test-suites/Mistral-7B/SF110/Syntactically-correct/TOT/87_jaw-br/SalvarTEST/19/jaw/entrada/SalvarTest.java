package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
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
 public void setUp() {
 entidades = new ArrayList<>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @Test
 public void testSalvarJawFile() {
 // Given - Entidades preenchidas
 entidades.add(new Entidade(1, "Entidade1", "Tipo1", "Descricao1"));
 entidades.add(new Entidade(2, "Entidade2", "Tipo2", "Descricao2"));
 salvar.entidade = entidades;

 // When - Salvar arquivo .jaw
 File arquivo = new File("path/to/save/test.jaw");
 salvar.arquivo = arquivo.getAbsolutePath();
 boolean result = salvar.salvar(entidades, arquivo.getAbsolutePath());

 // Then - Arquivo deve ser salvo com sucesso
 assertTrue(result);
 File fileSaved = new File(arquivo.getAbsolutePath());
 assertTrue(fileSaved.exists());
 }

 @Test
 public void testSalvarInvalidFileFormat() {
 // Given - Entidades preenchidas
 entidades.add(new Entidade(1, "Entidade1", "Tipo1", "Descricao1"));
 entidades.add(new Entidade(2, "Entidade2", "Tipo2", "Descricao2"));
 salvar.entidade = entidades;

 // When - Salvar arquivo com extensao invalida
 salvar.arquivo = "test.invalid";

 // Then - Arquivo nao deve ser salvo e nao deve haver mensagem de erro
 assertFalse(salvar.salvar(entidades, salvar.arquivo));
 assertNull(salvar.getMensagem());
 }

 @Test
 public void testSalvarXmlFile() {
 // Given - Entidades preenchidas
 entidades.add(new Entidade(1, "Entidade1", "Tipo1", "Descricao1"));
 entidades.add(new Entidade(2, "Entidade2", "Tipo2", "Descricao2"));
 salvar.entidade = entidades;

 // When - Salvar arquivo .jaw.xml
 File arquivo = new File("path/to/save/test.jaw.xml");
 salvar.arquivo = arquivo.getAbsolutePath();
 String xml = salvar.xml = new SerializacaoXML().entidades2XML(entidades);
 try {
 FileWriter escrevedor = new FileWriter(arquivo);
 escrevedor.write(xml);
 escrevedor.flush();
 escrevedor.close();
 } catch (IOException e) {
 fail("Erro ao salvar arquivo XML");
 }

 // Then - Arquivo deve ser salvo com sucesso e deve ter tamanho esperado
 File fileSaved = new File(arquivo.getAbsolutePath());
 assertTrue(fileSaved.exists());
 assertTrue(fileSaved.length() > 0);
 }

 @Test
 public void testSalvarInvalidXmlFile() {
 // Given - Entidades preenchidas
 entidades.add(new Entidade(1, "Entidade1", "Tipo1", "Descricao1"));
 entidades.add(new Entidade(2, "Entidade2", "Tipo2", "Descricao2"));
 salvar.entidade = entidades;

 // When - Salvar arquivo .xml invalido
 salvar.arquivo = "test.invalid.xml";
 String xml = salvar.xml = new SerializacaoXML().entidades2XML(entidades);
 try {
 FileWriter escrevedor = new FileWriter(salva.arquivo);
 escrevedor.write(xml);
 escrevedor.flush();
 escrevedor.close();
 } catch (IOException e) {
 fail("Erro ao salvar arquivo XML");
 }

 // Then - Arquivo nao deve ser salvo e nao deve haver mensagem de erro
 assertFalse(salvar.salvar(entidades, salvar.arquivo));
 assertNull(salvar.getMensagem());
 }
}