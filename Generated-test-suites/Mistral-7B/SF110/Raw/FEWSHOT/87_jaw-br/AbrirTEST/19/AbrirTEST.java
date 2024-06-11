package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import jaw.*;
import jaw.gui.*;

public class AbrirTest {

    @Test
    public void testAbrirXML_ValidXmlFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "test/resources/jaw.xml");
        assertEquals("test/resources/jaw.xml", abrir.xml);
        verify(form).setNomeArquivoSalvo(eq("test/resources/jaw.xml"));
        try {
            new SerializacaoXML().xml2Entidades(new FileReader("test/resources/jaw.xml"));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        verify(form).setEntidades(any());
    }

    @Test
    public void testAbrirSerializado_ValidJawFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "test/resources/test.jaw");
        assertEquals("test/resources/test.jaw", abrir.xml);
        verify(form).setNomeArquivoSalvo(eq("test/resources/test.jaw"));
        try {
            new Serializacao().abrir(new File("test/resources/test.jaw"));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        verify(form).setEntidades(any());
    }

    @Test
    public void testAbrirXML_InvalidXmlFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "test/resources/invalid.xml");
        assertEquals("", abrir.xml);
        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        JOptionPane.showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompatível com aplicação!"), eq("Importação XML"), eq(0));
    }

    @Test
    public void testAbrirSerializado_InvalidJawFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "test/resources/invalid.jaw");
        assertEquals("", abrir.xml);
        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        JOptionPane.showMessageDialog(eq(null), eq("Arquivo jaw corrompido ou incompatível com aplicação!"), eq("Abrir Arquivo jaw"), eq(0));
    }

    @Test
    public void testOpenFileDialog_XMLFilter() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showOpenDialog(eq(null))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File("test/resources/jaw.xml"));

        abrir.abrir(escolhedor);

        verify(form).setNomeArquivoSalvo(eq("test/resources/jaw.xml"));
        try {
            new SerializacaoXML().xml2Entidades(new FileReader("test/resources/jaw.xml"));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        verify(form).setEntidades(any());
    }

    @Test
    public void testOpenFileDialog_SerializadoFilter() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showOpenDialog(eq(null))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File("test/resources/test.jaw"));

        abrir.abrir(escolhedor);

        verify(form).setNomeArquivoSalvo(eq("test/resources/test.jaw"));
        try {
            new Serializacao().abrir(new File("test/resources/test.jaw"));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        verify(form).setEntidades(any());
    }

    @Test
    public void testOpenFileDialog_NullFileChooser() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        abrir.abrir(null);
        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        verify(form).mostrarMensagemDeErro("Erro ao abrir arquivo", "Erro durante a abertura do arquivo!", JOptionPane.ERROR_MESSAGE);
    }

    @Test
    public void testOpenFileDialog_EmptyDirectorySelected() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showOpenDialog(eq(null))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File(""));

        abrir.abrir(escolhedor);

        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        verify(form).mostrarMensagemDeErro("Erro ao abrir arquivo", "Nenhum arquivo foi selecionado!", JOptionPane.ERROR_MESSAGE);
    }
}