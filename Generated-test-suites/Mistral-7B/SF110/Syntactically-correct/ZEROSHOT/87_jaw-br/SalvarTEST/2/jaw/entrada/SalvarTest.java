package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
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
 entidades = new ArrayList<Entidade>();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarComXML() {
 // Criar alguns objetos Entidade para serem salvos
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();

 entidades.add(entidade1);
 entidades.add(entidade2);

 // Configurar o Salvar para salvar em XML
 salvar.xml = "teste.jaw.xml";

 // Salvar as entidades
 salvar.salvar(entidades, salvar.xml);

 // Verificar se o arquivo foi criado
 File arquivo = new File(salvar.xml);
 assertTrue(arquivo.exists());

 // Verificar se o conteudo do arquivo eh o esperado
 String xmlEsperado = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jaw>\n<entidade>\n<id>1</id>\n<nome>Entidade 1</nome>\n</entidade>\n<entidade>\n<id>2</id>\n<nome>Entidade 2</nome>\n</entidade>\n</jaw>";
 String xmlLido = "";
 try {
 FileReader leitor = new FileReader(salvar.xml);
 BufferedReader buffer = new BufferedReader(leitor);
 String linha = buffer.readLine();
 while (linha != null) {
 xmlLido += linha;
 linha = buffer.readLine();
 }
 buffer.close();
 leitor.close();
 } catch (IOException e) {
 e.printStackTrace();
 fail("Nao deve haver erros na leitura do arquivo");
 }
 assertEquals(xmlEsperado, xmlLido);
 }

 @Test
 public void testSalvarComArquivo() {
 // Criar alguns objetos Entidade para serem salvos
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();

 entidades.add(entidade1);
 entidades.add(entidade2);

 // Configurar o Salvar para salvar com arquivo .jaw
 salvar.arquivo = "teste.jaw";

 // Salvar as entidades
 salvar.salvar(entidades, salvar.arquivo);

 // Verificar se o arquivo foi criado
 File arquivo = new File(salvar.arquivo);
 assertTrue(arquivo.exists());

 // Verificar se o tamanho do arquivo eh o esperado
 long tamanhoEsperado = 1024; // Tamanho em bytes
 long tamanhoLido = arquivo.length();
 assertEquals(tamanhoEsperado, tamanhoLido);
 }

 @Test
 public void testNaoSalvarComNull() {
 // Configurar o Salvar para tentar salvar com arquivo null
 salvar.arquivo = null;

 // Tentar salvar as entidades
 salvar.salvar(entidades, salvar.arquivo);

 // Verificar se o metodo nao retornou erro
 fail("Deve haver erro ao tentar salvar com arquivo nulo");
 }

 @Test
 public void testNaoSalvarSemEntidades() {
 // Configurar o Salvar para tentar salvar sem entidades
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.arquivo = "teste.jaw";

 // Tentar salvar as entidades
 salvar.salvar(null, salvar.arquivo);

 // Verificar se o metodo nao retornou erro
 fail("Deve haver erro ao tentar salvar sem entidades");
 }
}