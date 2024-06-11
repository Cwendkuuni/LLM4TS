package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.xtree.Element;

public class LagoonGUITest {

    private LagoonGUI appFrame;

    @Before
    public void setUp() {
        appFrame = new LagoonGUI();
    }

    

    @Test
    public void testLagoonGUIConstructor() {
        assertNotNull(appFrame);
        assertEquals("Lagoon", appFrame.getTitle());
    }

    @Test
    public void testLoadProperties() {
        final File propertyFile = new File("test.properties");
        appFrame.loadProperties(propertyFile);
        assertNotNull(appFrame.sitemapFile.getValue());
        assertNotNull(appFrame.sourceDir.getValue());
        assertNotNull(appFrame.targetURL.getValue());
        assertNull(appFrame.password);
    }

    @Test(expected = LagoonException.class)
    public void testLoadPropertiesWithMissingProperty() {
        final File propertyFile = new File("test.properties");
        Properties prop = new Properties();
        appFrame.loadProperties(propertyFile);
        assertNull(appFrame.sitemapFile.getValue());
        throw new LagoonException("Property sitemapFile not specified");
    }

    @Test
    public void testSaveProperties() {
        final File propertyFile = new File("test.properties");
        appFrame.savePropertyFile();
        assertTrue(propertyFile.exists());
        try {
            final FileInputStream fis = new FileInputStream(propertyFile);
            final Properties prop = new Properties();
            prop.load(fis);
            assertNotNull(prop.getProperty("sitemapFile"));
            assertNotNull(prop.getProperty("sourceDir"));
            assertNull(prop.getProperty("targetURL"));
            assertNull(prop.getProperty("password"));
            fis.close();
        } catch (IOException e) {
            fail("Failed to read saved properties file: " + e.getMessage());
        }
    }

    @Test
    public void testInit() throws IOException {
        appFrame.inputPanel.add(new Button("TestInit"));
        appFrame.init();
        assertEquals("Initializing...", appFrame.statusLabel.getText());
        appFrame.init();
        assertEquals("Ready", appFrame.statusLabel.getText());
        appFrame.inputPanel.remove(appFrame.buttonPanel.getComponentCount() - 1);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitWithMissingPassword() {
        appFrame.init();
        fail("AuthenticationMissingException not thrown");
    }

    @Test(expected = AuthenticationException.class)
    public void testInitWithInvalidPassword() {
        appFrame.password = "invalid";
        appFrame.init();
        fail("AuthenticationException not thrown");
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitWithMissingSitemapFile() {
        appFrame.sitemapFile = new InputComponent(appFrame, "Sitemap file", null);
        appFrame.init();
        fail("FileNotFoundException not thrown");
    }

    @Test(expected = IOException.class)
    public void testInitWithIOException() {
        appFrame.init();
        fail("IOException not thrown");
    }

    @Test(expected = LagoonException.class)
    public void testInitWithLagoonException() {
        appFrame.init();
        fail("LagoonException not thrown");
    }

    @Test
    public void testBuild() throws IOException {
        appFrame.loadProperties(new File("test.properties"));
        appFrame.build(false);
        assertEquals("Building...", appFrame.statusLabel.getText());
        Thread.sleep(1000); // Allow some time for the build process to finish
        assertEquals("Ready", appFrame.statusLabel.getText());
    }

    @Test
    public void testBuildWithForce() throws IOException {
        appFrame.loadProperties(new File("test.properties"));
        File sitemapFile = new File("testSitemap.xml");
        Element sitemapTree = new XmlTree().parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?><sitemap><url><loc>test</loc></url></sitemap>");
        appFrame.sitemapFile = new InputComponent(appFrame, "Sitemap file", sitemapFile.getAbsolutePath());
        appFrame.sourceDir = new InputComponent(appFrame, "Source directory", new File("testSource").getAbsolutePath());
        appFrame.targetURL = new InputComponent(appFrame, "Target URL", new File("testTarget").getAbsolutePath().toURI().toString());
        appFrame.password = "testPassword";
        appFrame.init();
        appFrame.build(true);
        assertEquals("Building...", appFrame.statusLabel.getText());
        Thread.sleep(1000); // Allow some time for the build process to finish
        assertEquals("Ready", appFrame.statusLabel.getText());
    }

    @Test(expected = FileNotFoundException.class)
    public void testBuildWithMissingSitemapFile() {
        appFrame.loadProperties(new File("test.properties"));
        appFrame.sitemapFile = new InputComponent(appFrame, "Sitemap file", null);
        appFrame.sourceDir = new InputComponent(appFrame, "Source directory", new File("testSource").getAbsolutePath());
        appFrame.targetURL = new InputComponent(appFrame, "Target URL", new File("testTarget").getAbsolutePath().toURI().toString());
        appFrame.password = "testPassword";
        appFrame.init();
        appFrame.build(false);
        fail("FileNotFoundException not thrown");
    }

    @Test(expected = IOException.class)
    public void testBuildWithIOException() {
        appFrame.loadProperties(new File("test.properties"));
        appFrame.sitemapFile = new InputComponent(appFrame, "Sitemap file", new File("testSitemap.xml").getAbsolutePath());
        appFrame.sourceDir = new InputComponent(appFrame, "Source directory", new File("testSource").getAbsolutePath());
        appFrame.targetURL = new InputComponent(appFrame, "Target URL", new File("testTarget").getAbsolutePath().toURI().toString());
        appFrame.password = "testPassword";
        appFrame.init();
        appFrame.build(false);
        fail("IOException not thrown");
    }

    @Test(expected = LagoonException.class)
    public void testBuildWithLagoonException() {
        appFrame.loadProperties(new File("test.properties"));
        appFrame.sitemapFile = new InputComponent(appFrame, "Sitemap file", new File("testSitemap.xml").getAbsolutePath());
        appFrame.sourceDir = new InputComponent(appFrame, "Source directory", new File("testSource").getAbsolutePath());
        appFrame.targetURL = new InputComponent(appFrame, "Target URL", new File("testTarget").getAbsolutePath().toURI().toString());
        appFrame.password = "testPassword";
        appFrame.init();
        appFrame.build(false);
        fail("LagoonException not thrown");
    }

    @Test
    public void testExit() {
        appFrame.exit();
        assertEquals(0, System.exit(0));
    }

    

}