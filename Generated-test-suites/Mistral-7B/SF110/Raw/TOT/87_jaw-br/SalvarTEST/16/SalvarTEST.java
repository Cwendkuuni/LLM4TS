package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.entrada.Salvar;
import jaw.entrada.Salvar.Filtro;
import jaw.entrada.Salvar.Filtro2;
import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private List<Entidade> entidades;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        entidades = new ArrayList<>();
        salvar = new Salvar(form, Salvar.SALVAR_XML);
    }

    @Test
    public void testSaveXmlWithValidFile() {
        Entidade entidade = new Entidade();
        entidades.add(entidade);

        doReturn(entidades).when(form).getEntidades();
        Filtro2 xmlFilter = new Salvar.Filtro2();

        JFileChooser chooser = mock(JFileChooser.class);
        when(chooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw.xml"));

        salvar.setVel(true);
        salvar.escolherArquivo(chooser);

        SerializacaoXML serializacaoXML = new SerializacaoXML();
        String expectedXml = serializacaoXML.entidades2XML(entidades);

        FileWriter escrevedor = mock(FileWriter.class);

        try {
            salvar.salvar(entidades, "path/to/save/file.jaw.xml");
            verify(escrevedor).write(expectedXml);
            verify(escrevedor).flush();
            verify(escrevedor).close();
        } catch (IOException e) {
            fail("Test should not throw an IOException");
        }
    }

    @Test
    public void testSaveXmlWithInvalidFileExtension() {
        Entidade entidade = new Entidade();
        entidades.add(entidade);

        doReturn(entidades).when(form).getEntidades();
        Filtro xmlFilter = new Salvar.Filtro();

        JFileChooser chooser = mock(JFileChooser.class);
        when(chooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(new File("path/to/save/file.txt"));

        salvar.setVel(true);
        salvar.escolherArquivo(chooser);

        assertFalse("Expected Salvar to not save the file when it has an invalid extension", salvar.salvar(entidades, "path/to/save/file.txt"));
    }

    @Test
    public void testSaveXmlWithNoFileChosen() {
        Entidade entidade = new Entidade();
        entidades.add(entidade);

        doReturn(entidades).when(form).getEntidades();
        Filtro xmlFilter = new Salvar.Filtro2();

        JFileChooser chooser = mock(JFileChooser.class);
        when(chooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

        salvar.setVel(true);
        salvar.escolherArquivo(chooser);

        assertFalse("Expected Salvar to not save the file when no file is chosen", salvar.salvar(entidades, ""));
    }

    @Test
    public void testSaveJawWithValidFile() {
        Entidade entidade = new Entidade();
        entidades.add(entidade);

        doReturn(entidades).when(form).getEntidades();
        Filtro jawFilter = new Salvar.Filtro();

        JFileChooser chooser = mock(JFileChooser.class);
        when(chooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(new File("path/to/save/file.jaw"));

        salvar.setVel(true);
        salvar.escolherArquivo(chooser);

        try {
            Serializacao serializacao = new Serializacao();
            serializacao.salvar(entidades, "path/to/save/file.jaw");
            assertTrue("Expected Salvar to call Serializacao.salvar when saving a .jaw file", salvar.salvar(entidades, "path/to/save/file.jaw"));
        } catch (IOException e) {
            fail("Test should not throw an IOException");
        }
    }

    @Test
    public void testSaveJawWithInvalidFileExtension() {
        Entidade entidade = new Entidade();
        entidades.add(entidade);

        doReturn(entidades).when(form).getEntidades();
        Filtro jawFilter = new Salvar.Filtro();

        JFileChooser chooser = mock(JFileChooser.class);
        when(chooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(new File("path/to/save/file.txt"));

        salvar.setVel(true);
        salvar.escolherArquivo(chooser);

        assertFalse("Expected Salvar to not save the file when it has an invalid extension", salvar.salvar(entidades, "path/to/save/file.txt"));
    }

    @Test
    public void testSaveJawWithNoFileChosen() {
        Entidade entidade = new Entidade();
        entidades.add(entidade);

        doReturn(entidades).when(form).getEntidades();
        Filtro jawFilter = new Salvar.Filtro();

        JFileChooser chooser = mock(JFileChooser.class);
        when(chooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

        salvar.setVel(true);
        salvar.escolherArquivo(chooser);

        assertFalse("Expected Salvar to not save the file when no file is chosen", salvar.salvar(entidades, ""));
    }
}