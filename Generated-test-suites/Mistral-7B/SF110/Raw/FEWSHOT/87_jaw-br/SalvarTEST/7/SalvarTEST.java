package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Metamodelo;

public class SalvarTest {

    @Test
    public void testSalvarWithXMLFileFilter() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        File selectedFile = new File("test.jaw.xml");
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        doAnswer(invocation -> {
            FileWriter writer = (FileWriter) invocation.getArguments()[0];
            writer.write("XML data");
            return null;
        }).doNothing().when(salvar).salvar(anyList(), eq(selectedFile.getAbsolutePath()));

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).getSelectedFile();
        verify(salvar, times(1)).salvar(eq(new ArrayList<Entidade>()), eq(selectedFile.getAbsolutePath()));
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithJAWFileFilter() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        File selectedFile = new File("test.jaw");
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        doAnswer(invocation -> {
            FileWriter writer = (FileWriter) invocation.getArguments()[0];
            writer.write("JAW data");
            return null;
        }).doNothing().when(salvar).salvar(anyList(), eq(selectedFile.getAbsolutePath()));

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).getSelectedFile();
        verify(salvar, times(1)).salvar(eq(new ArrayList<Entidade>()), eq(selectedFile.getAbsolutePath()));
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithNullFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).getSelectedFile();
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithEmptyFileName() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getName()).thenReturn("");

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).getName();
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithInvalidFileFormat() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        File selectedFile = new File("test.txt");
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).getSelectedFile();
        verify(salvar, times(0)).salvar(anyList(), eq(selectedFile.getAbsolutePath()));
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithSuccess() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        File selectedFile = new File("test.jaw");
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        doAnswer(invocation -> {
            FileWriter writer = (FileWriter) invocation.getArguments()[0];
            writer.write("JAW data");
            return null;
        }).doNothing().when(salvar).salvar(anyList(), eq(selectedFile.getAbsolutePath()));

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).getSelectedFile();
        verify(salvar, times(1)).salvar(eq(new ArrayList<Entidade>()), eq(selectedFile.getAbsolutePath()));
        verify(JOptionPane.class, times(1)).showMessageDialog(any(Component.class), eq("Arquivo salvo com sucesso!"), eq("Jaw - Salvar"), eq(1));
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithXMLFileFilterAndEmptyFileName() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getName()).thenReturn("");

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).getName();
        verify(salvar, times(0)).salvar(anyList(), anyString());
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithXMLFileFilterAndInvalidFileFormat() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        File selectedFile = new File("test.txt");
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).getSelectedFile();
        verify(salvar, times(0)).salvar(anyList(), eq(selectedFile.getAbsolutePath()));
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarWithXMLFileFilterAndSuccess() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        File selectedFile = new File("test.jaw.xml");
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        doAnswer(invocation -> {
            FileWriter writer = (FileWriter) invocation.getArguments()[0];
            writer.write("XML data");
            return null;
        }).doNothing().when(salvar).salvarXML(anyList(), eq(selectedFile.getAbsolutePath()));

        salvar.salvar(form);

        verify(form, times(1)).getEntidades();
        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(fileChooser, times(1)).getSelectedFile();
        verify(salvar, times(1)).salvarXML(eq(new ArrayList<Entidade>()), eq(selectedFile.getAbsolutePath()));
        verify(JOptionPane.class, times(1)).showMessageDialog(any(Component.class), eq("Arquivo Exportado com sucesso!"), eq("Jaw - Salvar"), eq(1));
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(fileChooser);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarXMLWithNullFile() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        salvar.salvarXML(form.getEntidades(), null);

        verify(form, times(1)).getEntidades();
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testSalvarXMLWithSuccess() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<Entidade>());
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        doAnswer(invocation -> {
            FileWriter writer = (FileWriter) invocation.getArguments()[0];
            writer.write("XML data");
            return null;
        }).doNothing().when(salvar).salvarXML(anyList(), anyString());

        salvar.salvarXML(form.getEntidades(), new File("test.jaw.xml").getAbsolutePath());

        verify(form, times(1)).getEntidades();
        verify(salvar, times(1)).salvarXML(eq(new ArrayList<Entidade>()), anyString());
        verify(JOptionPane.class, times(1)).showMessageDialog(any(Component.class), eq("Arquivo Exportado com sucesso!"), eq("Jaw - Salvar"), eq(1));
        verifyNoMoreInteractions(form);
        verifyNoMoreInteractions(salvar);
    }

    @Test
    public void testFiltro2() {
        File filterFile = new File("test.jaw.xml");
        FileFilter filtro = new Salvar().new Filtro2();

        assertTrue(filtro.accept(filterFile));

        File filterDir = new File("test");
        assertTrue(filtro.accept(filterDir));
    }

    @Test
    public void testFiltro() {
        File filterFile = new File("test.jaw");
        FileFilter filtro = new Salvar().new Filtro();

        assertTrue(filtro.accept(filterFile));

        File filterDir = new File("test");
        assertTrue(filtro.accept(filterDir));
    }
}