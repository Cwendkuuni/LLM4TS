package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import javax.swing.filechooser.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;
    private File selectedFile;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
        selectedFile = new File("test.jaw.xml");
    }

    @Test
    public void testAbrirXML() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new FileReader(selectedFile.getAbsolutePath()))
                .thenReturn(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade id=\"1\" name=\"TestEntity\"/>\n</entidades>"));
        when(new SerializacaoXML().xml2Entidades(any(Reader.class)))
                .thenReturn(new Entidades());

        abrir = new Abrir(form, Abrir.ABRIR_XML, selectedFile);

        verify(form).setEntidades(any(Entidades.class));
        verify(form).setNomeArquivoSalvo(selectedFile.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testAbrirXMLFileNotFound() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new FileReader(selectedFile.getAbsolutePath()))
                .thenThrow(new FileNotFoundException());

        abrir = new Abrir(form, Abrir.ABRIR_XML, selectedFile);
    }

    @Test
    public void testAbrirSerializado() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new File(selectedFile.getAbsolutePath())).thenReturn(new File("test.jaw"));
        when(new Serializacao().abrir(eq(new File("test.jaw").getAbsolutePath())))
                .thenReturn(new Entidades());

        abrir = new Abrir(form, Abrir.ABRIR, selectedFile);

        verify(form).setEntidades(any(Entidades.class));
        verify(form).setNomeArquivoSalvo(selectedFile.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testAbrirSerializadoFileNotFound() {
        when(form.setNomeArquivoSalvo(anyString())).thenReturn(true);
        when(new File(selectedFile.getAbsolutePath())).thenReturn(new File("test.jaw"));
        when(new Serializacao().abrir(eq(new File("test.jaw").getAbsolutePath())))
                .thenThrow(new FileNotFoundException());

        abrir = new Abrir(form, Abrir.ABRIR, selectedFile);
    }

    @Test
    public void testOpenDialogXMLFilter() {
        JFileChooser chooser = mock(JFileChooser.class);
        when(new JFileChooser()).thenReturn(chooser);
        when(chooser.showOpenDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(new File(selectedFile.getAbsolutePath())).thenReturn(new File("test.jaw.xml"));

        abrir = new Abrir(form, Abrir.ABRIR_XML);

        verify(chooser).setFileFilter(any(FileFilter.class));
        verify(chooser).showOpenDialog(any(Frame.class));
    }

    @Test
    public void testOpenDialogSerializadoFilter() {
        JFileChooser chooser = mock(JFileChooser.class);
        when(new JFileChooser()).thenReturn(chooser);
        when(chooser.showOpenDialog(any(Frame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(new File(selectedFile.getAbsolutePath())).thenReturn(new File("test.jaw"));

        abrir = new Abrir(form, Abrir.ABRIR);

        verify(chooser).setFileFilter(any(FileFilter.class));
        verify(chooser).showOpenDialog(any(Frame.class));
    }

    @Test
    public void testOpenDialogCancel() {
        JFileChooser chooser = mock(JFileChooser.class);
        when(new JFileChooser()).thenReturn(chooser);
        when(chooser.showOpenDialog(any(Frame.class))).thenReturn(JFileChooser.CANCEL_OPTION);

        abrir = new Abrir(form, Abrir.ABRIR);

        verify(chooser, never()).setFileFilter(any(FileFilter.class));
    }
}