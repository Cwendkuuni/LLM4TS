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
import jaw.util.*;

public class SalvarTest {

    private Salvar salvar;
    private ProcessarEntidades form;
    private JFileChooser fileChooser;
    private Filtro filtroXML;
    private Filtro filtroJAW;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 20);
        fileChooser = mock(JFileChooser.class);
        when(form.getEntidades()).thenReturn(new ArrayList<>());
        when(form.getName()).thenReturn("");
        when(form.getNomeArquivoSalvo()).thenReturn("");
        when(form.getJanelaPrincipal()).thenReturn(mock(JanelaPrincipal.class));
        when(form.getLog()).thenReturn(mock(Log.class));
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        filtroXML = new Filtro2();
        filtroJAW = new Filtro();
        when(fileChooser.getFileFilter()).thenReturn(filtroJAW);
        doReturn(filtroXML).when(fileChooser).getFileFilter( eq(2) );
    }

    @Test
    public void testSalvarXML() {
        final String expectedArquivo = "teste.jaw.xml";
        doReturn(new File(expectedArquivo)).when(fileChooser).getSelectedFile();
        salvar = new Salvar(form, 10);
        salvar.Salvar();
        assertEquals(expectedArquivo, salvar.arquivo);
        verify(form).setNomeArquivoSalvo(expectedArquivo);
        verify(fileChooser).showSaveDialog(form);
    }

    @Test
    public void testSalvarJAW() {
        final String expectedArquivo = "teste.jaw";
        doReturn(new File(expectedArquivo)).when(fileChooser).getSelectedFile();
        salvar = new Salvar(form, 20);
        salvar.Salvar();
        assertEquals(expectedArquivo, salvar.arquivo);
        verify(form).setNomeArquivoSalvo(expectedArquivo);
        verify(fileChooser).showSaveDialog(form);
    }

    @Test
    public void testSalvarJAWComNomeArquivoDefault() {
        final String expectedArquivo = "teste.jaw";
        salvar = new Salvar(form, 30);
        salvar.Salvar();
        assertEquals(expectedArquivo, salvar.arquivo);
        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(fileChooser, never()).showSaveDialog(form);
    }

    @Test
    public void testNaoSalvarSemArquivo() {
        when(fileChooser.getSelectedFile()).thenReturn(null);
        salvar = new Salvar(form, 20);
        salvar.Salvar();
        assertNull(salvar.arquivo);
    }

    @Test
    public void testNaoSalvarSemEntidade() {
        when(form.getEntidades()).thenThrow(new Exception());
        salvar = new Salvar(form, 20);
        salvar.Salvar();
        assertNull(salvar.arquivo);
    }

    @Test(expected = Exception.class)
    public void testErroAoSalvar() {
        final Serializacao serializacao = mock(Serializacao.class);
        when(serializacao.salvar(anyList(), anyString())).thenThrow(new Exception());
        salvar = new Salvar(form, 20);
        salvar.Salvar();
    }

    @Test
    public void testSalvarXMLComArquivoNaoValido() {
        final String expectedArquivo = "teste.jaw.xml";
        doReturn(new File(expectedArquivo)).when(fileChooser).getSelectedFile();
        salvar = new Salvar(form, 10);
        salvar.Salvar();
        assertEquals(expectedArquivo, salvar.arquivo);
        verify(fileChooser).showSaveDialog(form);
        verify(form).setNomeArquivoSalvo(expectedArquivo);

        final String expectedXML = "<jaw>";
        when(salvar.entidade.get(0).toString()).thenReturn(expectedXML);
        final SerializacaoXML serializacaoXML = new SerializacaoXML();
        doReturn(expectedXML).when(salvar).xml;

        try {
            final FileWriter escrevedor = salvar.saveXML();
            fail();
        } catch (Exception e) {
            // expected
        }
        verify(fileChooser, never()).setCurrentDirectory(any(File.class));
    }
}