package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import jaw.entrada.FormProcessarEntidades;
import jaw.entrada.Salvar;
import jaw.gui.Main;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private FormProcessarEntidades form;
 private Salvar salvar;
 private Main main;

 @Before
 public void setUp() {
 form = Mockito.mock(FormProcessarEntidades.class);
 main = Mockito.mock(Main.class);
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @Test
 public void testSaveFileWithJawExtension() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 when(form.getEntidades()).thenReturn(entidades);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(fileChooser);
 doReturn(new File("test.jaw")).when(fileChooser).getSelectedFile();
 doReturn(true).when(form).isSaveOptionSelected();

 // When
 salvar.salvar(form, Salvar.SALVAR);

 // Then
 ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
 verify(new Serializacao()).salvar(eq(entidades), fileCaptor.capture());
 assertEquals("test.jaw", fileCaptor.getValue().getAbsolutePath());
 }

 @Test
 public void testSaveFileWithXmlExtensionWhenSaveAsOptionIsSelected() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 when(form.getEntidades()).thenReturn(entidades);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(fileChooser);
 doReturn(null).when(form).isSaveOptionSelected();
 doReturn(new File("test.jaw.xml")).when(fileChooser).getSelectedFile();

 // When
 salvar.salvar(form, Salvar.SALVAR_XML);

 // Then
 ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
 verify(new SerializacaoXML()).entidades2XML(eq(entidades));
 try {
 FileWriter writer = mock(FileWriter.class);
 verify(writer).write(anyString());
 verifyNoMoreInteractions(writer);
 } catch (IOException e) {
 // Ignore
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveFileWithNullSelectedFile() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 when(form.getEntidades()).thenReturn(entidades);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(fileChooser);
 doReturn(null).when(fileChooser).getSelectedFile();

 // When
 salvar.salvar(form, Salvar.SALVAR);

 // Then
 verify(form, never()).setNomeArquivoSalvo(anyString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveFileWithEmptyName() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 when(form.getEntidades()).thenReturn(entidades);
 JFileChooser fileChooser = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(fileChooser);
 doReturn(new File("")).when(fileChooser).getSelectedFile();
 doReturn(true).when(form).isSaveOptionSelected();

 // When
 salvar.salvar(form, Salvar.SALVAR);

 // Then
 verify(form, never()).setNomeArquivoSalvo(anyString());
 }
}