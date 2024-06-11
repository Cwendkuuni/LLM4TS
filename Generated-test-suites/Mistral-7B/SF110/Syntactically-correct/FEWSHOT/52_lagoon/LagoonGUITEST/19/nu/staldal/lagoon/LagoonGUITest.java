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
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue("testSourceDir");
        appFrame.targetURL.setValue("http://localhost:8080");
        appFrame.build(false);
        assertNotNull(appFrame.processor);
        assertEquals("Building...", appFrame.statusLabel.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithNullSitemapFile() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sourceDir.setValue("testSourceDir");
        appFrame.targetURL.setValue("http://localhost:8080");
        appFrame.sitemapFile.setValue(null);
        appFrame.build(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithEmptySourceDir() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue("");
        appFrame.targetURL.setValue("http://localhost:8080");
        appFrame.build(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithEmptyTargetURL() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue("testSourceDir");
        appFrame.targetURL.setValue("");
        appFrame.build(false);
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        appFrame.exitButton.doClick();
        assertEquals(0, System.exitCode());
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        File testPropertyFile = new File("testPropertyFile.properties");
        try {
            FileWriter writer = new FileWriter(testPropertyFile);
            writer.write("sitemapFile=testSitemap.xml\nsourceDir=testSourceDir\ntargetURL=http://localhost:8080\npassword=testPassword");
            writer.close();
            appFrame.loadProperties(testPropertyFile);
            assertEquals("testSitemap.xml", appFrame.sitemapFile.getValue());
            assertEquals("testSourceDir", appFrame.sourceDir.getValue());
            assertEquals("http://localhost:8080", appFrame.targetURL.getValue());
            assertEquals("testPassword", appFrame.password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue("testSourceDir");
        appFrame.targetURL.setValue("http://localhost:8080");
        appFrame.password = "testPassword";
        appFrame.savePropertyFile();
        File testPropertyFile = new File("testPropertyFile.properties");
        try {
            FileInputStream fis = new FileInputStream(testPropertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            assertEquals("testSitemap.xml", prop.getProperty("sitemapFile"));
            assertEquals("testSourceDir", prop.getProperty("sourceDir"));
            assertEquals("http://localhost:8080", prop.getProperty("targetURL"));
            assertEquals("testPassword", prop.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}