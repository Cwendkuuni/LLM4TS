package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, 20);
    }

    @Test
    public void testAbrirSerializadoArquivoValido() {
        String arquivoValido = "teste.jaw";
        when(form.setEntidades((Object) any())).thenReturn(true);

        SwingUtilities.invokeLater(() -> {
            JFileChooser escolhedor = new JFileChooser();
            File arquivoSelecionado = new File(arquivoValido);
            escolhedor.setSelectedFile(arquivoSelecionado);
            abrir.showOpenDialog(escolhedor);
            assertEquals(true, abrir.getEntidades() != null);
        });
    }

    @Test
    public void testAbrirSerializadoArquivoInvalido() {
        String arquivoInvalido = "teste.txt";
        when(form.setEntidades((Object) any())).thenReturn(false);

        SwingUtilities.invokeLater(() -> {
            JFileChooser escolhedor = new JFileChooser();
            File arquivoSelecionado = new File(arquivoInvalido);
            escolhedor.setSelectedFile(arquivoSelecionado);
            abrir.showOpenDialog(escolhedor);
            assertEquals(JOptionPane.showMessageDialog(null, (String) equalTo("Arquivo jaw corrompido ou incompatível com aplicação!"), "Abrir Arquivo jaw", 0), abrir.getMensagemErro());
        });
    }

    @Test
    public void testAbrirXMLArquivoValido() {
        String arquivoValido = "teste.jaw.xml";
        when(form.setEntidades((Object) any())).thenReturn(true);

        SwingUtilities.invokeLater(() -> {
            JFileChooser escolhedor = new JFileChooser();
            File arquivoSelecionado = new File(arquivoValido);
            escolhedor.setSelectedFile(arquivoSelecionado);
            abrir = new Abrir(form, 10);
            abrir.showOpenDialog(escolhedor);
            assertEquals(true, abrir.getEntidades() != null);
        });
    }

    @Test
    public void testAbrirXMLArquivoInvalido() {
        String arquivoInvalido = "teste.txt";
        when(form.setEntidades((Object) any())).thenReturn(false);

        SwingUtilities.invokeLater(() -> {
            JFileChooser escolhedor = new JFileChooser();
            File arquivoSelecionado = new File(arquivoInvalido);
            escolhedor.setSelectedFile(arquivoSelecionado);
            abrir = new Abrir(form, 10);
            abrir.showOpenDialog(escolhedor);
            assertEquals(JOptionPane.showMessageDialog(null, (String) equalTo("Arquivo xml corrompido ou incompatível com aplicação!"), "Importar XML", 0), abrir.getMensagemErro());
        });
    }

    @Test
    public void testAbrirCancelado() {
        SwingUtilities.invokeLater(() -> {
            JFileChooser escolhedor = new JFileChooser();
            abrir = new Abrir(form, 20);
            escolhedor.setSelectedFile(null);
            abrir.showOpenDialog(escolhedor);
            assertEquals(null, abrir.getEntidades());
        });
    }
}

class FileDialogStub extends FileDialog {

    private String arquivoSelecionado;

    public FileDialogStub(Frame frame, String title, String message, File directory, String[] files) {
        super(frame, title, message, directory, files);
        this.arquivoSelecionado = files[0];
    }

    @Override
    public String getFile() {
        return arquivoSelecionado;
    }
}