import org.junit.Test;
import jaw.entrada.Salvar;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class SalvarTest {

 @Test
 public void testSalvarWithValidInput() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
 ArrayList<Entidade> entidades = new ArrayList<>(); // assuming ProcessarEntidades has a getEntidades method that returns a list of Entidade objects
 entidades.add(new Entidade());
 entidades.add(new Entidade());
 salvar.entidade = entidades;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(new File("test.jaw"));
 salvar.arquivo = fileChooser.getSelectedFile().getAbsolutePath();
 Serializacao serializacao = new Serializacao();
 assertEquals(true, serializacao.salvar(salvar.entidade, salvar.arquivo));
 }

 @Test
 public void testSalvarWithNullEntidadeList() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
 salvar.entidade = null;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(new File("test.jaw"));
 salvar.arquivo = fileChooser.getSelectedFile().getAbsolutePath();
 Serializacao serializacao = new Serializacao();
 assertEquals(false, serializacao.salvar(salvar.entidade, salvar.arquivo));
 }

 @Test
 public void testSalvarWithEmptyFileChooser() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(null);
 assertEquals(false, salvar.vela);
 }

 @Test
 public void testSalvarWithValidFileChooserAndArquivoAlreadyExists() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
 ArrayList<Entidade> entidades = new ArrayList<>(); // assuming ProcessarEntidades has a getEntidades method that returns a list of Entidade objects
 entidades.add(new Entidade());
 entidades.add(new Entidade());
 salvar.entidade = entidades;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(new File("test.jaw"));
 salvar.arquivo = fileChooser.getSelectedFile().getAbsolutePath();
 Serializacao serializacao = new Serializacao();
 assertTrue(new File(salvar.arquivo).exists());
 assertEquals(true, serializacao.salvar(salvar.entidade, salvar.arquivo));
 }

 @Test
 public void testSalvarWithInvalidFileChooserAndArquivoDoesNotExist() {
 Salvar salvar = new Salvar(new ProcessarEntidades(), 20);
 ArrayList<Entidade> entidades = new ArrayList<>(); // assuming ProcessarEntidades has a getEntidades method that returns a list of Entidade objects
 entidades.add(new Entidade());
 entidades.add(new Entidade());
 salvar.entidade = entidades;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setSelectedFile(null);
 assertEquals(false, salvar.vela);
 }

}