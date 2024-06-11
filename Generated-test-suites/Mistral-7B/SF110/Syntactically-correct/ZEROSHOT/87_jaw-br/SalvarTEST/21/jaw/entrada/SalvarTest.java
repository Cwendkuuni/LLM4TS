package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import jaw.gui.ProcessarEntidades;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 entidades = new ArrayList<>();
 entidades.add(new Entidade("teste1", "teste1", "teste1"));
 entidades.add(new Entidade("teste2", "teste2", "teste2"));
 try {
 form.setEntidades(entidades);
 } catch (Exception e) {
 fail("Erro ao criar Entidades: " + e.getMessage());
 }
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @After
 public void tearDown() {
 form = null;
 salvar = null;
 }

 @Test
 public void testSalvarJawFile() {
 File expectedFile = new File("test.jaw");
 FileFilter jawFilter = new Salvar.Filtro();
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos().getPath()));
 chooser.setFileFilter(jawFilter);
 chooser.setSelectedFile(expectedFile);
 salvar.setVela(true);
 int returnVal = salvar.salvar(entidades, "test.jaw");
 assertTrue(returnVal == JFileChooser.APPROVE_OPTION);
 assertTrue(salvar.isVela());
 File actualFile = chooser.getSelectedFile();
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testSalvarJawFileWithNullFileChooser() {
 salvar.setVela(true);
 int returnVal = salvar.salvar(entidades, null);
 assertFalse(salvar.isVela());
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarJawFileWithEmptyFileName() {
 salvar.setVela(true);
 salvar.setArquivo("");
 int returnVal = salvar.salvar(entidades, null);
 assertFalse(salvar.isVela());
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarJawFileWithInvalidFileType() {
 salvar.setVela(true);
 salvar.setArquivo("test.txt");
 int returnVal = salvar.salvar(entidades, null);
 assertFalse(salvar.isVela());
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarJawFileWithValidFileName() {
 File expectedFile = new File("test.jaw");
 FileFilter jawFilter = new Salvar.Filtro();
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos().getPath()));
 chooser.setFileFilter(jawFilter);
 chooser.setSelectedFile(expectedFile);
 salvar.setVela(false);
 salvar.setArquivo("test.jaw");
 int returnVal = salvar.salvar(entidades, null);
 assertTrue(returnVal == JFileChooser.APPROVE_OPTION);
 assertFalse(salvar.isVela());
 assertEquals("test.jaw", salvar.getArquivo());
 }

 @Test
 public void testSalvarJawFileWithExistingFile() {
 File expectedFile = new File("test.jaw");
 FileFilter jawFilter = new Salvar.Filtro();
 JFileChooser chooser = new JFileChooser();
 chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos().getPath()));
 chooser.setFileFilter(jawFilter);
 expectedFile.createNewFile();
 salvar.setVela(false);
 salvar.setArquivo("test.jaw");
 int returnVal = salvar.salvar(entidades, null);
 assertTrue(returnVal == JFileChooser.APPROVE_OPTION);
 assertFalse(salvar.isVela());
 assertEquals("test.jaw", salvar.getArquivo());
 expectedFile.delete();
 }

 @Test
 public void testSalvarXMLFile() {
 String xmlExpected = "<entidades>\n<entidade>\n<nome>teste1</nome>\n<sigla>teste1</sigla>\n<descricao>teste1</descricao>\n</entidade>\n<entidade>\n<nome>teste2</nome>\n<sigla>teste2</sigla>\n<descricao>teste2</descricao>\n</entidade>\n</entidades>";
 File expectedFile = new File("test.jaw.xml");FileFilter xmlFilter = new Salvar.Filtro2(); JFileChooser chooser = new JFileChooser(); chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos().getPath())); chooser.setFileFilter(xmlFilter); chooser.setSelectedFile(expectedFile); salvar.setVela(true); int returnVal = salvar.salvar(entidades, "test.jaw.xml"); assertTrue(returnVal == JFileChooser.APPROVE_OPTION); assertTrue(salvar.isVela()); assertEquals("test.jaw.xml", salvar.getArquivo()); try { FileWriter writer = new FileWriter(expectedFile); writer.write(xmlExpected); writer.close(); } catch (IOException e) { fail("Erro ao escrever arquivo XML: " + e.getMessage()); } String xmlActual = ""; try { FileReader reader = new FileReader(expectedFile); xmlActual = new String(new byte[1024], 0, reader.read(new byte[1024])); } catch (IOException e) { fail("Erro ao ler arquivo XML: " + e.getMessage()); } assertEquals(xmlExpected, xmlActual); expectedFile.delete(); }

 @Test public void testSalvarXMLFileWithNullFileChooser() { salvar.setVela(true); int returnVal = salvar.salvar(entidades, null); assertFalse(salvar.isVela()); assertNull(salvar.getArquivo()); }

 @Test public void testSalvarXMLFileWithEmptyFileName() { salvar.setVela(true); salvar.setArquivo(""); int returnVal = salvar.salvar(entidades, null); assertFalse(salvar.isVela()); assertNull(salvar.getArquivo()); }

 @Test public void testSalvarXMLFileWithInvalidFileType() { salvar.setVela(true); salvar.setArquivo("test.jaw"); int returnVal = salvar.salvar(entidades, null); assertFalse(salvar.isVela()); assertNull(salvar.getArquivo()); }

 @Test public void testSalvarXMLFileWithValidFileName() { File expectedFile = new File("test.jaw.xml"); FileFilter xmlFilter = new Salvar.Filtro2(); JFileChooser chooser = new JFileChooser(); chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos().getPath())); chooser.setFileFilter(xmlFilter); chooser.setSelectedFile(expectedFile); salvar.setVela(false); salvar.setArquivo("test.jaw.xml"); int returnVal = salvar.salvar(entidades, null); assertTrue(returnVal == JFileChooser.APPROVE_OPTION); assertFalse(salvar.isVela()); assertEquals("test.jaw.xml", salvar.getArquivo()); }

 @Test public void testSalvarXMLFileWithExistingFile() { File expectedFile = new File("test.jaw.xml"); FileFilter xmlFilter = new Salvar.Filtro2(); JFileChooser chooser = new JFileChooser(); chooser.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos().getPath())); chooser.setFileFilter(xmlFilter); expectedFile.createNewFile(); salvar.setVela(false); salvar.setArquivo("test.jaw.xml"); int returnVal = salvar.salvar(entidades, null); assertTrue(returnVal == JFileChooser.APPROVE_OPTION); assertFalse(salvar.isVela()); assertEquals("test.jaw.xml", salvar.getArquivo()); expectedFile.delete(); }
}