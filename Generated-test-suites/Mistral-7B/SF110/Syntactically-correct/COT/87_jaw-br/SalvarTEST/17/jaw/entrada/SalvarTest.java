package jaw.entrada.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import jaw.entrada.Entidade;
import jaw.entrada.Form;
import jaw.entrada.Salvar;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

@RunWith(MockitoJUnitRunner.class)
public class SalvarTest {

 private Salvar salvar;
 private Form form;
 private Serializacao serializacao;
 private SerializacaoXML serializacaoXML;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 form = Mockito.mock(Form.class);
 serializacao = Mockito.mock(Serializacao.class);
 serializacaoXML = Mockito.mock(SerializacaoXML.class);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarWithValidParametersAndXmlFileFilter() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 Mockito.when(form.getEntidades()).thenReturn(entidades);

 File file = new File("path/to/test/output.xml");
 JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 Mockito.when(fileChooser.getSelectedFile()).thenReturn(file);
 Mockito.when(form.getName()).thenReturn("");

 // When
 salvar.Salvar(fileChooser, serializacaoXML, serializacao);

 // Then
 Mockito.verify(form, Mockito.times(1)).setNomeArquivoSalvo("path/to/test/output.jaw.xml");
 Mockito.verify(serializacaoXML, Mockito.times(1)).entidades2XML(entidades);
 FileWriter fileWriter = Mockito.verify(fileWriter).write(Mockito.anyString());
 fileWriter.close();
 JOptionPane.showMessageDialog(form, "Arquivo Exportado com sucesso!", "Jaw - Salvar", 1);
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullFileChooser() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 Mockito.when(form.getEntidades()).thenReturn(entidades);

 // When
 salvar.Salvar(null, serializacaoXML, serializacao);

 // Then
 // An exception should be thrown
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidFileChooserSelection() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 Mockito.when(form.getEntidades()).thenReturn(entidades);

 JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);
 Mockito.when(form.getName()).thenReturn("");

 // When
 salvar.Salvar(fileChooser, serializacaoXML, serializacao);

 // Then
 // An exception should be thrown
 }

 @Test(expected = IOException.class)
 public void testSalvarWithIOExceptionDuringFileWriter() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 Mockito.when(form.getEntidades()).thenReturn(entidades);

 File file = new File("path/to/test/output.xml");
 JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 Mockito.when(fileChooser.getSelectedFile()).thenReturn(file);
 Mockito.when(form.getName()).thenReturn("");
 FileWriter fileWriter = Mockito.mock(FileWriter.class);
 Mockito.when(file.createWriter(Mockito.anyBoolean())).thenReturn(fileWriter);
 Mockito.when(serializacaoXML.entidades2XML(entidades)).thenReturn("Invalid XML data");

 // When
 salvar.Salvar(fileChooser, serializacaoXML, serializacao);

 // Then
 JOptionPane.showMessageDialog(form, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades!", "Salvando Arquivo", 0);
 Mockito.verify(fileWriter, Mockito.never()).write(Mockito.anyString());
 }

 @Test
 public void testSalvarWithValidParametersAndJawFileFilter() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 Mockito.when(form.getEntidades()).thenReturn(entidades);

 File file = new File("path/to/test/output.jaw");
 JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 Mockito.when(fileChooser.getSelectedFile()).thenReturn(file);
 Mockito.when(form.getName()).thenReturn("TestJawFile");

 // When
 salvar.Salvar(fileChooser, serializacao, serializacao);

 // Then
 Mockito.verify(form, Mockito.times(1)).setNomeArquivoSalvo("path/to/test/output.jaw");
 Mockito.verify(serializacao, Mockito.times(1)).salvar(entidades, "path/to/test/output.jaw");
 JOptionPane.showMessageDialog(form, "Arquivo salvo com sucesso!", "Jaw - Salvar", 1);
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullName() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 Mockito.when(form.getEntidades()).thenReturn(entidades);

 File file = new File("path/to/test/output.jaw");
 JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 Mockito.when(fileChooser.getSelectedFile()).thenReturn(file);

 // When
 salvar.Salvar(fileChooser, serializacao, serializacao);

 // Then
 // An exception should be thrown
 }

 @Test
 public void testSalvarWithValidParametersAndNoFileSelected() {
 // Given
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade();
 Entidade entidade2 = new Entidade();
 entidades.add(entidade1);
 entidades.add(entidade2);
 Mockito.when(form.getEntidades()).thenReturn(entidades);
 JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 Mockito.when(form.getName()).thenReturn("");

 // When
 salvar.Salvar(fileChooser, serializacao, serializacao);

 // Then
 // Nothing should be done
 }
}