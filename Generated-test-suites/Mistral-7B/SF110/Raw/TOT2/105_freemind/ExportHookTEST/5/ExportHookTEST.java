import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.*;
import javax.swing.filechooser.*;
import java.text.*;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.image.*;

public class ExportHookTest {
    @Test
    public void testChooseFile_ValidFile() {
        // ... (same as Expert 1)
    }

    @Test
    public void testChooseFile_IncorrectExtension() {
        // ... (same as Expert 1)
    }

    @Test
    public void testChooseFile_UserCancelsDialog() {
        // ... (same as Expert 1)
    }

    @Test
    public void testGetTranslatableResourceString_ValidResourceKey() {
        // ... (same as Expert 2)
    }

    @Test
    public void testGetTranslatableResourceString_NullResourceKey() {
        // ... (same as Expert 2)
    }

    @Test
    public void testGetTranslatableResourceString_ResourceBundleNotFound() {
        // ... (same as Expert 2)
    }

    @Test
    public void testCreateBufferedImage_ValidView() {
        // ... (same as Expert 3)
    }

    @Test
    public void testCreateBufferedImage_NullView() {
        // ... (same as Expert 3)
    }

    @Test
    public void testCreateBufferedImage_ViewFailsToPrepareForPrinting() {
        // ... (same as Expert 3)
    }
}