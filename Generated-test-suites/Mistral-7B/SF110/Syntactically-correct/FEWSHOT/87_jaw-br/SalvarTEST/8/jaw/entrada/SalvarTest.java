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

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Metamodelo;

@RunWith(MockitoJUnitRunner.class)
public class SalvarTest {

    @Mock
    private ProcessarEntidades form;

    @InjectMocks
    private Salvar salvar;

    @Test
    public void testSalvarXMLWithValidFile() {
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getName()).thenReturn("");

        File expectedFile = new File("path/to/save/file.jaw.xml");
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

        salvar.Salvar(form, Salvar.SALVAR_XML);

        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw.xml");
        verify(form, times(1)).showMessageDialog(anyString(), anyString(), anyInt());
        try {
            FileWriter fileWriter = mock(FileWriter.class);
            when(form).getFileWriter = fileWriter;
            salvar.salvarXML(fileWriter, new ArrayList<>());
            verify(fileWriter, times(1)).write(anyString());
            verify(fileWriter, times(1)).flush();
            verify(fileWriter, times(1)).close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testSalvarXMLWithNullFile() {
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getName()).thenReturn("");

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        salvar.Salvar(form, Salvar.SALVAR_XML);

        verify(form, times(0)).setNomeArquivoSalvo(anyString());
        verify(form, times(0)).showMessageDialog(anyString(), anyString(), anyInt());
    }

    @Test
    public void testSalvarXMLWithEmptyName() {
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getName()).thenReturn("");

        salvar.Salvar(form, Salvar.SALVAR_XML);

        verify(form, times(0)).setNomeArquivoSalvo(anyString());
        verify(form, times(0)).showMessageDialog(anyString(), anyString(), anyInt());
    }

    @Test
    public void testSalvarXMLWithValidFileAndName() {
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getName()).thenReturn("testName");

        File expectedFile = new File("path/to/save/file.jaw.xml");
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

        salvar.Salvar(form, Salvar.SALVAR_XML);

        verify(form, times(1)).setNomeArquivoSalvo("testName.jaw.xml");
        verify(form, times(1)).showMessageDialog(anyString(), anyString(), anyInt());
        try {
            FileWriter fileWriter = mock(FileWriter.class);
            when(form).getFileWriter = fileWriter;
            salvar.salvarXML(fileWriter, new ArrayList<>());
            verify(fileWriter, times(1)).write(anyString());
            verify(fileWriter, times(1)).flush();
            verify(fileWriter, times(1)).close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testSalvarWithValidFile() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getName()).thenReturn("");

        File expectedFile = new File("path/to/save/file.jaw");
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

        salvar.Salvar(form, Salvar.SALVAR);

        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw");
        verify(form, times(1)).showMessageDialog(anyString(), anyString(), anyInt());
        try {
            Salvar.Serializacao serializacao = mock(Salvar.Serializacao.class);
            when(form.getSerializacao()).thenReturn(serializacao);
            serializacao.salvar(entidades, expectedFile.getAbsolutePath());
            verify(serializacao, times(1)).salvar(any(List.class), anyString());
            verify(form, times(1)).showMessageDialog(anyString(), anyString(), anyInt());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSalvarWithNullFile() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.showSaveDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getName()).thenReturn("");

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        salvar.Salvar(form, Salvar.SALVAR);

        verify(form, times(0)).setNomeArquivoSalvo(anyString());
        verify(form, times(0)).showMessageDialog(anyString(), anyString(), anyInt());
    }

    @Test
    public void testSalvarWithEmptyName() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.getName()).thenReturn("");

        salvar.Salvar(form, Salvar.SALVAR);

        verify(form, times(0)).setNomeArquivoSalvo(anyString());
        verify(form, times(0)).showMessageDialog(anyString(), anyString(), anyInt());
    }

    @Test
    public void testSalvarWithValidFileAndName() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.getName()).thenReturn("testName");

        File expectedFile = new File("path/to/save/file.jaw");
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

        Salvar.Serializacao serializacao = mock(Salvar.Serializacao.class);
        when(form.getSerializacao()).thenReturn(serializacao);

        salvar.Salvar(form, Salvar.SALVAR);

        verify(form, times(1)).setNomeArquivoSalvo("testName.jaw");
        verify(form, times(1)).showMessageDialog(anyString(), anyString(), anyInt());
        try {
            serializacao.salvar(entidades, expectedFile.getAbsolutePath());
            verify(serializacao, times(1)).salvar(any(List.class), anyString());
            verify(form, times(1)).showMessageDialog(anyString(), anyString(), anyInt());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSalvarComoWithValidFileAndName() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("");

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw"));

        salvar.Salvar(form, Salvar.SALVAR_COMO);

        verify(form, times(1)).setNomeArquivoSalvo("file.jaw");
        verify(form, times(1)).showSaveDialog(any(Frame.class));
        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw");
    }

    @Test
    public void testSalvarComoWithNullFile() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("");

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        salvar.Salvar(form, Salvar.SALVAR_COMO);

        verify(form, times(0)).setNomeArquivoSalvo(anyString());
        verify(form, times(0)).showMessageDialog(anyString(), anyString(), anyInt());
    }

    @Test
    public void testSalvarComoWithEmptyName() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("");

        salvar.Salvar(form, Salvar.SALVAR_COMO);

        verify(form, times(0)).setNomeArquivoSalvo(anyString());
        verify(form, times(0)).showMessageDialog(anyString(), anyString(), anyInt());
    }

    @Test
    public void testSalvarComoWithValidFileAndNameAndFileFilter() {
        List<Entidade> entidades = new ArrayList<>();
        entidades.add(mock(Entidade.class));

        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("Novo Projeto");

        JFileChooser fileChooser = mock(JFileChooser.class);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Jaw Files", "jaw");
        when(fileChooser.getFileFilter()).thenReturn(filter);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw"));

        salvar.Salvar(form, Salvar.SALVAR_COMO);

        verify(form, times(1)).setNomeArquivoSalvo("file.jaw");
        verify(form, times(1)).showSaveDialog(any(Frame.class));
        verify(form, times(1)).setNomeArquivoSalvo("path/to/save/file.jaw");
    }
}