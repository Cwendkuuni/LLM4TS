package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.processor);
        assertNull(appFrame.statusLabel.getText());
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("testPropertyFile.txt");
        try {
            FileOutputStream fos = new FileOutputStream(propertyFile);
            Properties prop = new Properties();
            prop.setProperty("sitemapFile", "testSitemap.xml");
            prop.setProperty("sourceDir", "testSourceDir");
            prop.setProperty("targetURL", "testTargetURL");
            prop.store(fos, "Lagoon properties");
            fos.close();
            appFrame.loadPropertyFile();
            assertEquals("testSitemap.xml", appFrame.sitemapFile.getValue());
            assertEquals("testSourceDir", appFrame.sourceDir.getValue());
            assertEquals("testTargetURL", appFrame.targetURL.getValue());
            assertEquals("Lagoon properties", appFrame.statusLabel.getText());
        } catch (IOException e) {
            fail("Error loading property file: " + e.getMessage());
        } finally {
            if (propertyFile.exists()) {
                propertyFile.delete();
            }
        }
    }

    @Test
    public void testSaveProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue("testSourceDir");
        appFrame.targetURL.setValue("testTargetURL");
        appFrame.savePropertyFile();
        assertEquals("Properties saved", appFrame.statusLabel.getText());
        File propertyFile = new File("testPropertyFile.txt");
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            assertEquals("testSitemap.xml", prop.getProperty("sitemapFile"));
            assertEquals("testSourceDir", prop.getProperty("sourceDir"));
            assertEquals("testTargetURL", prop.getProperty("targetURL"));
        } catch (IOException e) {
            fail("Error reading property file: " + e.getMessage());
        } finally {
            if (propertyFile.exists()) {
                propertyFile.delete();
            }
        }
    }

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        File sitemapFile = new File("testSitemap.xml");
        File sourceDir = new File("testSourceDir");
        File targetURL = new File("testTargetURL");
        appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
        appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
        appFrame.targetURL.setValue(targetURL.getAbsolutePath());
        appFrame.build(false);
        assertTrue("Build was not called", appFrame.processor != null);
    }

    @Test(expected = LagoonException.class)
    public void testBuildWithInvalidPropertyFile() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue("testSourceDir");
        appFrame.targetURL.setValue("testTargetURL");
        appFrame.loadPropertyFile(); // Throws an exception
        appFrame.build(false);
        fail("Build was called instead of throwing an exception");
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        appFrame.exitButton.doClick();
        assertEquals(0, System.exit(0));
    }
}