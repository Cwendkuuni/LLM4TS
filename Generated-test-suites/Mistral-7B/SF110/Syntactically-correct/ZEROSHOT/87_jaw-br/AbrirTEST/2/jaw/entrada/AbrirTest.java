package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import org.junit.Before;
import org.junit.Test;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, "test.jaw.xml");
 }

 @Test
 public void testAbrirXML() {
 String expected = "Expected XML content";
 FileReader reader = new FileReader("test.jaw.xml");
 try {
 form.setEntidades(new SerializacaoXML().xml2Entidades(reader));
 String actual = form.getEntidades().toString();
 assertEquals(expected, actual);
 } catch (Exception e) {
 fail("Exception should not be thrown");
 } finally {
 try {
 reader.close();
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }
 }
 }

 @Test
 public void testAbrirSerializadoInvalidFileType() {
 File invalidFile = new File("test.txt");
 abrir = new Abrir(form, invalidFile);
 assertEquals("", form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoInvalidFile() {
 File invalidFile = new File("test.jaw");
 abrir = new Abrir(form, invalidFile);
 assertEquals("", form.getEntidades());
 }

 @Test
 public void testAbrirSerializadoValidFile() {
 File validFile = new File("test.jaw");
 when(new Serializacao().abrir(validFile.getAbsolutePath()))
 .thenReturn(new Entidades("Test Entities"));
 abrir = new Abrir(form, validFile);
 String actual = form.getEntidades().toString();
 assertEquals("Test Entities", actual);
 }

 @Test
 public void testAbrirXMLInvalidFileType() {
 File invalidFile = new File("test.txt");
 abrir = new Abrir(form, invalidFile);
 assertEquals("", form.getEntidades());
 }

 @Test
 public void testAbrirXMLInvalidFile() {
 File invalidFile = new File("test.txt");
 abrir = new Abrir(form, invalidFile);
 assertEquals("", form.getEntidades());
 }

 @Test
 public void testAbrirXMLValidFile() {
 File validFile = new File("test.jaw.xml");
 when(new SerializacaoXML().xml2Entidades(new FileReader(validFile)))
 .thenReturn(new Entidades("Test Entities"));
 abrir = new Abrir(form, validFile);
 String actual = form.getEntidades().toString();
 assertEquals("Test Entities", actual);
 }

 @Test
 public void testJFileChooserOpenFileDialog() {
 FileDialog expectedFileDialog = new FileDialog(new Frame(), "Open", FileDialog.OPEN);
 JFileChooser jFileChooser = spy(new JFileChooser());
 doReturn(expectedFileDialog).when(jFileChooser).createDialog(eq(null), eq("Open"), eq(FileDialog.OPEN));
 abrir = new Abrir(form, jFileChooser, Abrir.ABRIR);
 verify(jFileChooser).setFileFilter(isA(FileFilter.class));
 verify(jFileChooser).setCurrentDirectory(argThat(new IsDirectoryPredicate()));
 verify(jFileChooser, times(1)).showOpenDialog(eq(null));
 }

 @Test
 public void testJFileChooserOpenXMLFileDialog() {
 FileDialog expectedFileDialog = new FileDialog(new Frame(), "Open", FileDialog.OPEN);
 JFileChooser jFileChooser = spy(new JFileChooser());
 FileFilter xmlFilter = spy(new Filtro2());
 doReturn(xmlFilter).when(jFileChooser).getFileFilter();
 doReturn(expectedFileDialog).when(jFileChooser).createDialog(eq(null), eq("Open"), eq(FileDialog.OPEN));
 abrir = new Abrir(form, jFileChooser, Abrir.ABRIR_XML);
 verify(jFileChooser).setFileFilter(isA(FileFilter.class));
 verify(jFileChooser).setCurrentDirectory(argThat(new IsDirectoryPredicate()));
 verify(jFileChooser, times(1)).showOpenDialog(eq(null));
 }

 @Test
 public void testJFileChooserOpenSerializadoFileDialog() {
 FileDialog expectedFileDialog = new FileDialog(new Frame(), "Open", FileDialog.OPEN);
 JFileChooser jFileChooser = spy(new JFileChooser());
 FileFilter serializadoFilter = spy(new Filtro());
 doReturn(serializadoFilter).when(jFileChooser).getFileFilter();
 doReturn(expectedFileDialog).when(jFileChooser).createDialog(eq(null), eq("Open"), eq(FileDialog.OPEN));
 abrir = new Abrir(form, jFileChooser, Abrir.ABRIR);
 verify(jFileChooser).setFileFilter(isA(FileFilter.class));
 verify(jFileChooser).setCurrentDirectory(argThat(new IsDirectoryPredicate()));
 verify(jFileChooser, times(1)).showOpenDialog(eq(null));
 }

 @Test
 public void testJFileChooserOpenDialogReturnsNullFile() {
 JFileChooser jFileChooser = spy(new JFileChooser());
 abrir = new Abrir(form, jFileChooser, Abrir.ABRIR);
 verify(jFileChooser, times(1)).showOpenDialog(eq(null));
 verify(jFileChooser).setFileFilter(isA(FileFilter.class));
 verify(jFileChooser).setCurrentDirectory(argThat(new IsDirectoryPredicate()));
 assertEquals("", form.getEntidades());
 }

 @Test
 public void testJFileChooserOpenDialogReturnsInvalidFile() {
 File invalidFile = new File("test.txt");
 JFileChooser jFileChooser = spy(new JFileChooser());
 doReturn(invalidFile).when(jFileChooser).getSelectedFile();
 abrir = new Abrir(form, jFileChooser, Abrir.ABRIR);
 assertEquals("", form.getEntidades());
 }

 @Test
 public void testJFileChooserOpenDialogReturnsXMLFile() {
 File xmlFile = new File("test.jaw.xml");
 JFileChooser jFileChooser = spy(new JFileChooser());
 doReturn(xmlFile).when(jFileChooser).getSelectedFile();
 abrir = new Abrir(form, jFileChooser, Abrir.ABRIR_XML);
 String actual = form.getEntidades().toString();
 assertEquals("Test Entities", actual);
 }

 @Test
 public void testJFileChooserOpenDialogReturnsSerializadoFile() {
 File serializadoFile = new File("test.jaw");
 JFileChooser jFileChooser = spy(new JFileChooser());
 doReturn(serializadoFile).when(jFileChooser).getSelectedFile();
 abrir = new Abrir(form, jFileChooser, Abrir.ABRIR);
 String actual = form.getEntidades().toString();
 assertEquals("Test Entities", actual);
 }

 @Test
 public void testConstructionWithValidXMLFile() {
 String expectedXml = "Expected XML content";
 String expectedFileName = "test.jaw.xml";
 form = new ProcessarEntidades();
 abrir = new Abrir(form, expectedXml, Abrir.ABRIR_XML);
 assertEquals(expectedXml, abrir.xml);
 assertEquals(expectedFileName, form.getNomeArquivoSalvo());
 }

 @Test
 public void testConstructionWithValidSerializadoFile() {
 String expectedFileName = "test.jaw";
 form = new ProcessarEntidades();
 abrir = new Abrir(form, expectedFileName, Abrir.ABRIR);
 assertEquals("", abrir.xml);
 assertEquals(expectedFileName, form.getNomeArquivoSalvo());
 }

 @Test
 public void testConstructionWithNullFileName() {
 form = new ProcessarEntidades();
 abrir = new Abrir(form, null, Abrir.ABRIR);
 assertEquals("", abrir.xml);
 assertEquals("", form.getNomeArquivoSalvo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructionWithInvalidXMLFile() {
 String invalidXml = "Invalid XML content";
 form = new ProcessarEntidades();
 abrir = new Abrir(form, invalidXml, Abrir.ABRIR_XML);
 fail("IllegalArgumentException should be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructionWithInvalidSerializadoFile() {
 String invalidFileName = "Invalid.jaw";
 form = new ProcessarEntidades();
 abrir = new Abrir(form, invalidFileName, Abrir.ABRIR);
 fail("IllegalArgumentException should be thrown");
 }

 private static class IsDirectoryPredicate implements ArgumentMatcher<File> {
 @Override
 public boolean matches(File file) {
 return file.isDirectory();
 }
 }
}