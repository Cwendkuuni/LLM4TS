package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Frame;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.JFileChooser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SalvarTest {

    @Test
    public void testSalvarWithXMLFileFilterAndValidFileSelected() {
        // Arrange
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);

        JFileChooser fileChooser = mock(JFileChooser.class);
        File selectedFile = new File("path/to/valid/file.jaw.xml");
        when(fileChooser.showSaveDialog(Mockito.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        // Act
        salvar.Salvar(form);

        // Assert
        assertTrue(salvar.vela);
        assertEquals(selectedFile.getAbsolutePath(), salvar.arquivo);
        assertEquals(Salvar.SALVAR_XML, salvar.tipoSalvar);

        FileFilter fileFilter = salvar.getFileFilter();
        assertTrue(fileFilter instanceof Salvar.Filtro);
        assertEquals("JAW files", ((Salvar.Filtro) fileFilter).getDescription());

        Serializacao serializacao = Mockito.spy(new Serializacao());
        when(serializacao.salvar(entidades, salvar.arquivo)).thenReturn(true);
        salvar.salvar(form);
        verify(serializacao).salvar(entidades, salvar.arquivo);
        assertTrue(salvar.xml.isEmpty());
    }

    @Test
    public void testSalvarWithXMLFileFilterAndEmptyName() {
        // Arrange
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("");

        // Act
        salvar.Salvar(form);

        // Assert
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertEquals(Salvar.SALVAR_XML, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarWithJAWFileFilterAndValidFileSelected() {
        // Arrange
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);

        JFileChooser fileChooser = mock(JFileChooser.class);
        File selectedFile = new File("path/to/valid/file.jaw");
        when(fileChooser.showSaveDialog(Mockito.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        // Act
        salvar.Salvar(form);

        // Assert
        assertTrue(salvar.vela);
        assertEquals(selectedFile.getAbsolutePath(), salvar.arquivo);
        assertEquals(Salvar.SALVAR, salvar.tipoSalvar);

        FileFilter fileFilter = salvar.getFileFilter();
        assertTrue(fileFilter instanceof Salvar.Filtro);
        assertEquals("XML JAW files", ((Salvar.Filtro) fileFilter).getDescription());

        Serializacao serializacao = Mockito.spy(new Serializacao());
        when(serializacao.salvar(entidades, salvar.arquivo)).thenReturn(true);
        salvar.salvar(form);
        verify(serializacao).salvar(entidades, salvar.arquivo);
        assertTrue(salvar.xml.isEmpty());
    }

    @Test
    public void testSalvarWithJAWFileFilterAndEmptyName() {
        // Arrange
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("");

        // Act
        salvar.Salvar(form);

        // Assert
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertEquals(Salvar.SALVAR, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarWithJAWFileFilterAndInvalidFileSelected() {
        // Arrange
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);

        JFileChooser fileChooser = mock(JFileChooser.class);
        File selectedFile = new File("path/to/invalid/file");
        when(fileChooser.showSaveDialog(Mockito.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        // Act
        salvar.Salvar(form);

        // Assert
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertEquals(Salvar.SALVAR, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarWithXMLFileFilterAndValidFileSelectedForNewProject() {
        // Arrange
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getName()).thenReturn("Novo Projeto");

        JFileChooser fileChooser = mock(JFileChooser.class);
        File selectedFile = new File("path/to/valid/file.jaw.xml");
        when(fileChooser.showSaveDialog(Mockito.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        // Act
        salvar.Salvar(form);

        // Assert
        assertTrue(salvar.vela);
        assertEquals(selectedFile.getAbsolutePath(), salvar.arquivo);
        assertEquals(Salvar.SALVAR_XML, salvar.tipoSalvar);

        FileFilter fileFilter = salvar.getFileFilter();
        assertTrue(fileFilter instanceof Salvar.Filtro2);
        assertEquals("XML JAW files", ((Salvar.Filtro2) fileFilter).getDescription());

        SerializacaoXML serializacaoXML = Mockito.spy(new SerializacaoXML());
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>";
        when(serializacaoXML.entidades2XML(entidades)).thenReturn(xml);

        try {
            FileWriter fileWriter = mock(FileWriter.class);
            when(fileWriter.write(xml)).thenReturn(xml.length());
            salvar.salvarXML(xml, form);
            verify(fileWriter).write(xml);
            assertTrue(salvar.xml.equals(xml));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testSalvarWithXMLFileFilterAndEmptyNameForNewProject() {
        // Arrange
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getName()).thenReturn("Novo Projeto");

        // Act
        salvar.Salvar(form);

        // Assert
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertEquals(Salvar.SALVAR_XML, salvar.tipoSalvar);
        assertNull(salvar.xml);
    }
}