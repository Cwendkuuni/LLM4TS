import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {
    @Test
    public void testConstructor() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Test", false, null, null, null);
        assertNotNull(fontChooserDialog);
    }
}