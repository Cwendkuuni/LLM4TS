package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private Abrir abrir;
    private ProcessarEntidades form;
    private JFileChooser escolhedor;
    private File selectedFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
        escolhedor = mock(JFileChooser.class);
        selectedFile = new File("test.jaw");
    }

    @Test
    public void testAbrirXML() {
        given(escolhedor.getSelectedFile()).willReturn(selectedFile);
        when(form.setEntidades(any())).then(invocation -> {});

        abrir = new Abrir(form, Abrir.ABRIR_XML);
        verify(escolhedor).setFileFilter(new Filtro2());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertTrue(abrir.xml.isEmpty());

        File xmlFile = new File("test.jaw.xml");
        given(escolhedor.getSelectedFile()).willReturn(xmlFile);

        abrir = new Abrir(form, Abrir.ABRIR_XML);
        verify(escolhedor).setFileFilter(new Filtro2());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertNotNull(abrir.xml);
    }

    @Test
    public void testAbrirSerializado() {
        given(escolhedor.getSelectedFile()).willReturn(selectedFile);
        when(form.setEntidades(any())).then(invocation -> {});

        abrir = new Abrir(form, Abrir.ABRIR);
        verify(escolhedor).setFileFilter(new Filtro());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertTrue(abrir.xml.isEmpty());

        given(escolhedor.getSelectedFile()).willReturn(selectedFile);
        doThrow(IOException.class).when(abrir).abrirSerializado(eq(selectedFile.getAbsolutePath()));

        abrir = new Abrir(form, Abrir.ABRIR);
        verify(escolhedor).setFileFilter(new Filtro());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertEquals("Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", abrir.getErrorMessage());
    }

    @Test
    public void testAbrirXMLInvalidFile() {
        given(escolhedor.getSelectedFile()).willReturn(new File("test.txt"));
        when(form.setEntidades(any())).then(invocation -> {});

        abrir = new Abrir(form, Abrir.ABRIR_XML);
        verify(escolhedor).setFileFilter(new Filtro2());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertEquals("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", abrir.getErrorMessage());
    }

    @Test
    public void testAbrirXMLEmptyFile() throws IOException {
        given(escolhedor.getSelectedFile()).willReturn(new File("test.jaw.xml"));
        when(form.setEntidades(any())).then(invocation -> {});

        abrir = new Abrir(form, Abrir.ABRIR_XML);
        verify(escolhedor).setFileFilter(new Filtro2());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertNotNull(abrir.xml);
        assertTrue(abrir.xml.isEmpty());
    }

    @Test
    public void testAbrirSerializadoEmptyFile() throws IOException {
        given(escolhedor.getSelectedFile()).willReturn(new File("test.jaw"));
        when(form.setEntidades(any())).then(invocation -> {});

        abrir = new Abrir(form, Abrir.ABRIR);
        verify(escolhedor).setFileFilter(new Filtro());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertTrue(abrir.xml.isEmpty());
    }

    @Test
    public void testAbrirSerializadoValidFile() throws IOException {
        given(escolhedor.getSelectedFile()).willReturn(new File("test.jaw"));
        when(form.setEntidades(any())).then(invocation -> {});

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade name=\"test1\">\n<property name=\"test1_1\">test1_1</property>\n<property name=\"test1_2\">test1_2</property>\n</entidade>\n</entidades>";
        when(new Serializacao().abrir(eq(new File("test.jaw").getAbsolutePath()))).thenReturn(expectedXml);

        abrir = new Abrir(form, Abrir.ABRIR);
        verify(escolhedor).setFileFilter(new Filtro());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertNotNull(abrir.xml);
        assertEquals(expectedXml, abrir.xml);
    }

    @Test
    public void testAbrirXMLValidFile() throws IOException {
        given(escolhedor.getSelectedFile()).willReturn(new File("test.jaw.xml"));
        when(form.setEntidades(any())).then(invocation -> {});

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade name=\"test1\">\n<property name=\"test1_1\">test1_1</property>\n<property name=\"test1_2\">test1_2</property>\n</entidade>\n</entidades>";
        when(new SerializacaoXML().xml2Entidades(any())).thenReturn(expectedXml.split("\n"));

        abrir = new Abrir(form, Abrir.ABRIR_XML);
        verify(escolhedor).setFileFilter(new Filtro2());
        verify(escolhedor, times(1)).showOpenDialog(eq(null));
        verify(escolhedor).getSelectedFile();
        assertNotNull(abrir.xml);
        assertEquals(expectedXml, String.join("\n", abrir.xml));
    }
}