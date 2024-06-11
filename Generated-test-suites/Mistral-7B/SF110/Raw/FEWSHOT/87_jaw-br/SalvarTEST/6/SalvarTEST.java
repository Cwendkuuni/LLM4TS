package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Frame;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SalvarTest {

    private Salvar salvar;
    @Mock
    private ProcessarEntidades form;
    @Mock
    private Frame frame;
    @Mock
    private JFileChooser fileChooser;
    @Mock
    private Serializacao serializacao;
    @Mock
    private SerializacaoXML serializacaoXML;
    @Mock
    private List<Entidade> entidades;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
    }

    @Test
    public void testSalvarXML() throws IOException {
        File tempFile = File.createTempFile("test-", ".xml");
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<entidades>\n<entidade>\n<nome>Entidade1</nome>\n<atributo1>Valor1</atributo1>\n<atributo2>Valor2</atributo2>\n</entidade>\n<entidade>\n<nome>Entidade2</nome>\n<atributo1>Valor3</atributo1>\n<atributo2>Valor4</atributo2>\n</entidade>\n</entidades>";
        when(serializacaoXML.entidades2XML(entidades)).thenReturn(expectedXml);

        salvar.arquivo = tempFile.getAbsolutePath();
        salvar.xml = salvar.salvarXML();

        assertEquals(expectedXml, salvar.xml);
        verify(fileChooser, never()).showSaveDialog(form);
        verifyZeroInteractions(serializacao);
        FileUtils.deleteQuietly(tempFile);
    }

    @Test
    public void testSalvarArquivo() {
        String arquivo = "test.jaw";
        when(fileChooser.showSaveDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(arquivo));

        salvar.arquivo = null;
        salvar.salvarArquivo();

        assertEquals(arquivo, salvar.arquivo);
        verify(fileChooser).showSaveDialog(frame);
        verifyZeroInteractions(serializacao);
    }

    @Test
    public void testSalvarArquivoExistente() {
        String arquivo = "test.jaw";
        when(fileChooser.showSaveDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(arquivo));
        when(serializacao.salvar(entidades, arquivo)).thenThrow(IOException.class);

        salvar.arquivo = null;
        salvar.salvarArquivo();

        verify(fileChooser).showSaveDialog(frame);
        verifyZeroInteractions(serializacaoXML);
        verifyZeroInteractions(serializacao);
    }

    @Test
    public void testSalvarArquivoSemExtensao() {
        String arquivo = "test";
        when(fileChooser.showSaveDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(arquivo));

        salvar.arquivo = null;
        salvar.salvarArquivo();

        assertEquals(arquivo + ".jaw", salvar.arquivo);
        verify(fileChooser).showSaveDialog(frame);
        verifyZeroInteractions(serializacaoXML);
        verifyZeroInteractions(serializacao);
    }

    @Test
    public void testSalvarXMLSemExtensao() {
        String arquivo = "test";
        when(fileChooser.showSaveDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(arquivo));
        when(serializacaoXML.entidades2XML(entidades)).thenReturn("XML data");

        salvar.arquivo = null;
        salvar.salvarXML();

        assertEquals(arquivo + ".jaw.xml", salvar.arquivo);
        verify(fileChooser).showSaveDialog(frame);
        verifyZeroInteractions(serializacaoXML);
        verifyZeroInteractions(serializacao);
    }

    @Test
    public void testSalvarXMLSemExcecao() throws IOException {
        String arquivo = "test.xml";
        when(fileChooser.showSaveDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(arquivo));
        when(serializacaoXML.entidades2XML(entidades)).thenReturn("XML data");

        salvar.arquivo = null;
        String result = salvar.salvarXML();

        assertEquals("XML data", result);
        verify(fileChooser).showSaveDialog(frame);
        verifyZeroInteractions(serializacaoXML);
        verifyZeroInteractions(serializacao);
    }

    @Test
    public void testSalvarArquivoComExcecao() {
        String arquivo = "test.jaw";
        when(fileChooser.showSaveDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File(arquivo));
        when(serializacao.salvar(entidades, arquivo)).thenThrow(IOException.class);

        salvar.arquivo = null;
        salvar.salvarArquivo();

        verify(fileChooser).showSaveDialog(frame);
        verifyZeroInteractions(serializacaoXML);
        verifyZeroInteractions(serializacao);
        verify(frame).pack();
        verify(JOptionPane.showMessageDialog(frame, anyString(), anyString(), anyInt())).isInstanceOf(JOptionPane.MessageType.ERROR);
    }

    @Test
    public void testSalvarArquivoSemEntidades() {
        when(form.getEntidades()).thenReturn(null);

        salvar.salvarArquivo();

        verifyZeroInteractions(fileChooser);
        verifyZeroInteractions(serializacaoXML);
        verifyZeroInteractions(serializacao);
        verifyZeroInteractions(frame);
        verifyZeroInteractions(JOptionPane.showMessageDialog(frame, anyString(), anyString(), anyInt()));
    }
}