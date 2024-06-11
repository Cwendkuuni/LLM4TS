package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNull(lagoonGUI.processor);
        assertNull(lagoonGUI.statusLabel.getText());
        assertNull(lagoonGUI.progressArea.getText());
    }

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        assertNotNull(lagoonGUI.processor);
        lagoonGUI.build(false);
        assertTrue(lagoonGUI.progressArea.getText().contains("Start build"));
        assertTrue(lagoonGUI.statusLabel.getText().contains("Building..."));
        // Add more assertions based on the expected behavior of the build method
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        // This test case can't be fully covered as it involves system exit and can't be run in a test environment
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        File propertyFile = new File("testPropertyFile.properties");
        lagoonGUI.loadPropertyFile();
        assertNull(lagoonGUI.sitemapFile.getValue());
        assertNull(lagoonGUI.sourceDir.getValue());
        assertNull(lagoonGUI.targetURL.getValue());
        assertNull(lagoonGUI.password);

        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            String sitemapFileValue = getProperty(prop, "sitemapFile");
            String sourceDirValue = getProperty(prop, "sourceDir");
            String targetURLValue = getProperty(prop, "targetURL");
            String passwordValue = prop.getProperty("password");

            lagoonGUI.loadProperties(propertyFile);
            assertEquals(sitemapFileValue, lagoonGUI.sitemapFile.getValue());
            assertEquals(sourceDirValue, lagoonGUI.sourceDir.getValue());
            assertEquals(targetURLValue, lagoonGUI.targetURL.getValue());
            assertEquals(passwordValue, lagoonGUI.password);
        } catch (IOException e) {
            // Handle the exception as needed
        }
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.sitemapFile.setValue("testSitemapFile.xml");
        lagoonGUI.sourceDir.setValue("testSourceDir");
        lagoonGUI.targetURL.setValue("testTargetURL.com");
        lagoonGUI.password = "testPassword";
        File propertyFile = new File("testPropertyFile.properties");
        lagoonGUI.savePropertyFile();
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            String sitemapFileValue = getProperty(prop, "sitemapFile");
            String sourceDirValue = getProperty(prop, "sourceDir");
            String targetURLValue = getProperty(prop, "targetURL");
            String passwordValue = prop.getProperty("password");

            assertEquals("testSitemapFile.xml", sitemapFileValue);
            assertEquals("testSourceDir", sourceDirValue);
            assertEquals("testTargetURL.com", targetURLValue);
            assertNotNull(passwordValue);
        } catch (IOException e) {
            // Handle the exception as needed
        }
    }

    private static String getProperty(Properties prop, String name) throws LagoonException {
        LagoonGUI lagoonGUI = new LagoonGUI();
        return lagoonGUI.getProperty(prop, name);
    }
}