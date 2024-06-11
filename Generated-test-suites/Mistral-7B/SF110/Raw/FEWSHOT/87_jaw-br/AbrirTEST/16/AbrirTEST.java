package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;
    private File selectedFile;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        selectedFile = new File("testFile.jaw.xml");
        abrir = new Abrir(form, selectedFile);
    }

    @Test
    public void testAbrirXML() {
        when(form.setEntidades((Object) any())).thenReturn(true);
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);

        FileReader reader = mock(FileReader.class);
        when(reader.readAllBytes()).thenReturn(new byte[0]);

        new SerializacaoXML().xml2Entidades(reader);

        abrir.abrirXML(selectedFile.getAbsolutePath());

        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(selectedFile.getAbsolutePath());
    }

    @Test(expected = IOException.class)
    public void testAbrirXMLWithException() {
        when(form.setEntidades((Object) any())).thenReturn(true);
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);

        FileReader reader = mock(FileReader.class);
        when(reader.readAllBytes()).thenThrow(new IOException());

        abrir.abrirXML(selectedFile.getAbsolutePath());

        verify(form).setEntidades(null);
        verify(form).setNomeArquivoSalvo(selectedFile.getAbsolutePath());
    }

    @Test
    public void testAbrirSerializado() {
        when(form.setEntidades((Object) any())).thenReturn(true);
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);

        abrir.abrirSerializado(selectedFile.getAbsolutePath());

        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(selectedFile.getAbsolutePath());
    }

    @Test
    public void testJFileChooserFileFilter() {
        JFileChooser chooser = mock(JFileChooser.class);
        FileFilter filtro = new Abrir.Filtro() {
            @Override
            public String getDescription() {
                return "XML JAW files";
            }
        };

        when(chooser.setFileFilter(filtro)).thenReturn(chooser);

        Abrir abrir = new Abrir(form, chooser, Abrir.ABRIR_XML);

        File file = new File("testFile.xml");
        File[] files = { file };

        when(chooser.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFiles()).thenReturn(files);

        abrir.abrir();

        verify(chooser).setFileFilter(filtro);
        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(file.getAbsolutePath());
    }

    @Test
    public void testJFileChooserFileFilterSerializado() {
        JFileChooser chooser = mock(JFileChooser.class);
        FileFilter filtro = new Abrir.Filtro() {
            @Override
            public String getDescription() {
                return "JAW files";
            }
        };

        when(chooser.setFileFilter(filtro)).thenReturn(chooser);

        Abrir abrir = new Abrir(form, chooser, Abrir.ABRIR);

        File file = new File("testFile.jaw");
        File[] files = { file };

        when(chooser.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFiles()).thenReturn(files);

        abrir.abrir();

        verify(chooser).setFileFilter(filtro);
        verify(form).setEntidades(any());
        verify(form).setNomeArquivoSalvo(file.getAbsolutePath());
    }
}