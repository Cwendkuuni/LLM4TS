package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PasswordDialog;
import nu.staldal.lagoon.core.YesNoQueryDialog;
import nu.staldal.lagoon.gui.InputComponent;
import nu.staldal.lagoon.gui.LagoonGUI;
import nu.staldal.lagoon.gui.MessageDialog;
import nu.staldal.lagoon.gui.TextAreaWriter;
import nu.staldal.xtree.TreeBuilder;

public class LagoonGUITest {
    private LagoonGUI appFrame;
    private InputComponent sitemapFile;
    private InputComponent sourceDir;
    private InputComponent targetURL;
    private TextAreaWriter progressAreaWriter;
    private static final String TEST_SITEMAP_FILE = "test.xml";
    private static final String TEST_SOURCE_DIR = "testSourceDir";
    private static final String TEST_TARGET_URL = "http://test.com";

    @Before
    public void setUp() {
        appFrame = new LagoonGUI();
        sitemapFile = appFrame.sitemapFile;
        sourceDir = appFrame.sourceDir;
        targetURL = appFrame.targetURL;
        progressAreaWriter = new TextAreaWriter(appFrame.progressArea);
    }

    @Test
    public void testInit() {
        appFrame.init();
        assertNull(appFrame.processor);
        assertEquals("Not initialized", appFrame.statusLabel.getText());
    }

    @Test
    public void testInitWithValidSitemapFile() throws Exception {
        final File sitemapFile = new File(TEST_SITEMAP_FILE);
        appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
        appFrame.init();
        assertNotNull(appFrame.processor);
        assertEquals("Ready", appFrame.statusLabel.getText());
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidSitemapFile() throws Exception {
        appFrame.sitemapFile.setValue("nonExistingFile.xml");
        appFrame.init();
        fail("Initialization should have thrown an exception");
    }

    @Test
    public void testBuildWithNoChange() throws Exception {
        appFrame.init();
        appFrame.build(false);
        assertEquals("Building...", appFrame.statusLabel.getText());
        assertEquals("--- Start build ---\n", appFrame.progressArea.getText());
        appFrame.build(false);
        assertEquals("Building...", appFrame.statusLabel.getText());
        assertEquals("--- Start build ---\n", appFrame.progressArea.getText());
    }

    @Test
    public void testBuildWithSitemapChange() throws Exception {
        final File sitemapFile = new File(TEST_SITEMAP_FILE);
        appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
        appFrame.init();
        appFrame.build(false);
        Thread.sleep(100); // give some time for initialization
        appFrame.sitemapFile.setValue("updatedTest.xml");
        appFrame.build(false);
        assertEquals("Building...", appFrame.statusLabel.getText());
        appFrame.waitForBuildFinished();
        assertEquals("--- Start build ---\n--- Build finished successfully 363 ms ---", appFrame.progressArea.getText());
    }

    @Test
    public void testBuildWithForce() throws Exception {
        final File sitemapFile = new File(TEST_SITEMAP_FILE);
        appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
        appFrame.init();
        appFrame.build(false);
        Thread.sleep(100); // give some time for initialization
        appFrame.build(true);
        assertEquals("Building...", appFrame.statusLabel.getText());
        appFrame.waitForBuildFinished();
        assertEquals("--- Start build ---\n--- Build finished successfully 363 ms ---", appFrame.progressArea.getText());
    }

    @Test
    public void testLoadProperties() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("sitemapFile", TEST_SITEMAP_FILE);
        properties.setProperty("sourceDir", TEST_SOURCE_DIR);
        properties.setProperty("targetURL", TEST_TARGET_URL);
        appFrame.loadProperties(new File("testProperties.xml"));
        assertEquals(TEST_SITEMAP_FILE, appFrame.sitemapFile.getValue());
        assertEquals(TEST_SOURCE_DIR, appFrame.sourceDir.getValue());
        assertEquals(TEST_TARGET_URL, appFrame.targetURL.getValue());
    }

    @Test
    public void testSaveProperties() throws Exception {
        appFrame.sitemapFile.setValue(TEST_SITEMAP_FILE);
        appFrame.sourceDir.setValue(TEST_SOURCE_DIR);
        appFrame.targetURL.setValue(TEST_TARGET_URL);
        appFrame.savePropertyFile();
        assertEquals("Properties saved", appFrame.statusLabel.getText());

        final File propertiesFile = new File("testProperties.xml");
        assertTrue(propertiesFile.exists());
        try (FileInputStream fis = new FileInputStream(propertiesFile)) {
            final Properties prop = new Properties();
            prop.load(fis);
            assertEquals(TEST_SITEMAP_FILE, prop.getProperty("sitemapFile"));
            assertEquals(TEST_SOURCE_DIR, prop.getProperty("sourceDir"));
            assertEquals(TEST_TARGET_URL, prop.getProperty("targetURL"));
        }
    }

    private void waitForBuildFinished() throws InterruptedException {
        while (appFrame.statusLabel.getText().contains("Building...")) {
            Thread.sleep(100);
        }
    }
}