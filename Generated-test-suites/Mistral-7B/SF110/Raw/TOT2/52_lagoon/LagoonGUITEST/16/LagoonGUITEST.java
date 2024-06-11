package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.*;

import java.awt.*;
import java.io.*;

public class LagoonGUITest {
    LagoonGUI lagoonGUI;

    @Before
    public void setUp() throws Exception {
        // Set up input components
        // ...
    }

    @Test
    public void testBuild() throws Exception {
        lagoonGUI.build(false);
        assertEquals("Building...", lagoonGUI.statusLabel.getText());
        // Verify the status and progress area
        // ...
    }

    @Test
    public void testExit() {
        lagoonGUI.exit();
        assertTrue(lagoonGUI.isVisible() == false);
    }

    @Test(expected = FileNotFoundException.class)
    public void testBuildWithMissingSitemapFile() throws Exception {
        File sitemapFile = new File("non_existent_file.xml");
        lagoonGUI = new LagoonGUI();
        lagoonGUI.sitemapFile = new InputComponent(lagoonGUI, "Sitemap file", sitemapFile.getAbsolutePath(), true);
        lagoonGUI.init();
        lagoonGUI.build(false);
    }

    @Test(expected = FileNotFoundException.class)
    public void testBuildWithMissingSourceDirectory() throws Exception {
        File sourceDir = new File("non_existent_directory");
        lagoonGUI = new LagoonGUI();
        lagoonGUI.sourceDir = new InputComponent(lagoonGUI, "Source directory", sourceDir.getAbsolutePath(), true);
        lagoonGUI.init();
        lagoonGUI.build(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithInvalidTargetURL() throws Exception {
        lagoonGUI = new LagoonGUI();
        lagoonGUI.targetURL = new InputComponent(lagoonGUI, "Target URL", "", true);
        lagoonGUI.init();
        lagoonGUI.build(false);
    }

    @Test(expected = AuthenticationException.class)
    public void testBuildWithInvalidPassword() throws Exception {
        lagoonGUI = new LagoonGUI();
        lagoonGUI.password = "invalid_password";
        lagoonGUI.init();
        lagoonGUI.build(false);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitWithMissingPassword() throws Exception {
        lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
    }

    @Test(expected = SAXException.class)
    public void testInitWithUnparseableSitemapFile() throws Exception {
        lagoonGUI = new LagoonGUI();
        File sitemapFile = new File("invalid_xml.xml");
        lagoonGUI.sitemapFile = new InputComponent(lagoonGUI, "Sitemap file", sitemapFile.getAbsolutePath(), true);
        lagoonGUI.init();
    }

    @Test(expected = IOException.class)
    public void testInitWithLagoonProcessorError() throws Exception {
        lagoonGUI = new LagoonGUI();
        lagoonGUI.sitemapFile = new InputComponent(lagoonGUI, "Sitemap file", "", true);
        lagoonGUI.sourceDir = new InputComponent(lagoonGUI, "Source directory", "", true);
        lagoonGUI.targetURL = new InputComponent(lagoonGUI, "Target URL", "", true);
        lagoonGUI.password = "password";
        lagoonGUI.init();
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadPropertyFileWithMissingFile() throws Exception {
        lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
    }

    @Test(expected = IOException.class)
    public void testLoadPropertyFileWithUnreadableFile() throws Exception {
        File propertyFile = new File("unreadable_file.xml");
        lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
    }

    @Test(expected = LagoonException.class)
    public void testLoadPropertiesWithMissingProperties() throws Exception {
        File propertyFile = new File("property_file.xml");
        lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(propertyFile);
    }

    @Test(expected = IOException.class)
    public void testSavePropertyFileWithUnwritableFile() throws Exception {
        File propertyFile = new File("unwritable_file.xml");
        lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
    }
}