import org.junit.Test;
import static org.junit.Assert.*;
import osa.ora.server.client.ui.utils.FontChooserDialog;

public class FontChooserDialogTest {
    @Test
    public void testFontChooserDialogConstructor() {
        Component c = new JLabel();
        String title = "FontChooserDialog";
        boolean modal = true;
        JFontChooser chooserPane = new JFontChooser();
        ActionListener okListener = (ActionEvent e) -> {
        };
        ActionListener cancelListener = (ActionEvent e) -> {
        };
        FontChooserDialog fontChooserDialog = new FontChooserDialog(c, title, modal, chooserPane, okListener, cancelListener);
        assertNotNull(fontChooserDialog);
    }
}