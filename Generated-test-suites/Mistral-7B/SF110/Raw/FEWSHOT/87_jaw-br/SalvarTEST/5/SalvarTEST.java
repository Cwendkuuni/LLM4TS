package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.metamodelo.Projeto;
import jaw.util.Serializacao;
import jaw.util.SerializacaoXML;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;
    private JFileChooser fileChooser;
    private Frame frame;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        salvar = new Salvar(form, 20);
        fileChooser = mock(JFileChooser.class);
        frame = mock(Frame.class);

        when(form.getEntidades()).thenReturn(new Vector<Entidade>() {{
            add(MetamodeloFactory.createProjeto("Projeto1", new String[]{"Campo1", "Campo2"}));
        }});

        when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.jaw"));

        salvar.vela = true;
    }

    @Test
    public void testSalvarJawFile() throws IOException {
        when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");
        salvar.salvar(salvar.entidade, salvar.arquivo);
        verify(fileChooser).showSaveDialog(form);
        verify(new Serializacao()).salvar(salvar.entidade, salvar.arquivo);
    }

    @Test
    public void testSalvarXmlFile() throws IOException {
        when(form.getNomeArquivoSalvo()).thenReturn("");
        salvar.salvar(salvar.entidade, salvar.arquivo);
        verify(new File("teste.jaw.xml"), Mockito.isInstance(File.class)).canWrite();
        verify(new SerializacaoXML()).entidades2XML(salvar.entidade);
        verify(new FileWriter(salvar.arquivo)).write(anyString());
        verify(new FileWriter(salvar.arquivo)).flush();
        verify(new FileWriter(salvar.arquivo)).close();
    }

    @Test
    public void testSalvarWithNullArquivo() {
        salvar.salvar(salvar.entidade, null);
        assertEquals(salvar.arquivo, "");
    }

    @Test
    public void testAcceptFileFilterJaw() {
        FileFilter filter = salvar.new Filtro();
        assertTrue(filter.accept(new File("teste.jaw")));
        assertFalse(filter.accept(new File("teste.xml")));
    }

    @Test
    public void testAcceptFileFilterXml() {
        FileFilter filter = salvar.new Filtro2();
        assertTrue(filter.accept(new File("teste.jaw.xml")));
        assertFalse(filter.accept(new File("teste.jaw")));
    }

    @Test(expected = NullPointerException.class)
    public void testNullFileChooser() {
        salvar.salvar(salvar.entidade, null);
    }
}