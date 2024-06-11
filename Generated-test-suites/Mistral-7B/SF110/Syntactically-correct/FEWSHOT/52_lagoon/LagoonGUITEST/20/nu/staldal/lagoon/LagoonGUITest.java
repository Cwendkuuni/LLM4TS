package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.getProcessor());
        appFrame.init();
        assertNotNull(appFrame.getProcessor());
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("testPropertyFile.txt");
        appFrame.loadPropertyFile();
        assertNull(appFrame.getSitemapFile().getValue());
        assertNull(appFrame.getSourceDir().getValue());
        assertNull(appFrame.getTargetURL().getValue());
        assertNull(appFrame.getPassword());
        try {
            appFrame.loadProperties(propertyFile);
            String sitemapFileValue = appFrame.getSitemapFile().getValue();
            String sourceDirValue = appFrame.getSourceDir().getValue();
            String targetURLValue = appFrame.getTargetURL().getValue();
            String passwordValue = appFrame.getPassword();
            assertNotNull(sitemapFileValue);
            assertNotNull(sourceDirValue);
            assertNotNull(targetURLValue);
            assertNull(passwordValue);
        } catch (LagoonException e) {
            e.printStackTrace();
            fail("Failed to load properties from file");
        }
    }

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        File sitemapFile = new File("testSitemapFile.xml");
        appFrame.setSitemapFile(sitemapFile);
        try {
            appFrame.init();
            appFrame.build(false);
            fail("Expected build to fail without a processor");
        } catch (Exception e) {
            // Expected
        }

        appFrame.init();
        appFrame.getSitemapFile().setValue("testSitemapFile.xml");
        appFrame.getSourceDir().setValue("testSourceDir");
        appFrame.getTargetURL().setValue("testTargetURL");
        appFrame.build(false);
        assertTrue(appFrame.getStatusLabel().getText().contains("Building..."));
        // Add assertions for build success or failure based on your expectations
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.exit();
        fail("Expected System.exit(0) to terminate test");
    }

    @Test
    public void testLoadPropertiesWithPassword() {
        LagoonGUI appFrame = new LagoonGUI();
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "testSitemapFile.xml");
        prop.setProperty("sourceDir", "testSourceDir");
        prop.setProperty("targetURL", "testTargetURL");
        prop.setProperty("password", "testPassword");
        File propertyFile = new File("testPropertyFile.txt");
        try {
            FileWriter writer = new FileWriter(propertyFile);
            prop.store(writer, "Lagoon properties");
            writer.close();

            appFrame.loadPropertyFile();
            assertNull(appFrame.getPassword());

            appFrame.loadProperties(propertyFile);
            String passwordValue = appFrame.getPassword();
            assertNotNull(passwordValue);
            assertEquals("testPassword", passwordValue);

        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to write or read property file");
        }
    }
}