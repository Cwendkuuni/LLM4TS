package jaw.entrada.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import jaw.entrada.Salvar;
import jaw.gui.ProcessarEntidades;

public class SalvarTest {
    
    @Test
    public void testSalvar() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        Salvar salvar = new Salvar(form, 20);
        assertNull(salvar.getArquivo());
        assertNull(salvar.getXml());
        assertFalse(salvar.isVela());
    }
    
    @Test
    public void testSalvarWithFileChooser() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw"));
        Salvar salvar = new Salvar(form, 20);
        salvar.setFileChooser(fileChooser);
        assertEquals("test.jaw", salvar.getArquivo());
        assertNull(salvar.getXml());
        assertFalse(salvar.isVela());
    }
    
    @Test
    public void testSalvarWithException() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        when(form.getEntidades()).thenThrow(new Exception("Error getting entidades"));
        Salvar salvar = new Salvar(form, 20);
        assertNull(salvar.getArquivo());
        assertNull(salvar.getXml());
        assertFalse(salvar.isVela());
    }
    
    @Test
    public void testSaveAsJawWithInvalidFileFormat() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test"));
        Salvar salvar = new Salvar(form, 20);
        salvar.setFileChooser(fileChooser);
        assertNull(salvar.getArquivo());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSaveWithNullFileChooser() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, 20);
        salvar.setFileChooser(null);
        salvar.save();
    }
    
    @Test(expected = IOException.class)
    public void testSaveWithIOException() throws IOException {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw"));
        Salvar salvar = new Salvar(form, 20);
        salvar.setFileChooser(fileChooser);
        FileWriter writer = mock(FileWriter.class);
        when(writer.write(anyString())).thenThrow(new IOException());
        when(salvar.save(writer)).thenReturn(false);
        assertNull(salvar.getArquivo());
        assertNull(salvar.getXml());
        assertFalse(salvar.isVela());
    }
}