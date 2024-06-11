package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.xtree.*;

import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        final LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.getProcessor());
        assertNull(appFrame.getStatusLabel().getText());
        assertEquals(0, appFrame.getSitemapLastModified());
    }

    @Test
    public void testBuildFalse() {
        final LagoonGUI appFrame = new LagoonGUI();
        appFrame.setSitemapFileValue("test.xml");
        appFrame.setSourceDirValue("testDir");
        appFrame.setTargetURLValue("http://localhost:8080");
        appFrame.init();
        assertNotNull(appFrame.getProcessor());
        assertTrue(appFrame.getStatusLabel().getText().startsWith("Initializing..."));
        appFrame.build(false);
        assertTrue(appFrame.getStatusLabel().getText().contains("Building..."));
    }

    @Test
    public void testBuildTrue() {
        final LagoonGUI appFrame = new LagoonGUI();
        appFrame.setSitemapFileValue("test.xml");
        appFrame.setSourceDirValue("testDir");
        appFrame.setTargetURLValue("http://localhost:8080");
        appFrame.init();
        assertNotNull(appFrame.getProcessor());
        assertTrue(appFrame.getStatusLabel().getText().startsWith("Initializing..."));
        appFrame.build(true);
        assertTrue(appFrame.getStatusLabel().getText().contains("Force build"));
    }

    @Test
    public void testLoadProperties() {
        final LagoonGUI appFrame = new LagoonGUI();
        final File propertiesFile = new File("test.properties");
        try {
            final FileOutputStream fos = new FileOutputStream(propertiesFile);
            final Properties prop = new Properties();
            prop.setProperty("sitemapFile", "test.xml");
            prop.setProperty("sourceDir", "testDir");
            prop.setProperty("targetURL", "http://localhost:8080");
            prop.setProperty("password", "testPassword");
            prop.store(fos, "Lagoon properties");
            fos.close();
            appFrame.loadPropertyFile();
            assertEquals("test.xml", appFrame.getSitemapFileValue());
            assertEquals("testDir", appFrame.getSourceDirValue());
            assertEquals("http://localhost:8080", appFrame.getTargetURLValue());
            assertNotNull(appFrame.getPassword());
            assertEquals("testPassword", appFrame.getPassword());
        } catch (IOException e) {
            fail("Error loading properties file: " + e.getMessage());
        }
    }

    @Test
    public void testSaveProperties() {
        final LagoonGUI appFrame = new LagoonGUI();
        appFrame.setSitemapFileValue("test.xml");
        appFrame.setSourceDirValue("testDir");
        appFrame.setTargetURLValue("http://localhost:8080");
        appFrame.setPassword("testPassword");
        appFrame.savePropertyFile();
        assertTrue(appFrame.getStatusLabel().getText().contains("Properties saved"));

        FileDialog propertyFileDialog = new FileDialog(appFrame, "Save property file", 1);
        propertyFileDialog.show();
        File propertyFile = (propertyFileDialog.getFile() == null) ? null : new File(propertyFileDialog.getDirectory(), propertyFileDialog.getFile());
        if (propertyFile != null) {
            try {
                FileInputStream fis = new FileInputStream(propertyFile);
                Properties prop = new Properties();
                prop.load(fis);
                fis.close();
                assertEquals("test.xml", prop.getProperty("sitemapFile"));
                assertEquals("testDir", prop.getProperty("sourceDir"));
                assertEquals("http://localhost:8080", prop.getProperty("targetURL"));
                assertEquals("testPassword", prop.getProperty("password"));
            } catch (IOException e) {
                fail("Error reading property file: " + e.getMessage());
            }
        }
    }
}