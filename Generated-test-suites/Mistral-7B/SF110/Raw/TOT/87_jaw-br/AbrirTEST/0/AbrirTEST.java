package jaw.entrada;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {
    
    
    @Test
    public void testAbrirXMLWithValidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertNotNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirXMLWithInvalidFileExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.txt";
        Abrir abrir = new Abrir(form, arquivo);
        assertNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirXMLWithNullFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, null);
        assertNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithValidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw";
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        assertNotNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithInvalidFileExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.txt";
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        assertNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirSerializadoWithNullFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        assertNull(abrir.getForm().getEntidades());
    }

    @Test(expected = Exception.class)
    public void testAbrirXMLWithCorruptedFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.xml.corrupted";
        new Abrir(form, arquivo).abrirXML(arquivo);
    }

    @Test(expected = Exception.class)
    public void testAbrirSerializadoWithCorruptedFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "test.jaw.corrupted";
        new Abrir(form, Abrir.ABRIR).abrirSerializado(arquivo);
    }

    
}