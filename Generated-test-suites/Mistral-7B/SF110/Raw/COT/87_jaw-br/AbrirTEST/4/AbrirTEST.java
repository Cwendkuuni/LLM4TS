package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import java.io.File;
import java.io.FileFilter;
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
 String xmlFilePath = "testData/valid.jaw.xml";
 String expectedName = "valid.jaw.xml";
 abrir = new Abrir(form, xmlFilePath);
 assertEquals(expectedName, form.getNomeArquivoSalvo());
 // Check the size and content of the Entidades list here
 }

 @Test
 public void testAbrirSerializadoWithValidJawFile() {
 String jawFilePath = "testData/valid.jaw";
 String expectedName = "valid.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File chooserFile = new File("testData");
 chooserFile.setAbsolutePath(Main.configuracao.getCaminhoDosProjetos()); // Mimic the behavior of setting the current directory in the constructor
 chooserFile.setPath(expectedName); // Set the selected file path
 when(form.getFile()).thenReturn(chooserFile);
 abrir.abrirSerializado(null);
 assertEquals(expectedName, form.getNomeArquivoSalvo());
 // Check the size and content of the Entidades list here
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithInvalidXmlFile() {
 String xmlFilePath = "testData/invalid.xml";
 abrir = new Abrir(form, xmlFilePath);
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoWithInvalidJawFile() {
 String jawFilePath = "testData/invalid.jaw";
 abrir = new Abrir(form, Abrir.ABRIR);
 File chooserFile = new File("testData");
 chooserFile.setAbsolutePath(Main.configuracao.getCaminhoDosProjetos());
 chooserFile.setPath(jawFilePath);
 when(form.getFile()).thenReturn(chooserFile);
 abrir.abrirSerializado(null);
 }

 @Test
 public void testOpenDialogWithXmlFilter() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 FileDialog dialog = (FileDialog) spy(new JFileChooser()).doCallRealMethod("showOpenDialog", (JFrame) anyObject(), anyBoolean());
 verify(dialog).setFileFilter(arg0 -> arg0 instanceof Filtro2);
 }

 @Test
 public void testOpenDialogWithJawFilter() {
 abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog dialog = (FileDialog) spy(new JFileChooser()).doCallRealMethod("showOpenDialog", (JFrame) anyObject(), anyBoolean());
 verify(dialog).setFileFilter(arg0 -> arg0 instanceof Filtro);
 }

 @Test
 public void testDoNotOpenDialogWhenFileIsNotSelected() {
 abrir = new Abrir(form, Abrir.ABRIR);
 FileDialog dialog = (FileDialog) spy(new JFileChooser()).doCallRealMethod("showOpenDialog", (JFrame) anyObject(), anyBoolean());
 verifyNoInteractions(dialog);
 }
}