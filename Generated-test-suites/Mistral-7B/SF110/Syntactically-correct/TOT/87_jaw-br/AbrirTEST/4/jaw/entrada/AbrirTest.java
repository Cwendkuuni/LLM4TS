package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLWithValidXmlFile() {
 String xmlFile = "testData/valid.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertEquals("expected XML file name", xmlFile, form.getNomeArquivoSalvo());
 assertNotNull("expected Entidades not null", form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 File jawFile = new File("testData/valid.jaw");
 abrir = new Abrir(form, Abrir.ABRIR, jawFile);
 assertEquals("expected XML file name", jawFile.getName(), form.getNomeArquivoSalvo());
 assertNotNull("expected Entidades not null", form.getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 String xmlFile = "testData/invalid.xml";
 abrir = new Abrir(form, xmlFile);
 fail("expected IllegalArgumentException but got none");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 File jawFile = new File("testData/invalid.jaw");
 abrir = new Abrir(form, Abrir.ABRIR, jawFile);
 fail("expected IllegalArgumentException but got none");
 }

 @Test
 public void testOpenFileDialogWithXmlFilter() {
 FileDialog fileDialog = new FileDialog(new Frame(), "Open XML file", File.OPEN);
 fileDialog.setFileFilter(new Abrir.Filtro2());
 assertEquals("expected number of files, 1", 1, fileDialog.getFiles().length);
 }

 @Test
 public void testOpenFileDialogWithJawFilter() {
 FileDialog fileDialog = new FileDialog(new Frame(), "Open JAW file", File.OPEN);
 fileDialog.setFileFilter(new Abrir.Filtro());
 assertEquals("expected number of files, 1", 1, fileDialog.getFiles().length);
 }

 @After
 public void tearDown() {
 form = null;
 abrir = null;
 }

}