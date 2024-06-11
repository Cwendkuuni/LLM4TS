package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.processor);
        appFrame.init();
        assertNotNull(appFrame.processor);
    }

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init(); // Initialize with a dummy sitemap file

        appFrame.build(false);
        assertTrue(appFrame.statusLabel.getText().contains("Building..."));
        // Add assertions for expected output in progressArea and statusLabel

        appFrame.build(false); // Build again, it should be faster
        assertTrue(appFrame.statusLabel.getText().contains("Ready"));

        appFrame.build(true); // Force build
        assertTrue(appFrame.statusLabel.getText().contains("Building..."));
        // Add assertions for expected output in progressArea and statusLabel
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show(); // Display the frame, but don't interact with it

        appFrame.exit();
        // Perform assertions to check if the frame is closed and system exits
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("test-properties.xml"); // A file with test properties

        appFrame.loadProperties(propertyFile);
        assertEquals("test-sitemap.xml", appFrame.sitemapFile.getValue());
        assertEquals("test-source-dir", appFrame.sourceDir.getValue());
        assertEquals("test-target-url", appFrame.targetURL.getValue());
        assertNull(appFrame.password);

        appFrame.savePropertyFile(); // Save the properties after loading

        appFrame.loadProperties(propertyFile); // Load again to test if the properties were saved correctly
        assertEquals("test-sitemap.xml", appFrame.sitemapFile.getValue());
        assertEquals("test-source-dir", appFrame.sourceDir.getValue());
        assertEquals("test-target-url", appFrame.targetURL.getValue());
        assertNull(appFrame.password);
    }
}