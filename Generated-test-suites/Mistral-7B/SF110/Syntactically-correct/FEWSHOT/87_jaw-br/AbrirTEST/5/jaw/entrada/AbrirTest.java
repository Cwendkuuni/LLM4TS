package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private Abrir abrir;
    private ProcessarEntidades form;
    private FileDialog fileDialog;
    private JFileChooser fileChooser;
    private FileFilter filtro;
    private FileFilter filtro2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
        fileDialog = mock(FileDialog.class);
        fileChooser = mock(JFileChooser.class);
        filtro = mock(FileFilter.class);
        filtro2 = mock(FileFilter.class);
    }

    @Test
    public void testAbrirXMLWithValidXmlFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw.xml"));

        when(filtro2.accept(any(File.class))).thenReturn(true);
        when(fileChooser.getFileFilter()).thenReturn(filtro2);

        abrir = new Abrir(form, Abrir.ABRIR_XML);

        abrir.abrir(fileChooser, fileDialog);

        verify(form).setNomeArquivoSalvo("test.jaw.xml");
        verify(form).setEntidades(anyList());
    }

    @Test
    public void testAbrirSerializadoWithValidJawFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw"));

        when(filtro.accept(any(File.class))).thenReturn(true);
        when(fileChooser.getFileFilter()).thenReturn(filtro);

        abrir = new Abrir(form, Abrir.ABRIR);

        abrir.abrir(fileChooser, fileDialog);

        verify(form).setNomeArquivoSalvo("test.jaw");
        verify(form).setEntidades(anyList());
    }

    @Test
    public void testAbrirXMLWithInvalidXmlFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.txt"));

        when(filtro2.accept(any(File.class))).thenReturn(true);
        when(fileChooser.getFileFilter()).thenReturn(filtro2);

        abrir = new Abrir(form, Abrir.ABRIR_XML);

        abrir.abrir(fileChooser, fileDialog);

        verify(JOptionPane::showMessageDialog, times(1)).withArguments(eq("Importa\u00c3§\u00c3£o XML"), eq("Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!"), eq(0));
    }

    @Test
    public void testAbrirSerializadoWithInvalidJawFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.txt"));

        when(filtro.accept(any(File.class))).thenReturn(true);
        when(fileChooser.getFileFilter()).thenReturn(filtro);

        abrir = new Abrir(form, Abrir.ABRIR);

        abrir.abrir(fileChooser, fileDialog);

        verify(JOptionPane::showMessageDialog, times(1)).withArguments(eq("Abrir Arquivo jaw"), eq("Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!"), eq(0));
    }

    @Test
    public void testAbrirWithNullFile() {
        when(fileChooser.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        abrir = new Abrir(form, Abrir.ABRIR_XML);

        abrir.abrir(fileChooser, fileDialog);

        verify(fileDialog, never()).setFile("test");
    }
}