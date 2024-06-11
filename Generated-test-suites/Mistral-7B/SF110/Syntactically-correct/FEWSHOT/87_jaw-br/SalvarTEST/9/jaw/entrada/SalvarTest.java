package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Metamodelo;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private List<Entidade> entidadeList;
    private JFileChooser fileChooser;
    private Filtro filtro;
    private Filtro2 filtro2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        entidadeList = new ArrayList<>();
        form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(entidadeList);
        salvar = new Salvar(form, 20);
        fileChooser = spy(new JFileChooser());
        filtro = new Filtro();
        filtro2 = new Filtro2();
    }

    @Test
    public void testSalvar_SaveAsJawFile() {
        File expectedFile = new File("path/to/save/test.jaw");
        fileChooser.showSaveDialog(form);
        verify(fileChooser).setFileFilter(filtro);
        assertEquals(JFileChooser.APPROVE_OPTION, fileChooser.showOptionDialog(form, "", JFileChooser.SAVE_DIALOG,
                JFileChooser.DEFAULT_OPTION, false));
        assertEquals(expectedFile, salvar.getArquivo());
        assertEquals(entidadeList, salvar.getEntidade());
    }

    @Test
    public void testSalvar_SaveAsJawFileWithInvalidExtension() {
        File expectedFile = new File("path/to/save/test.txt");
        fileChooser.showSaveDialog(form);
        verify(fileChooser).setFileFilter(filtro);
        assertEquals(JFileChooser.APPROVE_OPTION, fileChooser.showOptionDialog(form, "", JFileChooser.SAVE_DIALOG,
                JFileChooser.DEFAULT_OPTION, false));
        assertFalse(salvar.getArquivo().endsWith(".jaw"));
        assertEquals(entidadeList, salvar.getEntidade());
    }

    @Test
    public void testSalvar_SaveAsJawFileNoFileChosen() {
        fileChooser.setSelectedFile(null);
        fileChooser.showSaveDialog(form);
        verify(fileChooser).setFileFilter(filtro);
        assertNull(salvar.getArquivo());
        assertEquals(entidadeList, salvar.getEntidade());
    }

    @Test
    public void testSalvar_SaveAsXMLFile() {
        File expectedFile = new File("path/to/save/test.jaw.xml");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(expectedFile);
        when(form.getName()).thenReturn("");

        SwingUtilities.invokeLater(() -> salvar.Salvar(form, 10));

        verify(fileChooser).setFileFilter(filtro2);
        assertEquals(expectedFile, salvar.getArquivo());
        assertNull(salvar.getXml());
    }

    @Test
    public void testSalvar_SaveAsXMLFileWithNoFileChosen() {
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

        SwingUtilities.invokeLater(() -> salvar.Salvar(form, 10));

        assertNull(salvar.getArquivo());
        assertNull(salvar.getXml());
    }

    @Test
    public void testSalvar_SaveAsXMLFileWithInvalidExtension() {
        File expectedFile = new File("path/to/save/test.txt");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

        SwingUtilities.invokeLater(() -> salvar.Salvar(form, 10));

        assertFalse(salvatagedFile.getAbsolutePath().endsWith(".jaw.xml"));
        assertNull(salvar.getXml());
    }
}