package jaw.entrada;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AbrirTest {

 private Abrir abrir;
 private ProcessarEntidades form;

 @Before
 public void setup() {
 form = mock(ProcessarEntidades.class);
 abrir = new Abrir(form, "");
 }

 @Test
 public void testAbrirXML() {
 // Arrange
 String arquivoXml = "teste.jaw.xml";
 when(form.setNomeArquivoSalvo(String.class)).thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 ((String) invocation.getArgumentAt(0, 0)).equals(arquivoXml);
 return null;
 }
 });
 when(form.setEntidades((Entidades) anyObject())).thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 // TODO: Implement this mocking logic
 return null;
 }
 });

 // Act
 abrir.abrirXML(arquivoXml);

 // Assert
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLInvalidFileType() {
 // Arrange
 String arquivoInvalido = "teste.txt";

 // Act & Assert
 abrir.abrirXML(arquivoInvalido);
 }

 @Test
 public void testAbrirSerializado() {
 // Arrange
 String arquivoJaw = "teste.jaw";
 when(form.setNomeArquivoSalvo(String.class)).thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 ((String) invocation.getArgumentAt(0, 0)).equals(arquivoJaw);
 return null;
 }
 });
 when(form.setEntidades((Entidades) anyObject())).thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 // TODO: Implement this mocking logic
 return null;
 }
 });

 // Act
 abrir.abrirSerializado(arquivoJaw);

 // Assert
 assertNotNull(abrir.getXml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoInvalidFileType() {
 // Arrange
 String arquivoInvalido = "teste.txt";

 // Act & Assert
 abrir.abrirSerializado(arquivoInvalido);
 }

 @Test
 public void testAbrirFileChooserXMLFilter() {
 // Arrange
 JFileChooser fileChooser = mock(JFileChooser.class);
 FileFilter xmlFilter = new Filtro2();
 when(fileChooser.showOpenDialog(anyObject())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getFileFilter()).thenReturn(xmlFilter);

 // Act
 abrir = new Abrir(form, Abrir.ABRIR_XML);
 File arquivo = new File("teste.jaw.xml");
 when(fileChooser.showOpenDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(arquivo);

 // Act
 abrir.abrir();

 // Assert
 assertNotNull(abrir.getXml());
 }

 @Test
 public void testAbrirFileChooserSerializadoFilter() {
 // Arrange
 JFileChooser fileChooser = mock(JFileChooser.class);
 FileFilter serializadoFilter = new Filtro();
 when(fileChooser.showOpenDialog(anyObject())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getFileFilter()).thenReturn(serializadoFilter);

 // Act
 abrir = new Abrir(form, Abrir.ABRIR);
 File arquivo = new File("teste.jaw");
 when(fileChooser.showOpenDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(arquivo);

 // Act
 abrir.abrir();

 // Assert
 assertNotNull(abrir.getXml());
 }

}