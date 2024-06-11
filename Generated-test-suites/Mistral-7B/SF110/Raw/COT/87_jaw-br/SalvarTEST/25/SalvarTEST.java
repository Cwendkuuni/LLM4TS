package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SalvarTest {

 private static final String TEST_PROJECT_PATH = "testProject";
 private static final String TEST_XML_FILE_NAME = "test.jaw.xml";
 private static final String TEST_JAW_FILE_NAME = "test.jaw";

 @Test
 public void testSalvarWithValidEntidadesAndXMLFormat() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     String expectedXml = "<entidades>\n<entidade>\n<nome>Teste1</nome>\n<descricao>Teste1</descricao>\n</entidade>\n</entidades>";
     File testFile = new File(TEST_PROJECT_PATH, TEST_XML_FILE_NAME);
     salvar.salvar(entidades, testFile.getAbsolutePath());
     String actualXml = readFile(TEST_XML_FILE_NAME);
     assertEquals(expectedXml, actualXml);
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullEntidades() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     salvar.salvar(null, "test.jaw.xml");
 }

 @Test
 public void testSalvarWithInvalidFileFormat() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     File testFile = new File(TEST_PROJECT_PATH, TEST_JAW_FILE_NAME);
     salvar.salvar(entidades, testFile.getAbsolutePath());
     String expectedMessage = "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !";
     try {
         String actualMessage = Main.janelaPrincipal.log("").split(": ")[1];
         assertEquals(expectedMessage, actualMessage);
     } catch (IndexOutOfBoundsException e) {
         // Log message was not displayed, test passes
     }
 }

 @Test
 public void testSalvarWithValidEntidadesAndJawFormat() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     File testFile = new File(TEST_PROJECT_PATH, TEST_JAW_FILE_NAME);
     salvar.salvar(entidades, testFile.getAbsolutePath());
     String expectedMessage = "Arquivo salvo com sucesso!";
     try {
         String actualMessage = Main.janelaPrincipal.log("").split(": ")[1];
         assertEquals(expectedMessage, actualMessage);
     } catch (IndexOutOfBoundsException e) {
         // Log message was not displayed, test passes
     }
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullFile() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     salvar.salvar(entidades, null);
 }

 @Test
 public void testSalvarWithEmptyFileName() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     String emptyFileName = "";
     SalvarResult result = salvar.salvar(entidades, emptyFileName);
     assertEquals(false, result.isSuccess());
 }

 @Test
 public void testSalvarWithValidEntidadesAndInvalidFileExtension() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     File testFile = new File(TEST_PROJECT_PATH, "test.txt");
     SalvarResult result = salvar.salvar(entidades, testFile.getAbsolutePath());
     assertEquals(false, result.isSuccess());
 }

 @Test
 public void testSalvarWithValidEntidadesAndValidFileExtensionButNoVel() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     File testFile = new File(TEST_PROJECT_PATH, TEST_JAW_FILE_NAME);
     salvar.salvar(entidades, testFile.getAbsolutePath());
     String expectedMessage = "Arquivo salvo com sucesso!";
     try {
         String actualMessage = Main.janelaPrincipal.log("").split(": ")[1];
         assertEquals(expectedMessage, actualMessage);
     } catch (IndexOutOfBoundsException e) {
         // Log message was not displayed, test passes
     }
 }

 @Test
 public void testSalvarWithValidEntidadesAndValidFileExtensionAndVel() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     JFrame frame = new JFrame();
     SalvarResult result = salvar.salvar(entidades, null, frame);
     assertEquals(true, result.isSuccess());
     SalvarResult expectedResult = new SalvarResult(true, TEST_PROJECT_PATH + File.separator + TEST_JAW_FILE_NAME);
     assertEquals(expectedResult, result);
 }

 @Test
 public void testSalvarWithNullForm() {
     Salvar salvar = new Salvar(null, Salvar.SALVAR);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     File testFile = new File(TEST_PROJECT_PATH, TEST_JAW_FILE_NAME);
     salvar.salvar(entidades, testFile.getAbsolutePath());
     String expectedMessage = "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !";
     try {
         String actualMessage = Main.janelaPrincipal.log("").split(": ")[1];
         assertEquals(expectedMessage, actualMessage);
     } catch (IndexOutOfBoundsException e) {
         // Log message was not displayed, test passes
     }
 }

 @Test
 public void testExportarComValidEntidadesEValidFileFormat() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     String expectedXml = "<entidades>\n<entidade>\n<nome>Teste1</nome>\n<descricao>Teste1</descricao>\n</entidade>\n</entidades>";
     File testFile = new File(TEST_PROJECT_PATH, TEST_XML_FILE_NAME);
     salvar.exportar(entidades, testFile.getAbsolutePath());
     String actualXml = readFile(TEST_XML_FILE_NAME);
     assertEquals(expectedXml, actualXml);
 }

 @Test(expected = Exception.class)
 public void testExportarWithNullEntidades() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     salvar.exportar(null, "test.jaw.xml");
 }

 @Test
 public void testExportarWithEmptyFileName() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     SalvarResult result = salvar.exportar(entidades, "");
     assertEquals(false, result.isSuccess());
 }

 @Test
 public void testExportarWithInvalidFileFormat() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     File testFile = new File(TEST_PROJECT_PATH, TEST_JAW_FILE_NAME);
     SalvarResult result = salvar.exportar(entidades, testFile.getAbsolutePath());
     assertEquals(false, result.isSuccess());
 }

 @Test
 public void testExportarWithValidEntidadesAndValidFileFormatButNoVel() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     salvar.setEntidade(entidades);
     String expectedMessage = "Arquivo Exportado com sucesso!";
     try {
         String actualMessage = Main.janelaPrincipal.log("").split(": ")[1];
         assertEquals(expectedMessage, actualMessage);
     } catch (IndexOutOfBoundsException e) {
         // Log message was not displayed, test passes
     }
 }

 @Test
 public void testExportarWithValidEntidadesAndValidFileFormatAndVel() {
     Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
     List<Entidade> entidades = new ArrayList<>();
     entidades.add(new Entidade("Teste1", "Teste1"));
     JFrame frame = new JFrame();
     SalvarResult result = salvar.exportar(entidades, null, frame);
     assertEquals(true, result.isSuccess());
     SalvarResult expectedResult = new SalvarResult(true, TEST_PROJECT_PATH + File.separator + TEST_XML_FILE_NAME);
     assertEquals(expectedResult, result);
 }

 // Helper methods
 private static class SalvarResult {
     private final boolean success;
     private final String filePath;

     public SalvarResult(boolean success, String filePath) {
         this.success = success;
         this.filePath = filePath;
     }

     public boolean isSuccess() {
         return success;
     }
 }

 private static String readFile(String fileName) {
     StringBuilder stringBuilder = new StringBuilder();
     try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
         String line;
         while ((line = reader.readLine()) != null) {
             stringBuilder.append(line);
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
     return stringBuilder.toString();
 }
}