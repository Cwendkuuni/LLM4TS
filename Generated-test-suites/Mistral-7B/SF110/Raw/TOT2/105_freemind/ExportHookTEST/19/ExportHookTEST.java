package freemind.extensions;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.main.FreeMindFrame;
import freemind.modes.DefaultMapController;
import org.junit.Test;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ModeController controller = new DefaultMapController();
        ExportHook exportHook = new ExportHook();
        File chosenFile = exportHook.chooseFile("png", "Save as PNG", null, controller);
        assertNotNull(chosenFile);

        // Test edge cases and exception handling
        assertNull(exportHook.chooseFile(null, null, null, null));
        assertNull(exportHook.chooseFile("", null, null, null));
        assertNull(exportHook.chooseFile("png", null, null, null));
        assertNull(exportHook.chooseFile("png", "Save as PNG", null, null));
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceString = exportHook.getTranslatableResourceString("%msg1%");
        assertNotNull(resourceString);

        // Test edge cases and exception handling
        assertNull(exportHook.getTranslatableResourceString(null));
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);

        // Test edge cases and exception handling
        assertNull(exportHook.createBufferedImage());
        exportHook.setView(null);
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "freemind/extensions";
        String fileName = "images/test.png";
        String destinationDirectory = "target/test-output";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File file = new File(destinationDirectory, fileName);
        assertFalse(file.exists()); // Test for negative case

        // Test edge cases and exception handling
        assertThrows(NullPointerException.class, () -> exportHook.copyFromResource(null, null, null));
        assertThrows(NullPointerException.class, () -> exportHook.copyFromResource("", null, null));
        assertThrows(NullPointerException.class, () -> exportHook.copyFromResource(prefix, null, null));
        assertThrows(NullPointerException.class, () -> exportHook.copyFromResource(prefix, fileName, null));
        assertThrows(IOException.class, () -> exportHook.copyFromResource("freemind/extensions", "images/test.png", "target/test-output/"));
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "src/test/resources";
        String fileName = "images/test.png";
        String destinationDirectory = "target/test-output";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());

        // Test edge cases and exception handling
        assertThrows(NullPointerException.class, () -> exportHook.copyFromFile(null, null, null));
        assertThrows(NullPointerException.class, () -> exportHook.copyFromFile("", null, null));
        assertThrows(NullPointerException.class, () -> exportHook.copyFromFile(dir, null, null));
        assertThrows(NullPointerException.class, () -> exportHook.copyFromFile(dir, fileName, null));
        assertThrows(IOException.class, () -> exportHook.copyFromFile("src/test/resources", "images/test.png", "target/test-output/"));
    }
}