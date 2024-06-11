package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import jaw.*;
import jaw.gui.*;
import jaw.metamodelo.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class SalvarTest {

    private Salvar salvar;
    private FinalizaEntidades form;
    private ProcessarEntidades mockedProcessarEntidades;

    @Before
    public void setup() {
        mockedProcessarEntidades = mock(ProcessarEntidades.class);
        form = new FinalizaEntidades() {
            @Override
            public void setNomeArquivoSalvo(String nomeArquivo) {
                this.nomeArquivoSalvo = nomeArquivo;
            }

            @Override
            public List<Entidade> getEntidades() {
                return Collections.singletonList(new Entidade());
            }
        };
        MockitoAnnotations.initMocks(this);
        salvar = new Salvar(form, 20);
    }

    @Test
    public void testConstruction() {
        assertNotNull(salvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(20, salvar.tipoSalvar);
    }

    @Test
    public void testSaveJawFileWithValidFileChooserSelection() {
        File selectedFile = new File("path/to/valid/file.jaw");
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        salvar.salvar();

        verify(mockedProcessarEntidades).getEntidades();
        verify(form).setNomeArquivoSalvo("path/to/valid/file.jaw");
        verify(form).showSaveDialog(ArgumentMatchers.eq(form));
        assertTrue(new File("path/to/valid/file.jaw").exists());
    }

    @Test
    public void testSaveJawFileWithNullSelectionAndEmptyName() {
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.CANCEL_OPTION);
        when(form.getName()).thenReturn("");

        salvar.salvar();

        verifyNoInteractions(mockedProcessarEntidades);
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSaveJawFileWithInvalidFileExtension() {
        File selectedFile = new File("path/to/invalid/file.txt");
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        salvar.salvar();

        verifyNoInteractions(mockedProcessarEntidades);
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSaveJawFileWithValidFileChooserSelectionAndValidSerializacao() {
        File selectedFile = new File("path/to/valid/file.jaw");
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);
        when(new Serializacao().salvar(ArgumentMatchers.eq(salvar.entidade), ArgumentMatchers.eq(salvar.arquivo))).thenReturn(true);

        salvar.salvar();

        verify(mockedProcessarEntidades).getEntidades();
        verify(form).setNomeArquivoSalvo("path/to/valid/file.jaw");
        verify(form).showSaveDialog(ArgumentMatchers.eq(form));
        assertTrue(new File("path/to/valid/file.jaw").exists());
        verify(form).showMessageDialog(ArgumentMatchers.eq(form), ArgumentMatchers.eq("Arquivo salvo com sucesso!"), ArgumentMatchers.eq("Jaw - Salvar"), ArgumentMatchers.eq(1));
    }

    @Test
    public void testSaveXmlFileWithValidFileChooserSelection() {
        File selectedFile = new File("path/to/valid/file.jaw.xml");
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; // XML content
        when(new SerializacaoXML().entidades2XML(ArgumentMatchers.eq(salvar.entidade))).thenReturn(xml);

        salvar.salvar();

        verify(mockedProcessarEntidades).getEntidades();
        verify(form).setNomeArquivoSalvo("path/to/valid/file.jaw.xml");
        verify(form).showSaveDialog(ArgumentMatchers.eq(form));
        assertTrue(new File("path/to/valid/file.jaw.xml").exists());
        try (FileReader reader = new FileReader("path/to/valid/file.jaw.xml")) {
            assertEquals(xml, reader.readAllLines()[0]);
        }
        verify(form).showMessageDialog(ArgumentMatchers.eq(form), ArgumentMatchers.eq("Arquivo Exportado com sucesso!"), ArgumentMatchers.eq("Jaw - Salvar"), ArgumentMatchers.eq(1));
    }

    @Test
    public void testSaveXmlFileWithNullSelectionAndEmptyName() {
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.CANCEL_OPTION);
        when(form.getName()).thenReturn("");

        salvar.salvar();

        verifyNoInteractions(mockedProcessarEntidades);
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSaveXmlFileWithInvalidFileExtension() {
        File selectedFile = new File("path/to/invalid/file.txt");
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);

        salvar.salvar();

        verifyNoInteractions(mockedProcessarEntidades);
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSaveXmlFileWithValidFileChooserSelectionAndValidSerializacaoXML() {
        File selectedFile = new File("path/to/valid/file.jaw.xml");
        when(form.showSaveDialog(ArgumentMatchers.eq(form))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(form.getSelectedFile()).thenReturn(selectedFile);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; // XML content
        when(new SerializacaoXML().entidades2XML(ArgumentMatchers.eq(salvar.entidade))).thenReturn(xml);

        salvar.salvar();

        verify(mockedProcessarEntidades).getEntidades();
        verify(form).setNomeArquivoSalvo("path/to/valid/file.jaw.xml");
        verify(form).showSaveDialog(ArgumentMatchers.eq(form));
        assertTrue(new File("path/to/valid/file.jaw.xml").exists());
        try (FileReader reader = new FileReader("path/to/valid/file.jaw.xml")) {
            assertEquals(xml, reader.readAllLines()[0]);
        }
        verify(form).showMessageDialog(ArgumentMatchers.eq(form), ArgumentMatchers.eq("Arquivo Exportado com sucesso!"), ArgumentMatchers.eq("Jaw - Salvar"), ArgumentMatchers.eq(1));
    }
}