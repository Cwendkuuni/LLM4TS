package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrir;

    @Before
    public void setup() {
        form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, null);
    }

    @Test
    public void testConstructorWithNullProcessarEntidades() {
        assertThrows(NullPointerException.class, () -> new Abrir(null, ""));
    }

    @Test
    public void testConstructorWithNullString() {
        assertThrows(NullPointerException.class, () -> new Abrir(form, null));
    }

    @Test
    public void testConstructorWithJawXmlFile() {
        final String arquivo = "teste.jaw.xml";
        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));
        abrir = new Abrir(form, arquivo);
        assertEquals(arquivo, abrir.xml);
    }

    @Test
    public void testConstructorWithJawFile() {
        final String arquivo = "teste.jaw";
        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));
        abrir = new Abrir(form, arquivo);
        assertNull(abrir.xml);
    }

    @Test
    public void testConstructorWithJFileChooserAndJawXmlFile() {
        final String arquivo = "teste.jaw.xml";
        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivo));

        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));
        abrir = new Abrir(form, escolhedor, 10);
        assertEquals(arquivo, abrir.xml);
    }

    @Test
    public void testConstructorWithJFileChooserAndJawFile() {
        final String arquivo = "teste.jaw";
        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(new File(arquivo));

        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));
        abrir = new Abrir(form, escolhedor, 20);
        assertNull(abrir.xml);
    }

    @Test
    public void testAbrirSerializadoWithJawFile() {
        final String arquivo = "teste.jaw";
        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));

        abrir = new Abrir(form, arquivo);
        assertNull(abrir.xml);
    }

    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        final String arquivo = "teste.txt";
        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));

        abrir = new Abrir(form, arquivo);
        assertNull(abrir.xml);
    }

    @Test
    public void testAbrirXMLWithJawXmlFile() {
        final String arquivo = "teste.jaw.xml";
        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));

        abrir = new Abrir(form, arquivo);
        assertNull(abrir.xml);
    }

    @Test
    public void testAbrirXMLWithInvalidFile() {
        final String arquivo = "teste.txt";
        when(form.getCaminhoDosProjetos()).thenReturn(new File("teste"));

        abrir = new Abrir(form, arquivo);
        assertNull(abrir.xml);
    }

    @Test
    public void testFiltroAccept() {
        final FileFilter filtro = new Abrir().new Filtro2();

        final File file = new File("teste.txt");
        assertTrue(filtro.accept(file));

        final File file2 = new File("teste.jaw.xml");
        assertTrue(filtro.accept(file2));

        final File file3 = new File("teste.jaw");
        assertTrue(filtro.accept(file3));

        final File file4 = new File("teste.xml");
        assertFalse(filtro.accept(file4));
    }

    @Test
    public void testFiltroDescription() {
        final FileFilter filtro = new Abrir().new Filtro2();
        assertEquals("XML JAW files", filtro.getDescription());
    }

    @Test
    public void testFiltro2Accept() {
        final FileFilter filtro = new Abrir().new Filtro();

        final File file = new File("teste.txt");
        assertTrue(filtro.accept(file));

        final File file2 = new File("teste.jaw.xml");
        assertTrue(filtro.accept(file2));

        final File file3 = new File("teste.jaw");
        assertTrue(filtro.accept(file3));

        final File file4 = new File("teste.xml");
        assertFalse(filtro.accept(file4));
    }

    @Test
    public void testFiltroDescription2() {
        final FileFilter filtro = new Abrir().new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

    @Test
    public void testAbrirWithJFileChooserNullSelectedFile() {
        final JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.showOpenDialog(null)).thenThrow(HeadlessException.class);

        abrir = new Abrir(form, escolhedor, 10);
        assertNull(abrir.xml);
    }
}