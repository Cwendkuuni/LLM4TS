package jaw.entrada;

import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import jaw.metamodelo.*;
import jaw.gui.*;
import jaw.*;

public class SalvarTest {

 private static final String TEST_PROJECT_PATH = "src/test/resources/test_files/";
 private static final String DEFAULT_XML_FILE = "Default.jaw.xml";
 private static final String DEFAULT_JAW_FILE = "Default.jaw";
 private static final String JAW_FILE_WITH_INVALID_EXTENSION = "Default.jaw.txt";
 private static final String XML_FILE_WITH_INVALID_EXTENSION = "Default.jaw.xml.txt";
 private Salvar salvar;
 private ProcessarEntidades form;

 @Before
 public void setUp() {
 Formas f1 = new Formas();
 f1.setNome("Circulo");
 f1.setTipo("Circulo");
 f1.setRaio(2.0);
 Entidade e1 = new Entidade();
 e1.setNome("Teste Entidade 1");
 e1.setTipo("Entidade");
 e1.addFormas(f1);
 form = new ProcessarEntidades();
 form.setEntidades(new ArrayList<Entidade>());
 form.getEntidades().add(e1);
 }

 @Test
 public void testSalvarWithValidParametersAndJawFormat() {
     int tipoSalvar = Salvar.SALVAR;
     salvar = new Salvar(form, tipoSalvar);
     File expectedFile = new File(TEST_PROJECT_PATH + DEFAULT_JAW_FILE);
     assertEquals(expectedFile.getAbsolutePath(), salvar.getArquivo());
     assertFalse(salvar.isVelaz());
     Serializacao serializacao = new Serializacao();
     assertTrue(serializacao.salvar(salvar.getEntidade(), salvar.getArquivo()));
 }

 @Test
 public void testSalvarWithValidParametersAndXmlFormat() {
     int tipoSalvar = Salvar.SALVAR_XML;
     salvar = new Salvar(form, tipoSalvar);
     File expectedFile = new File(TEST_PROJECT_PATH + DEFAULT_XML_FILE);
     assertEquals(expectedFile.getAbsolutePath(), salvar.getArquivo());
     assertTrue(salvar.isVelaz());
     SerializacaoXML serializacaoXML = new SerializacaoXML();
     String xml = serializacaoXML.entidades2XML(salvar.getEntidade());
     try {
         FileWriter writer = new FileWriter(salvar.getArquivo());
         writer.write(xml);
         writer.close();
     } catch (IOException e) {
         e.printStackTrace();
     }
     assertTrue(salvar.getXml().equals(xml));
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidExtensionAndJawFormat() {
     int tipoSalvar = Salvar.SALVAR;
     salvar = new Salvar(form, tipoSalvar);
     String invalidFile = TEST_PROJECT_PATH + JAW_FILE_WITH_INVALID_EXTENSION;
     salvar.setArquivo(invalidFile);
     Serializacao serializacao = new Serializacao();
     try {
         serializacao.salvar(salvar.getEntidade(), salvar.getArquivo());
         fail("Expected an exception");
     } catch (Exception e) {
         // Expected exception
     }
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidExtensionAndXmlFormat() {
     int tipoSalvar = Salvar.SALVAR_XML;
     salvar = new Salvar(form, tipoSalvar);
     String invalidFile = TEST_PROJECT_PATH + XML_FILE_WITH_INVALID_EXTENSION;
     salvar.setArquivo(invalidFile);
     SerializacaoXML serializacaoXML = new SerializacaoXML();
     try {
         String xml = serializacaoXML.entidades2XML(salvar.getEntidade());
         FileWriter writer = new FileWriter(salvar.getArquivo());
         writer.write(xml);
         writer.close();
         fail("Expected an exception");
     } catch (Exception e) {
         // Expected exception
     }
 }

 @Test
 public void testSalvarWithNullFileChooser() {
     int tipoSalvar = Salvar.SALVAR;
     salvar = new Salvar(form, tipoSalvar);
     salvar.setEntidade(form.getEntidades());
     salvar.setArquivo(null);
     assertEquals(0, salvar.getEntidade().size());
 }

 @Test
 public void testSalvarWhenFileChooserReturnsNull() {
     int tipoSalvar = Salvar.SALVAR;
     salvar = new Salvar(form, tipoSalvar);
     JFileChooser fileChooser = new JFileChooser();
     fileChooser.showSaveDialog(form);
     salvar.setVelaz(fileChooser.isSaveDialog());
     assertFalse(salvar.isVelaz());
 }

 @Test
 public void testSalvarWhenFileChooserReturnsValidFile() {
     int tipoSalvar = Salvar.SALVAR;
     salvar = new Salvar(form, tipoSalvar);
     JFileChooser fileChooser = new JFileChooser();
     File selectedFile = new File(TEST_PROJECT_PATH + "TestFile.jaw");
     fileChooser.setSelectedFile(selectedFile);
     fileChooser.showSaveDialog(form);
     salvar.setVelaz(fileChooser.isSaveDialog());
     salvar.setArquivo(fileChooser.getSelectedFile().getAbsolutePath());
     assertTrue(salvar.isVelaz());
 }

 @Test
 public void testSalvarWhenFileChooserReturnsEmptyName() {
     int tipoSalvar = Salvar.SALVAR;
     salvar = new Salvar(form, tipoSalvar);
     JFileChooser fileChooser = new JFileChooser();
     fileChooser.setSelectedFile(null);
     fileChooser.showSaveDialog(form);
     salvar.setVelaz(fileChooser.isSaveDialog());
     assertFalse(salvar.isVelaz());
 }

 @Test
 public void testSalvarWhenFileChooserReturnsValidNameAndJawFormat() {
     int tipoSalvar = Salvar.SALVAR;
     salvar = new Salvar(form, tipoSalvar);
     JFileChooser fileChooser = new JFileChooser();
     File selectedFile = new File(TEST_PROJECT_PATH + "TestFile.jaw");
     fileChooser.setSelectedFile(selectedFile);
     fileChooser.showSaveDialog(form);
     salvar.setVelaz(fileChooser.isSaveDialog());
     salvar.setArquivo(fileChooser.getSelectedFile().getAbsolutePath());
     Serializacao serializacao = new Serializacao();
     assertTrue(serializacao.salvar(salvar.getEntidade(), salvar.getArquivo()));
 }

 @Test
 public void testSalvarWhenFileChooserReturnsValidNameAndXmlFormat() {
     int tipoSalvar = Salvar.SALVAR_XML;
     salvar = new Salvar(form, tipoSalvar);
     JFileChooser fileChooser = new JFileChooser();
     File selectedFile = new File(TEST_PROJECT_PATH + "TestFile.jaw.xml");
     fileChooser.setSelectedFile(selectedFile);
     fileChooser.showSaveDialog(form);
     salvar.setVelaz(fileChooser.isSaveDialog());
     salvar.setArquivo(fileChooser.getSelectedFile().getAbsolutePath());
     SerializacaoXML serializacaoXML = new SerializacaoXML();
     String xml = serializacaoXML.entidades2XML(salvar.getEntidade());
     try {
         FileWriter writer = new FileWriter(salvar.getArquivo());
         writer.write(xml);
         writer.close();
     } catch (IOException e) {
         e.printStackTrace();
     }
     assertTrue(salvar.getXml().equals(xml));
 }

}