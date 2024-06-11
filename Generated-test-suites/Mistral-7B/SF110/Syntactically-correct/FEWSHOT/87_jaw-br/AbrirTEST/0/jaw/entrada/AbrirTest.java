package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;
    private FileDialog dialog;
    private JFileChooser fileChooser;
    private File selectedFile;

    @Before
    public void setup() {
        form = mock(ProcessarEntidades.class);
        dialog = mock(FileDialog.class);
        fileChooser = mock(JFileChooser.class);
        selectedFile = new File("test.jaw");

        when(dialog.getSelectedFile()).thenReturn(selectedFile);
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getFileFilter()).thenReturn(new Filtro2());
    }

    @Test
    public void testAbrirXML() {
        new Abrir(form, "test.jaw.xml");
        assertEquals("test.jaw.xml", form.getNomeArquivoSalvo());

        try {
            new SerializacaoXML().xml2Entidades(new FileReader("test.jaw.xml"));
        } catch (IOException e) {
            // expected
        }

        assertTrue(form.getEntidades() != null);
    }

    @Test
    public void testAbrirSerializado() {
        new Abrir(form, "test.jaw");
        assertEquals("test.jaw", form.getNomeArquivoSalvo());

        try {
            new Serializacao().abrir("test.jaw");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Arquivo jaw corrompido ou incompatível com aplicação!", "Abrir Arquivo jaw", 0);
            assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!", JOptionPane.getMessageDialog(null, "Abrir Arquivo jaw", JOptionPane.PLAIN_MESSAGE));
            assertTrue(form.getEntidades() == null);
        }
    }

    @Test
    public void testAbrirNullFile() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.CANCEL_OPTION);
        new Abrir(form, 20);
        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
    }

    @Test
    public void testAbrirInvalidFile() {
        when(fileChooser.getFileFilter()).thenReturn(new Filtro());
        new Abrir(form, 20);
        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
    }

    @Test
    public void testOpenDialogXML() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw.xml"));

        new Abrir(form, 10);
        assertEquals("test.jaw.xml", form.getNomeArquivoSalvo());

        try {
            new SerializacaoXML().xml2Entidades(new FileReader("test.jaw.xml"));
        } catch (IOException e) {
            // expected
        }

        assertTrue(form.getEntidades() != null);
    }

    @Test
    public void testOpenDialogSerializado() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        new Abrir(form, 20);
        assertEquals("test.jaw", form.getNomeArquivoSalvo());

        try {
            new Serializacao().abrir(selectedFile.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "Arquivo jaw corrompido ou incompatível com aplicação!", "Abrir Arquivo jaw", 0);
            assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!", JOptionPane.getMessageDialog(dialog, "Abrir Arquivo jaw", JOptionPane.PLAIN_MESSAGE));
            assertNull(form.getEntidades());
        }
    }
}

class Filtro2 extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
    }

    @Override
    public String getDescription() {
        return "XML JAW files";
    }
}

class Filtro extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
    }

    @Override
    public String getDescription() {
        return "JAW files";
    }
}