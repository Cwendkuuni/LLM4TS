package jaw.entrada;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {
    
    
    @Test
    public void testAbrirXMLWithValidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        assertNotNull(abrir.getXml());
        assertNotNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithValidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20, new File("test.jaw"));
        assertNotNull(abrir.getXml());
        assertNotNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirXMLWithInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.txt");
        assertEquals("", abrir.getXml());
        assertNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20, new File("test.txt"));
        assertEquals("", abrir.getXml());
        assertNull(abrir.getForm().getEntidades());
    }

    @Test(expected = Exception.class)
    public void testAbrirXMLWithNullFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, null);
    }

    @Test(expected = Exception.class)
    public void testAbrirSerializadoWithNullFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20, null);
    }

    @Test
    public void testOpenDialogFileFilterXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 10);
        assertNotNull(abrir.getForm().getFileFilter());
        assertEquals("XML JAW files", abrir.getForm().getFileFilter().getDescription());
    }

    @Test
    public void testOpenDialogFileFilterJAW() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20);
        assertNotNull(abrir.getForm().getFileFilter());
        assertEquals("JAW files", abrir.getForm().getFileFilter().getDescription());
    }

    
}