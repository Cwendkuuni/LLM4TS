package jaw.entrada;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {
    private Abrir abrir;
    private ProcessarEntidades form;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        abrir = new Abrir(form, "test.jaw.xml");
    }

    @Test
    public void testAbrirXML() {
        // Testing normal case
        assertNotNull(abrir.getForm().getEntidades());

        // Testing error case when file is not a valid JAW XML
        abrir = new Abrir(form, "test.txt");
        assertNull(abrir.getForm().getEntidades());

        // Testing error case when file is a valid JAW XML but unable to read
        abrir = new Abrir(form, "test.jaw.xml.corrupted");
        assertNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirSerializado() {
        // Testing normal case
        abrir = new Abrir(form, "test.jaw");
        assertNotNull(abrir.getForm().getEntidades());

        // Testing error case when file is not a valid JAW
        abrir = new Abrir(form, "test.txt");
        assertNull(abrir.getForm().getEntidades());
    }

    @Test
    public void testAbrirDialog() {
        // Testing normal case
        abrir = new Abrir(form, Abrir.ABRIR);
        assertNotNull(abrir.getForm().getEntidades());

        // Testing error case when no file is selected
        abrir = new Abrir(form, Abrir.ABRIR);
        assertNull(abrir.getForm().getEntidades());

        // Testing error case when file filter is incorrect
        abrir = new Abrir(form, Abrir.ABRIR_XML);
        assertNull(abrir.getForm().getEntidades());
    }
}