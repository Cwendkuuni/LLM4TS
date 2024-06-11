package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

 private ProcessarEntidades form;
 private String expectedXml;
 private File selectedFile;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 expectedXml = "expected_xml_content";
 selectedFile = new File("test_file.jaw.xml");
 }

 @After
 public void tearDown() {
 form = null;
 expectedXml = null;
 selectedFile = null;
 }

 @Test
 public void testAbrirWithXmlFile() {
 Abrir abrir = new Abrir(form, "test_file.jaw.xml");
 assertEquals("", abrir.xml);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithoutFileOrXml() {
 new Abrir(form, new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithInvalidXmlFile() {
 new Abrir(form, "test_file.txt");
 }

 @Test
 public void testAbrirWithSerializadoFile() {
 final JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setFileFilter(new Filtro());
 escolhedor.showOpenDialog(null);
 selectedFile = escolhedor.getSelectedFile();
 assertNotNull(selectedFile);
 Abrir abrir = new Abrir(form, Abrir.ABRIR);
 assertEquals("", abrir.xml);
 assertNotNull(form.getEntidades());
 }

 @Test(expected = IOException.class)
 public void testAbrirWithInvalidSerializadoFile() {
 final JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setFileFilter(new Filtro());
 escolhedor.showOpenDialog(null);
 selectedFile = escolhedor.getSelectedFile();
 assertNotNull(selectedFile);
 Abrir abrir = new Abrir(form, Abrir.ABRIR);
 assertNotSame("", abrir.xml);
 assertNull(form.getEntidades());
 }

 @Test
 public void testAbrirWithXmlFileFilter() {
 final JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setFileFilter(new Filtro2());
 escolhedor.showOpenDialog(null);
 selectedFile = escolhedor.getSelectedFile();
 assertNotNull(selectedFile);
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 assertEquals(expectedXml, abrir.xml);
 assertNotNull(form.getEntidades());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirWithInvalidXmlFileFilter() {
 final JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setFileFilter(new Filtro());
 escolhedor.showOpenDialog(null);
 selectedFile = escolhedor.getSelectedFile();
 assertNotNull(selectedFile);
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 assertNotSame(expectedXml, abrir.xml);
 assertNull(form.getEntidades());
 }
}