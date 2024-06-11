import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setup() {
 entidades = new ArrayList<>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, 20);
 }

 @Test
 public void testSaveJawFile() throws IOException {
 // given
 Entidade entidade1 = new Entidade("nome1", 1.0, 1.0, 1.0);
 Entidade entidade2 = new Entidade("nome2", 2.0, 2.0, 2.0);
 entidades.add(entidade1);
 entidades.add(entidade2);
 salvar.entidade = entidades;
 File file = new File("temp/salvarTest.jaw");

 // when
 salvar.arquivo = file.getAbsolutePath();
 salvar.salvar(entidades, arquivo);

 // then
 assertTrue(file.exists());
 File newFile = new File("temp/salvarTest_copy.jaw");
 FileChecker.copyFile(file, newFile);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>nome1</nome>\n<x>1.0</x>\n<y>1.0</y>\n<z>1.0</z>\n</entidade>\n<entidade>\n<nome>nome2</nome>\n<x>2.0</x>\n<y>2.0</y>\n<z>2.0</z>\n</entidade>\n</entidades>";
 String actualXml = new String(Files.readAllBytes(newFile.toPath()));
 assertEquals(expectedXml, actualXml);
 // clean up
 Files.delete(file.toPath());
 Files.delete(newFile.toPath());
 }

 @Test
 public void testSaveJawFileWithInvalidExtension() {
 // given
 Entidade entidade1 = new Entidade("nome1", 1.0, 1.0, 1.0);
 Entidade entidade2 = new Entidade("nome2", 2.0, 2.0, 2.0);
 entidades.add(entidade1);
 entidades.add(entidade2);
 salvar.entidade = entidades;

 // when
 String invalidFileName = "temp/salvarTest.txt";
 salvar.arquivo = invalidFileName;
 salvar.salvar(entidades, arquivo);

 // then
 assertFalse(new File(invalidFileName).exists());
 }

 @Test
 public void testSaveXmlFile() {
 // given
 Entidade entidade1 = new Entidade("nome1", 1.0, 1.0, 1.0);
 Entidade entidade2 = new Entidade("nome2", 2.0, 2.0, 2.0);
 entidades.add(entidade1);
 entidades.add(entidade2);
 salvar.entidade = entidades;

 // when
 salvar.xml = "<entidades><entidade><nome>nome1</nome><x>1.0</x><y>1.0</y><z>1.0</z></entidade><entidade><nome>nome2</nome><x>2.0</x><y>2.0</y><z>2.0</z></entidade></entidades>";
 salvar.arquivo = "temp/salvarTest.jaw.xml";
 SerializacaoXML serializaXML = new SerializacaoXML();
 String expectedXml = serializaXML.xml2entidades(salvar.xml);

 // then
 assertEquals(expectedXml, salvar.xml);
 File file = new File("temp/salvarTest.jaw.xml");
 try {
 FileWriter escrevedor = new FileWriter(file);
 escrevedor.write(salvar.xml);
 escrevedor.flush();
 escrevedor.close();
 } catch (IOException e) {
 fail("Could not write file");
 }
 String actualXml = new String(Files.readAllBytes(file.toPath()));
 assertEquals(expectedXml, actualXml);
 // clean up
 try {
 Files.delete(file.toPath());
 } catch (IOException e) {
 fail("Could not delete file");
 }
 }

 @Test
 public void testSaveXmlFileWithEmptyXml() {
 // given
 salvar.xml = "";
 salvar.arquivo = "temp/salvarTest.jaw.xml";

 // when
 String expectedXml = "";
 File file = new File("temp/salvarTest.jaw.xml");
 try {
 FileWriter escrevedor = new FileWriter(file);
 escrevedor.write(salvar.xml);
 escrevedor.flush();
 escrevedor.close();
 } catch (IOException e) {
 fail("Could not write file");
 }
 String actualXml = new String(Files.readAllBytes(file.toPath()));

 // then
 assertEquals(expectedXml, actualXml);
 // clean up
 try {
 Files.delete(file.toPath());
 } catch (IOException e) {
 fail("Could not delete file");
 }
 }
}

class FileChecker {
 static void copyFile(File source, File dest) throws IOException {
 FileUtils.copyFile(source, dest);
 }
}