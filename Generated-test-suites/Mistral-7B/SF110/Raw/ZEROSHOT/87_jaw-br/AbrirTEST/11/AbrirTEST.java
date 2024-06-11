package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 form = mock(ProcessarEntidades.class);
 abrir = new Abrir(form, "test.xml");
 }

 @Test
 public void testAbrirXML() {
 String expectedXml = "expectedXml";
 when(form.setEntidades(anyList())).thenReturn(expectedXml);
 File file = new File("test.xml");
 when(form.setNomeArquivoSalvo(anyString())).thenReturn("test.xml");

 abrir = new Abrir(form, "test.xml");

 assertEquals(expectedXml, abrir.xml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithWrongFileType() {
 abrir = new Abrir(form, new File("test.txt"));
 }

 @Test
 public void testAbrirSerializado() {
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 JFileChooser chooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(chooser);
 File file = new File("test.xml");
 when(chooser.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(file);

 abrir.abrirSerializado(file);

 verify(form, times(1)).setNomeArquivoSalvo("test.xml");
 verify(form, times(1)).setEntidades(anyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithWrongFileType() {
 abrir = new Abrir(form, Abrir.ABRIR);
 JFileChooser chooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(chooser);
 File file = new File("test.jaw");
 when(chooser.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(file);

 abrir.abrirSerializado(file);
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithFileNotFound() throws FileNotFoundException {
 when(form.setEntidades(anyList())).thenReturn("expectedEntidades");
 when(form.setNomeArquivoSalvo(anyString())).thenReturn("test.xml");

 FileReader reader = mock(FileReader.class);
 when(reader.readAllLines()).thenThrow(new FileNotFoundException());

 abrir = new Abrir(form, "test.xml");
 abrir.abrirXML("test.xml");

 verify(form, times(1)).setEntidades(anyList());
 verifyZeroInteractions(reader);
 }

 @Test
 public void testAbrirXMLWithFileContent() throws IOException {
 String expectedXml = "expectedXml";
 when(form.setEntidades(anyList())).thenReturn(expectedXml);
 when(form.setNomeArquivoSalvo(anyString())).thenReturn("test.xml");

 FileReader reader = mock(FileReader.class);
 when(reader.readLine()).thenReturn("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>...</entidades>");

 abrir = new Abrir(form, "test.xml");
 abrir.abrirXML("test.xml");

 assertEquals(expectedXml, abrir.xml);
 }
}