package jaw.entrada.test;

 import static org.junit.Assert.*;
 import java.io.File;
 import org.junit.Test;
 import jaw.entrada.Abrir;
 import jaw.entrada.ProcessarEntidades;
 import jaw.gui.Main;
 import javax.swing.JFileChooser;
 import javax.swing.filechooser.FileFilter;

 public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setup() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null);
 }

 @Test
 public void testAbrirWithNullFormAndNullArquivo() {
 Abrir abrir = new Abrir(null, null);
 assertNull(abrir.xml);
 }

 @Test
 public void testAbrirWithFormAndXmlFile() throws Exception {
 String xmlFile = "teste.jaw.xml";
 File file = new File(xmlFile);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setFileFilter(new Filtro2());
 escolhedor.showOpenDialog(null);
 escolhedor.setSelectedFile(file);
 abrir = new Abrir(form, 10);
 assertEquals(xmlFile, form.getNomeArquivoSalvo());
 assertNotNull(abrir.xml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithFormAndInvalidXmlFile() {
 String xmlFile = "teste.txt";
 File file = new File(xmlFile);
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
 escolhedor.setFileFilter(new Filtro2());
 escolhedor.showOpenDialog(null);
 escolhedor.setSelectedFile(file);
 abrir = new Abrir(form, 10);
 }

 @Test
 public void testAbrirWithFormAndSerializiedFile() throws Exception {
 String serializiedFile = "teste.jaw";
 File file = new File(serializiedFile);
 abrir = new Abrir(form, 20);
 abrir.abrirSerializado(file.getAbsolutePath());
 assertEquals(serializiedFile, form.getNomeArquivoSalvo());
 assertNotNull(abrir.xml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirWithFormAndInvalidSerializiedFile() {
 String serializiedFile = "teste.txt";
 File file = new File(serializiedFile);
 abrir = new Abrir(form, 20);
 abrir.abrirSerializado(file.getAbsolutePath());
 }

 @Test
 public void testAbrirWithFormAndOpenDialogCancelled() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showOpenDialog(null);
 abrir = new Abrir(form, 10);
 assertTrue(abrir.xml.isEmpty());
 }

 @Test
 public void testAbrirXMLWithNullFormAndNullArquivo() {
 Abrir abrir = new Abrir(null, null);
 abrir.abrirXML(null);
 assertNull(abrir.xml);
 }

 @Test
 public void testAbrirXMLWithFormAndXmlFile() throws Exception {
 String xmlFile = "teste.jaw.xml";
 File file = new File(xmlFile);
 abrir = new Abrir(form, null);
 abrir.abrirXML(file.getAbsolutePath());
 assertEquals(xmlFile, form.getNomeArquivoSalvo());
 assertNotNull(abrir.xml);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithFormAndInvalidXmlFile() {
 String xmlFile = "teste.txt";
 abrir = new Abrir(form, null);
 abrir.abrirXML(xmlFile);
 }

 @Test
 public void testOpenFileDialogWithNoFileFilter() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.showOpenDialog(null);
 abrir = new Abrir(form, 20);
 assertTrue(abrir.xml.isEmpty());
 }

 @Test
 public void testOpenFileDialogWithXmlFileFilter() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setFileFilter(new Filtro2());
 escolhedor.showOpenDialog(null);
 abrir = new Abrir(form, 10);
 assertNotNull(abrir.xml);
 }

 @Test
 public void testOpenFileDialogWithSerializiedFileFilter() {
 JFileChooser escolhedor = new JFileChooser();
 escolhedor.setFileFilter(new Filtro());
 escolhedor.showOpenDialog(null);
 abrir = new Abrir(form, 20);
 assertNotNull(abrir.xml);
 }

 private static class Filtro extends FileFilter {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
 }

 private static class Filtro2 extends FileFilter {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
 }
 }