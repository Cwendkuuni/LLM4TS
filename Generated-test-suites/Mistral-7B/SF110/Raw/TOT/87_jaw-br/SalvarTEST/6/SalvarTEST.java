package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.entrada.*;
import jaw.gui.*;
import jaw.metamodelo.*;
import jaw.serializacao.*;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidadeMock;

 @Before
 public void setUp() throws Exception {
 Formas forma1 = new Formas("Forma1", 1.0, 1.0, 1.0, "Forma1");
 Formas forma2 = new Formas("Forma2", 2.0, 2.0, 2.0, "Forma2");
 entidadeMock = new ArrayList<Entidade>(Arrays.asList(forma1, forma2));
 form = mock(ProcessarEntidades.class);
 when(form.getEntidades()).thenReturn(entidadeMock);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @After
 public void tearDown() throws Exception {
 salvar = null;
 form = null;
 entidadeMock = null;
 }

 @Test
 public void testSalvarXML_ValidFile_Success() throws IOException {
 // Given
 String expectedFilePath = "test/test.jaw.xml";
 String expectedXML = "<entidades>\n<formas>\n<forma id=\"1\">\n<nome>Forma1</nome>\n<x>1.0</x>\n<y>1.0</y>\n<z>1.0</z>\n</forma>\n<forma id=\"2\">\n<nome>Forma2</nome>\n<x>2.0</x>\n<y>2.0</y>\n<z>2.0</z>\n</forma>\n</entidades>";
 File testFile = new File(expectedFilePath);
 SerializacaoXML serializacaoXML = mock(SerializacaoXML.class);
 when(serializacaoXML.entidades2XML(entidadeMock)).thenReturn(expectedXML);
 when(salvar.arquivo).thenReturn(expectedFilePath);
 when(salvar.xml).thenReturn(expectedXML);
 when(salvar.entidade.size()).thenReturn(2);

 FileWriter fileWriterMock = mock(FileWriter.class);
 when(fileWriterMock.write(anyString())).thenReturn(expectedXML.length());
 when(fileWriterMock.flush()).thenReturn(true);
 when(fileWriterMock.close()).thenReturn(true);
 when(salvar.new FileWriter(salvar.arquivo)).thenReturn(fileWriterMock);

 // When
 salvar.salvar(entidadeMock, salvar.arquivo);

 // Then
 assertEquals(expectedXML, salvar.xml);
 assertTrue(testFile.exists());
 assertTrue(testFile.delete());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarXML_NullFile_Exception() {
 // Given
 String arquivo = null;
 salvar.arquivo = arquivo;

 // When & Then
 assertThrows(IllegalArgumentException.class, () -> salvar.salvar(entidadeMock, salvar.arquivo));
 }

 @Test(expected = IOException.class)
 public void testSalvarXML_InvalidFile_IOException() throws IOException {
 // Given
 String expectedFilePath = "test/test.jaw.xml";
 File testFile = new File(expectedFilePath);
 SerializacaoXML serializacaoXML = mock(SerializacaoXML.class);
 when(salvar.arquivo).thenReturn(expectedFilePath);
 when(salvar.entidade.size()).thenReturn(2);
 when(serializacaoXML.entidades2XML(entidadeMock)).thenThrow(new IOException());

 FileWriter fileWriterMock = mock(FileWriter.class);
 when(salvar.new FileWriter(salvar.arquivo)).thenReturn(fileWriterMock);

 // When & Then
 assertThrows(IOException.class, () -> salvar.salvar(entidadeMock, salvar.arquivo));
 assertTrue(testFile.deleteOnExit());
 }

 @Test
 public void testSalvar_ValidFile_Success() throws IOException {
 // Given
 String expectedFilePath = "test/test.jaw";
 salvar.arquivo = expectedFilePath;
 when(salvar.entidade.size()).thenReturn(2);
 Serializacao serializacao = mock(Serializacao.class);
 when(salvar.new Serializacao()).thenReturn(serializacao);
 when(serializacao.salvar(entidadeMock, salvar.arquivo)).thenReturn(true);

 // When
 salvar.salvar(entidadeMock, salvar.arquivo);

 // Then
 assertTrue(new File(expectedFilePath).exists());
 assertTrue(new File(expectedFilePath).delete());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvar_NullFile_Exception() {
 // Given
 String arquivo = null;
 salvar.arquivo = arquivo;

 // When & Then
 assertThrows(IllegalArgumentException.class, () -> salvar.salvar(entidadeMock, salvar.arquivo));
 }

 @Test(expected = IllegalStateException.class)
 public void testSalvar_VelaFalse_Exception() {
 // Given
 salvar.vela = false;
 when(salvar.arquivo).thenReturn("test/test.jaw");
 when(salvar.entidade.size()).thenReturn(2);
 Serializacao serializacao = mock(Serializacao.class);
 when(salvar.new Serializacao()).thenReturn(serializacao);
 when(serializacao.salvar(entidadeMock, salvar.arquivo)).thenReturn(true);

 // When & Then
 assertThrows(IllegalStateException.class, () -> salvar.salvar(entidadeMock, salvar.arquivo));
 }
}