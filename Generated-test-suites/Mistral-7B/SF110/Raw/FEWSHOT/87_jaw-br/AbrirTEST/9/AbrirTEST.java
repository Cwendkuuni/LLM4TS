package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, new String[0]);
    }

    @Test
    public void testAbrirXML() {
        String arquivoXml = "teste.jaw.xml";
        when(form.setEntidades(any(Entidades.class))).thenReturn(true);
        when(new FileReader(arquivoXml).readAllBytes()).thenReturn("XML data".getBytes());

        abrir = new Abrir(form, arquivoXml, Abrir.ABRIR_XML);

        assertTrue(abrir.form.isEntidadesSet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirXMLWithInvalidFile() {
        String arquivoInvalido = "teste.txt";

        abrir = new Abrir(form, arquivoInvalido, Abrir.ABRIR_XML);

        fail("Should not reach this point");
    }

    @Test
    public void testAbrirSerializado() throws IOException {
        String arquivoSerializado = "teste.jaw";
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new Serializacao().abrir(arquivoSerializado)).thenReturn(new Entidades());

        abrir = new Abrir(form, arquivoSerializado, Abrir.ABRIR);

        assertEquals(new Entidades(), abrir.form.getEntidades());
        verify(form).setNomeArquivoSalvo(arquivoSerializado);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirSerializadoWithInvalidFile() {
        String arquivoInvalido = "teste.txt";

        abrir = new Abrir(form, arquivoInvalido, Abrir.ABRIR);

        fail("Should not reach this point");
    }

    @Test
    public void testOpenFileDialogXMLFilter() {
        JFileChooser escolhedor = mock(JFileChooser.class);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML JAW files", "jaw.xml");

        when(new JFileChooser().showOpenDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getFileFilter()).thenReturn(filter);

        abrir = new Abrir(form, new String[0], Abrir.ABRIR_XML);

        assertEquals(filter, abrir.getFileChooser().getFileFilter());
    }

    @Test
    public void testOpenFileDialogSerializadoFilter() {
        JFileChooser escolhedor = mock(JFileChooser.class);
        FileFilter filter = mock(FileFilter.class);

        when(new JFileChooser().showOpenDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getFileFilter()).thenReturn(filter);

        abrir = new Abrir(form, new String[0], Abrir.ABRIR);

        assertEquals(filter, abrir.getFileChooser().getFileFilter());
    }

    @Test
    public void testShowMessageDialogErrorXML() {
        JOptionPane optionPane = mock(JOptionPane.class);

        when(new JOptionPane().showMessageDialog(any(Component.class), anyString(), anyString(), JOptionPane.ERROR_MESSAGE)).thenReturn("Error message");

        abrir = spy(new Abrir(form, new File("teste.xml"), Abrir.ABRIR_XML));

        doThrow(new IOException("Error opening file")).when(abrir).abrirXML(anyString());

        assertEquals("Error message", abrir.showErrorMessageDialog("Error opening file", "Importa\u00c3§\u00c3£o XML"));
        verify(optionPane, times(1)).showMessageDialog(any(Component.class), eq("Error message"), eq("Importa\u00c3§\u00c3£o XML"), eq(JOptionPane.ERROR_MESSAGE));
    }

    @Test
    public void testShowMessageDialogErrorSerializado() {
        JOptionPane optionPane = mock(JOptionPane.class);

        when(new JOptionPane().showMessageDialog(any(Component.class), anyString(), anyString(), JOptionPane.ERROR_MESSAGE)).thenReturn("Error message");

        abrir = spy(new Abrir(form, new File("teste.jaw"), Abrir.ABRIR));

        doThrow(new IOException("Error opening file")).when(abrir).abrirSerializado(anyString());

        assertEquals("Error message", abrir.showErrorMessageDialog("Error opening file", "Abrir Arquivo jaw"));
        verify(optionPane, times(1)).showMessageDialog(any(Component.class), eq("Error message"), eq("Abrir Arquivo jaw"), eq(JOptionPane.ERROR_MESSAGE));
    }
}