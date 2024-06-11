package jaw.entrada;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;
    private Filtro2 filtroXml;
    private Filtro filtroJaw;

    @Before
    public void setUp() {
        form = mock(ProcessarEntidades.class);
        filtroXml = new Filtro2();
        filtroJaw = new Filtro();
    }

    @After
    public void tearDown() {
        form = null;
        abrir = null;
    }

    @Test
    public void testAbrirXml() {
        String arquivoXml = "teste.jaw.xml";
        abrir = new Abrir(form, arquivoXml);
        assertEquals("teste.jaw.xml", abrir.xml);
        assertTrue(abrir.form instanceof ProcessarEntidades);
        assertSame(form, abrir.form);
    }

    @Test
    public void testAbrirSerializado() {
        String arquivoJaw = "teste.jaw";
        abrir = new Abrir(form, arquivoJaw);
        assertNull(abrir.xml);
        assertFalse(abrir.form instanceof ProcessarEntidades);
    }

    @Test
    public void testAbrirJFileChooserXmlFilter() {
        String arquivoXml = "teste.jaw.xml";
        abrir = new Abrir(form, Abrir.ABRIR_XML);
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.getSelectedFiles()).thenReturn(new File[]{new File(arquivoXml)});

        // When the method is invoked
        abrir.abrir(dialog);

        // Then the xml property should be set to the selected file
        ArgumentCaptor<String> xmlCaptor = ArgumentCaptor.forClass(String.class);
        verify(form).setNomeArquivoSalvo(xmlCaptor.capture());
        assertEquals(arquivoXml, xmlCaptor.getValue());

        // And the entidades property should be populated
        assertEquals(abrir.form.getEntidades(), abrir.form.getEntidades());
    }

    @Test
    public void testAbrirJFileChooserSerializadoFilter() {
        String arquivoJaw = "teste.jaw";
        abrir = new Abrir(form, Abrir.ABRIR);
        FileDialog dialog = mock(FileDialog.class);
        when(dialog.getSelectedFiles()).thenReturn(new File[]{new File(arquivoJaw)});

        // When the method is invoked
        abrir.abrir(dialog);

        // Then the xml property should be empty
        assertNull(abrir.xml);

        // And the entidades property should be populated
        verify(form).setEntidades(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirJFileChooserNullFileDialog() {
        abrir = new Abrir(form, Abrir.ABRIR);
        abrir.abrir(null);
    }

    @Test
    public void testAbrirXmlFileFilterAccept() {
        assertTrue(filtroXml.accept(new File("teste.jaw.xml")));
        assertTrue(filtroXml.accept(new File("teste.jaw.xml.bak")));
        assertFalse(filtroXml.accept(new File("teste.xml")));
        assertFalse(filtroXml.accept(new File("teste.txt")));
        assertFalse(filtroXml.accept(new File("teste")));
    }

    @Test
    public void testAbrirJawFileFilterAccept() {
        assertTrue(filtroJaw.accept(new File("teste.jaw")));
        assertTrue(filtroJaw.accept(new File("teste.jaw.bak")));
        assertFalse(filtroJaw.accept(new File("teste.xml")));
        assertFalse(filtroJaw.accept(new File("teste.txt")));
        assertFalse(filtroJaw.accept(new File("teste")));
    }

    @Test
    public void testAbrirFileFilterDescription() {
        assertEquals("XML JAW files", filtroXml.getDescription());
        assertEquals("JAW files", filtroJaw.getDescription());
    }

    @Test
    public void testAbrirExceptionWhenReadingXmlFile() throws IOException {
        String arquivoXml = "teste.jaw.xml";
        when(new FileReader(arquivoXml).read()).thenThrow(new IOException("Simulado"));
        abrir = new Abrir(form, arquivoXml);
        assertEquals("teste.jaw.xml", abrir.xml);
        assertTrue(abrir.form instanceof ProcessarEntidades);
        assertSame(form, abrir.form);
        assertEquals("Arquivo xml corrompido ou incompatível com aplicação!", abrir.form.getMensagemDeErro());
    }

    @Test
    public void testAbrirExceptionWhenReadingSerializadoFile() throws IOException {
        String arquivoJaw = "teste.jaw";
        when(new FileReader(arquivoJaw)).thenThrow(new /******/InputMismatchException("Simulado"));
        abrir = new Abrir(form, arquivoJaw);
        assertNull(abrir.xml);
        assertFalse(abrir.form instanceof ProcessarEntidades);
        assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!", abrir.form.getMensagemDeErro());
    }

}