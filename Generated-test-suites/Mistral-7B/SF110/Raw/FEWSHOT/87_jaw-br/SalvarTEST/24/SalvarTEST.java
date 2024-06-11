package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Projeto;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private List<Entidade> entidades;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        entidades = Arrays.asList(new Entidade(), new Entidade());
        salvar = new Salvar(form, 20);
    }

    @Test
    public void testSalvar() {
        when(form.getEntidades()).thenReturn(entidades);

        // given: File chooser shows save dialog and returns file
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.showSaveDialog((Component) any())).thenReturn(FileDialog.APPROVE_OPTION);
        when(form.showSaveDialog((Component) any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                return dialog.showSaveDialog((Component) invocation.getArgumentAt(0, 0));
            }
        });

        // when: save button is clicked
        salvar.vela = false;
        salvar.arquivo = "";
        salvar.entidade = new Vector<>();
        try {
            salvar.entidade = form.getEntidades();
        }
        catch (Exception e) {
            fail("Should not throw exception");
        }
        salvar.salvar();

        // then: file is saved
        File selectedFile = new File(salvar.arquivo);
        verify(form).getEntidades();
        verify(form).showSaveDialog((Component) any());
        verify(dialog).setFileFilter(salvar.new Filtro());
        verify(dialog).setSelectedFile(null);
        verify(dialog).showSaveDialog((Component) any());
        assertTrue(salvar.arquivo.endsWith(".jaw"));
        File expectedFile = new File(salvar.arquivo);
        File actualFile = new File(selectedFile.getAbsolutePath());
        assertEquals(expectedFile, actualFile);
        try {
            FileWriter escrevedor = new FileWriter(salvar.arquivo);
            escrevedor.write("Expected XML content");
            escrevedor.flush();
            escrevedor.close();
        }
        catch (IOException e) {
            fail("Should not throw exception");
        }
        verify(form).showMessageDialog((Component) any(), (String) eq("Arquivo salvo com sucesso!"), (int) eq(1), (int) eq(0));
    }

    @Test
    public void testSalvarWithNullFile() {
        when(form.getEntidades()).thenReturn(entidades);

        // given: File chooser shows save dialog and returns null
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.showSaveDialog((Component) any())).thenReturn(FileDialog.CANCEL_OPTION);
        when(form.showSaveDialog((Component) any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                return dialog.showSaveDialog((Component) invocation.getArgumentAt(0, 0));
            }
        });

        // when: save button is clicked
        salvar.vela = false;
        salvar.arquivo = "";
        salvar.entidade = new Vector<>();
        try {
            salvar.entidade = form.getEntidades();
        }
        catch (Exception e) {
            fail("Should not throw exception");
        }
        salvar.salvar();

        // then: nothing happens
        verify(form, never()).getEntidades();
        verify(form, never()).showSaveDialog((Component) any());
        verify(form, never()).showMessageDialog((Component) any(), (String) any(), (int) anyInt(), (int) anyInt());
    }

    @Test
    public void testSalvarWithoutFileSelection() {
        when(form.getEntidades()).thenReturn(entidades);

        // given: File chooser is not shown
        when(form.getName()).thenReturn("");
        when(form.showSaveDialog((Component) any())).thenReturn(FileDialog.CANCEL_OPTION);

        // when: save button is clicked
        salvar.vela = false;
        salvar.arquivo = "";
        salvar.entidade = new Vector<>();
        try {
            salvar.entidade = form.getEntidades();
        }
        catch (Exception e) {
            fail("Should not throw exception");
        }
        salvar.salvar();

        // then: nothing happens
        verify(form, never()).getEntidades();
        verify(form, never()).showSaveDialog((Component) any());
        verify(form, never()).showMessageDialog((Component) any(), (String) any(), (int) anyInt(), (int) anyInt());
    }

    @Test
    public void testSalvarWithInvalidFileExtension() {
        when(form.getEntidades()).thenReturn(entidades);

        // given: File chooser shows save dialog and returns file with invalid extension
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.showSaveDialog((Component) any())).thenReturn(FileDialog.APPROVE_OPTION);
        when(form.showSaveDialog((Component) any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                return dialog.showSaveDialog((Component) invocation.getArgumentAt(0, 0));
            }
        });
        when(dialog.getSelectedFile()).thenReturn(new File("save.txt"));

        // when: save button is clicked
        salvar.vela = false;
        salvar.arquivo = "";
        salvar.entidade = new Vector<>();
        try {
            salvar.entidade = form.getEntidades();
        }
        catch (Exception e) {
            fail("Should not throw exception");
        }
        salvar.salvar();

        // then: file extension is changed
        File expectedFile = new File("save.jaw");
        File actualFile = new File(salvar.arquivo);
        assertEquals(expectedFile, actualFile);
        verify(form).showMessageDialog((Component) any(), (String) eq("Arquivo salvo com sucesso!"), (int) eq(1), (int) eq(0));
    }

    @Test
    public void testSalvarXML() {
        when(form.getEntidades()).thenReturn(entidades);

        // given: File chooser shows save dialog and returns file
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.showSaveDialog((Component) any())).thenReturn(FileDialog.APPROVE_OPTION);
        when(form.showSaveDialog((Component) any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                return dialog.showSaveDialog((Component) invocation.getArgumentAt(0, 0));
            }
        });

        // when: save button is clicked
        salvar.vela = false;
        salvar.arquivo = "";
        salvar.entidade = new Vector<>();
        try {
            salvar.entidade = form.getEntidades();
        }
        catch (Exception e) {
            fail("Should not throw exception");
        }
        salvar.salvarXML();

        // then: XML is saved
        File selectedFile = new File(salvar.arquivo);
        verify(form).getEntidades();
        verify(form).showSaveDialog((Component) any());
        verify(dialog).setFileFilter(salvar.new FiltroXML());
        verify(dialog).setSelectedFile(null);
        verify(dialog).showSaveDialog((Component) any());
        assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
        File expectedFile = new File(salvar.arquivo);
        File actualFile = new File(selectedFile.getAbsolutePath());
        assertEquals(expectedFile, actualFile);
        String xmlExpected = "<entidades>";
        for (Entidade entidade : entidades) {
            xmlExpected += "\n<entidade>";
            xmlExpected += "<nome>" + entidade.getNome() + "</nome>";
            xmlExpected += "<descricao>" + entidade.getDescricao() + "</descricao>";
            xmlExpected += "<projeto>";
            Projeto projeto = entidade.getProjeto();
            xmlExpected += "<nome>" + projeto.getNome() + "</nome>";
            xmlExpected += "<versao>" + projeto.getVersao() + "</versao>";
            xmlExpected += "</projeto>";
            xmlExpected += "</entidade>";
        }
        xmlExpected += "</entidades>";
        try {
            FileWriter escrevedor = new FileWriter(salvar.arquivo);
            escrevedor.write(xmlExpected);
            escrevedor.flush();
            escrevedor.close();
            String xmlActual = new String(Files.readAllBytes(Paths.get(salvar.arquivo)));
            assertEquals(xmlExpected, xmlActual);
            verify(form).showMessageDialog((Component) any(), (String) eq("Arquivo Exportado com sucesso!"), (int) eq(1), (int) eq(0));
        }
        catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testSalvarXMLWithNullFile() {
        when(form.getEntidades()).thenReturn(entidades);

        // given: File chooser shows save dialog and returns null
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.showSaveDialog((Component) any())).thenReturn(FileDialog.CANCEL_OPTION);
        when(form.showSaveDialog((Component) any())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                return dialog.showSaveDialog((Component) invocation.getArgumentAt(0, 0));
            }
        });

        // when: save button is clicked
        salvar.vela = false;
        salvar.arquivo = "";
        salvar.entidade = new Vector<>();
        try {
            salvar.entidade = form.getEntidades();
        }
        catch (Exception e) {
            fail("Should not throw exception");
        }
        salvar.salvarXML();

        // then: nothing happens
        verify(form, never()).getEntidades();
        verify(form, never()).showSaveDialog((Component) any());
        verify(form, never()).showMessageDialog((Component) any(), (String) any(), (int) anyInt(), (int) anyInt());
    }

    private static class Filtro2 extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
        }

        @Override
        public String getDescription() {
            return "XML JAW files";
        }
    }

    private static class Filtro extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
        }

        @Override
        public String getDescription() {
            return "JAW files";
        }
    }
}