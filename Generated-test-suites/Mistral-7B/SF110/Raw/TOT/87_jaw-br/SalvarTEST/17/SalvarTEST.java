import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {

 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidades = new ArrayList<>();
 salvar = new Salvar(null, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarXmlWithValidFile() throws IOException {
 // Given: Entidades para serem salvos
 entidades.add(new Entidade("Teste1", 1.0, "Teste1"));
 entidades.add(new Entidade("Teste2", 2.0, "Teste2"));

 // When: Salvar arquivo XML com sucesso
 salvar.entidade = entidades;
 File file = new File("teste.jaw.xml");
 salvar.arquivo = file.getAbsolutePath();
 salvar.salvar(entidades, arquivo);

 // Then: Arquivo deve ser criado com sucesso
 assertTrue(file.exists());

 // Clean up
 file.delete();
 }

 @Test(expected = IOException.class)
 public void testSalvarXmlWithInvalidFile() throws IOException {
 // Given: Entidades para serem salvos
 entidades.add(new Entidade("Teste1", 1.0, "Teste1"));
 entidades.add(new Entidade("Teste2", 2.0, "Teste2"));

 // When: Tentativa de salvar arquivo XML com nome inválido
 salvar.entidade = entidades;
 salvar.arquivo = "teste.xml";
 salvar.salvar(entidades, arquivo);

 // Then: Exceção deve ser lançada
 fail("Deve lançar exceção ao salvar arquivo XML com nome inválido");
 }

 @Test(expected = IOException.class)
 public void testSalvarXmlWithNullList() {
 // Given: Nulo list of Entidades
 salvar.entidade = null;

 // When: Tentativa de salvar arquivo XML
 salvar.arquivo = "teste.jaw.xml";

 // Then: Exceção deve ser lançada
 fail("Deve lançar exceção ao salvar arquivo XML com list null");
 }

 @Test
 public void testSalvarJawWithValidFile() throws IOException {
 // Given: Entidades para serem salvos
 entidades.add(new Entidade("Teste1", 1.0, "Teste1"));
 entidades.add(new Entidade("Teste2", 2.0, "Teste2"));

 // When: Salvar arquivo JAW com sucesso
 salvar.entidade = entidades;
 salvar.arquivo = "teste.jaw";
 salvar.salvar(entidades, arquivo);

 // Then: Arquivo deve ser criado com sucesso
 File file = new File("teste.jaw");
 assertTrue(file.exists());

 // Clean up
 file.delete();
 }

 @Test(expected = IOException.class)
 public void testSalvarJawWithInvalidFile() throws IOException {
 // Given: Entidades para serem salvos
 entidades.add(new Entidade("Teste1", 1.0, "Teste1"));
 entidades.add(new Entidade("Teste2", 2.0, "Teste2"));

 // When: Tentativa de salvar arquivo JAW com nome inválido
 salvar.entidade = entidades;
 salvar.arquivo = "teste.txt";
 salvar.salvar(entidades, arquivo);

 // Then: Exceção deve ser lançada
 fail("Deve lançar exceção ao salvar arquivo JAW com nome inválido");
 }

 @Test(expected = IOException.class)
 public void testSalvarJawWithNullList() {
 // Given: Nulo list of Entidades
 salvar.entidade = null;

 // When: Tentativa de salvar arquivo JAW
 salvar.arquivo = "teste.jaw";

 // Then: Exceção deve ser lançada
 fail("Deve lançar exceção ao salvar arquivo JAW com list null");
 }

}