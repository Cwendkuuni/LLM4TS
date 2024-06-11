package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<>();
 entidades.add(new Entidade("Id1", "Nome1", "Descricao1"));
 entidades.add(new Entidade("Id2", "Nome2", "Descricao2"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @After
 public void tearDown() {
 form = null;
 salvar = null;
 }

 @Test
 public void testSalvarComArquivoValidoXml() {
 // Teste para Salvar com arquivo válido no formato XML
 String arquivo = "teste.jaw.xml";
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(arquivo));
 salvar.setArquivo(arquivo);
 salvar.salvar(entidades, Salvar.SALVAR_XML);
 File file = new File(arquivo);
 assertTrue(file.exists());
 }

 @Test(expected = AssertionError.class)
 public void testSalvarComArquivoInvalidoXml() {
 // Teste para Salvar com arquivo inválido no formato XML
 String arquivo = "teste.txt";
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(arquivo));
 salvar.setArquivo(arquivo);
 salvar.salvar(entidades, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarComArquivoValido() {
 // Teste para Salvar com arquivo válido no formato JAW
 String arquivo = "teste.jaw";
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(arquivo));
 salvar.setArquivo(arquivo);
 salvar.salvar(entidades, Salvar.SALVAR);
 File file = new File(arquivo);
 assertTrue(file.exists());
 }

 @Test(expected = AssertionError.class)
 public void testSalvarComArquivoInvalido() {
 // Teste para Salvar com arquivo inválido no formato JAW
 String arquivo = "teste.txt";
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File(arquivo));
 salvar.setArquivo(arquivo);
 salvar.salvar(entidades, Salvar.SALVAR);
 }

 @Test
 public void testSalvarComNomeArquivoValido() {
 // Teste para Salvar com nome de arquivo válido
 String arquivo = "teste.jaw";
 salvar.setArquivo(arquivo);
 salvar.salvar(entidades, Salvar.SALVAR_COMO);
 }

 @Test(expected = AssertionError.class)
 public void testSalvarComNomeArquivoInvalido() {
 // Teste para Salvar com nome de arquivo inválido
 String arquivo = "teste";
 salvar.setArquivo(arquivo);
 salvar.salvar(entidades, Salvar.SALVAR_COMO);
 }
}