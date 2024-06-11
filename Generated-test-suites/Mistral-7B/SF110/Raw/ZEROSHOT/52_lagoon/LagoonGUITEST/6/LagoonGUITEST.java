package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testConstructor() {
        assertNotNull(lagoonGUI);
        assertEquals("Lagoon", lagoonGUI.getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWindowWidth() {
        final Frame frame = new Frame("-1");
        assertFalse(frame.isDisplayable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputComponent() {
        new LagoonGUI().add(null, "North");
    }

    @Test
    public void testInputPanelLayout() {
        assertEquals(GridLayout.class, lagoonGUI.inputPanel.getLayoutClass());
        assertEquals(0, lagoonGUI.inputPanel.getComponentCount());
    }

    @Test
    public void testCenterPanelLayout() {
        assertEquals(BorderLayout.class, lagoonGUI.centerPanel.getLayoutClass());
    }

    @Test
    public void testButtonPanelLayout() {
        assertEquals(GridLayout.class, lagoonGUI.buttonPanel.getLayoutClass());
        assertEquals(6, lagoonGUI.buttonPanel.getComponentCount());
    }

    @Test
    public void testLoadButtonActionPerformed() {
        final File inputFile = new File("inputFile.properties");
        lagoonGUI.loadButton.doClick();
        assertEquals("inputFile.properties", lagoonGUI.sitemapFile.getValue());
        assertEquals("inputFile.properties", lagoonGUI.loadProperties(inputFile).get("sitemapFile"));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadPropertiesNullFile() {
        lagoonGUI.loadProperties(null);
    }

    @Test
    public void testSaveProperties() {
        final File outputFile = new File("outputFile.properties");
        lagoonGUI.savePropertyFile();
        assertEquals("outputFile.properties", lagoonGUI.progressArea.getText().split("\n")[0].trim());
        assertEquals("outputFile.properties", lagoonGUI.getProperty(lagoonGUI.getProperties(), "sitemapFile"));
        assertFalse(outputFile.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testBuild() throws Exception {
        final File sitemapFile = new File("sitemapFile.xml");
        lagoonGUI.sitemapFile.setValue(sitemapFile.getPath());
        lagoonGUI.build(false);
        assertTrue(sitemapFile.lastModified() < lagoonGUI.sitemapLastModified);
        lagoonGUI.sitemapFile.setValue(sitemapFile.getPath());
        lagoonGUI.build(true);
        assertTrue(sitemapFile.lastModified() < lagoonGUI.sitemapLastModified);
    }

    @Test
    public void testExit() {
        assertFalse(lagoonGUI.isDisplayable());
        lagoonGUI.exit();
        assertTrue(lagoonGUI.isDisposed());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWithProcessor() {
        lagoonGUI.processor = new LagoonProcessor(null, null);
        lagoonGUI.init();
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidSitemapFile() throws Exception {
        lagoonGUI.sitemapFile.setValue("invalidFile.xml");
        lagoonGUI.init();
    }

    @Test(expected = IOException.class)
    public void testInitWithInvalidSourceDirectory() throws Exception {
        lagoonGUI.sourceDir.setValue("invalidDirectory");
        lagoonGUI.init();
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidTargetURL() throws Exception {
        lagoonGUI.targetURL.setValue("invalidURL");
        lagoonGUI.init();
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitWithMissingPassword() throws Exception {
        lagoonGUI.init();
    }

    @Test(expected = AuthenticationException.class)
    public void testInitWithInvalidPassword() throws Exception {
        lagoonGUI.password = "invalidPassword";
        lagoonGUI.init();
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitWithMissingSitemapFile() throws Exception {
        lagoonGUI.sitemapFile.setValue("missingFile.xml");
        lagoonGUI.init();
    }

    @Test(expected = IOException.class)
    public void testInitWithInvalidInputFile() throws Exception {
        lagoonGUI.sitemapFile.setValue("invalidFile.xml");
        lagoonGUI.init();
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidLagoonProcessor() throws Exception {
        lagoonGUI.sitemapFile.setValue("sitemapFile.xml");
        lagoonGUI.sourceDir.setValue("sourceDirectory");
        lagoonGUI.targetURL.setValue("targetURL");
        lagoonGUI.password = "password";
        lagoonGUI.init();
    }

    private Properties getProperties() {
        return lagoonGUI.getProperties();
    }

    private Properties getProperties(final Properties prop) {
        final FileOutputStream fos = new FileOutputStream(new File("output.properties"));
        prop.store(fos, "Lagoon properties");
        fos.close();
        return lagoonGUI.getProperties();
    }
}