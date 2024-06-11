package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import jaw.gui.*;
import jaw.metamodelo.*;
import jaw.*;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, Salvar.SALVAR);
    }

    @Test
    public void testConstructor() {
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(Salvar.SALVAR, salvar.tipoSalvar);
    }

    @Test
    public void testGetTipoSalvar() {
        assertEquals(Salvar.SALVAR, salvar.getTipoSalvar());
    }

    @Test
    public void testSetTipoSalvar() {
        salvar.setTipoSalvar(Salvar.SALVAR_XML);
        assertEquals(Salvar.SALVAR_XML, salvar.getTipoSalvar());
    }

    @Test
    public void testGetEntidade() {
        List<Entidade> entidades = new ArrayList<>();
        when(form.getEntidades()).thenReturn(entidades);
        salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals(entidades, salvar.getEntidade());
    }

    @Test
    public void testGetVel() {
        assertFalse(salvar.getVel());
    }

    @Test
    public void testSetVel() {
        salvar.setVel(true);
        assertTrue(salvar.getVel());
    }

    @Test
    public void testGetArquivo() {
        assertEquals("", salvar.getArquivo());
    }

    @Test
    public void testSetArquivo() {
        salvar.setArquivo("test.jaw");
        assertEquals("test.jaw", salvar.getArquivo());
    }

    @Test
    public void testGetXml() {
        assertEquals("", salvar.getXml());
    }

    @Test
    public void testSetXml() {
        salvar.setXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>", salvar.getXml());
    }

    @Test
    public void testSelecionarArquivoNulo() {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        salvar = new Salvar(form, Salvar.SALVAR);
        salvar.escolherArquivo(escolhedor);
        verify(escolhedor, never()).showSaveDialog(form);
    }

    @Test
    public void testSelecionarArquivoValido() {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw"));
        salvar = new Salvar(form, Salvar.SALVAR);
        salvar.escolherArquivo(escolhedor);
        verify(escolhedor).showSaveDialog(form);
        verify(escolhedor).setFileFilter(argThat(isA(Filtro.class)));
    }

    @Test
    public void testSelecionarArquivoInvalido() {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        salvar.escolherArquivo(escolhedor);
        verify(escolhedor).showSaveDialog(form);
        verify(escolhedor).setFileFilter(argThat(isA(Filtro2.class)));
    }

    @Test
    public void testSalvarArquivoValido() throws IOException {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw"));
        Serializacao serializacao = mock(Serializacao.class);
        when(serializacao.salvar(eq(salvar.entidade), eq(salvar.arquivo))).thenReturn(true);
        salvar = new Salvar(form, Salvar.SALVAR);
        salvar.escolherArquivo(escolhedor);
        ArgumentCaptor<JFileChooser> escolhedorCaptor = ArgumentCaptor.forClass(JFileChooser.class);
        verify(form).setNomeArquivoSalvo(argThat(equalTo("test.jaw")));
        verify(escolhedor).showSaveDialog(form);
        verify(escolhedor).setFileFilter(argThat(isA(Filtro.class)));
        verify(serializacao).salvar(eq(salvar.entidade), eq(salvar.arquivo));
        verify(form).showMessageDialog(eq(form), eq("Arquivo salvo com sucesso!"), eq("Jaw - Salvar"), eq(1));
    }

    @Test
    public void testSalvarArquivoInvalido() throws IOException {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("test.jaw");
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw"));
        Serializacao serializacao = mock(Serializacao.class);
        when(serializacao.salvar(eq(salvar.entidade), eq(salvar.arquivo))).thenThrow(new IOException());
        salvar = new Salvar(form, Salvar.SALVAR);
        salvar.escolherArquivo(escolhedor);
        ArgumentCaptor<JFileChooser> escolhedorCaptor = ArgumentCaptor.forClass(JFileChooser.class);
        verify(form).setNomeArquivoSalvo(argThat(equalTo("test.jaw")));
        verify(escolhedor).showSaveDialog(form);
        verify(escolhedor).setFileFilter(argThat(isA(Filtro.class)));
        verify(form).showMessageDialog(eq(form), eq("Não foi possível salvar esse arquivo\n Verifique o formato das entidades !"), eq("Salvando Arquivo"), eq(0));
        verify(Main.janelaPrincipal).log(argThat(containsString("Não foi possível salvar esse arquivo\n Verifique o formato das entidades !")));
    }

    @Test
    public void testExportarArquivoValido() throws IOException {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw.xml"));
        SerializacaoXML serializacaoXML = mock(SerializacaoXML.class);
        ArgumentCaptor<List<Entidade>> entidadesCaptor = ArgumentCaptor.forClass(List.class);
        when(form.getEntidades()).thenReturn(entidadesCaptor.capture());
        when(serializacaoXML.entidades2XML(entidadesCaptor.getValue())).thenReturn("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>");
        FileWriter escrevedor = mock(FileWriter.class);
        when(escrevedor.write(argThat(startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?"))))
                .thenThrow(new IOException());
        when(escrevedor.write(eq("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>"))).thenReturn(0);
        when(escrevedor.flush()).thenReturn(0);
        when(escrevedor.close()).thenReturn(0);
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        salvar.escolherArquivo(escolhedor);
        ArgumentCaptor<JFileChooser> escolhedorCaptor = ArgumentCaptor.forClass(JFileChooser.class);
        verify(form).setNomeArquivoSalvo(argThat(equalTo("test.jaw.xml")));
        verify(escolhedor).showSaveDialog(form);
        verify(escolhedor).setFileFilter(argThat(isA(Filtro2.class)));
        verify(form.getEntidades()).thenReturn(entidadesCaptor.getValue());
        verify(serializacaoXML).entidades2XML(entidadesCaptor.getValue());
        verify(escrevedor).write(argThat(startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?")));
        verify(escrevedor).write(eq("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>"));
        verify(escrevedor).flush();
        verify(escrevedor).close();
        verify(form).showMessageDialog(eq(form), eq("Arquivo Exportado com sucesso!"), eq("Jaw - Salvar"), eq(1));
    }

    @Test
    public void testExportarArquivoInvalido() throws IOException {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        when(escolhedor.getSelectedFile()).thenReturn(new File("test.jaw.xml"));
        SerializacaoXML serializacaoXML = mock(SerializacaoXML.class);
        ArgumentCaptor<List<Entidade>> entidadesCaptor = ArgumentCaptor.forClass(List.class);
        when(form.getEntidades()).thenReturn(entidadesCaptor.capture());
        when(serializacaoXML.entidades2XML(entidadesCaptor.getValue())).thenThrow(new IOException());
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        salvar.escolherArquivo(escolhedor);
        ArgumentCaptor<JFileChooser> escolhedorCaptor = ArgumentCaptor.forClass(JFileChooser.class);
        verify(form).setNomeArquivoSalvo(argThat(equalTo("test.jaw.xml")));
        verify(escolhedor).showSaveDialog(form);
        verify(escolhedor).setFileFilter(argThat(isA(Filtro2.class)));
        verify(form.getEntidades()).thenReturn(entidadesCaptor.getValue());
        verify(serializacaoXML).entidades2XML(entidadesCaptor.getValue());
        verify(form).showMessageDialog(eq(form), eq("Não foi possível salvar esse arquivo\n Verifique o formato das entidades !"), eq("Salvando Arquivo"), eq(0));
        verify(Main.janelaPrincipal).log(argThat(containsString("Não foi possível salvar esse arquivo\n Verifique o formato das entidades !")));
    }
}