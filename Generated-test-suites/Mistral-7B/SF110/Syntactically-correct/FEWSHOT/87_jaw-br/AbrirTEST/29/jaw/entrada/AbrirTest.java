package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    private static final String TEST_XML_FILE = "test.jaw.xml";
    private static final String TEST_SERIALIZED_FILE = "test.jaw";
    private ProcessarEntidades form;
    private Abrir abrir;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, TEST_XML_FILE);
    }

    @Test
    public void testAbrirXMLFile() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenReturn(new Object[0]);

        abrir = new Abrir(form, TEST_XML_FILE);

        assertEquals(TEST_XML_FILE, abrir.xml);
        assertEquals(form, abrir.form);
        assertTrue(abrir.form.getEntidades().length > 0);
        verify(form).setEntidades(argThat(isA(Object[].class)));
    }

    @Test
    public void testAbrirSerializadoFile() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new Serializacao().abrir(anyString())).thenReturn(new Object[0]);

        abrir = new Abrir(form, TEST_SERIALIZED_FILE);

        assertEquals(TEST_SERIALIZED_FILE, abrir.xml);
        assertEquals(form, abrir.form);
        assertTrue(abrir.form.getEntidades().length > 0);
        verify(form).setEntidades(argThat(isA(Object[].class)));
    }

    @Test
    public void testNullFileWhenOpenFileChooser() {
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.CANCEL_OPTION);

        abrir = new Abrir(form, 20);
        abrir.setFileChooser(fileChooser);

        assertNull(abrir.xml);
        assertNull(abrir.form);
    }

    @Test
    public void testOpenXMLFileWhenFileChooser() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenReturn(new Object[0]);

        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.getSelectedFile()).thenReturn(new File(TEST_XML_FILE));
        when(fileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        abrir = new Abrir(form, 10);
        abrir.setFileChooser(fileChooser);

        assertEquals(TEST_XML_FILE, abrir.xml);
        assertEquals(form, abrir.form);
        assertTrue(abrir.form.getEntidades().length > 0);
        verify(form).setEntidades(argThat(isA(Object[].class)));
    }

    @Test
    public void testOpenSerializadoFileWhenFileChooser() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new Serializacao().abrir(anyString())).thenReturn(new Object[0]);

        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.getSelectedFile()).thenReturn(new File(TEST_SERIALIZED_FILE));
        when(fileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        abrir = new Abrir(form, 20);
        abrir.setFileChooser(fileChooser);

        assertEquals(TEST_SERIALIZED_FILE, abrir.xml);
        assertEquals(form, abrir.form);
        assertTrue(abrir.form.getEntidades().length > 0);
        verify(form).setEntidades(argThat(isA(Object[].class)));
    }

    @Test
    public void testInvalidFileWhenOpenFileChooser() {
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.getSelectedFile()).thenReturn(new File("invalidFile.txt"));

        abrir = new Abrir(form, 20);
        abrir.setFileChooser(fileChooser);

        assertNull(abrir.xml);
        assertNull(abrir.form);
        verify(form, never()).setEntidades(any(Object[].class));
        verifyNoMessages();
    }

    @Test
    public void testOpenFileWithFileFilter() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenReturn(new Object[0]);

        final JFileChooser fileChooser = mock(JFileChooser.class);
        final FileFilter xmlFilter = new Filtro2();
        when(fileChooser.getFileFilter()).thenReturn(xmlFilter);
        when(xmlFilter.accept(any(File.class))).thenReturn(true);
        when(fileChooser.getSelectedFile()).thenReturn(new File(TEST_XML_FILE));
        when(fileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        abrir = new Abrir(form, 10);
        abrir.setFileChooser(fileChooser);

        assertEquals(TEST_XML_FILE, abrir.xml);
        assertEquals(form, abrir.form);
        assertTrue(abrir.form.getEntidades().length > 0);
        verify(form).setEntidades(argThat(isA(Object[].class)));
    }

    @Test
    public void testOpenFileWithInvalidFileFilter() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenReturn(new Object[0]);

        final JFileChooser fileChooser = mock(JFileChooser.class);
        final FileFilter invalidFilter = new Filtro();
        when(fileChooser.getFileFilter()).thenReturn(invalidFilter);
        when(fileChooser.getSelectedFile()).thenReturn(new File(TEST_XML_FILE));
        when(fileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        abrir = new Abrir(form, 10);
        abrir.setFileChooser(fileChooser);

        assertEquals(TEST_XML_FILE, abrir.xml);
        assertEquals(form, abrir.form);
        assertTrue(abrir.form.getEntidades().length > 0);
        verify(form).setEntidades(argThat(isA(Object[].class)));
    }

    @Test
    public void testOpenFileWithInvalidFileExtension() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenReturn(new Object[0]);

        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.getFileFilter()).thenReturn(new Filtro2());
        when(fileChooser.getSelectedFile()).thenReturn(new File("invalidFile.txt"));
        when(fileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        abrir = new Abrir(form, 10);
        abrir.setFileChooser(fileChooser);

        assertNull(abrir.xml);
        assertNull(abrir.form);
        verify(form, never()).setEntidades(any(Object[].class));
        verifyNoMessages();
    }

    @Test(expected = IOException.class)
    public void testReadFileWhenInvalidFile() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenThrow(new IOException());

        abrir = new Abrir(form, TEST_XML_FILE);

        assertNull(abrir.xml);
        assertNull(abrir.form);
        verify(form, never()).setEntidades(any(Object[].class));
        verifyNoMessages();
    }

    @Test
    public void testShowErrorMessageWhenInvalidFile() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new SerializacaoXML().xml2Entidades(any(Reader.class))).thenThrow(new IOException());

        final JFileChooser fileChooser = mock(JFileChooser.class);
        final FileFilter xmlFilter = new Filtro2();
        when(fileChooser.getFileFilter()).thenReturn(xmlFilter);
        when(fileChooser.showOpenDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        abrir = new Abrir(form, 10);
        abrir.setFileChooser(fileChooser);

        assertEquals(TEST_XML_FILE, abrir.xml);
        assertEquals(form, abrir.form);
        verify(form).setEntidades(argThat(isA(Object[].class)));
        verify(JOptionPane.showMessageDialog, eq(null), eq("Importa\u00c3§\u00c3£o XML"), eq(0), isA(JOptionPane.ERROR_MESSAGE));
    }
}