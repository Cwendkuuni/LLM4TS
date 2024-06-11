package jaw.entrada;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Frame;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.EntidadeFactory;

public class SalvarTest {

    private FileFilter xmlFilter;
    private FileFilter jawFilter;
    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setUp() {
        xmlFilter = mock(FileFilter.class);
        when(xmlFilter.accept(any(File.class))).thenReturn(true);
        when(xmlFilter.getDescription()).thenReturn("XML JAW files");

        jawFilter = mock(FileFilter.class);
        when(jawFilter.accept(any(File.class))).thenReturn(true);
        when(jawFilter.getDescription()).thenReturn("JAW files");

        form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getNomeArquivoSalvo()).thenReturn("");
    }

    @Test
    public void testSalvarWithXMLFilterAndValidFile() {
        final Salvar salvar = new Salvar(form, 10);

        final File selectedFile = new File("path/to/file.jaw.xml");
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        salvar.run();

        assertTrue(salvar.vela);
        assertEquals("path/to/file.jaw.xml", salvar.arquivo);
        assertTrue(salvar.xml.isEmpty());
        assertTrue(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvarWithXMLFilterAndNullFile() {
        final Salvar salvar = new Salvar(form, 10);

        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(null);

        salvar.run();

        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertNull(salvar.xml);
        assertTrue(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvarWithXMLFilterAndInvalidFile() {
        final Salvar salvar = new Salvar(form, 10);

        final File selectedFile = new File("path/to/invalid_file");
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        salvar.run();

        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertNull(salvar.xml);
        assertTrue(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvarWithJawFilterAndValidFile() {
        final Salvar salvar = new Salvar(form, 20);

        final File selectedFile = new File("path/to/file.jaw");
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        salvar.run();

        assertTrue(salvar.vela);
        assertEquals("path/to/file.jaw", salvar.arquivo);
        assertNotNull(salvar.xml);
        assertFalse(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvarWithJawFilterAndNullFile() {
        final Salvar salvar = new Salvar(form, 20);

        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(null);

        salvar.run();

        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertNull(salvar.xml);
        assertFalse(salvar.entidade.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testSalvarWithXMLFileAndIOException() {
        final Salvar salvar = new Salvar(form, 10);

        final File selectedFile = new File("path/to/file.xml");
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        when(xmlFilter.accept(selectedFile)).thenReturn(true);

        final List<Entidade> entidades = new ArrayList<>();
        entidades.add(EntidadeFactory.createEntidade("Teste"));

        salvar.entidade = entidades;

        salvar.run();

        // This test should throw an IOException
    }

    @Test(expected = IOException.class)
    public void testSalvarWithJawFileAndIOException() {
        final Salvar salvar = new Salvar(form, 20);

        final File selectedFile = new File("path/to/file.jaw");
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        when(jawFilter.accept(selectedFile)).thenReturn(true);

        final List<Entidade> entidades = new ArrayList<>();
        entidades.add(EntidadeFactory.createEntidade("Teste"));

        salvar.entidade = entidades;

        salvar.run();

        // This test should throw an IOException
    }
}