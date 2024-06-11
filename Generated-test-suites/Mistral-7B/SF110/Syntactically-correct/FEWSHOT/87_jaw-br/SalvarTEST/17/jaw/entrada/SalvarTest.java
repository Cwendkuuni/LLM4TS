package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.*;
import jaw.util.Serializacao;
import jaw.util.SerializacaoXML;

import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;
    private Entidade entidade1, entidade2;
    private List<Entidade> entidades;

    @Before
    public void setup() {
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 20);
        entidade1 = new Entidade("Entidade1", "Tipo1");
        entidade2 = new Entidade("Entidade2", "Tipo2");
        entidades = Arrays.asList(entidade1, entidade2);
    }

    @Test
    public void testSalvarJawFile() {
        when(form.getEntidades()).thenReturn(entidades);
        File selectedFile = new File("path/to/save/file.jaw");
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.showSaveDialog(eq(form), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.Salvar(fileChooser);

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).showSaveDialog(eq(form), anyInt());
        verify(fileChooser, times(1)).getSelectedFile();
        assertTrue(salvar.vela);
        assertEquals("path/to/save/file.jaw", salvar.arquivo);
        try {
            Serializacao serializacao = new Serializacao();
            serializacao.salvar(entidades, salvar.arquivo);
            File file = new File(salvar.arquivo);
            assertTrue(file.exists());
            file.delete();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSalvarJawFileIfAlreadyHasJawExtension() {
        when(form.getEntidades()).thenReturn(entidades);
        File selectedFile = new File("path/to/save/file.jaw");
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.showSaveDialog(eq(form), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.Salvar(fileChooser);

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).showSaveDialog(eq(form), anyInt());
        verify(fileChooser, times(1)).getSelectedFile();
        assertTrue(salvar.vela);
        assertEquals("path/to/save/file.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarXmlFile() {
        when(form.getEntidades()).thenReturn(entidades);
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>Entidade1</nome>\n<tipo>Tipo1</tipo>\n</entidade>\n<entidade>\n<nome>Entidade2</nome>\n<tipo>Tipo2</tipo>\n</entidade>\n</entidades>";
        SerializacaoXML serializacaoXML = new SerializacaoXML();

        salvar.xml = serializacaoXML.entidades2XML(entidades);
        File selectedFile = new File("path/to/save/file.jaw.xml");

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.showSaveDialog(eq(form), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.SalvarXml(fileChooser);

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).showSaveDialog(eq(form), anyInt());
        verify(fileChooser, times(1)).getSelectedFile();
        assertTrue(salvar.vela);
        assertEquals("path/to/save/file.jaw.xml", salvar.arquivo);
        try {
            FileWriter fileWriter = new FileWriter(salvar.arquivo);
            fileWriter.write(salvar.xml);
            fileWriter.flush();
            fileWriter.close();
            File file = new File(salvar.arquivo);
            assertTrue(file.exists());
            file.delete();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSalvarXmlFileIfAlreadyHasXmlExtension() {
        when(form.getEntidades()).thenReturn(entidades);
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>Entidade1</nome>\n<tipo>Tipo1</tipo>\n</entidade>\n<entidade>\n<nome>Entidade2</nome>\n<tipo>Tipo2</tipo>\n</entidade>\n</entidades>";
        SerializacaoXML serializacaoXML = new SerializacaoXML();

        salvar.xml = serializacaoXML.entidades2XML(entidades);
        File selectedFile = new File("path/to/save/file.jaw.xml");

        JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.showSaveDialog(eq(form), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);

        salvar.SalvarXml(fileChooser);

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).showSaveDialog(eq(form), anyInt());
        verify(fileChooser, times(1)).getSelectedFile();
        assertTrue(salvar.vela);
        assertEquals("path/to/save/file.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarWithoutSelectedFile() {
        when(form.getEntidades()).thenReturn(entidades);
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.showSaveDialog(eq(form), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        Salvar salvar = new Salvar(form, 20);
        salvar.Salvar(fileChooser);

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).showSaveDialog(eq(form), anyInt());
        verify(fileChooser, times(1)).getSelectedFile();
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarWithEmptyFileName() {
        when(form.getEntidades()).thenReturn(entidades);
        Frame frame = mock(Frame.class);
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        when(form.getRootPane()).thenReturn(frame);
        when(form.showSaveDialog(eq(form), anyInt())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(null);

        Salvar salvar = new Salvar(form, 20);
        salvar.Salvar(fileChooser);

        verify(form, times(1)).getEntidades();
        verify(form, times(1)).getRootPane();
        verify(form, times(1)).showSaveDialog(eq(form), anyInt());
        verify(fileChooser, times(1)).getSelectedFile();
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }
}