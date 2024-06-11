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

    @Test(expected = FileNotFoundException.class)
    public void testBuildThrowsFileNotFoundExceptionWhenSitemapFileDoesNotExist() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("nonexistentfile.xml");
        appFrame.init();
        appFrame.build(false);
    }

    @Test
    public void testBuildSavesProgressAreaOutputToString() {
        LagoonGUI appFrame = new LagoonGUI();
        String expectedOutput = "--- Start build ---\nBuilding...\n--- Build finished successfully 2s ---";
        appFrame.progressArea = new TextArea(expectedOutput, 5, 50, 1);
        appFrame.init();
        appFrame.build(false);
        assertEquals(expectedOutput, appFrame.progressArea.getText().trim());
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitThrowsAuthenticationMissingExceptionWhenPasswordMissing() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
    }

    @Test
    public void testInitDoesNotThrowAuthenticationMissingExceptionWhenPasswordPresent() {
        LagoonGUI appFrame = new LagoonGUI();
        String password = "password";
        appFrame.password = password;
        try {
            appFrame.init();
        } catch (Exception e) {
            fail("Init should not throw exception when password is present");
        }
    }

    @Test
    public void testLoadPropertiesFromFile() {
        LagoonGUI appFrame = new LagoonGUI();
        String sitemapFileValue = "sitemap.xml";
        String sourceDirValue = "source";
        String targetURLValue = "http://example.com";
        String propertyFileValue = "properties.txt";

        File propertyFile = new File(propertyFileValue);
        try (PrintWriter writer = new PrintWriter(propertyFile)) {
            writer.println("sitemapFile=" + sitemapFileValue);
            writer.println("sourceDir=" + sourceDirValue);
            writer.println("targetURL=" + targetURLValue);
            writer.println("password=password");
        }

        appFrame.loadPropertyFile();
        File sitemapFile = new File(appFrame.sitemapFile.getValue());
        assertEquals(sitemapFileValue, appFrame.sitemapFile.getValue());
        assertEquals(sourceDirValue, appFrame.sourceDir.getValue());
        assertEquals(targetURLValue, appFrame.targetURL.getValue());
        assertEquals("password", appFrame.password);
    }

    @Test
    public void testSavePropertiesToFile() {
        LagoonGUI appFrame = new LagoonGUI();
        String sitemapFileValue = "sitemap.xml";
        String sourceDirValue = "source";
        String targetURLValue = "http://example.com";
        String password = "password";
        String propertyFileValue = "properties.txt";

        File propertyFile = new File(propertyFileValue);
        try (PrintWriter writer = new PrintWriter(propertyFile)) {
            appFrame.loadProperties(propertyFile);
            appFrame.sitemapFile.setValue(sitemapFileValue);
            appFrame.sourceDir.setValue(sourceDirValue);
            appFrame.targetURL.setValue(targetURLValue);
            appFrame.password = password;
            appFrame.savePropertyFile();
        }

        String expectedOutput = "sitemapFile=" + sitemapFileValue + "\nsourceDir=" + sourceDirValue + "\ntargetURL=" + targetURLValue + "\npassword=password";
        String actualOutput = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(propertyFile))) {
            while ((actualOutput = reader.readLine()) != null) {
                // empty
            }
        } catch (IOException e) {
            fail("Failed to read property file: " + e.getMessage());
        }
        assertEquals(expectedOutput, actualOutput);
    }
}