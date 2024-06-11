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

import jaw.entrada.Entidade;
import jaw.entrada.ProcessarEntidades;
import jaw.metamodelo.Serializacao;
import jaw.metamodelo.SerializacaoXML;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;
    private JFileChooser fileChooser;
    private List<Entidade> entidades;

    @Before
    public void setup() {
        form = Mockito.mock(ProcessarEntidades.class);
        entidades = new ArrayList<>();
        entidades.add(new Entidade("teste1"));
        entidades.add(new Entidade("teste2"));
        salvar = new Salvar(form, 20);
        fileChooser = Mockito.mock(JFileChooser.class);
        when(form.getEntidades()).thenReturn(entidades);
        when(form.getNomeArquivoSalvo()).thenReturn("");
        when(form.getName()).thenReturn("");
        when(form.getFileChooser()).thenReturn(fileChooser);
    }

    @Test
    public void testSalvarComArquivoValido() throws IOException {
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.jaw"));

        salvar.salvar(entidades, "teste.jaw");

        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("teste.jaw");
        Serializacao serializacao = new Serializacao();
        assertTrue(serializacao.salvar(entidades, "teste.jaw"));
        verify(form, times(1)).showMessageDialog(eq(form), eq("Arquivo salvo com sucesso!"), eq("Jaw - Salvar"), eq(1));
    }

    @Test
    public void testSalvarSemArquivoEscolhido() {
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

        salvar.salvar(entidades, "");

        verify(fileChooser, times(1)).showSaveDialog(form);
        // Nenhuma verificações de mensagens pois nenhuma é exibida
    }

    @Test
    public void testSalvarComArquivoInvalido() {
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.txt"));

        salvar.salvar(entidades, "");

        verify(fileChooser, times(1)).showSaveDialog(form);
        // Nenhuma verificações de mensagens pois nenhuma é exibida
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSalvarComNula() {
        salvar.salvar(null, "");
    }

    @Test
    public void testSalvarXmlComArquivoValido() throws IOException {
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.jaw.xml"));

        String xml = salvar.salvarXml(entidades, "teste.jaw.xml");

        verify(fileChooser, times(1)).showSaveDialog(form);
        verify(form, times(1)).setNomeArquivoSalvo("teste.jaw.xml");
        SerializacaoXML serializacaoXml = new SerializacaoXML();
        String esperado = "<entidades>";
        for (Entidade entidade : entidades) {
            esperado += "<entidade>";
            esperado += "<nome>" + entidade.getNome() + "</nome>";
            esperado += "</entidade>";
        }
        esperado += "</entidades>";
        assertEquals(esperado, xml);
        verify(form, times(1)).showMessageDialog(eq(form), eq("Arquivo Exportado com sucesso!"), eq("Jaw - Salvar"), eq(1));
    }

    @Test
    public void testSalvarXmlSemArquivoEscolhido() {
        when(fileChooser.showSaveDialog(form)).thenReturn(JFileChooser.CANCEL_OPTION);

        String xml = salvar.salvarXml(entidades, "");

        assertEquals(null, xml);
        // Nenhuma verificações de mensagens pois nenhuma é exibida
    }

    @Test
    public void testSalvarXmlComArquivoInvalido() {
        when(fileChooser.getSelectedFile()).thenReturn(new File("teste.txt"));

        String xml = salvar.salvarXml(entidades, "");

        assertEquals(null, xml);
        // Nenhuma verificações de mensagens pois nenhuma é exibida
    }

    @Test(expected = NullPointerException.class)
    public void testSalvarXmlComNulo() {
        salvar.salvarXml(null, "");
    }

}