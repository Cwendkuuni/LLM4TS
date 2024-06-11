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
import javax.swing.filechooser.FileFilterManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.metamodelo.MetamodeloPackage;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private FileDialog dialog;
    private JFileChooser fileChooser;
    private Entidade entidade1, entidade2;
    private List<Entidade> entidades;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        dialog = mock(FileDialog.class);
        fileChooser = mock(JFileChooser.class);
        entidade1 = MetamodeloFactory.eINSTANCE.createEntidade();
        entidade2 = MetamodeloFactory.eINSTANCE.createEntidade();
        entidades = new ArrayList<>();
        entidades.add(entidade1);
        entidades.add(entidade2);
        when(form.getEntidades()).thenReturn(entidades);
        salvar = new Salvar(form, 20);
    }

    @Test
    public void testSalvarJawFile() throws IOException {
        File selectedFile = new File("test.jaw");
        when(fileChooser.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.Salvar(form, 20);

        verify(form, times(1)).setNomeArquivoSalvo("test.jaw");
        verify(fileChooser, times(1)).setFileFilter(ArgumentMatchers.isA(Filtro.class));
        verify(fileChooser, times(1)).showSaveDialog(ArgumentMatchers.eq(form));
        verify(form, never()).setNomeArquivoSalvo(containsString(".jaw.xml"));
        verify(salvar, times(1)).arquivoValido(selectedFile);
        verify(salvar, times(1)).salvar(entidades, selectedFile.getAbsolutePath());
        verify(form, times(1)).getNomeArquivoSalvo();
        verify(form, times(1)).getEntidades();
    }

    @Test
    public void testSalvarXmlFile() throws IOException {
        File selectedFile = new File("test.jaw.xml");
        when(fileChooser.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.Salvar(form, 10);

        verify(form, times(1)).setNomeArquivoSalvo("test.jaw.xml");
        verify(fileChooser, times(1)).setFileFilter(ArgumentMatchers.isA(Filtro2.class));
        verify(fileChooser, times(1)).showSaveDialog(ArgumentMatchers.eq(form));
        verify(form, times(1)).getNomeArquivoSalvo();
        verify(form, times(1)).getEntidades();
        verify(salvar, times(1)).xmlValido(entidades);
        verify(salvar, times(1)).salvarXml(entidades, selectedFile.getAbsolutePath());
        verify(form, times(1)).getNomeArquivoSalvo();
        verify(form, times(1)).getEntidades();
    }

    @Test
    public void testSalvarCanceled() {
        when(fileChooser.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.CANCEL_OPTION);

        salvar.Salvar(form, 20);

        verify(form, never()).setNomeArquivoSalvo("test.jaw");
        verify(fileChooser, never()).setFileFilter(ArgumentMatchers.isA(Filtro.class));
        verify(fileChooser, never()).showSaveDialog(ArgumentMatchers.eq(form));
        verify(form, never()).getNomeArquivoSalvo();
        verify(form, never()).getEntidades();
    }

    @Test
    public void testSalvarNoFileSelected() {
        when(fileChooser.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.CANCEL_OPTION);
        when(form.getName()).thenReturn("");

        salvar.Salvar(form, 20);

        verify(form, never()).setNomeArquivoSalvo("test.jaw");
        verify(fileChooser, never()).setFileFilter(ArgumentMatchers.isA(Filtro.class));
        verify(fileChooser, never()).showSaveDialog(ArgumentMatchers.eq(form));
        verify(form, never()).getNomeArquivoSalvo();
        verify(form, never()).getEntidades();
    }

    @Test(expected = IllegalStateException.class)
    public void testSalvarNullEntidades() {
        salvar = new Salvar(form, 20);
        salvar.Salvar(form, 20);
    }
}