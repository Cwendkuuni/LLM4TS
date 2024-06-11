import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {
    @Test
    public void testConstructor() {
        // Create an instance of the LagoonGUI class
        new LagoonGUI();

        // Check if the constructor executed successfully
        assertNotNull(new LagoonGUI().getContentPane());
    }

    @Test
    public void testLoadProperties() {
        // Set up the LagoonGUI instance
        final LagoonGUI appFrame = new LagoonGUI();

        // Mock the file input and set its value
        final File mockFile = new File("test.properties");
        appFrame.loadProperties(mockFile);

        // Check if the properties were loaded correctly
        assertEquals("expectedSitemapFile", appFrame.sitemapFile.getValue());
        assertEquals("expectedSourceDir", appFrame.sourceDir.getValue());
        assertEquals("expectedTargetURL", appFrame.targetURL.getValue());
        assertEquals("expectedPassword", appFrame.password);
    }

    @Test(expected = LagoonException.class)
    public void testLoadPropertiesWithMissingProperty() {
        // Set up the LagoonGUI instance
        final LagoonGUI appFrame = new LagoonGUI();

        // Mock the file input and set its value
        final File mockFile = new File("test.properties");
        appFrame.loadProperties(mockFile);

        // Check if an exception is thrown when a required property is missing
        assertNull(appFrame.password);
        appFrame.loadProperties(mockFile);
    }

    @Test
    public void testBuild() {
        // Set up the LagoonGUI instance
        final LagoonGUI appFrame = new LagoonGUI();

        // Mock the file inputs and set their values
        appFrame.sitemapFile = new InputComponent(appFrame, "Sitemap file", "testSitemap.xml");
        appFrame.sourceDir = new InputComponent(appFrame, "Source directory", "testSourceDir");
        appFrame.targetURL = new InputComponent(appFrame, "Target URL", "testTargetURL");

        // Call the build method
        appFrame.build(false);

        // Check if the method executed successfully
        assertTrue(appFrame.statusLabel.getText().contains("Building..."));

        // Simulate the build process completion and check the status label
        appFrame.statusLabel.setText("Build finished successfully");
        assertEquals("Build finished successfully", appFrame.statusLabel.getText());
    }

    @Test(expected = IOException.class)
    public void testBuildWithIOException() {
        // Set up the LagoonGUI instance
        final LagoonGUI appFrame = new LagoonGUI();

        // Mock the file inputs and set their values
        appFrame.sitemapFile = new InputComponent(appFrame, "Sitemap file", "testSitemap.xml");
        appFrame.sourceDir = new InputComponent(appFrame, "Source directory", "testSourceDir");
        appFrame.targetURL = new InputComponent(appFrame, "Target URL", "testTargetURL");

        // Call the build method with an IOException
        appFrame.build(false);
        appFrame.progressArea.setText("IOException thrown during build process");

        // Check if an exception is thrown
        appFrame.statusLabel.setText("Build finished with error(s)");
        assertEquals("Build finished with error(s)", appFrame.statusLabel.getText());
    }
}