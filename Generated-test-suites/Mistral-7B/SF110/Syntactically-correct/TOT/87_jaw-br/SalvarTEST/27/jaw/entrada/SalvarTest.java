import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import jaw.entrada.*;
import jaw.metamodelo.*;
import jaw.gui.*;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidadeList;
 private Entidade entidade;
 private String expectedArquivo;

 @Before
 public void setUp() {
 entidade = new Entidade();
 entidadeList = new ArrayList<Entidade>();
 entidadeList.add(entidade);
 form = new ProcessarEntidades() {
 @Override
 public List<Entidade> getEntidades() throws Exception {
 return entidadeList;
 }
 };
 }

 @Test
 public void testSalvarXML() {
 Salvar expectedSalvar = new Salvar(form, Salvar.SALVAR_XML);
 expectedArquivo = "expectedPath/project.jaw.xml";
 expectedSalvar.arquivo = expectedArquivo;
 SerializacaoXML serializaXML = new SerializacaoXML();
 String xml = serializaXML.entidades2XML(entidadeList);
 expectedSalvar.xml = xml;

 try {
 FileWriter escrevedor = new FileWriter(expectedArquivo);
 escrevedor.write(xml);
 escrevedor.flush();
 escrevedor.close();
 } catch (IOException e) {
 fail("Failed to write expected file.");
 }

 Salvar actualSalvar = new Salvar(form, Salvar.SALVAR_XML);
 actualSalvar.arquivo = expectedArquivo;
 SerializacaoXML actualSerializaXML = new SerializacaoXML();
 String actualXml = actualSerializaXML.entidades2XML(entidadeList);
 actualSalvar.xml = actualXml;

 assertEquals("Expected and actual XML should be equal.", xml, actualSalvar.xml);
 try {
 File file = new File(expectedArquivo);
 String actualFileContent = new String(new FileInputStream(file).readAllBytes());
 assertEquals("Expected and actual file content should be equal.", xml, actualFileContent);
 } catch (IOException e) {
 fail("Failed to read expected file.");
 }
 }

 @Test
 public void testSalvarJaw() {
 Salvar expectedSalvar = new Salvar(form, Salvar.SALVAR);
 expectedArquivo = "expectedPath/project.jaw";
 expectedSalvar.arquivo = expectedArquivo;
 Serializacao serializacao = new Serializacao();
 try {
 serializacao.salvar(entidadeList, expectedArquivo);
 } catch (Exception e) {
 fail("Failed to save expected file.");
 }
 Salvar actualSalvar = new Salvar(form, Salvar.SALVAR);
 actualSalvar.arquivo = expectedArquivo;
 try {
 actualSalvar.salvar(entidadeList, actualSalvar.arquivo);
 } catch (Exception e) {
 fail("Failed to save actual file.");
 }

 assertFalse("Expected and actual files should not be equal.", expectedArquivo.equals(actualSalvar.arquivo));
 try {
 File expectedFile = new File(expectedArquivo);
 File actualFile = new File(actualSalvar.arquivo);
 assertEquals("Expected and actual files should have the same size.", expectedFile.length(), actualFile.length());
 String expectedFileContent = new String(new FileInputStream(expectedFile).readAllBytes());
 String actualFileContent = new String(new FileInputStream(actualFile).readAllBytes());
 assertEquals("Expected and actual file content should be equal.", expectedFileContent, actualFileContent);
 } catch (IOException e) {
 fail("Failed to read expected or actual files.");
 }
 }

 @Test
 public void testNullArquivo() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.arquivo = null;
 salvar.salvar(entidadeList, salvar.arquivo);
 assertTrue("Expected an exception to be thrown.", salvar.vela);
 }

 @Test
 public void testEmptyNomeArquivo() {
 ProcessarEntidades formWithEmptyName = new ProcessarEntidades() {
 @Override
 public String getNomeArquivoSalvo() {
 return "";
 }
 };
 Salvar salvar = new Salvar(formWithEmptyName, Salvar.SALVAR_XML);
 salvar.salvar(entidadeList, salvar.arquivo);
 assertFalse("Expected an exception to be thrown.", salvar.vela);
 }
}