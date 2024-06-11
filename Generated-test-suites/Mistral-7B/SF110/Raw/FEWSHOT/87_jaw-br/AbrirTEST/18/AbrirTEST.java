package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbrirTest {

 private Abrir objectUnderTest;
 @Mock
 private ProcessarEntidades form;
 @Mock
 private FileDialog fileDialog;
 @Mock
 private JFileChooser fileChooser;
 @Mock
 private FileFilter filtro;
 @Mock
 private FileFilter filtro2;
 @Mock
 private Serializacao serializacao;
 @Mock
 private SerializacaoXML serializacaoXML;
 @Mock
 private JOptionPane optionPane;
 @Mock
 private Main janelaPrincipal;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 objectUnderTest = new Abrir(form, null);
 when(form.getNomeArquivoSalvo()).thenReturn("");
 when(serializacao.abrir(anyString())).thenReturn(new Entidades());
 when(serializacaoXML.xml2Entidades(any(Reader.class))).thenReturn(new Entidades());
 when(optionPane.showMessageDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.PLAIN_MESSAGE);
 }

 @Test
 public void testAbrirXML() {
 final String arquivoXml = "teste.jaw.xml";
 objectUnderTest = new Abrir(form, arquivoXml);
 verify(fileChooser, never()).showOpenDialog(any(Component.class));
 verify(fileDialog, never()).setVisible(true);
 verify(optionPane, never()).showMessageDialog(any(Component.class), anyString(), anyString(), 0);
 assertNotNull(objectUnderTest.getXml());
 assertEquals(arquivoXml, objectUnderTest.getXml());
 }

 @Test
 public void testAbrirSerializado() {
 final String arquivoSerializado = "teste.jaw";
 objectUnderTest = new Abrir(form, arquivoSerializado);
 verify(fileChooser, never()).showOpenDialog(any(Component.class));
 verify(fileDialog, never()).setVisible(true);
 verify(optionPane, times(1)).showMessageDialog(any(Component.class), anyString(), "Abrir Arquivo jaw", 0);
 assertNull(objectUnderTest.getXml());
 }

 @Test
 public void testAbrirFileChooserXML() {
 final String arquivoXml = "teste.jaw.xml";
 objectUnderTest = new Abrir(form, 10);
 verify(fileChooser, times(1)).setFileFilter(filtro2);
 verify(fileChooser, times(1)).showOpenDialog(any(Component.class));
 verify(fileDialog, never()).setVisible(true);
 verify(optionPane, never()).showMessageDialog(any(Component.class), anyString(), anyString(), 0);
 }

 @Test
 public void testAbrirFileChooserSerializado() {
 final String arquivoSerializado = "teste.jaw";
 objectUnderTest = new Abrir(form, 20);
 verify(fileChooser, times(1)).setFileFilter(filtro);
 verify(fileChooser, times(1)).showOpenDialog(any(Component.class));
 verify(fileDialog, never()).setVisible(true);
 verify(optionPane, times(1)).showMessageDialog(any(Component.class), anyString(), "Abrir Arquivo jaw", 0);
 }

 @Test
 public void testAbrirFileChooserXMLNoFileSelected() {
 final String arquivoXml = "teste.jaw.xml";
 objectUnderTest = new Abrir(form, 10);
 verify(fileChooser, times(1)).setFileFilter(filtro2);
 verify(fileChooser, times(1)).showOpenDialog(any(Component.class));
 verify(fileDialog, never()).setVisible(true);
 verify(optionPane, never()).showMessageDialog(any(Component.class), anyString(), anyString(), 0);
 }

 @Test
 public void testAbrirFileChooserSerializadoNoFileSelected() {
 final String arquivoSerializado = "teste.jaw";
 objectUnderTest = new Abrir(form, 20);
 verify(fileChooser, times(1)).setFileFilter(filtro);
 verify(fileChooser, times(1)).showOpenDialog(any(Component.class));
 verify(fileDialog, never()).setVisible(true);
 verify(optionPane, never()).showMessageDialog(any(Component.class), anyString(), anyString(), 0);
 }
}