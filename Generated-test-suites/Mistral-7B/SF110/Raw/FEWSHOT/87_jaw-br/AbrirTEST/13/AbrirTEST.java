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

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
    }

    @Test
    public void testAbrirXMLWithValidFileExtension() {
        String arquivoXml = "teste.jaw.xml";
        when(form.setEntidades(any())).thenReturn(true);

        abrir = new Abrir(form, arquivoXml);

        assertEquals(arquivoXml, abrir.xml);
        assertTrue(abrir.form.getEntidades() instanceof Entidades);
    }

    @Test
    public void testAbrirXMLWithInvalidFileExtension() {
        String arquivoInvalido = "teste.txt";

        abrir = new Abrir(form, arquivoInvalido);

        assertNull(abrir.xml);
        assertFalse(abrir.form.getEntidades() instanceof Entidades);
    }

    @Test
    public void testAbrirSerializadoWithValidFileExtension() {
        String arquivoJaw = "teste.jaw";

        when(form.setEntidades(any())).thenReturn(true);

        abrir = new Abrir(form, arquivoJaw);

        assertEquals(arquivoJaw, abrir.xml);
        assertTrue(abrir.form.getEntidades() instanceof Entidades);
    }

    @Test
    public void testAbrirSerializadoWithInvalidFileExtension() {
        String arquivoInvalido = "teste.xml";

        abrir = new Abrir(form, arquivoInvalido);

        assertNull(abrir.xml);
        assertFalse(abrir.form.getEntidades() instanceof Entidades);
    }

    @Test
    public void testOpenFileChooserWithXMLFilter() {
        String arquivoXml = "teste.jaw.xml";

        JFileChooser fileChooser = spy(new JFileChooser());
        FileFilter filterXML = new Filtro2();

        abrir = new Abrir(form, Abrir.ABRIR_XML);
        abrir.abrir(fileChooser);

        verify(fileChooser).setFileFilter(eq(filterXML));
    }

    @Test
    public void testOpenFileChooserWithJAWFilter() {
        String arquivoJaw = "teste.jaw";

        JFileChooser fileChooser = spy(new JFileChooser());
        FileFilter filterJAW = new Filtro();

        abrir = new Abrir(form, Abrir.ABRIR);
        abrir.abrir(fileChooser);

        verify(fileChooser).setFileFilter(eq(filterJAW));
    }

    @Test
    public void testOpenFileChooserWithNullFile() {
        JFileChooser fileChooser = spy(new JFileChooser());

        abrir = new Abrir(form, Abrir.ABRIR);
        abrir.abrir(fileChooser);

        verify(fileChooser, never()).showOpenDialog(any());
    }

    @Test
    public void testOpenFileChooserWithValidFile() throws IOException {
        String arquivoValido = "teste.jaw";
        File selectedFile = new File(arquivoValido);

        JFileChooser fileChooser = spy(new JFileChooser());
        FileFilter filterJAW = new Filtro();

        abrir = new Abrir(form, Abrir.ABRIR);
        abrir.abrir(fileChooser);

        fileChooser.setCurrentDirectory(new File(""));
        fileChooser.setSelectedFile(selectedFile);
        fileChooser.showOpenDialog(mock(Frame.class));

        verify(fileChooser).setFileFilter(eq(filterJAW));
        assertEquals(arquivoValido, abrir.xml);
    }

    @Test
    public void testOpenFileChooserWithInvalidFile() throws IOException {
        String arquivoInvalido = "teste.txt";
        File selectedFile = new File(arquivoInvalido);

        JFileChooser fileChooser = spy(new JFileChooser());
        FileFilter filterJAW = new Filtro();

        abrir = new Abrir(form, Abrir.ABRIR);
        abrir.abrir(fileChooser);

        fileChooser.setCurrentDirectory(new File(""));
        fileChooser.setSelectedFile(selectedFile);
        fileChooser.showOpenDialog(mock(Frame.class));

        verify(fileChooser, never()).setSelectedFile(selectedFile);
        assertNull(abrir.xml);
    }
}