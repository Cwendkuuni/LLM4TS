package jaw.entrada;

import static org.junit.Assert.*;
import jaw.metamodelo.*;
import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SalvarTest {

 private Entidade entidadeMock;
 private ProcessarEntidades formMock;
 private Salvar salvar;

 @Before
 public void setup() {
 entidadeMock = mock(Entidade.class);
 formMock = mock(ProcessarEntidades.class);
 salvar = new Salvar(formMock, Salvar.SALVAR);
 }

 @Test
 public void testSalvarWithValidData() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));

 // When
 SalvarResult result = salvar.salvar();

 // Then
 assertTrue(result.isSuccess());
 verify(formMock, times(1)).setNomeArquivoSalvo("");
 verify(formMock, times(1)).showSaveDialog(any(JFrame.class));
 File selectedFile = any(File.class);
 verify(formMock, times(1)).setNomeArquivoSalvo(selectedFile.getAbsolutePath());
 assertTrue(salvar.getVelvetState());
 }

 @Test(expected = Exception.class)
 public void testSalvarWithoutSelectedFile() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));
 when(formMock.getName()).thenReturn("");
 when(formMock.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 doReturn(null).when(formMock).getNomeArquivoSalvo();

 // When and Then
 salvar.salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullFile() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));
 when(formMock.getName()).thenReturn("");
 when(formMock.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(formMock.getNomeArquivoSalvo()).thenReturn(null);

 // When and Then
 salvar.salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidFormatFile() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));
 when(formMock.getName()).thenReturn("");
 when(formMock.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(formMock.getNomeArquivoSalvo()).thenReturn("teste.txt");

 // When and Then
 salvar.salvar();
 }

 @Test
 public void testSalvarXMLWithValidData() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));
 when(formMock.getName()).thenReturn("");
 when(formMock.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 doReturn(new File("teste.xml")).when(formMock).getNomeArquivoSalvo();

 // When
 SalvarXMLResult result = salvar.salvarXML();

 // Then
 assertTrue(result.isSuccess());
 verify(formMock, times(1)).setNomeArquivoSalvo("");
 verify(formMock, times(1)).showSaveDialog(any(JFrame.class));
 File selectedFile = any(File.class);
 verify(formMock, times(1)).setNomeArquivoSalvo(selectedFile.getAbsolutePath() + ".jaw.xml");
 verify(salvar, times(1)).xml = anyString();
 try {
 FileWriter writer = new FileWriter(selectedFile);
 writer.write(salvar.getXml());
 writer.close();
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithoutSelectedFile() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));
 when(formMock.getName()).thenReturn("");
 when(formMock.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 doReturn(null).when(formMock).getNomeArquivoSalvo();

 // When and Then
 salvar.salvarXML();
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithNullFile() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));
 when(formMock.getName()).thenReturn("");
 when(formMock.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(formMock.getNomeArquivoSalvo()).thenReturn(null);

// When and Then
salvar.salvarXML();

}

 @Test(expected = Exception.class)
 public void testSalvarXMLWithInvalidFormatFile() {
 // Given
 when(formMock.getEntidades()).thenReturn(Collections.singletonList(entidadeMock));
 when(formMock.getName()).thenReturn("");
 when(formMock.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(formMock.getNomeArquivoSalvo()).thenReturn("teste.txt");

 // When and Then
 salvar.salvarXML();
 }

}