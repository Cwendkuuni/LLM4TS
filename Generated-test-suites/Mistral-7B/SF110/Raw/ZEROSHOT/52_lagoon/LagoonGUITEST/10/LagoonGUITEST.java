package nu.staldal.lagoon.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.event.ActionEvent;
import java.io.File;

public class LagoonGUITest {

    private LagoonGUI gui;

    @Before
    public void setUp() {
        gui = new LagoonGUI();
        gui.loadPropertyFile(new File("src/test/resources/test.properties"));
    }

    @After
    public void tearDown() {
        gui.exit();
    }

    @Test
    public void testInitialization() {
        assertEquals("Lagoon", gui.getTitle());
        assertNotNull(gui.getButtonPanel());
        assertNotNull(gui.getInputPanel());
        assertNotNull(gui.getCenterPanel());
        assertNotNull(gui.getLoadButton());
        assertNotNull(gui.getSaveButton());
        assertNotNull(gui.getBuildButton());
        assertNotNull(gui.getForceButton());
        assertNotNull(gui.getExitButton());
        assertNotNull(gui.getSitemapFile());
        assertNotNull(gui.getSourceDir());
        assertNotNull(gui.getTargetURL());
        assertNull(gui.getProcessor());
        assertNotNull(gui.getStatusLabel());
        assertNotNull(gui.getProgressArea());
    }

    @Test
    public void testLoadButtonActionPerformed() {
        final File testFile = new File("src/test/resources/test.properties");
        gui.loadButton.doClick();
        assertTrue(gui.getSitemapFile().hasChanged());
        gui.sitemapFile.setValue(testFile.getAbsolutePath());
        gui.loadProperties(testFile);
        assertEquals(testFile.getAbsolutePath(), gui.getSitemapFile().getValue());
        assertNotNull(gui.getProcessor());
        gui.getSitemapFile().setValue("");
        gui.loadButton.doClick();
        assertNull(gui.getProcessor());
    }

    @Test
    public void testSaveButtonActionPerformed() throws IOException {
        final File testFile = new File("src/test/resources/testSave.properties");
        final Properties testProps = new Properties();
        testProps.setProperty("sitemapFile", "test.xml");
        testProps.setProperty("sourceDir", "src/test/resources");
        testProps.setProperty("targetURL", "http://test.com");
        gui.saveButton.doClick();
        gui.savePropertyFile();
        File savedFile = new File("testSave.properties");
        assertTrue(savedFile.exists());
        File savedFileContent = new File(savedFile.getAbsolutePath() + File.separator + "Lagoon.properties");
        Properties savedProps = new Properties();
        savedProps.load(new FileInputStream(savedFileContent));
        assertEquals(testProps.getProperty("sitemapFile"), savedProps.getProperty("sitemapFile"));
        assertEquals(testProps.getProperty("sourceDir"), savedProps.getProperty("sourceDir"));
        assertEquals(testProps.getProperty("targetURL"), savedProps.getProperty("targetURL"));
        assertTrue(savedFile.delete());
    }

    @Test
    public void testBuildButtonActionPerformed() throws IOException, LagoonException {
        final File testSitemap = new File("src/test/resources/test.xml");
        gui.loadButton.doClick();
        gui.sitemapFile.setValue(testSitemap.getAbsolutePath());
        gui.loadProperties(testSitemap);
        gui.buildButton.doClick();
        assertNotNull(gui.getProcessor());
        assertEquals("Building...", gui.getStatusLabel().getText());
        String progressAreaText = gui.getProgressArea().getText();
        assertTrue(progressAreaText.contains("Start build"));
        assertTrue(progressAreaText.contains("Finished successfully"));
        assertEquals("Ready", gui.getStatusLabel().getText());
    }

    @Test(expected = LagoonException.class)
    public void testBuildButtonActionPerformedWithInvalidSitemap() throws IOException, LagoonException {
        final File testSitemap = new File("src/test/resources/nonexistent.xml");
        gui.loadButton.doClick();
        gui.sitemapFile.setValue(testSitemap.getAbsolutePath());
        gui.loadProperties(testSitemap);
        gui.buildButton.doClick();
    }
}