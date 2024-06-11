import org.junit.Test;
import org.junit.Assert;
import jaw.entrada.Salvar;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private Entidade entidade;

 @Test
 public void testSalvarWithValidParameters() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(new ArrayList<Entidade>());
 salvar.salvar();
 Assert.assertNotNull(salvar.getXml());
 }

 @Test
 public void testSalvarWithNullEntidades() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(null);
 salvar.salvar();
 Assert.assertNull(salvar.getXml());
 }

 @Test
 public void testSalvarWithInvalidFileFilterForXml() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.setFileFilter(new Salvar.Filtro());
 salvor.salvar("", fileChooser);
 Assert.assertFalse(salvar.isVelas());
 }

 @Test
 public void testSalvarWithValidFileForXml() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 File selectedFile = new File("test.jaw.xml");
 fileChooser.setSelectedFile(selectedFile);
 salvar.salvar("", fileChooser);
 Assert.assertTrue(salvar.isVelas());
 Assert.assertFalse(salvar.getArquivo().equals(""));
 }

 @Test
 public void testSalvarWithNullFilenameAndXmlFileFilter() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar("", null);
 Assert.assertFalse(salvar.isVelas());
 Assert.assertEquals("", salvar.getArquivo());
 }

 @Test
 public void testSalvarWithValidFilenameAndXmlFileFilter() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 String arquivo = "test.jaw.xml";
 salvar.salvar(arquivo, null);
 Assert.assertTrue(salvar.isVelas());
 Assert.assertEquals(arquivo, salvar.getArquivo());
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidFileFilterForJaw() {
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 fileChooser.setFileFilter(new Salvar.Filtro2());
 salvar.salvar("", fileChooser);
 }

 @Test
 public void testSalvarWithValidFileForJaw() {
 salvar = new Salvar(form, Salvar.SALVAR);
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 File selectedFile = new File("test.jaw");
 fileChooser.setSelectedFile(selectedFile);
 salvar.salvar("", fileChooser);
 Assert.assertTrue(salvar.isVelas());
 Assert.assertFalse(salvar.getArquivo().endsWith(".jaw.xml"));
 Assert.assertFalse(salvar.getArquivo().equals(""));
 }

 @Test
 public void testSalvarWithNullFilenameAndJawFileFilter() {
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.salvar("", null);
 Assert.assertFalse(salvar.isVelas());
 Assert.assertEquals("", salvar.getArquivo());
 }

 @Test
 public void testSalvarWithValidFilenameAndJawFileFilter() {
 salvar = new Salvar(form, Salvar.SALVAR);
 String arquivo = "test.jaw";
 salvar.salvar(arquivo, null);
 Assert.assertTrue(salvar.isVelas());
 Assert.assertEquals(arquivo, salvar.getArquivo());
 }

 @Test
 public void testSalvarWithValidParametersForXmlExport() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(new ArrayList<Entidade>());
 salvar.salvarXml();
 Assert.assertNotNull(salvar.getXml());
 }

 @Test
 public void testSalvarXmlWithNullEntidades() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(null);
 salvar.salvarXml();
 Assert.assertNull(salvar.getXml());
 }

 @Test
 public void testSalvarXmlWithInvalidFilename() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvarXml("test.xml");
 Assert.assertFalse(salvar.getArquivo().endsWith(".jaw.xml"));
 }

 @Test(expected = IOException.class)
 public void testSalvarXmlWithInvalidFile() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 File file = new File("test.xml");
 salvar.salvarXml(file.getAbsolutePath());
 }

 @Test
 public void testSalvarXmlWithValidFile() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 File file = new File("test.jaw.xml");
 salvar.salvarXml(file.getAbsolutePath());
 Assert.assertFalse(salvar.getArquivo().equals(""));
 Assert.assertEquals(file.getAbsolutePath(), salvar.getArquivo());
 }

}