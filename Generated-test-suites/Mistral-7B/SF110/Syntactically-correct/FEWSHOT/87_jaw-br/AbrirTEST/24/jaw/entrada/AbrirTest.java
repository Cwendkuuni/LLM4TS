package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

public class AbrirTest {

    @Test
    public void testAbrirXMLWithValidXMLFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "src/test/resources/validXMLFile.jaw.xml");
        assertNotNull(abrir.getXml());
    }

    @Test(expected = AssertionError.class)
    public void testAbrirXMLWithInvalidXMLFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "src/test/resources/invalidXMLFile");
        fail("Expected an error message");
    }

    @Test
    public void testAbrirSerializadoWithValidJawFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR, new File("src/test/resources/validJawFile.jaw"));
        assertNotNull(abrir.getXml());
    }

    @Test(expected = AssertionError.class)
    public void testAbrirSerializadoWithInvalidJawFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR, new File("src/test/resources/invalidJawFile"));
        fail("Expected an error message");
    }

    @Test
    public void testFileChooserShowsJawFilesFilter() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        File[] files = abrir.getJFileChooser().getFilesSelected();
        FileFilter jawFilter = abrir.getJFileChooser().getFileFilter();
        assertTrue(jawFilter instanceof Filtro);
        File validJawFile = new File("src/test/resources/validJawFile.jaw");
        FileDialog dialog = abrir.getJFileChooser().getDialog(null, "Open Dialog");
        dialog.setFileFilter(jawFilter);
        dialog.setCurrentDirectory(new File("."));
        dialog.setSelectedFile(validJawFile);
        dialog.setVisible(true);
        assertEquals(validJawFile, files[0]);
    }

    @Test
    public void testFileChooserShowsXMLFilesFilter() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        File[] files = abrir.getJFileChooser().getFilesSelected();
        FileFilter xmlFilter = abrir.getJFileChooser().getFileFilter();
        assertTrue(xmlFilter instanceof Filtro2);
        File validXMLFile = new File("src/test/resources/validXMLFile.jaw.xml");
        FileDialog dialog = abrir.getJFileChooser().getDialog(null, "Open Dialog");
        dialog.setFileFilter(xmlFilter);
        dialog.setCurrentDirectory(new File("."));
        dialog.setSelectedFile(validXMLFile);
        dialog.setVisible(true);
        assertEquals(validXMLFile, files[0]);
    }

    @Test
    public void testAbrirWithInvalidFileExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        String invalidFile = "src/test/resources/invalidFile.txt";
        Abrir abrir = new Abrir(form, Abrir.ABRIR, invalidFile);
        assertEquals("", abrir.getXml());
    }
}