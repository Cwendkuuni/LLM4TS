package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import jaw.entidade.Entidade;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

 private Abrir testee;
 private ProcessarEntidades mockForm;
 private Serializacao serializacao;
 private SerializacaoXML serializacaoXML;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockForm = mock(ProcessarEntidades.class);
 serializacao = mock(Serializacao.class);
 serializacaoXML = mock(SerializacaoXML.class);
 testee = new Abrir(mockForm, "teste.jaw.xml");
 }

 @Test
 public void testAbrirXML() {
 String expected = "teste";
 when(serializacaoXML.xml2Entidades(any(Reader.class))).thenReturn(new Entidade[]{new Entidade(expected)});

 testee.abrirXML("teste.jaw.xml");
 assertEquals(expected, testee.form.getEntidades().get(0).getNome());
 }

 @Test(expected = IOException.class)
 public void testAbrirXMLThrowsIOException() {
 when(serializacaoXML.xml2Entidades(any(Reader.class))).thenThrow(new IOException());
 testee.abrirXML("teste.jaw.xml");
 }

 @Test
 public void testAbrirSerializado() {
 testee = new Abrir(mockForm, "teste.jaw");
 verify(serializacao, times(1)).abrir("teste.jaw");
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoThrowsIOException() {
 when(serializacao.abrir("teste.jaw")).thenThrow(new IOException());
 testee.abrirSerializado("teste.jaw");
 }

 @Test
 public void testOpenFileChooserXML() {
 File expected = new File("teste.jaw.xml");
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(expected);

 testee.abrirXML(fileChooser);
 assertEquals(expected.getAbsolutePath(), testee.xml);
 }

 @Test
 public void testOpenFileChooserSerializado() {
 File expected = new File("teste.jaw");
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(expected);

 testee.abrir(fileChooser, 10);
 assertEquals(expected.getAbsolutePath(), testee.xml);
 }

 @Test
 public void testOpenFileChooserNull() {
 testee.abrir(null, 10);
 assertNull(testee.xml);
 }

 @Test
 public void testOpenFileChooserNoFileSelected() {
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.CANCEL_OPTION);

 testee.abrir(fileChooser, 10);
 assertNull(testee.xml);
 }

 @Test
 public void testOpenFileChooserWrongFileType() {
 File expected = new File("teste.txt");
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(expected);

 testee.abrir(fileChooser, 10);
 assertNull(testee.xml);
 }

 @Test
 public void testOpenFileChooserWrongFileTypeMessage() {
 File expected = new File("teste.txt");
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(expected);

 testee.abrir(fileChooser, 10);
 assertEquals("Arquivo xml corrompido ou incompatível com aplicação!", testee.form.getMensagem());
 }
}