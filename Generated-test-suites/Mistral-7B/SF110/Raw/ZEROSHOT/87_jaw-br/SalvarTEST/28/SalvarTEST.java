package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.entidade.Entidade;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private File arquivoSelecionado;

 @Before
 public void setup() {
 form = mock(ProcessarEntidades.class);
 salvar = new Salvar(form, 20);
 }

 @Test
 public void testSalvarArquivoComValidosParametros() {
 // Given
 final List<Entidade> entidades = new ArrayList<>();
 entidades.add(Mockito.mock(Entidade.class));
 salvar.entidade = entidades;

 final File arquivo = Mockito.mock(File.class);
 when(arquivo.getAbsolutePath()).thenReturn("C:/teste/arquivo.jaw");
 when(form.getNomeArquivoSalvo()).thenReturn("C:/teste/arquivo.jaw");
 when(form.getName()).thenReturn("");

 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(escolhedor.getSelectedFile()).thenReturn(arquivo);

 // When
 salvar.salvar();

 // Then
 verify(form, times(1)).getEntidades();
 verify(escolhedor, times(1)).showSaveDialog(form);
 verify(salvar, times(1)).arquivo =="C:/teste/arquivo.jaw";
 Serializacao serializacao = new Serializacao();
 try {
 serializacao.salvar(entidades, "C:/teste/arquivo.jaw");
 } catch (IOException e) {
 fail("Não deve haver exceção");
 }
 JOptionPane.showMessageDialog(form, "Arquivo salvo com sucesso!", "Jaw - Salvar", 1);
 }

 @Test
 public void testSalvarArquivoSemNomeArquivo() {
 // Given
 final List<Entidade> entidades = new ArrayList<>();
 entidades.add(Mockito.mock(Entidade.class));
 salvar.entidade = entidades;

 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(form.getNomeArquivoSalvo()).thenReturn("");
 when(form.getName()).thenReturn("");
 when(escolhedor.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(escolhedor.getSelectedFile()).thenReturn(null);

 // When
 salvar.salvar();

 // Then
 verify(form, times(1)).getEntidades();
 verify(escolhedor, times(1)).showSaveDialog(form);
 verify(salvar, never()).arquivo =="";
 }

 @Test(expected = IOException.class)
 public void testSalvarArquivoComErroSerializacao() {
 // Given
 final List<Entidade> entidades = new ArrayList<>();
 entidades.add(Mockito.mock(Entidade.class));
 salvar.entidade = entidades;

 final File arquivo = Mockito.mock(File.class);
 when(arquivo.getAbsolutePath()).thenReturn("C:/teste/arquivo.jaw");
 when(form.getNomeArquivoSalvo()).thenReturn("C:/teste/arquivo.jaw");
 when(form.getName()).thenReturn("");

 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(escolhedor.getSelectedFile()).thenReturn(arquivo);

 Serializacao serializacao = Mockito.mock(Serializacao.class);
 when(serializacao.salvar(entidades, "C:/teste/arquivo.jaw")).thenThrow(new IOException());

 // When
 salvar.salvar();

 // Then
 JOptionPane.showMessageDialog(form, "Não foi possível salvar esse arquivo\n Verifique o formato das entidades !", "Salvando Arquivo", 0);
 verify(form, times(1)).getEntidades();
 verify(escolhedor, times(1)).showSaveDialog(form);
 verify(salvar, times(1)).arquivo =="C:/teste/arquivo.jaw";
 verify(serializacao, times(1)).salvar(entidades, "C:/teste/arquivo.jaw");
 }

 @Test
 public void testExportarArquivoXml() {
 // Given
 final List<Entidade> entidades = new ArrayList<>();
 entidades.add(Mockito.mock(Entidade.class));
 salvar.entidade = entidades;
 String xml = "<entidades>";
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 when(serializacaoXML.entidades2XML(entidades)).thenReturn(xml);

 // When
 salvar.xml = serializacaoXML.entidades2XML(entidades);

 // Then
 assertEquals("<?xml version='1.0' encoding='UTF-8'?><entidades>" + xml, salvar.xml);
 }

 @Test
 public void testExportarArquivoXmlSemArquivoSelecionado() {
 // Given
 final List<Entidade> entidades = new ArrayList<>();
 entidades.add(Mockito.mock(Entidade.class));
 salvar.entidade = entidades;

 // When
 salvar.xml = "";

 // Then
 assertNull(salvar.xml);
 }

 @Test(expected = IOException.class)
 public void testExportarArquivoXmlComErroSerializacao() {
 // Given
 final List<Entidade> entidades = new ArrayList<>();
 entidades.add(Mockito.mock(Entidade.class));
 salvar.entidade = entidades;
 String xml = "<entidades>";
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 when(serializacaoXML.entidades2XML(entidades)).thenThrow(new IOException());

 // When
 salvar.xml = serializacaoXML.entidades2XML(entidades);

 // Then
 fail("Não deve haver exceção");
 }

 @Test
 public void testExportarArquivoXmlComSucesso() {
 // Given
 final List<Entidade> entidades = new ArrayList<>();
 entidades.add(Mockito.mock(Entidade.class));
 salvar.entidade = entidades;
 String xml = "<entidades>";
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 when(serializacaoXML.entidades2XML(entidades)).thenReturn(xml);
 FileWriter escrevedor = mock(FileWriter.class);

 // When
 try {
 salvar.xml = serializacaoXML.entidades2XML(entidades);
 FileWriter fileWriter = new FileWriter("C:/teste/arquivo.xml");
 fileWriter.write(salvar.xml);
 fileWriter.close();
 } catch (IOException e) {
 fail("Não deve haver exceção");
 }

 // Then
 assertEquals("<?xml version='1.0' encoding='UTF-8'?><entidades>" + xml, salvar.xml);
 JOptionPane.showMessageDialog(form, "Arquivo Exportado com sucesso!", "Jaw - Salvar", 1);
 }

}