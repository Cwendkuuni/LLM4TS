import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 // Given
 entidades.add(new Entidade("Teste1", "Teste1", new String[]{"Teste11", "Teste12"}));
 salvar.entidade = entidades;

 // When
 String expectedFilePath = "test/expected/salvarTest/testFile.jaw";
 File selectedFile = new File(expectedFilePath);
 salvar.arquivo = selectedFile.getAbsolutePath();
 salvar.vela = true;

 // Then
 assertTrue(salvar.salvar(entidades, arquivo));
 File savedFile = new File(expectedFilePath);
 assertTrue(savedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarJawFileNullEntidades() {
 // Given
 salvar.entidade = null;

 // When
 assertFalse(salvar.salvar(entidades, arquivo));
 }

 @Test(expected = IOException.class)
 public void testSalvarJawFileIOException() {
 // Given
 File tempFile = File.createTempFile("temp", "jaw");
 salvar.arquivo = tempFile.getAbsolutePath();
 salvar.vela = true;

 // When and Then
 assertThrows(IOException.class, () -> salvar.salvar(entidades, arquivo));
 assertFalse(tempFile.exists());
 }

 @Test
 public void testSalvarXmlFile() {
 // Given
 entidades.add(new Entidade("Teste1", "Teste1", new String[]{"Teste11", "Teste12"}));
 salvar.xml = "<entidades>";
 salvar.entidade = entidades;

 // When
 String expectedFilePath = "test/expected/salvarTest/testFile.jaw.xml";
 File selectedFile = new File(expectedFilePath);
 salvar.arquivo = selectedFile.getAbsolutePath();
 salvar.vela = true;

 // Then
 String expectedXml = "<entidades><entidade><nome>Teste1</nome><descricao>Teste1</descricao><componentes><componente>Teste11</componente><componente>Teste12</componente></componentes></entidade></entidades>";
 String actualXml = salvar.salvarXml(entidades, arquivo);
 assertEquals(expectedXml, actualXml);
 File savedFile = new File(expectedFilePath);
 assertTrue(savedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXmlFileNullEntidades() {
 // Given
 salvar.entidade = null;

 // When
 assertThrows(IllegalArgumentException.class, () -> salvar.salvarXml(entidades, arquivo));
 }

 @Test(expected = IOException.class)
 public void testSalvarXmlFileIOException() {
 // Given
 File tempFile = File.createTempFile("temp", "xml");
 salvar.arquivo = tempFile.getAbsolutePath();
 salvar.vela = true;

 // When and Then
 assertThrows(IOException.class, () -> salvar.salvarXml(entidades, arquivo));
 assertFalse(tempFile.exists());
 }
}