package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbrirTest {

    @InjectMocks
    private Abrir abrir;

    @Mock
    private Serializacao serializacao;
    @Mock
    private SerializacaoXML serializacaoXML;
    @Mock
    private ProcessarEntidades form;
    @Mock
    private JOptionPane optionPane;
    @Mock
    private Main janelaPrincipal;

    @Mock
    private FileFilter filtro2;
    @Mock
    private FileFilter filtro;
    @Mock
    private JFileChooser escolhedor;

    @Test
    public void testAbrirXML() {
        String xmlFilePath = "testXMLFile.jaw.xml";
        when(form.getNomeArquivoSalvo()).thenReturn("");

        // Given: Abrir arquivo XML
        abrir.abrirXML(xmlFilePath);

        // Then: Entidades deve ser preenchido com dados do arquivo XML
        verify(serializacaoXML).xml2Entidades(any(Reader.class));
        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(xmlFilePath);

        // Given: Arquivo XML inválido
        xmlFilePath = "testXMLFile.xml";
        doThrow(IOException.class).when(serializacaoXML).xml2Entidades(any(Reader.class));

        // Then: Erro deve ser exibido
        abrir.abrirXML(xmlFilePath);
        verify(optionPane).showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompatível com aplicação!"), eq("Importação XML"), eq(0));
        verify(janelaPrincipal).log(anyString());
    }

    @Test
    public void testAbrirSerializado() {
        String jawFilePath = "testJawFile.jaw";
        when(form.getNomeArquivoSalvo()).thenReturn("");

        // Given: Abrir arquivo jaw
        abrir.abrirSerializado(jawFilePath);

        // Then: Entidades deve ser preenchido com dados do arquivo jaw
        verify(serializacao).abrir(jawFilePath);
        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(jawFilePath);

        // Given: Arquivo jaw inválido
        jawFilePath = "testJawFile.txt";
        doThrow(IOException.class).when(serializacao).abrir(jawFilePath);

        // Then: Erro deve ser exibido
        abrir.abrirSerializado(jawFilePath);
        verify(optionPane).showMessageDialog(eq(null), eq("Arquivo jaw corrompido ou incompatível com aplicação!"), eq("Abrir Arquivo jaw"), eq(0));
        verify(janelaPrincipal).log(anyString());
    }

    @Test
    public void testAbrirArquivo() {
        String arquivoPath = "testFile.jaw.xml";

        // Given: Setar filtros no JFileChooser
        when(filtro2.accept(any())).thenReturn(true);
        when(filtro.accept(any())).thenReturn(false);
        when(escolhedor.getFileFilter()).thenReturn(filtro);

        // Given: Abrir arquivo com filtro XML
        doReturn(filtro2).when(abrir).new Filtro2();
        abrir.abrir(form, Abrir.ABRIR_XML);

        // Then: Escolher arquivo XML no JFileChooser
        verify(escolhedor).showOpenDialog(eq(null));
        verify(escolhedor).setFileFilter(eq(filtro2));

        // Given: Arquivo XML selecionado
        File arquivoXML = mock(File.class);
        when(arquivoXML.getAbsolutePath()).thenReturn(arquivoPath);
        when(escolhedor.getSelectedFile()).thenReturn(arquivoXML);

        // Given: Abrir arquivo XML
        abrir.abrirXML(arquivoPath);

        // Then: Entidades deve ser preenchido com dados do arquivo XML
        verify(serializacaoXML).xml2Entidades(any(Reader.class));
        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(arquivoPath);

        // Given: Setar filtros no JFileChooser
        when(filtro.accept(any())).thenReturn(true);
        when(filtro2.accept(any())).thenReturn(false);

        // Given: Abrir arquivo com filtro JAW
        doReturn(filtro).when(abrir).new Filtro();
        abrir.abrir(form, Abrir.ABRIR);

        // Then: Escolher arquivo JAW no JFileChooser
        verify(escolhedor).showOpenDialog(eq(null));
        verify(escolhedor).setFileFilter(eq(filtro));

        // Given: Arquivo JAW selecionado
        File arquivoJaw = mock(File.class);
        when(arquivoJaw.getAbsolutePath()).thenReturn(arquivoPath);
        when(escolhedor.getSelectedFile()).thenReturn(arquivoJaw);

        // Given: Abrir arquivo JAW
        abrir.abrirSerializado(arquivoPath);

        // Then: Entidades deve ser preenchido com dados do arquivo JAW
        verify(serializacao).abrir(arquivoPath);
        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(arquivoPath);
    }
}