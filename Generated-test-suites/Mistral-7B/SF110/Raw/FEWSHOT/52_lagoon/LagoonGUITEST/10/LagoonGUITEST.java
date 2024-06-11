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
        assertTrue(appFrame.statusLabel.getText().contains("Building..."));
        // Add more assertions based on the expected behavior of the 'build' method
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        appFrame.exit();
        assertFalse(appFrame.isDisplayable());
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("test.properties");
        try {
            FileWriter writer = new FileWriter(propertyFile);
            writer.write("sitemapFile=test.xml\nsourceDir=testDir\ntargetURL=http://test.com\npassword=testPassword");
            writer.close();
            appFrame.loadProperties(propertyFile);
            assertEquals("test.xml", appFrame.sitemapFile.getValue());
            assertEquals("testDir", appFrame.sourceDir.getValue());
            assertEquals("http://test.com", appFrame.targetURL.getValue());
            assertEquals("testPassword", appFrame.password);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (propertyFile.exists()) {
                propertyFile.delete();
            }
        }
    }

    @Test
    public void testSaveProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("test.xml");
        appFrame.sourceDir.setValue("testDir");
        appFrame.targetURL.setValue("http://test.com");
        appFrame.password = "testPassword";
        File propertyFile = new File("test.properties");
        appFrame.savePropertyFile();
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            assertEquals("test.xml", prop.getProperty("sitemapFile"));
            assertEquals("testDir", prop.getProperty("sourceDir"));
            assertEquals("http://test.com", prop.getProperty("targetURL"));
            assertNull(prop.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (propertyFile.exists()) {
                propertyFile.delete();
            }
        }

        // Add more tests for other methods in the LagoonGUI class
    }
}