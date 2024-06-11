import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.*;
import java.io.*;
import java.util.*;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidades = new ArrayList<Entidade>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @After
 public void tearDown() {
 // Clear any created files
 if (salvar.getArquivo() != null && new File(salvar.getArquivo()).exists()) {
 File file = new File(salvar.getArquivo());
 file.delete();
 }
 }

 @Test
 public void testSalvarJawFile() {
 // Add some Entidades to the list
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1", new ArrayList<String>());
 entidade1.setId(1);
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2", new ArrayList<String>());
 entidade2.setId(2);
 entidades.add(entidade1);
 entidades.add(entidade2);

 // Set the entidades in the form and call the Salvar method
 salvar.setEntidade(entidades);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File("test.jaw"));
 salvar.escolherArquivo(escolhedor);
 salvar.salvar();

 // Check if the file was created and contains the correct data
 File arquivo = new File(salvar.getArquivo());
 assertTrue(arquivo.exists());
 try {
 FileInputStream inputStream = new FileInputStream(arquivo);
 Serializacao deserializacao = new Serializacao();
 List<Entidade> lstEntidades = deserializacao.deserializar(inputStream);
 assertEquals(2, lstEntidades.size());
 assertNotNull(lstEntidades.get(0).getId());
 assertNotNull(lstEntidades.get(1).getId());
 inputStream.close();
 } catch (IOException e) {
 fail("Failed to read file: " + e.getMessage());
 }
 }

 @Test
 public void testSalvarXmlFile() {
 // Add some Entidades to the list
 Entidade entidade1 = new Entidade("Teste1", "Teste1", "Teste1", new ArrayList<String>());
 entidade1.setId(1);
 Entidade entidade2 = new Entidade("Teste2", "Teste2", "Teste2", new ArrayList<String>());
 entidade2.setId(2);
 entidades.add(entidade1);
 entidades.add(entidade2);

 // Set the entidades in the form and call the Salvar method with XML option
 salvar.setEntidade(entidades);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setSelectedFile(new File("test.xml"));
 salvar.escolherArquivo(escolhedor);
 salvar.salvarXml();

 // Check if the file was created and contains the correct data
 File arquivo = new File(salvar.getArquivo());
 assertTrue(arquivo.exists());
 try {
 FileReader reader = new FileReader(arquivo);
 StringBuilder xmlData = new StringBuilder();
 int c;
 while ((c = reader.read()) != -1) {
 xmlData.append((char) c);
 }
 reader.close();
 SerializacaoXML deserializacaoXML = new SerializacaoXML();
 XmlEntidade xmlEntidade = deserializacaoXML.xml2Entidade(xmlData.toString());
 assertEquals(2, xmlEntidade.getEntidade().size());
 assertNotNull(xmlEntidade.getEntidade().get(0).getId());
 assertNotNull(xmlEntidade.getEntidade().get(1).getId());
 } catch (IOException e) {
 fail("Failed to read file: " + e.getMessage());
 }
 }

 @Test
 public void testSalvarWithoutFile() {
 // Set the form's nomeArquivoSalvo to an empty string
 form.setNomeArquivoSalvo("");
 salvar.setEntidade(entidades);
 salvar.salvar();
 assertNull(salvar.getArquivo());
 }
}