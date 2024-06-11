package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

 @Mock
 private ProcessarEntidades form;
 @InjectMocks
 private Abrir systemUnderTest;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void shouldAbrirXmlFile() throws IOException {
 final String xmlFilePath = "test.jaw.xml";
 final File xmlFile = new File(xmlFilePath);

 when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
 when(form.setEntidades(anyList())).thenReturn(true);

 systemUnderTest.new Abrir(form, xmlFilePath);

 assertEquals("test.jaw.xml", systemUnderTest.getXml());
 verify(form, times(1)).setEntidades(anyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void shouldThrowExceptionWhenXmlFileDoesNotExist() throws IOException {
 final String xmlFilePath = "nonexistent.jaw.xml";

 systemUnderTest.new Abrir(form, xmlFilePath);
 }

 @Test(expected = IllegalArgumentException.class)
 public void shouldThrowExceptionWhenXmlFileIsNotJawXml() throws IOException {
 final String xmlFilePath = "test.txt";
 final File xmlFile = new File(xmlFilePath);

 systemUnderTest.new Abrir(form, xmlFilePath);
 }

 @Test
 public void shouldAbrirSerializadoFile() throws IOException {
 final String serFile = "test.jaw";
 final File serFileObject = new File(serFile);

 when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
 when(form.setEntidades(anyList())).thenReturn(true);

 systemUnderTest.new Abrir(form, serFile, 20);

 assertEquals(serFile, systemUnderTest.getForm().getNomeArquivoSalvo());
 verify(form, times(1)).setEntidades(anyList());
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionWhenFileIsNotJawFile() throws IOException {
 final String serFile = "test.txt";
 final File serFileObject = new File(serFile);

 systemUnderTest.new Abrir(form, serFile, 20);
 }

 @Test
 public void shouldOpenJFileChooserWhenCalledWithFileType() {
 final int fileType = 10;
 final JFileChooser expectedJFileChooser = mock(JFileChooser.class);

 when(form.getJFileChooser()).thenReturn(expectedJFileChooser);
 when(expectedJFileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);

 systemUnderTest.new Abrir(form, fileType);

 verify(expectedJFileChooser, times(1)).showOpenDialog(anyInt());
 }

 @Test
 public void shouldOpenJFileChooserWhenCalledWithFileFilter() {
 final int fileType = 20;
 final FileFilter expectedFileFilter = mock(FileFilter.class);
 final JFileChooser expectedJFileChooser = mock(JFileChooser.class);

 when(form.getJFileChooser()).thenReturn(expectedJFileChooser);
 when(expectedJFileChooser.getFileFilter()).thenReturn(expectedFileFilter);
 when(expectedFileFilter.accept(any(File.class))).thenReturn(true);

 systemUnderTest.new Abrir(form, fileType);

 verify(expectedJFileChooser, times(1)).setFileFilter(expectedFileFilter);
 verify(expectedJFileChooser, times(1)).showOpenDialog(anyInt());
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionWhenJFileChooserIsNotSetup() {
 systemUnderTest.new Abrir(form, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void shouldThrowExceptionWhenFileChooserReturnsNull() throws IOException {
 final int fileType = 10;
 when(form.getJFileChooser()).thenReturn(null);
 systemUnderTest.new Abrir(form, fileType);
 }
}