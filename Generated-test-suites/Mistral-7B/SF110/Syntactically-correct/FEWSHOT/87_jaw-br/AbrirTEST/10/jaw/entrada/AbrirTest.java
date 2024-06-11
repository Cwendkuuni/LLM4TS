package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.io.*;

public class AbrirTest {

    private ProcessarEntidades formMock;
    private String arquivo;

    @Before
    public void setup() {
        formMock = mock(ProcessarEntidades.class);
        arquivo = "test.jaw.xml";
    }

    @Test
    public void testAbrirXMLWithValidXmlFile() {
        final File file = new File(arquivo);
        final Reader readerMock = mock(Reader.class);
        when(readerMock.read(anyIntArray(), anyInt(), anyInt())).thenReturn(42); // return some data for testing
        when(new SerializacaoXML().xml2Entidades(readerMock)).thenReturn(new Entidades());

        new Abrir(formMock, arquivo);

        verify(formMock).setNomeArquivoSalvo(arquivo);
        verify(formMock).setEntidades(new Entidades());
    }

    @Test
    public void testAbrirXMLWithInvalidXmlFile() {
        final File file = new File(arquivo);

        new Abrir(formMock, arquivo);

        verify(formMock, never()).setEntidades(any());
        verify(formMock).setNomeArquivoSalvo(eq(arquivo));
        verify(formMock).showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompatível com aplicação!"), eq("Importação XML"), eq(0));
    }

    @Test
    public void testAbrirSerializadoWithValidFile() {
        final File file = new File(arquivo);

        new Abrir(formMock, Abrir.ABRIR);

        verify(formMock).setNomeArquivoSalvo(anyString());
        verify(formMock).setEntidades(any());
    }

    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        final File file = new File(arquivo);

        new Abrir(formMock, Abrir.ABRIR);

        verify(formMock, never()).setEntidades(any());
        verify(formMock).showMessageDialog(eq(null), eq("Arquivo jaw corrompido ou incompatível com aplicação!"), eq("Abrir Arquivo jaw"), eq(0));
    }

    @Test
    public void testOpenFileDialogWithXMLFilter() {
        final int returnValue = JOptionPane.showConfirmDialog(null, "Open XML file", "Open Dialog", JOptionPane.OPEN_DIALOG);
        final FileFilter xmlFilterMock = mock(FileFilter.class);
        when(xmlFilterMock.accept(any(File.class))).thenReturn(true);
        when(new JFileChooser().getFileFilter()).thenReturn(xmlFilterMock);

        new Abrir(formMock, Abrir.ABRIR_XML);

        verify(formMock).setNomeArquivoSalvo(anyString());
        verify(formMock).setEntidades(any());
        verify(new JFileChooser()).showOpenDialog(eq(null));
    }

    @Test
    public void testOpenFileDialogWithNoFileSelected() {
        final int returnValue = JOptionPane.showConfirmDialog(null, "Open file", "Open Dialog", JOptionPane.OPEN_DIALOG);
        final FileFilter xmlFilterMock = mock(FileFilter.class);
        when(xmlFilterMock.accept(any(File.class))).thenReturn(true);
        when(new JFileChooser().getFileFilter()).thenReturn(xmlFilterMock);

        new Abrir(formMock, Abrir.ABRIR_XML);

        verify(formMock, never()).setEntidades(any());
        verify(new JFileChooser()).showOpenDialog(eq(null));
    }
}