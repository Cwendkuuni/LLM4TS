package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import org.junit.Test;
import org.mockito.Mockito;

public class AbrirTest {

    @Test
    public void testAbrirXMLWithValidXMLFile() {
        ProcessarEntidades formMock = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(formMock, "test.jaw.xml");
        assertEquals("", abrir.xml);
        verify(formMock).setNomeArquivoSalvo("test.jaw.xml");
        try {
            when(new FileReader("test.jaw.xml").readAllBytes()).thenReturn(" xml data ".getBytes());
            abrir.abrirXML("test.jaw.xml");
            verify(formMock).setEntidades(anyList());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testAbrirXMLWithInvalidXMLFile() {
        ProcessarEntidades formMock = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(formMock, "test.txt");
        assertEquals("", abrir.xml);
        verify(formMock, never()).setEntidades(anyList());
        verify(formMock).showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!"), eq("Importa\u00c3§\u00c3£o XML"), eq(0));
    }

    @Test
    public void testAbrirSerializadoWithValidJawFile() {
        ProcessarEntidades formMock = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(formMock, Abrir.ABRIR, "test.jaw");
        assertEquals("", abrir.xml);
        File file = Mockito.mock(File.class);
        when(file.getAbsolutePath()).thenReturn("test.jaw");
        JFileChooser escolhedorMock = mock(JFileChooser.class);
        when(escolhedorMock.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedorMock.getSelectedFile()).thenReturn(file);
        when(new FileReader("test.jaw").readAllBytes()).thenReturn(" serialized data ".getBytes());

        doReturn(escolhedorMock).when(formMock).new JFileChooser();

        abrir.abrirSerializado(arquivo);

        verify(formMock).setNomeArquivoSalvo("test.jaw");
        try {
            when(new Serializacao().abrir(anyByteArray())).thenReturn(anyList());
            abrir.form.setEntidades(anyList());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAbrirSerializadoWithInvalidJawFile() {
        ProcessarEntidades formMock = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(formMock, Abrir.ABRIR, "test.txt");
        assertEquals("", abrir.xml);
        verify(formMock, never()).setEntidades(anyList());
        verify(formMock).showMessageDialog(eq(null), eq("Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!"), eq("Abrir Arquivo jaw"), eq(0));
    }

    @Test
    public void testOpenFileDialogWithXMLFilter() {
        ProcessarEntidades formMock = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(formMock, Abrir.ABRIR_XML);

        JFileChooser escolhedorMock = mock(JFileChooser.class);
        when(escolhedorMock.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedorMock.getSelectedFile()).thenReturn(new File("test.jaw.xml"));

        doReturn(escolhedorMock).when(formMock).new JFileChooser();

        abrir.abrir(formMock, Abrir.ABRIR_XML);

        verify(formMock).setNomeArquivoSalvo("test.jaw.xml");
    }

    @Test
    public void testOpenFileDialogWithJawFilter() {
        ProcessarEntidades formMock = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(formMock, Abrir.ABRIR);

        JFileChooser escolhedorMock = mock(JFileChooser.class);
        when(escolhedorMock.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedorMock.getSelectedFile()).thenReturn(new File("test.jaw"));

        doReturn(escolhedorMock).when(formMock).new JFileChooser();

        abrir.abrir(formMock, Abrir.ABRIR);

        verify(formMock).setNomeArquivoSalvo("test.jaw");
    }

    @Test
    public void testOpenFileDialogWithEmptyPath() {
        ProcessarEntidades formMock = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(formMock, Abrir.ABRIR);

        JFileChooser escolhedorMock = mock(JFileChooser.class);
        when(escolhedorMock.showOpenDialog(any())).thenReturn(JFileChooser.CANCEL_OPTION);

        doReturn(escolhedorMock).when(formMock).new JFileChooser();

        abrir.abrir(formMock, Abrir.ABRIR);

        verify(formMock, never()).setNomeArquivoSalvo(anyString());
    }
}