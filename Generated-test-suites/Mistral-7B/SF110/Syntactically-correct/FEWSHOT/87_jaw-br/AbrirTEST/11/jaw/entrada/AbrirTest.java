package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private final ProcessarEntidades formMock = mock(ProcessarEntidades.class);
    private final Serializacao serializacaoMock = mock(Serializacao.class);
    private final SerializacaoXML serializacaoXMLMock = mock(SerializacaoXML.class);

    private Abrir abrir;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAbrirXMLWithValidFile() {
        final String arquivoXml = "teste.jaw.xml";
        when(serializacaoXMLMock.xml2Entidades(any(Reader.class))).thenReturn(new Object());

        abrir = new Abrir(formMock, arquivoXml);

        verify(formMock, times(1)).setNomeArquivoSalvo(arquivoXml);
        verify(formMock, times(1)).setEntidades(any());
        verifyNoInteractions(serializacaoMock);
    }

    @Test
    public void testAbrirSerializadoWithValidFile() {
        final String arquivoJaw = "teste.jaw";
        when(serializacaoMock.abrir(arquivoJaw)).thenReturn(new Object());

        abrir = new Abrir(formMock, 20, new File(arquivoJaw));

        verify(formMock, times(1)).setNomeArquivoSalvo(arquivoJaw);
        verify(formMock, times(1)).setEntidades(any());
        verify(serializacaoMock, times(1)).abrir(arquivoJaw);
        verifyNoInteractions(serializacaoXMLMock);
    }

    @Test
    public void testOpenFileDialogWithJawXmlFilter() {
        final JFileChooser escolhedor = mock(JFileChooser.class);
        final Filtro2 filtro2 = new Filtro2();

        abrir = new Abrir(formMock, 10);

        when(escolhedor.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File("teste.jaw.xml"));

        abrir.abrir(escolhedor, null);

        verify(escolhedor, times(1)).setFileFilter(filtro2);
        verify(escolhedor, times(1)).showOpenDialog(any());
        verify(formMock, times(1)).setNomeArquivoSalvo("teste.jaw.xml");
        verify(formMock, times(1)).setEntidades(any());
        verifyNoInteractions(serializacaoMock);
        verifyNoInteractions(serializacaoXMLMock);
    }

    @Test
    public void testOpenFileDialogWithJawFilter() {
        final JFileChooser escolhedor = mock(JFileChooser.class);
        final Filtro filtro = new Filtro();

        abrir = new Abrir(formMock, 20);

        when(escolhedor.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(new File("teste.jaw"));

        abrir.abrir(escolhedor, null);

        verify(escolhedor, times(1)).setFileFilter(filtro);
        verify(escolhedor, times(1)).showOpenDialog(any());
        verify(formMock, times(1)).setNomeArquivoSalvo("teste.jaw");
        verify(formMock, times(1)).setEntidades(any());
        verifyNoInteractions(serializacaoMock);
        verifyNoInteractions(serializacaoXMLMock);
    }

    @Test
    public void testOpenFileDialogWithNullFile() {
        final JFileChooser escolhedor = mock(JFileChooser.class);

        abrir = new Abrir(formMock, 20);

        when(escolhedor.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(escolhedor.getSelectedFile()).thenReturn(null);

        abrir.abrir(escolhedor, null);

        verify(escolhedor, times(1)).showOpenDialog(any());
        verifyNoInteractions(formMock);
        verifyNoInteractions(serializacaoMock);
        verifyNoInteractions(serializacaoXMLMock);
    }

    @Test
    public void testAbrirXMLWithInvalidFile() {
        final String arquivoXml = "teste.txt";

        abrir = new Abrir(formMock, arquivoXml);

        verify(formMock, never()).setNomeArquivoSalvo(arquivoXml);
        verify(formMock, never()).setEntidades(any());
        verify(serializacaoMock, never()).abrir(anyString());
        verify(serializacaoXMLMock, never()).xml2Entidades(any(Reader.class));
        verify(formMock, times(1)).setErro("Arquivo xml corrompido ou incompatível com aplicação!");
    }

    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        final String arquivoJaw = "teste.txt";

        abrir = new Abrir(formMock, 20, new File(arquivoJaw));

        verify(formMock, never()).setNomeArquivoSalvo(arquivoJaw);
        verify(formMock, never()).setEntidades(any());
        verify(serializacaoMock, never()).abrir(anyString());
        verify(formMock, times(1)).setErro("Arquivo jaw corrompido ou incompatível com aplicação!");
    }

    @Test
    public void testAbrirWithInvalidFileType() {
        abrir = new Abrir(formMock, "30");

        verify(formMock, never()).setNomeArquivoSalvo(anyString());
        verify(formMock, never()).setEntidades(any());
        verify(formMock, times(1)).setErro("Opção inválida!");
    }

    @Test
    public void testAbrirWithNullForm() {
        abrir = new Abrir(null, "teste.jaw.xml");

        verify(formMock, never()).setNomeArquivoSalvo(anyString());
        verify(formMock, never()).setEntidades(any());
        verifyNoInteractions(serializacaoMock);
        verifyNoInteractions(serializacaoXMLMock);
        verify(formMock, times(1)).setErro("Formulário inválido!");
    }

    @Test
    public void testAbrirWithNullArquivo() {
        abrir = new Abrir(formMock, null);

        verify(formMock, never()).setNomeArquivoSalvo(anyString());
        verify(formMock, never()).setEntidades(any());
        verifyNoInteractions(serializacaoMock);
        verifyNoInteractions(serializacaoXMLMock);
        verify(formMock, times(1)).setErro("Arquivo inválido!");
    }
}