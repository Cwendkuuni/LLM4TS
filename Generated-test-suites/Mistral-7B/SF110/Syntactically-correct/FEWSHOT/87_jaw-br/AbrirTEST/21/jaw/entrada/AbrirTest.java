package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private Abrir abrir;
    private ProcessarEntidades form;
    private FileDialog dialog;
    private JFileChooser fileChooser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, "teste.jaw.xml");
        dialog = mock(FileDialog.class);
        fileChooser = mock(JFileChooser.class);
    }

    @Test
    public void testAbrirXML() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.jaw.xml"));

        abrir = new Abrir(form, Abrir.ABRIR_XML);

        verify(form).setNomeArquivoSalvo("teste.jaw.xml");
        try {
            new SerializacaoXML().xml2Entidades(new FileReader("teste.jaw.xml"));
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testAbrirSerializado() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.jaw"));

        abrir = new Abrir(form, Abrir.ABRIR);

        verify(form).setNomeArquivoSalvo("teste.jaw");
        try {
            new Serializacao().abrir("teste.jaw");
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testAbrirNullFile() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        abrir = new Abrir(form, Abrir.ABRIR);

        verify(form, never()).setNomeArquivoSalvo(anyString());
    }

    @Test
    public void testAbrirInvalidFileType() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.txt"));

        abrir = new Abrir(form, Abrir.ABRIR);

        verify(form, never()).setNomeArquivoSalvo(anyString());
    }

    @Test
    public void testAbrirWithInvalidFileFilter() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getFileFilter()).thenReturn(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return false;
            }

            @Override
            public String getDescription() {
                return "Invalid Filter";
            }
        });

        abrir = new Abrir(form, Abrir.ABRIR);

        verify(form, never()).setNomeArquivoSalvo(anyString());
    }

    @Test
    public void testAbrirWithValidFileFilter() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getFileFilter()).thenReturn(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
            }

            @Override
            public String getDescription() {
                return "XML JAW files";
            }
        });

        abrir = new Abrir(form, Abrir.ABRIR_XML);

        verify(form).setNomeArquivoSalvo("teste.jaw.xml");
    }
}