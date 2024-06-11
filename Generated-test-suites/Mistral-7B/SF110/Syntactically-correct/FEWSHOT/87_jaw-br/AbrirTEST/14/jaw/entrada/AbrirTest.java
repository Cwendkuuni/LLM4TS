package jaw.entrada;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.JFileChooser;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
    }

    @Test
    public void testAbrirXMLWithValidXmlFile() {
        String xmlFilePath = "test/resources/valid-xml-file.jaw.xml";
        File xmlFile = new File(xmlFilePath);

        when(form.setNomeArquivoSalvo(xmlFilePath)).thenReturn(true);
        when(form.setEntidades((Object) new Object[0]))
                .thenReturn(true);

        abrir = new Abrir(form, Abrir.ABRIR_XML, xmlFile);

        assertTrue(abrir.xml.isEmpty());
        assertEquals(xmlFilePath, abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithValidFile() {
        String serializadoFilePath = "test/resources/valid-serializado-file.jaw";
        File serializadoFile = new File(serializadoFilePath);

        when(form.setNomeArquivoSalvo(serializadoFilePath)).thenReturn(true);
        when(form.setEntidades((Object) new Object[0]))
                .thenReturn(true);

        abrir = new Abrir(form, Abrir.ABRIR, serializadoFile);

        assertTrue(abrir.xml.isEmpty());
        assertEquals(serializadoFilePath, abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }

    @Test
    public void testAbrirXMLWithInvalidXmlFile() {
        String invalidXmlFilePath = "test/resources/invalid-xml-file.jaw.xml";
        File invalidXmlFile = new File(invalidXmlFilePath);

        when(form.setNomeArquivoSalvo(invalidXmlFilePath)).thenReturn(false);

        abrir = new Abrir(form, Abrir.ABRIR_XML, invalidXmlFile);

        assertEquals("", abrir.xml);
        assertFalse(abrir.form.getNomeArquivoSalvo().isEmpty());
        assertNull(abrir.form.getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        String invalidSerializadoFilePath = "test/resources/invalid-serializado-file.jaw";
        File invalidSerializadoFile = new File(invalidSerializadoFilePath);

        when(form.setNomeArquivoSalvo(invalidSerializadoFilePath)).thenReturn(false);

        abrir = new Abrir(form, Abrir.ABRIR, invalidSerializadoFile);

        assertEquals("", abrir.xml);
        assertFalse(abrir.form.getNomeArquivoSalvo().isEmpty());
        assertNull(abrir.form.getEntidades());
    }

    @Test
    public void testChooseFileWithXmlFilter() throws IOException {
        FileDialog fileDialog = mock(FileDialog.class);
        JFileChooser fileChooser = mock(JFileChooser.class);
        Frame frame = mock(Frame.class);

        when(fileChooser.showOpenDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test/resources/valid-xml-file.jaw.xml"));
        when(fileChooser.getFileFilter()).thenReturn(new Filtro2());

        when(fileDialog.getFiles()).thenReturn(new String[]{"test/resources/valid-xml-file.jaw.xml"});
        when(fileDialog.getSelectedFiles()).thenReturn(new File[]{new File("test/resources/valid-xml-file.jaw.xml")});

        abrir = new Abrir(form, Abrir.ABRIR_XML, fileDialog);

        assertEquals("test/resources/valid-xml-file.jaw.xml", abrir.xml);
        assertEquals("test/resources/valid-xml-file.jaw.xml", abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }

    @Test
    public void testChooseFileWithSerializadoFilter() throws IOException {
        FileDialog fileDialog = mock(FileDialog.class);
        JFileChooser fileChooser = mock(JFileChooser.class);
        Frame frame = mock(Frame.class);

        when(fileChooser.showOpenDialog(frame)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("test/resources/valid-serializado-file.jaw"));
        when(fileChooser.getFileFilter()).thenReturn(new Filtro());

        when(fileDialog.getFiles()).thenReturn(new String[]{"test/resources/valid-serializado-file.jaw"});
        when(fileDialog.getSelectedFiles()).thenReturn(new File[]{new File("test/resources/valid-serializado-file.jaw")});

        abrir = new Abrir(form, Abrir.ABRIR, fileDialog);

        assertEquals("test/resources/valid-serializado-file.jaw", abrir.xml);
        assertEquals("test/resources/valid-serializado-file.jaw", abrir.form.getNomeArquivoSalvo());
        assertNotNull(abrir.form.getEntidades());
    }

    @Test
    public void testChooseFileWithNoFileSelected() {
        FileDialog fileDialog = mock(FileDialog.class);
        JFileChooser fileChooser = mock(JFileChooser.class);
        Frame frame = mock(Frame.class);

        when(fileChooser.showOpenDialog(frame)).thenReturn(JFileChooser.CANCEL_OPTION);

        abrir = new Abrir(form, Abrir.ABRIR, fileDialog);

        assertEquals("", abrir.xml);
        assertNull(abrir.form.getNomeArquivoSalvo());
        assertNull(abrir.form.getEntidades());
    }

    @Test
    public void testSetFileFilterWithXmlFilter() {
        FileFilter xmlFilter = new Filtro2();

        abrir = new Abrir(form, Abrir.ABRIR_XML);

        assertEquals(xmlFilter, abrir.getFileFilter());
    }

    @Test
    public void testSetFileFilterWithSerializadoFilter() {
        FileFilter serializadoFilter = new Filtro();

        abrir = new Abrir(form, Abrir.ABRIR);

        assertEquals(serializadoFilter, abrir.getFileFilter());
    }
}