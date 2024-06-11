package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbrirTest {

    @Mock
    private ProcessarEntidades form;

    @Mock
    private File escolhedor;

    @Mock
    private Serializacao serializacao;

    @Mock
    private SerializacaoXML serializacaoXML;

    @Mock
    private Reader leitor;

    @Mock
    private Filtro filtroXML;

    @Mock
    private Filtro filtroJaw;

    private Abrir systemUnderTest;

    @Test
    public void testAbrirXMLWithValidFile() throws IOException {
        MockitoAnnotations.initMocks(this);

        when(escolhedor.getAbsolutePath()).thenReturn("path/to/valid/xml/file.jaw.xml");
        when(form.setNomeArquivoSalvo(String.class)).thenReturn(this);
        when(form.setEntidades(Iterable.class)).thenReturn(this);
        when(serializacaoXML.xml2Entidades(Reader.class)).thenReturn(mock(Iterable.class));

        systemUnderTest = new Abrir(form, escolhedor);

        verify(serializacaoXML).xml2Entidades(leitor);
        assertEquals("path/to/valid/xml/file.jaw.xml", systemUnderTest.xml);
    }

    @Test
    public void testAbrirXMLWithInvalidFile() {
        MockitoAnnotations.initMocks(this);

        when(escolhedor.getAbsolutePath()).thenReturn("path/to/invalid/file");

        systemUnderTest = new Abrir(form, escolhedor);

        verify(form, never()).setNomeArquivoSalvo(String.class);
        verify(form, never()).setEntidades(Iterable.class);
    }

    @Test
    public void testAbrirSerializadoWithValidFile() throws IOException, Exception {
        MockitoAnnotations.initMocks(this);

        when(escolhedor.getAbsolutePath()).thenReturn("path/to/valid/serializado/file.jaw");
        when(form.setNomeArquivoSalvo(String.class)).thenReturn(this);
        when(form.setEntidades(Iterable.class)).thenReturn(this);
        when(serializacao.abrir(String.class)).thenReturn(mock(Object.class));

        systemUnderTest = new Abrir(form, escolhedor);

        verify(serializacao).abrir(eq("path/to/valid/serializado/file.jaw"));
        assertEquals("path/to/valid/serializado/file.jaw", systemUnderTest.xml);
    }

    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        MockitoAnnotations.initMocks(this);

        when(escolhedor.getAbsolutePath()).thenReturn("path/to/invalid/file");

        systemUnderTest = new Abrir(form, escolhedor);

        verify(form, never()).setNomeArquivoSalvo(String.class);
        verify(form, never()).setEntidades(Iterable.class);
    }

    @Test
    public void testOpenDialogWithXMLFilter() {
        MockitoAnnotations.initMocks(this);

        when(form.setNomeArquivoSalvo(String.class)).thenReturn(this);
        when(form.setEntidades(Iterable.class)).thenReturn(this);
        when(new Filtro2()).thenReturn(filtroXML);

        systemUnderTest = new Abrir(form, Abrir.ABRIR_XML);

        verify(form).setNomeArquivoSalvo("path/to/valid/xml/file.jaw.xml");
        verify(form).setEntidades(any(Iterable.class));
        verify(escolhedor).setFileFilter(filtroXML);
    }

    @Test
    public void testOpenDialogWithSerializadoFilter() {
        MockitoAnnotations.initMocks(this);

        when(form.setNomeArquivoSalvo(String.class)).thenReturn(this);
        when(form.setEntidades(Iterable.class)).thenReturn(this);
        when(new Filtro()).thenReturn(filtroJaw);

        systemUnderTest = new Abrir(form, Abrir.ABRIR);

        verify(form).setNomeArquivoSalvo("path/to/valid/serializado/file.jaw");
        verify(form).setEntidades(any(Iterable.class));
        verify(escolhedor).setFileFilter(filtroJaw);
    }
}