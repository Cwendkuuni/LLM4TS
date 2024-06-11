package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private Abrir abrir;
    private ProcessarEntidades form;
    private JFileChooser escolhedor;
    private File arquivo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
        escolhedor = mock(JFileChooser.class);
        arquivo = new File("test.jaw");
    }

    @Test
    public void testAbrirXMLWithValidFile() {
        when(escolhedor.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File("valid.jaw.xml"));

        abrir = new Abrir(form, escolhedor);

        verify(form).setNomeArquivoSalvo("valid.jaw.xml");
        verify(form).setEntidades(any());
    }

    @Test
    public void testAbrirSerializadoWithValidFile() {
        when(escolhedor.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(arquivo);

        abrir = new Abrir(form, 20, escolhedor);

        try {
            abrir.abrirSerializado(arquivo.getAbsolutePath());
            verify(form).setNomeArquivoSalvo(arquivo.getAbsolutePath());
            verify(form).setEntidades(any());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirXMLWithInvalidFile() {
        when(escolhedor.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File("invalid.txt"));

        abrir = new Abrir(form, escolhedor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirSerializadoWithInvalidFile() {
        when(escolhedor.showOpenDialog(anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(arquivo);

        abrir = new Abrir(form, 20, escolhedor);

        try {
            abrir.abrirSerializado(new File("invalid.jaw").getAbsolutePath());
            fail("IllegalArgumentException should be thrown");
        } catch (Exception e) {
            verifyNoMoreInteractions(form);
        }
    }

    @Test
    public void testFileFilters() {
        JFileChooser chooser = spy(new JFileChooser());
        when(chooser.getFileFilter()).thenReturn(abrir.new Filtro());

        FileDialog dialog = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
        chooser.setFileChooserDialog(new Frame(), "Choose a file", dialog.FILE_OPEN, chooser.getFileFilter());

        chooser.showOpenDialog(new Frame());

        verify(chooser).setFileFilter(abrir.new Filtro());
    }

    @Test
    public void testFileFilterDescription() {
        assertEquals("JAW files", new Filtro().getDescription());
    }
}