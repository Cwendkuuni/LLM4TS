package osa.ora.server.client.ui.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {
    @Test
    public void testConstructor() {
        FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, new JFontChooser() {
            // empty implementation
        }, null, null);
        assertNotNull(dialog);
    }
}