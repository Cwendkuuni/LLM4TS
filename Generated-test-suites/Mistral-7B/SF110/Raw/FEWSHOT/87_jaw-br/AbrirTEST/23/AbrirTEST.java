package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import javax.swing.filechooser.JFileChooser;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private Abrir objectUnderTest;
    private ProcessarEntidades form;
    private JFileChooser fileChooser;
    private File selectedFile;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        fileChooser = mock(JFileChooser.class);
        selectedFile = new File("test.jaw.xml");
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(selectedFile);
        objectUnderTest = new Abrir(form, Abrir.ABRIR_XML);
    }

    @Test
    public void testAbrirXML() {
        givenAFileWithXmlIsSelected();
        whenOpeningXmlFile();
        thenEntidadesAttributeIsSet();
        thenNameArquivoSalvoAttributeIsSet();
    }

    @Test
    public void testAbrirSerializado() {
        givenAFileWithJawIsSelected();
        whenOpeningSerializadoFile();
        thenEntidadesAttributeIsSet();
        thenNameArquivoSalvoAttributeIsSet();
    }

    @Test
    public void testAbrirInvalidFileXml() {
        givenAFileWithInvalidExtensionIsSelected();
        whenOpeningXmlFile();
        thenNameArquivoSalvoAttributeIsNotSet();
        thenEntidadesAttributeIsNotSet();
    }

    @Test
    public void testAbrirInvalidFileJaw() {
        givenAFileWithInvalidExtensionIsSelected();
        whenOpeningSerializadoFile();
        thenNameArquivoSalvoAttributeIsNotSet();
        thenEntidadesAttributeIsNotSet();
    }

    @Test
    public void testAbrirCanceled() {
        givenAFileChooserShowsCanceledOption();
        whenOpeningXmlFile();
        thenObjectUnderTestIsEmpty();
    }

    private void givenAFileWithXmlIsSelected() {
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw.xml"));
    }

    private void givenAFileWithSerializadoIsSelected() {
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.jaw"));
    }

    private void givenAFileWithInvalidExtensionIsSelected() {
        when(fileChooser.getSelectedFile()).thenReturn(new File("test.txt"));
    }

    private void givenAFileChooserShowsCanceledOption() {
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.CANCEL_OPTION);
    }

    private void whenOpeningXmlFile() {
        objectUnderTest = new Abrir(form, Abrir.ABRIR_XML);
    }

    private void whenOpeningSerializadoFile() {
        objectUnderTest = new Abrir(form, Abrir.ABRIR);
    }

    private void thenEntidadesAttributeIsSet() {
        assertEquals("", objectUnderTest.getEntidades());
    }

    private void thenNameArquivoSalvoAttributeIsSet() {
        assertEquals(selectedFile.getAbsolutePath(), objectUnderTest.getNomeArquivoSalvo());
    }

    private void thenNameArquivoSalvoAttributeIsNotSet() {
        assertNull(objectUnderTest.getNomeArquivoSalvo());
    }

    private void thenObjectUnderTestIsEmpty() {
        assertEquals("", objectUnderTest.getXml());
    }
}