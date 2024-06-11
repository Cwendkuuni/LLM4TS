package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private Abrir classUnderTest;
    private ProcessarEntidades form;
    private Serializacao serializacao;
    private SerializacaoXML serializacaoXML;
    private FileFilter filtro;
    private FileFilter filtro2;
    private JFileChooser escolhedor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        serializacao = mock(Serializacao.class);
        serializacaoXML = mock(SerializacaoXML.class);
        classUnderTest = new Abrir(form, null);
        filtro = new Filtro();
        filtro2 = new Filtro2();
        escolhedor = mock(JFileChooser.class);
    }

    @Test
    public void testAbrirXML() {
        String arquivo = "teste.jaw.xml";
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivo));

        classUnderTest = new Abrir(form, escolhedor);
        classUnderTest.abrirXML(arquivo);

        verify(form).setNomeArquivoSalvo(arquivo);
        verify(serializacaoXML).xml2Entidades(any(Reader.class));
    }

    @Test
    public void testAbrirSerializado() {
        String arquivo = "teste.jaw";
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivo));

        classUnderTest = new Abrir(form, escolhedor);
        classUnderTest.abrirSerializado(arquivo);

        verify(form).setNomeArquivoSalvo(arquivo);
        verify(serializacao).abrir(arquivo);
    }

    @Test
    public void testAbrirXMLInvalidFile() {
        String arquivo = "teste.txt";
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivo));

        classUnderTest = new Abrir(form, escolhedor);
        classUnderTest.abrirXML(arquivo);

        verify(form, never()).setNomeArquivoSalvo(arquivo);
        verify(serializacaoXML, never()).xml2Entidades(any(Reader.class));
        verify(JOptionPane).showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompatível com aplicação!"), eq("Importação XML"), eq(0));
    }

    @Test
    public void testAbrirSerializadoInvalidFile() {
        String arquivo = "teste.jaw.xml";
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivo));

        classUnderTest = new Abrir(form, escolhedor);
        classUnderTest.abrirSerializado(arquivo);

        verify(form, never()).setNomeArquivoSalvo(arquivo);
        verify(serializacao, never()).abrir(arquivo);
        verify(JOptionPane).showMessageDialog(eq(null), eq("Arquivo jaw corrompido ou incompatível com aplicação!"), eq("Abrir Arquivo jaw"), eq(0));
    }

    @Test
    public void testAbrirXMLWithFiltro2() {
        File arquivo = new File("teste.jaw.xml");
        when(escolhedor.getSelectedFile()).thenReturn(arquivo);

        classUnderTest = new Abrir(form, escolhedor, new Filtro2());
        classUnderTest.abrirXML(arquivo.getAbsolutePath());

        verify(form).setNomeArquivoSalvo(arquivo.getAbsolutePath());
        verify(serializacaoXML).xml2Entidades(any(Reader.class));
        verify(filtro2).accept(arquivo);
    }

    @Test
    public void testAbrirWithNullFileChooser() {
        classUnderTest.abrir(form, null);

        verify(form, never()).setNomeArquivoSalvo(anyString());
        verify(serializacao, never()).abrir(anyString());
        verify(escolhedor, never()).showOpenDialog(any(Component.class));
    }
}