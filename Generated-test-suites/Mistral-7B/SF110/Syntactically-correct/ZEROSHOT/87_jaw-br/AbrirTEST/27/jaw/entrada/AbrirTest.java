package jaw.entrada.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.Filtro;
import jaw.entrada.Filtro2;
import jaw.gui.ProcessarEntidades;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = mock(ProcessarEntidades.class);
 abrir = new Abrir(form, "test.jaw.xml");
 }

 @Test
 public void testAbrirXml() throws Exception {
 // Given
 String expected = "expected XML data";
 File file = new File("test.jaw.xml");
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));
 when(new FileReader(file).readAllBytes()).thenReturn(expected.getBytes());
 when(new SerializacaoXML().xml2Entidades(any())).then(invocation -> invocation.getArgument(0));

 // When
 abrir = new Abrir(form, Abrir.ABRIR_XML_VALUE);

 // Then
 assertEquals(expected, abrir.getEntidades());
 assertNull(abrir.getXml());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXmlFileNotFound() throws Exception {
 // Given
 File file = new File("test.jaw.xml");
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));
 when(new FileReader(file)).thenThrow(new FileNotFoundException());

 // When
 abrir = new Abrir(form, Abrir.ABRIR_XML_VALUE);

 // Then
 assertNull(abrir.getEntidades());
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirSerializado() throws Exception {
 // Given
 byte[] expected = new byte[] { 1, 2, 3 };
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));
 when(new Serializacao().abrir(any())).thenReturn(expected);

 // When
 abrir = new Abrir(form, "test.jaw");

 // Then
 assertEquals(expected, abrir.getEntidades().toByteArray());
 assertNull(abrir.getXml());
 }

 @Test(expected = IOException.class)
 public void testAbrirSerializadoIOException() throws Exception {
 // Given
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));
 when(new Serializacao().abrir(any())).thenThrow(new IOException());

 // When
 abrir = new Abrir(form, "test.jaw");

 // Then
 assertNull(abrir.getEntidades());
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirJFileChooserNull() throws Exception {
 // Given
 JFileChooser escolhedor = null;
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));

 // When
 abrir = new Abrir(form, Abrir.ABRIR_VALUE);

 // Then
 assertNull(abrir.getEntidades());
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirJFileChooserEmpty() throws Exception {
 // Given
 JFileChooser escolhedor = mock(JFileChooser.class);
 when(escolhedor.getSelectedFile()).thenReturn(null);
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));
 when(form.setNomeArquivoSalvo(anyString())).then(invocation -> {});

 // When
 abrir = new Abrir(form, escolhedor, Abrir.ABRIR_VALUE);

 // Then
 assertNull(abrir.getEntidades());
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirSerializadoWrongFileType() throws Exception {
 // Given
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));

 // When
 abrir = new Abrir(form, "test.txt");

 // Then
 assertNull(abrir.getEntidades());
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirXmlWrongFileType() throws Exception {
 // Given
 when(form.setEntidades((Object) any())).then(invocation -> invocation.getArgument(0));

 // When
 abrir = new Abrir(form, "test.csv");

 // Then
 assertNull(abrir.getEntidades());
 assertNull(abrir.getXml());
 }

}