package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.*;
import jaw.metamodelo.*;
import jaw.*;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.form = mock(ProcessarEntidades.class);
        this.salvar = new Salvar(this.form, Salvar.SALVAR);
    }

    @Test
    public void testSalvarWhenUserClicksSaveButtonAndFileIsSelected() throws Exception {
        final File file = new File("test.jaw");
        when(this.form.getEntidades()).thenReturn(Collections.singletonList(new Entidade()));
        when(this.form.getName()).thenReturn("");
        when(this.form.getNomeArquivoSalvo()).thenReturn("");

        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showSaveDialog(this.form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(file);

        this.salvar.vela = false;
        this.salvar.arquivo = "";

        this.salvar.Salvar();

        verify(this.form).setNomeArquivoSalvo("test.jaw");
        verify(this.salvar).arquivo = "test.jaw";
        verify(new Serializacao()).salvar(eq(this.salvar.entidade), eq(file));
        verify(this.form).showMessageDialog(eq(this.form), eq("Arquivo salvo com sucesso!"), eq("Jaw - Salvar"), eq(JOptionPane.INFORMATION_MESSAGE));
    }

    @Test
    public void testSalvarWhenUserClicksSaveButtonAndNoFileIsSelected() {
        when(this.form.getEntidades()).thenReturn(Collections.singletonList(new Entidade()));
        when(this.form.getName()).thenReturn("");
        when(this.form.getNomeArquivoSalvo()).thenReturn("");

        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showSaveDialog(this.form)).thenReturn(JFileChooser.CANCEL_OPTION);

        this.salvar.vela = false;
        this.salvar.arquivo = "";

        this.salvar.Salvar();

        verify(this.form, never()).setNomeArquivoSalvo(anyString());
        verify(this.salvar, never()).arquivo = anyString();
        verify(this.form, never()).showMessageDialog(eq(this.form), anyString(), anyString(), eq(JOptionPane.INFORMATION_MESSAGE));
    }

    @Test
    public void testSalvarWhenUserClicksSaveButtonAndFileHasInvalidExtension() throws Exception {
        final File file = new File("test.txt");
        when(this.form.getEntidades()).thenReturn(Collections.singletonList(new Entidade()));
        when(this.form.getName()).thenReturn("");
        when(this.form.getNomeArquivoSalvo()).thenReturn("");

        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showSaveDialog(this.form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(file);

        this.salvar.vela = false;
        this.salvar.arquivo = "";

        this.salvar.Salvar();

        verify(this.form).setNomeArquivoSalvo("test.jaw");
        verify(this.salvar).arquivo = "test.jaw";
        verify(this.form).showMessageDialog(eq(this.form), eq("Arquivo salvo com sucesso!"), eq("Jaw - Salvar"), eq(JOptionPane.INFORMATION_MESSAGE));
        verify(file).renameTo(new File("test.jaw"));
    }

    @Test
    public void testSalvarXMLWhenUserClicksSaveButtonAndFileIsSelected() throws Exception {
        final File file = new File("test.jaw.xml");
        when(this.form.getEntidades()).thenReturn(Collections.singletonList(new Entidade()));
        when(this.form.getName()).thenReturn("");
        when(this.form.getNomeArquivoSalvo()).thenReturn("");

        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showSaveDialog(this.form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(file);

        this.salvar.vela = false;
        this.salvar.arquivo = "";

        final String xml = "<entidades>";
        this.salvar.xml = xml;

        this.salvar.SalvarXML();

        verify(new FileWriter(file)).write(xml);
        verify(this.form).showMessageDialog(eq(this.form), eq("Arquivo Exportado com sucesso!"), eq("Jaw - Salvar"), eq(JOptionPane.INFORMATION_MESSAGE));
    }

    @Test
    public void testSalvarXMLWhenUserClicksSaveButtonAndNoFileIsSelected() {
        when(this.form.getEntidades()).thenReturn(Collections.singletonList(new Entidade()));
        when(this.form.getName()).thenReturn("");
        when(this.form.getNomeArquivoSalvo()).thenReturn("");

        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showSaveDialog(this.form)).thenReturn(JFileChooser.CANCEL_OPTION);

        this.salvar.vela = false;
        this.salvar.arquivo = "";

        this.salvar.SalvarXML();

        verify(this.form, never()).showMessageDialog(eq(this.form), anyString(), anyString(), eq(JOptionPane.INFORMATION_MESSAGE));
    }
}