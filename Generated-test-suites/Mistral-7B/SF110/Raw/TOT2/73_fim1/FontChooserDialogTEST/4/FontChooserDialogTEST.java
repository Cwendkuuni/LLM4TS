import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class FontChooserDialogTest {

    @Test
    public void testFontChooserDialogConstructor() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(new JFrame(), "Test Title", true, new JFontChooser(), null, null);
        assertNotNull(fontChooserDialog);
    }
}