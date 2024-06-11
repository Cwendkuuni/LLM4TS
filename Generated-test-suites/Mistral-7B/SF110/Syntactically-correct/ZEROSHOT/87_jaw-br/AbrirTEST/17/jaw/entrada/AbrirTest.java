package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private File configDir;
 private String configFilePath;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 configDir = new File(System.getProperty("user.dir") + File.separator + "config");
 configFilePath = configDir.getAbsolutePath() + File.separator + "config.xml";
 Main.configuracao = new Configuracao();
 Main.configuracao.setCaminhoDosProjetos(configFilePath);
 }

 @Test
 public void testAbrirXMLWithValidFile() throws IOException {
 File xmlFile = new File(configFilePath + File.separator + "test.jaw.xml");
 abrir = new Abrir(form, xmlFile.getAbsolutePath());
 assertEquals("test.jaw.xml", abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 abrir = new Abrir(form, new File("test.txt").getAbsolutePath());
 fail("Should throw an IllegalArgumentException with message: Arquivo xml corrompido ou incompatível com aplicação!");
 }

 @Test
 public void testAbrirSerializadoWithValidFile() throws IOException, InterruptedException {
 File serialFile = new File(configFilePath + File.separator + "test.jaw");
 Serializacao serializacao = new Serializacao();
 serializacao.grava(form.getEntidades(), serialFile);
 abrir = new Abrir(form, Abrir.ABRIR, null);
 JFileChooser escolhedor = abrir.getEscolhedor();
 File selectedFile = new File(serialFile.getAbsolutePath());
 escolhedor.setSelectedFile(selectedFile);
 escolhedor.setFileFilter(new Filtro());
 escolhedor.showOpenDialog(null);
 assertEquals(form.getEntidades(), abrir.getEntidades());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 abrir = new Abrir(form, Abrir.ABRIR, null);
 JFileChooser escolhedor = abrir.getEscolhedor();
 escolhedor.setSelectedFile(new File("test.txt"));
 escolhedor.setFileFilter(new Filtro());
 escolhedor.showOpenDialog(null);
 fail("Should throw an IllegalArgumentException with message: Arquivo jaw corrompido ou incompatível com aplicação!");
 }

 @Test
 public void testOpenFileDialogWithXMLFilter() {
 abrir = new Abrir(form, Abrir.ABRIR_XML, null);
 JFileChooser escolhedor = abrir.getEscolhedor();
 FileFilter xmlFilter = new Filtro2();
 escolhedor.setFileFilter(xmlFilter);
 File selectedFile = new File(configFilePath + File.separator + "test.jaw.xml");
 escolhedor.setSelectedFile(selectedFile);
 escolhedor.showOpenDialog(null);
 assertTrue(escolhedor.isFileSelectionEnabled());
 }

 @Test
 public void testOpenFileDialogWithSerializadoFilter() {
 abrir = new Abrir(form, Abrir.ABRIR, null);
 JFileChooser escolhedor = abrir.getEscolhedor();
 FileFilter serializadoFilter = new Filtro();
 escolhedor.setFileFilter(serializadoFilter);
 File selectedFile = new File(configFilePath + File.separator + "test.jaw");
 escolhedor.setSelectedFile(selectedFile);
 escolhedor.showOpenDialog(null);
 assertTrue(escolhedor.isFileSelectionEnabled());
 }

}