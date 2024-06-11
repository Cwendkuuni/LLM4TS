package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Projeto;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private List<Entidade> entidades;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        entidades = new ArrayList<>();
        entidades.add(new Entidade("teste1", new Projeto("testeProjeto")));
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, Salvar.SALVAR_XML);
    }

    @Test
    public void testConstruction() {
        assertNotNull(salvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertNull(salvar.xml);
        assertEquals(new Vector<>(), salvar.entidade);
    }

    @Test
    public void testSetEntidades() {
        salvar.setEntidades(entidades);
        assertEquals(entidades, salvar.entidade);
    }

    @Test
    public void testSalvarWithValidXMLFileFilterAndSaveDialog() throws IOException {
        when(form.getEntidades()).thenReturn(entidades);
        doReturn(Mockito.mock(File.class)).when(form).getNomeArquivoSalvo();

        JFileChooser fileChooser = mock(JFileChooser.class);
        doReturn(fileChooser).when(form).getFileChooser();

        doReturn(FileDialog.class).when(fileChooser).showSaveDialog((Frame) anyObject());
        doReturn(true).when(fileChooser).isSaveDialog();

        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.jaw.xml"));

        salvar.salvar();

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).getFileChooser();
        verify(fileChooser, times(1)).showSaveDialog((Frame) anyObject());
        verify(fileChooser, times(1)).isSaveDialog();
        verify(fileChooser, times(1)).getSelectedFile();
        assertEquals("teste.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarWithValidXMLFileFilterAndNameInput() throws IOException {
        when(form.getEntidades()).thenReturn(entidades);
        doReturn("teste.jaw.xml").when(form).getNomeArquivoSalvo();

        salvar.salvar();

        verify(form, times(1)).getEntidades();
        verify(form, times(0)).getFileChooser();
        assertEquals("teste.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarWithInvalidXMLFileFilterAndSaveDialog() {
        when(form.getEntidades()).thenReturn(entidades);

        doReturn(null).when(form).getNomeArquivoSalvo();
        doReturn(JFileChooser.class).when(form).getFileChooser();
        doReturn(FileDialog.class).when(form.getFileChooser()).showSaveDialog((Frame) anyObject());
        doReturn(false).when(form.getFileChooser()).isSaveDialog();

        salvar.salvar();

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).getFileChooser();
        verify(form.getFileChooser(), times(0)).showSaveDialog((Frame) anyObject());
        verify(form.getFileChooser(), times(0)).isSaveDialog();
        verifyZeroInteractions(form.getFileChooser().getSelectedFile());
        verifyZeroInteractions(salvar);
    }

    @Test
    public void testSalvarWithInvalidXMLFileFilterAndNameInput() {
        when(form.getEntidades()).thenReturn(entidades);
        doReturn("").when(form).getNomeArquivoSalvo();

        salvar.salvar();

        verify(form, times(1)).getEntidades();
        verify(form, times(0)).getFileChooser();
        verifyZeroInteractions(salvar);
    }

    @Test(expected = Exception.class)
    public void testSalvarWithException() throws IOException {
        when(form.getEntidades()).thenThrow(new Exception("Test exception"));

        salvar.salvar();
    }

    @Test
    public void testSalvarXMLWithValidFileFilterAndFileWriter() throws IOException {
        when(form.getEntidades()).thenReturn(entidades);
        doReturn("teste.jaw.xml").when(form).getNomeArquivoSalvo();
        String xml = "<entidades><entidade><nome>teste1</nome></entidade></entidades>";
        doReturn(xml).when(salvar).toXML(entidades);

        FileWriter writer = mock(FileWriter.class);
        doNothing().when(writer).write(xml);
        doNothing().when(writer).flush();
        doNothing().when(writer).close();

        salvar.salvarXML();

        verify(form, times(1)).getEntidades();
        verify(form, times(0)).getFileChooser();
        verify(writer, times(1)).write(xml);
        verify(writer, times(1)).flush();
        verify(writer, times(1)).close();
    }

    @Test(expected = IOException.class)
    public void testSalvarXMLWithIOException() throws IOException {
        when(form.getEntidades()).thenReturn(entidades);
        doReturn("teste.jaw.xml").when(form).getNomeArquivoSalvo();
        String xml = "<entidades><entidade><nome>teste1</nome></entidade></entidades>";
        doThrow(IOException.class).when(salvar).toXML(entidades);

        FileWriter writer = mock(FileWriter.class);

        salvar.salvarXML();
    }

    @Test
    public void testToXML() throws Exception {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(new Entidade("teste1", new Projeto("testeProjeto")));

        String xml = "<entidades><entidade><nome>teste1</nome></entidade></entidades>";
        assertEquals(xml, salvar.toXML(entidades));
    }
}