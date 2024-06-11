package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

 @After
 public void tearDown() {
 // Clean up, if necessary
 }

 @Test
 public void testSalvarWithoutFileChooser() {
 // Test case for saving without file chooser
 salvar.setEntidades(entidades);
 salvar.salvar("test.jaw");
 assertTrue(new File("test.jaw").exists());
 }

 @Test
 public void testSalvarWithFileChooser() {
 // Test case for saving with file chooser
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setCurrentDirectory(new File("src/test/resources"));
 File selectedFile = new File("test.jaw");
 fileChooser.setSelectedFile(selectedFile);
 salvar.setEntidades(entidades);
 salvar.salvar(fileChooser, Salvar.SALVAR);
 assertTrue(selectedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithNullEntidades() {
 // Test case for saving with null entidades
 salvar.salvar(null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithEmptyFileName() {
 // Test case for saving with empty fileName
 salvar.setEntidades(entidades);
 salvar.setArquivo("");
 salvar.salvar();
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithInvalidFileExtension() {
 // Test case for saving with invalid file extension
 salvar.setEntidades(entidades);
 salvar.setArquivo("test.txt");
 salvar.salvar();
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testExportarArquivoXml() {
 // Test case for exporting XML file
 salvar.setEntidades(entidades);
 salvar.setArquivo("test.jaw.xml");
 salvar.exportarArquivoXml();
 File xmlFile = new File("test.jaw.xml");
 assertTrue(xmlFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExportarArquivoXmlWithNullEntidades() {
 // Test case for exporting XML file with null entidades
 salvar.setArquivo("test.jaw.xml");
 salvar.exportarArquivoXml();
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExportarArquivoXmlWithEmptyFileName() {
 // Test case for exporting XML file with empty fileName
 salvar.setEntidades(entidades);
 salvar.setArquivo("");
 salvar.exportarArquivoXml();
 fail("Should throw an IllegalArgumentException");
 }
}