package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private Abrir abrir;
    private ProcessarEntidades form;
    private FileFilter filtro;
    private FileFilter filtro2;
    private JFileChooser escolhedor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
        filtro = mock(FileFilter.class);
        filtro2 = mock(FileFilter.class);
        escolhedor = mock(JFileChooser.class);
    }

    @Test
    public void testAbrirXML() {
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw.xml"));
        when(escolhedor.getFileFilter()).thenReturn(filtro2);
        when(filtro2.accept(any(File.class))).thenReturn(true);

        abrir = new Abrir(form, escolhedor, Abrir.ABRIR_XML);

        verify(form).setNomeArquivoSalvo("test.jaw.xml");
        try {
            verify(new SerializacaoXML()).xml2Entidades(any(Reader.class));
        } catch (Exception e) {
            fail("xml2Entidades should not throw an exception");
        }
    }

    @Test
    public void testAbrirSerializado() {
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw"));
        when(escolhedor.getFileFilter()).thenReturn(filtro);
        when(filtro.accept(any(File.class))).thenReturn(true);

        abrir = new Abrir(form, escolhedor, Abrir.ABRIR);

        verify(form).setNomeArquivoSalvo("test.jaw");
        try {
            verify(new Serializacao()).abrir(eq("test.jaw"));
        } catch (Exception e) {
            fail("abrir should not throw an exception");
        }
    }

    @Test
    public void testAbrirNullFile() {
        when(escolhedor.getSelectedFile()).thenReturn(null);

        abrir = new Abrir(form, escolhedor, Abrir.ABRIR);

        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
    }

    @Test
    public void testAbrirInvalidFileXML() {
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.txt"));
        when(escolhedor.getFileFilter()).thenReturn(filtro2);
        when(filtro2.accept(any(File.class))).thenReturn(true);

        abrir = new Abrir(form, escolhedor, Abrir.ABRIR_XML);

        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        verify(JOptionPane).showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!"), eq("Importa\u00c3§\u00c3£o XML"), eq(0));
    }

    @Test
    public void testAbrirInvalidFileJaw() {
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.txt"));
        when(escolhedor.getFileFilter()).thenReturn(filtro);
        when(filtro.accept(any(File.class))).thenReturn(true);

        abrir = new Abrir(form, escolhedor, Abrir.ABRIR);

        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        verify(JOptionPane).showMessageDialog(eq(null), eq("Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!"), eq("Abrir Arquivo jaw"), eq(0));
    }

    @Test
    public void testAbrirInvalidFileXMLFilter() {
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw.xml"));
        when(escolhedor.getFileFilter()).thenReturn(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return false;
            }

            @Override
            public String getDescription() {
                return "";
            }
        });

        abrir = new Abrir(form, escolhedor, Abrir.ABRIR_XML);

        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        verify(JOptionPane).showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!"), eq("Importa\u00c3§\u00c3£o XML"), eq(0));
    }

    @Test
    public void testAbrirInvalidFileJawFilter() {
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw.xml"));
        when(escolhedor.getFileFilter()).thenReturn(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return true;
            }

            @Override
            public String getDescription() {
                return "Invalid Filter";
            }
        });

        abrir = new Abrir(form, escolhedor, Abrir.ABRIR_XML);

        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(form, never()).setEntidades(any());
        verify(JOptionPane).showMessageDialog(eq(null), eq("Invalid Filter"), eq("Error"), eq(0));
    }
}