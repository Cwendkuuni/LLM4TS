package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.FileDialog;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileFilterException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AbrirTest {

    private static final String TEST_XML_FILE = "test.jaw.xml";
    private static final String TEST_SERIALIZED_FILE = "test.jaw";
    private static final String TEST_INVALID_XML_FILE = "test.xml";
    private static final String TEST_INVALID_SERIALIZED_FILE = "test";

    private Abrir abrir;
    private ProcessarEntidades form;
    private Serializacao serializacao;
    private SerializacaoXML serializacaoXML;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        form = mock(ProcessarEntidades.class);
        serializacao = mock(Serializacao.class);
        serializacaoXML = mock(SerializacaoXML.class);
        abrir = new Abrir(form, TEST_XML_FILE);
    }

    @Test
    public void testAbrirXML() {
        when(serializacaoXML.xml2Entidades(any(Reader.class))).thenReturn(new Object[0]);

        abrir.abrirXML(TEST_XML_FILE);

        assertEquals(TEST_XML_FILE, form.getNomeArquivoSalvo());
        verify(form).setEntidades(anyArray());
    }

    @Test(expected = FileFilterException.class)
    public void testOpenDialogWithInvalidFilter2() {
        final JFileChooser escolhedor = new JFileChooser();
        escolhedor.setFileFilter(new Object() {});
        abrir.new Abrir(form, 10, escolhedor);
    }

    @Test(expected = HeadlessException.class)
    public void testOpenDialogWithNullFrame() {
        final JFileChooser escolhedor = new JFileChooser();
        abrir.new Abrir(form, 20, null, escolhedor);
    }

    @Test
    public void testOpenDialog() {
        final FileDialog dialog = mock(FileDialog.class);
        when(dialog.show).thenReturn(JFileChooser.APPROVE_OPTION);

        final File file = new File("test.jaw.xml");
        when(dialog.getFilesSelected()).thenReturn(new File[] { file });

        abrir.new Abrir(form, 20, dialog);

        assertEquals(TEST_XML_FILE, form.getNomeArquivoSalvo());
        verify(form).setEntidades(anyArray());
    }

    @Test
    public void testOpenDialogWithNoFileSelected() {
        final FileDialog dialog = mock(FileDialog.class);
        when(dialog.show).thenReturn(JFileChooser.CANCEL_OPTION);

        abrir.new Abrir(form, 20, dialog);

        assertNull(form.getNomeArquivoSalvo());
        verifyNoInteractions(form);
    }

    @Test
    public void testAbrirSerializado() throws IOException {
        when(serializacao.abrir(anyString())).thenReturn(new Object[0]);

        abrir.abrirSerializado(TEST_SERIALIZED_FILE);

        assertEquals(TEST_SERIALIZED_FILE, form.getNomeArquivoSalvo());
        verify(form).setEntidades(anyArray());
    }

    @Test
    public void testAbrirInvalidSerializado() {
        abrir.abrirSerializado(TEST_INVALID_SERIALIZED_FILE);

        assertEquals("", form.getNomeArquivoSalvo());
        verifyNoInteractions(form);
        verify(form).showMessageDialog(eq(null), eq("Arquivo jaw corrompido ou incompatível com aplicação!"), eq(0));
    }

    @Test
    public void testAbrirXMLInvalid() {
        when(serializacaoXML.xml2Entidades(any(Reader.class))).thenThrow(new IOException());

        abrir.abrirXML(TEST_INVALID_XML_FILE);

        assertEquals("", form.getNomeArquivoSalvo());
        verifyNoInteractions(form);
        verify(form).showMessageDialog(eq(null), eq("Arquivo xml corrompido ou incompatível com aplicação!"), eq(0));
    }

    @Test
    public void testAbrirXMLEmpty() throws IOException {
        when(serializacaoXML.xml2Entidades(any(Reader.class))).thenReturn(new Object[0]);

        abrir = new Abrir(form, TEST_XML_FILE);

        assertEquals("", form.getNomeArquivoSalvo());
        verifyNoInteractions(form);
    }
}