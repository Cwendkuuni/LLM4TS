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
        appFrame.init();
        appFrame.build(false);
        // Add more assertions based on the expected behavior of the build method
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.exit();
        // Add assertions to check if the application exits correctly
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("testPropertyFile.properties");
        try {
            PropertyResourceBundle resourceBundle = new PropertyResourceBundle(new FileInputResource(propertyFile));
            Properties prop = resourceBundle.getProperties();
            appFrame.loadProperties(propertyFile);
            String sitemapFileValue = appFrame.sitemapFile.getValue();
            String sourceDirValue = appFrame.sourceDir.getValue();
            String targetURLValue = appFrame.targetURL.getValue();
            String passwordValue = appFrame.password;
            // Add assertions to check if the properties are loaded correctly
        } catch (IOException e) {
            fail("Failed to load property file: " + e.getMessage());
        } finally {
            if (propertyFile.exists()) {
                propertyFile.delete();
            }
        }
    }

    @Test
    public void testSaveProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("testPropertyFile.properties");
        appFrame.loadProperties(propertyFile);
        appFrame.savePropertyFile();
        // Add assertions to check if the properties are saved correctly
        // You may need to use FileUtils or other libraries to read the file content
    }
}