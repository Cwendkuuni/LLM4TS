package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;

public class LagoonGUITest {

    private static final String TEST_PROPERTY_FILE = "testPropertyFile.properties";
    private static final String TEST_SIMAP_FILE = "testSitemap.xml";
    private static final String TEST_SOURCE_DIR = "testSourceDir";
    private static final String TEST_TARGET_URL = "http://test.com";
    private static final String TEST_PASSWORD = "testPassword";

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.io.tmpdir", System.getProperty("user.dir"));
        lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
    }

    @Test
    public void testInitialization() {
        assertEquals("Lagoon", lagoonGUI.getTitle());
        assertNotNull(lagoonGUI.getInputPanel());
        assertNotNull(lagoonGUI.getCenterPanel());
        assertNotNull(lagoonGUI.getButtonPanel());
        assertNotNull(lagoonGUI.getLoadButton());
        assertNotNull(lagoonGUI.getSaveButton());
        assertNotNull(lagoonGUI.getBuildButton());
        assertNotNull(lagoonGUI.getForceButton());
        assertNotNull(lagoonGUI.getExitButton());
        assertNotNull(lagoonGUI.getSitemapFile());
        assertNotNull(lagoonGUI.getSourceDir());
        assertNotNull(lagoonGUI.getTargetURL());
        assertEquals("Not initialized", lagoonGUI.getStatusLabel().getText());
    }

    @Test
    public void testLoadProperties() throws Exception {
        lagoonGUI.loadPropertyFile();
        FileDialog propertyFileDialog = new FileDialog(lagoonGUI, "Load property file");
        propertyFileDialog.show();
        File propertyFile = propertyFileDialog.getFile();
        assertNull(propertyFile);
        lagoonGUI.savePropertyFile();
        propertyFile = new File(TEST_PROPERTY_FILE);
        lagoonGUI.loadProperties(propertyFile);
        assertEquals(TEST_SIMAP_FILE, lagoonGUI.getSitemapFile().getValue());
        assertEquals(TEST_SOURCE_DIR, lagoonGUI.getSourceDir().getValue());
        assertEquals(TEST_TARGET_URL, lagoonGUI.getTargetURL().getValue());
        assertNull(lagoonGUI.getPassword());
    }

    @Test
    public void testSaveProperties() throws Exception {
        lagoonGUI.savePropertyFile();
        FileDialog propertyFileDialog = new FileDialog(lagoonGUI, "Save property file", 1);
        propertyFileDialog.show();
        File propertyFile = propertyFileDialog.getFile();
        assertNotNull(propertyFile);
        lagoonGUI.loadProperties(propertyFile);
        assertEquals(TEST_SIMAP_FILE, lagoonGUI.getSitemapFile().getValue());
        assertEquals(TEST_SOURCE_DIR, lagoonGUI.getSourceDir().getValue());
        assertEquals(TEST_TARGET_URL, lagoonGuis.getTargetURL().getValue());
        assertEquals(TEST_PASSWORD, lagoonGUI.getPassword());
    }

    @Test
    public void testBuild() throws Exception {
        lagoonGUI.sitemapFile.setValue(TEST_SIMAP_FILE);
        lagoonGUI.sourceDir.setValue(TEST_SOURCE_DIR);
        lagoonGUI.targetURL.setValue(TEST_TARGET_URL);
        lagoonGUI.build(false);
        assertEquals("Building...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the build process to start
        assertEquals("Ready", lagoonGUI.getStatusLabel().getText());
        lagoonGUI.build(true);
        assertEquals("Building...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the build process to start
        assertEquals("Ready", lagoonGUI.getStatusLabel().getText());
        lagoonGUI.sitemapFile.setValue(new File(TEST_SIMAP_FILE + "_changed").getAbsolutePath());
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
        lagoonGUI.build(false);
        assertEquals("Building...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the build process to start
        assertEquals("Ready", lagoonGUI.getStatusLabel().getText());
        lagoonGUI.sitemapFile.setValue(new File(TEST_SIMAP_FILE).getAbsolutePath());
        lagoonGUI.sourceDir.setValue(new File(TEST_SOURCE_DIR + "_changed").getAbsolutePath());
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
        lagoonGUI.build(false);
        assertEquals("Building...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the build process to start
        assertEquals("Ready", lagoonGUI.getStatusLabel().getText());
        lagoonGUI.sourceDir.setValue(TEST_SOURCE_DIR);
        lagoonGUI.targetURL.setValue(new File(TEST_TARGET_URL + "_changed").getAbsolutePath());
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
        lagoonGUI.build(false);
        assertEquals("Building...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the build process to start
        assertEquals("Ready", lagoonGUI.getStatusLabel().getText());
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testBuildWithMissingPassword() throws Exception {
        lagoonGUI.sitemapFile.setValue(TEST_SIMAP_FILE);
        lagoonGUI.sourceDir.setValue(TEST_SOURCE_DIR);
        lagoonGUI.targetURL.setValue(TEST_TARGET_URL);
        lagoonGUI.build(false);
        assertEquals("Initializing...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the initialization process to start
        lagoonGUI.init();
        assertEquals("Initializing...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the initialization process to start
        lagoonGUI.build(false);
    }

    @Test(expected = AuthenticationException.class)
    public void testBuildWithInvalidPassword() throws Exception {
        lagoonGUI.sitemapFile.setValue(TEST_SIMAP_FILE);
        lagoonGUI.sourceDir.setValue(TEST_SOURCE_DIR);
        lagoonGUI.targetURL.setValue(TEST_TARGET_URL);
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
        lagoonGUI.password = "wrongPassword";
        lagoonGUI.init();
        assertEquals("Initializing...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the initialization process to start
        lagoonGUI.build(false);
    }

    @Test(expected = FileNotFoundException.class)
    public void testBuildWithMissingSitemapFile() throws Exception {
        lagoonGUI.sitemapFile.setValue(new File("missingSitemapFile.xml").getAbsolutePath());
        lagoonGUI.sourceDir.setValue(TEST_SOURCE_DIR);
        lagoonGUI.targetURL.setValue(TEST_TARGET_URL);
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
        lagoonGUI.build(false);
    }

    @Test(expected = IOException.class)
    public void testBuildWithIOException() throws Exception {
        lagoonGUI.sitemapFile.setValue(TEST_SIMAP_FILE);
        lagoonGUI.sourceDir.setValue(TEST_SOURCE_DIR);
        lagoonGUI.targetURL.setValue(TEST_TARGET_URL);
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
        lagoonGUI.init();
        assertEquals("Initializing...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the initialization process to start
        try (FileInputStream fis = new FileInputStream(new File(TEST_SIMAP_FILE))) {
            throw new IOException("Test IOException");
        }
        lagoonGUI.build(false);
    }

    @Test(expected = LagoonException.class)
    public void testBuildWithLagoonException() throws Exception {
        lagoonGUI.sitemapFile.setValue(TEST_SIMAP_FILE);
        lagoonGUI.sourceDir.setValue(TEST_SOURCE_DIR);
        lagoonGUI.targetURL.setValue(TEST_TARGET_URL);
        lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));
        lagoonGUI.init();
        assertEquals("Initializing...", lagoonGUI.getStatusLabel().getText());
        Thread.sleep(100); // give some time for the initialization process to start
        lagoonGUI.processor = new LagoonProcessor(TEST_TARGET_URL, new Element[0], new File(TEST_SOURCE_DIR), null, null, null);
        lagoonGUI.build(false);
    }
}