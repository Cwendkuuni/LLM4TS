import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {
    
    @Test
    public void testAbrirXML() {
        String arquivo = "test.jaw.xml";
        Abrir obj = new Abrir(new ProcessarEntidades(), arquivo);
        assertEquals("test.jaw.xml", obj.getXml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirInvalidXML() {
        String arquivo = "test.txt";
        Abrir obj = new Abrir(new ProcessarEntidades(), arquivo);
        fail("No exception was thrown when trying to open an invalid file.");
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testAbrirMissingFile() {
        String arquivo = "non_existent_file.jaw.xml";
        Abrir obj = new Abrir(new ProcessarEntidades(), arquivo);
        fail("No exception was thrown when trying to open a missing file.");
    }

    @Test
    public void testAbrirSerializado() {
        String arquivo = "test.jaw";
        Abrir obj = new Abrir(new ProcessarEntidades(), Abrir.ABRIR);
        assertNotNull(obj.getForm().getEntidades());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirInvalidArquivo() {
        String arquivo = "test.txt";
        Abrir obj = new Abrir(new ProcessarEntidades(), Abrir.ABRIR);
        fail("No exception was thrown when trying to open an invalid file.");
    }

    @Test(expected = FileNotFoundException.class)
    public void testAbrirMissingFileWithOpenDialog() {
        Abrir obj = new Abrir(new ProcessarEntidades(), Abrir.ABRIR);
        fail("No exception was thrown when trying to open a missing file using the open dialog.");
    }
    
    @Test
    public void testFileFilterXML() {
        JFileChooser escolhedor = new JFileChooser();
        Filtro2 filtro = new Filtro2();
        escolhedor.setFileFilter(filtro);
        File file = new File("test.xml");
        File[] files = new File[] {file};
        escolhedor.setFilesOfType(files);
        assertTrue(escolhedor.isFileSelectionEnabled());
        assertTrue(escolhedor.isFileFilterEnabled());
        assertTrue(escolhedor.getFileFilter() instanceof Filtro2);
    }

    @Test
    public void testFileFilterSerializado() {
        JFileChooser escolhedor = new JFileChooser();
        Filtro filtro = new Filtro();
        escolhedor.setFileFilter(filtro);
        File file = new File("test.jaw");
        File[] files = new File[] {file};
        escolhedor.setFilesOfType(files);
        assertTrue(escolhedor.isFileSelectionEnabled());
        assertTrue(escolhedor.isFileFilterEnabled());
        assertTrue(escolhedor.getFileFilter() instanceof Filtro);
    }
    
}