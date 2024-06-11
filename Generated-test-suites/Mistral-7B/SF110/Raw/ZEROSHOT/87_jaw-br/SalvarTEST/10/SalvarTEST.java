package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades processarEntidadesMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 processarEntidadesMock = mock(ProcessarEntidades.class);
 salvar = new Salvar(processarEntidadesMock, 20);
 }

 @Test
 public void testSalvarComArquivoValido() {
 // Arrange
 final File arquivo = new File("teste.jaw");
 JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.showSaveDialog(processarEntidadesMock)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(escolhedor.getSelectedFile()).thenReturn(arquivo);

 // Act
 salvar.salvar(escolhedor);

 // Assert
 verify(processarEntidadesMock, times(1)).getEntidades();
 verify(salvar, times(1)).arquivo = arquivo.getAbsolutePath();
 Serializacao serializacao = new Serializacao();
 try {
 serializacao.salvar(salvar.entidade, arquivo.getAbsolutePath());
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 JOptionPane.showMessageDialog(processarEntidadesMock, "Arquivo salvo com sucesso!", "Jaw - Salvar", 1);
 }

 @Test
 public void testSalvarSemArquivoSelecionado() {
 // Arrange
 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.showSaveDialog(processarEntidadesMock)).thenReturn(JFileChooser.CANCEL_OPTION);

 // Act
 salvar.salvar(escolhedor);

 // Assert
 verify(processarEntidadesMock, times(0)).getEntidades();
 verify(salvar, times(0)).arquivo = anyString();
 verify(processarEntidadesMock, times(0)).setNomeArquivoSalvo("");
 }

 @Test
 public void testSalvarComArquivoInvalido() {
 // Arrange
 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.showSaveDialog(processarEntidadesMock)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(escolhedor.getSelectedFile()).thenReturn(new File("teste.txt"));

 // Act
 salvar.salvar(escolhedor);

 // Assert
 verify(processarEntidadesMock, times(1)).getEntidades();
 verify(salvar, times(0)).arquivo = anyString();
 verify(processarEntidadesMock, times(1)).setNomeArquivoSalvo("teste.txt.jaw");
 }

 @Test
 public void testSalvarXmlComArquivoValido() {
 // Arrange
 final File arquivo = new File("teste.jaw.xml");
 JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.showSaveDialog(processarEntidadesMock)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(escolhedor.getSelectedFile()).thenReturn(arquivo);

 // Act
 salvar.salvarXml(escolhedor);

 // Assert
 verify(processarEntidadesMock, times(1)).getEntidades();
 verify(salvar, times(1)).arquivo = arquivo.getAbsolutePath();
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade = new Entidade();
 entidades.add(entidade);
 SerializacaoXML serializacaoXML = new SerializacaoXML();
 String xml = serializacaoXML.entidades2XML(entidades);
 try {
 FileWriter escritor = new FileWriter(arquivo.getAbsolutePath());
 escritor.write(xml);
 escritor.flush();
 escritor.close();
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 JOptionPane.showMessageDialog(processarEntidadesMock, "Arquivo Exportado com sucesso!", "Jaw - Salvar", 1);
 }

 @Test
 public void testSalvarXmlSemArquivoSelecionado() {
 // Arrange
 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.showSaveDialog(processarEntidadesMock)).thenReturn(JFileChooser.CANCEL_OPTION);

 // Act
 salvar.salvarXml(escolhedor);

 // Assert
 verify(processarEntidadesMock, times(0)).getEntidades();
 verify(salvar, times(0)).arquivo = anyString();
 verify(processarEntidadesMock, times(0)).setNomeArquivoSalvo("");
 }
}