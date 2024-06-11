package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.TextAreaWriter;

public class LagoonGUITest {

    private LagoonGUI appFrame;

    @Before
    public void setUp() {
        appFrame = new LagoonGUI();
    }

    @Test
    public void testConstructor() {
        assertNotNull(appFrame);
    }

    @Test
    public void testLoadPropertiesFile() {
        File propertyFile = new File("path/to/propertyFile.xml");
        appFrame.loadPropertiesFile();
        Properties prop = appFrame.getProperties();
        String sitemapFile = prop.getProperty("sitemapFile");
        String sourceDir = prop.getProperty("sourceDir");
        String targetURL = prop.getProperty("targetURL");
        String password = prop.getProperty("password");
        assertNotNull(sitemapFile);
        assertNotNull(sourceDir);
        assertNotNull(targetURL);
        assertNull(password); // by default, password should not be saved in the property file
    }

    @Test
    public void testSavePropertiesFile() {
        File propertyFile = new File("path/to/propertyFile.xml");
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "sitemap.xml");
        prop.setProperty("sourceDir", "source");
        prop.setProperty("targetURL", "http://example.com");
        prop.setProperty("password", "password123");
        appFrame.savePropertiesFile(propertyFile, prop);
        File savedPropertyFile = new File("path/to/propertyFile.xml");
        Properties loadedProp = new Properties();
        try (FileInputStream fis = new FileInputStream(savedPropertyFile)) {
            loadedProp.load(fis);
        }
        String sitemapFile = loadedProp.getProperty("sitemapFile");
        String sourceDir = loadedProp.getProperty("sourceDir");
        String targetURL = loadedProp.getProperty("targetURL");
        String password = loadedProp.getProperty("password");
        assertEquals("sitemap.xml", sitemapFile);
        assertEquals("source", sourceDir);
        assertEquals("http://example.com", targetURL);
        assertEquals("password123", password);
    }

    @Test(expected = LagoonException.class)
    public void testLoadPropertiesFileWithInvalidPropertyFile() {
        File propertyFile = new File("path/to/invalidPropertyFile.xml");
        appFrame.loadPropertiesFile();
    }

    @Test
    public void testInit() {
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("source");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.init();
        assertNotNull(appFrame.processor);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitWithInvalidSitemapFile() {
        appFrame.sitemapFile.setValue("nonExistentFile.xml");
        appFrame.sourceDir.setValue("source");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.init();
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidSourceDir() {
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("nonExistentDir");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.init();
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidTargetURL() {
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("source");
        appFrame.targetURL.setValue("http://example.com/nonExistentPage");
        appFrame.init();
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitWithMissingPassword() {
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("source");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.init();
    }

    @Test(expected = AuthenticationException.class)
    public void testInitWithInvalidPassword() {
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("source");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.password = "invalidPassword";
        appFrame.init();
    }

    @Test
    public void testBuild() {
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("source");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.init();
        appFrame.build(false);
        // Add assertions here to check the statusLabel and progressArea
    }

    @Test
    public void testBuildWithForce() {
        appFrame.sitemapFile.setValue("sitemap.xml");
        appFrame.sourceDir.setValue("source");
        appFrame.targetURL.setValue("http://example.com");
        appFrame.init();
        appFrame.build(true);
        // Add assertions here to check the statusLabel and progressArea
    }

    @Test
    public void testExit() {
        appFrame.exit();
        assertTrue(appFrame.isDisposed());
    }

    @Test
    public void testWindowClosing() {
        appFrame.show();
        appFrame.exitButton.doClick();
        assertTrue(appFrame.isDisposed());
    }

    @Test
    public void testLoadPropertiesWithValidFile() {
        File propertyFile = new File("path/to/propertyFile.xml");
        appFrame.loadProperties(propertyFile);
        Properties prop = appFrame.getProperties();
        String sitemapFile = prop.getProperty("sitemapFile");
        String sourceDir = prop.getProperty("sourceDir");
        String targetURL = prop.getProperty("targetURL");
        String password = prop.getProperty("password");
        assertNotNull(sitemapFile);
        assertNotNull(sourceDir);
        assertNotNull(targetURL);
        assertNull(password); // by default, password should not be saved in the property file
    }
}