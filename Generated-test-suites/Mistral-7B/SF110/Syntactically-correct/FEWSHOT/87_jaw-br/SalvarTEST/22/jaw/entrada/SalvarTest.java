package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import jaw.gui.*;
import jaw.metamodelo.*;
import jaw.util.*;

@RunWith(MockitoJUnitRunner.class)
public class SalvarTest {

    @Mock
    private ProcessarEntidades form;

    @Test
    public void testSalvarWhenTipoSalvarIs10AndValidFileNameIsSelectedThenFileIsSavedAsJawXml() {
        // Given
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getName()).thenReturn("");

        final Salvar salvar = new Salvar(form, 10);

        // When
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getFileChooser()).thenReturn(fileChooser);
        doReturn(fileChooser).when(salvar).getFileChooser();

        doReturn(new File("test.jaw.xml")).when(fileChooser).getSelectedFile();

        salvar.salvar();

        // Then
        assertEquals("test.jaw.xml", salvar.getArquivo());
        verify(fileChooser, times(1)).showSaveDialog(form);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndInvalidFileNameIsSelectedThenNoFileIsSaved() {
        // Given
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getName()).thenReturn("");

        final Salvar salvar = new Salvar(form, 10);

        // When
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getFileChooser()).thenReturn(fileChooser);
        doReturn(fileChooser).when(salvar).getFileChooser();

        doReturn(null).when(fileChooser).getSelectedFile();

        salvar.salvar();

        // Then
        assertNull(salvar.getArquivo());
        verify(fileChooser, times(1)).showSaveDialog(form);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs10AndNoNameIsProvidedThenNoFileIsSaved() {
        // Given
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getName()).thenReturn("");

        final Salvar salvar = new Salvar(form, 10);

        // When
        salvar.salvar();

        // Then
        assertNull(salvar.getArquivo());
    }

    @Test
    public void testSalvarWhenTipoSalvarIs20Or30AndValidFileNameIsSelectedThenFileIsSavedAsJaw() {
        // Given
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");

        final Salvar salvar = new Salvar(form, 20);

        // When
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getFileChooser()).thenReturn(fileChooser);
        doReturn(fileChooser).when(salvar).getFileChooser();

        doReturn(new File("test.jaw")).when(fileChooser).getSelectedFile();

        salvar.salvar();

        // Then
        assertEquals("test.jaw", salvar.getArquivo());
        verify(fileChooser, times(1)).showSaveDialog(form);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs20Or30AndInvalidFileNameIsSelectedThenNoFileIsSaved() {
        // Given
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");

        final Salvar salvar = new Salvar(form, 20);

        // When
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getFileChooser()).thenReturn(fileChooser);
        doReturn(fileChooser).when(salvar).getFileChooser();

        doReturn(null).when(fileChooser).getSelectedFile();

        salvar.salvar();

        // Then
        assertNull(salvar.getArquivo());
        verify(fileChooser, times(1)).showSaveDialog(form);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs30AndNoNameIsProvidedThenFileIsSavedAsNewJawFileInTheProjectDirectory() throws IOException {
        // Given
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getNomeArquivoSalvo()).thenReturn("");
        final String projectDirectory = "projectDirectory";
        when(Main.configuracao.getCaminhoDosProjetos()).thenReturn(projectDirectory);

        final Salvar salvar = new Salvar(form, 30);

        // When
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getFileChooser()).thenReturn(fileChooser);
        doReturn(fileChooser).when(salvar).getFileChooser();

        doReturn(new File(projectDirectory, "test.jaw")).when(fileChooser).getSelectedFile();

        salvar.salvar();

        // Then
        assertEquals("test.jaw", salvar.getArquivo());
        verify(fileChooser, times(1)).showSaveDialog(form);
    }

    @Test
    public void testSalvarWhenTipoSalvarIs30AndNoNameIsProvidedThenFileIsSavedAsNewJawFileInTheProjectDirectoryWithJawExtension() throws IOException {
        // Given
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getNomeArquivoSalvo()).thenReturn("");
        final String projectDirectory = "projectDirectory";
        when(Main.configuracao.getCaminhoDosProjetos()).thenReturn(projectDirectory);

        final Salvar salvar = new Salvar(form, 30);

        // When
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getFileChooser()).thenReturn(fileChooser);
        doReturn(fileChooser).when(salvar).getFileChooser();

        doReturn(new File(projectDirectory, "test.jaw.jaw")).when(fileChooser).getSelectedFile();

        salvar.salvar();

        // Then
        assertEquals("test.jaw.jaw", salvar.getArquivo());
        verify(fileChooser, times(1)).showSaveDialog(form);
    }

    @Test
    public void testSaveWhenValidEntidadesAreProvidedThenXmlFileIsSaved() throws IOException {
        // Given
        final Entidade entidade1 = new Entidade();
        final Entidade entidade2 = new Entidade();

        when(form.getEntidades()).thenReturn(Arrays.asList(entidade1, entidade2));

        final Salvar salvar = new Salvar(form, 10);

        // When
        final String xml = "<entidades>\n" +
                "  <entidade>\n" +
                "    <nome>Entidade 1</nome>\n" +
                "  </entidade>\n" +
                "  <entidade>\n" +
                "    <nome>Entidade 2</nome>\n" +
                "  </entidade>\n" +
                "</entidades>";

        doReturn(new File("test.jaw.xml")).when(salvar).getFile();

        final FileWriter writer = mock(FileWriter.class);
        doReturn(writer).when(salvar).getFileWriter();

        salvar.salvarXML(xml);

        // Then
        verify(writer).write(xml);
        verify(writer).flush();
        verify(writer).close();
    }

    @Test
    public void testSaveWhenInvalidEntidadesAreProvidedThenExceptionIsThrown() {
        // Given
        final Entidade entidade = new Entidade();

        when(form.getEntidades()).thenThrow(new Exception());

        final Salvar salvar = new Salvar(form, 10);

        // When & Then
        assertThrows(Exception.class, () -> salvar.salvarXML(""));
    }

    private File getFile() {
        return new File("temp/test.xml");
    }

    private FileWriter getFileWriter() throws IOException {
        return new FileWriter(getFile());
    }

    private Salvar getSalvar() {
        return new Salvar(form, 10);
    }

    private String getArquivo() {
        return getSalvar().getArquivo();
    }
}