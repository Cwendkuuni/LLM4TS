package nu.staldal.lagoon.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.*;

public class LagoonGUITest {
    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testConstruction() {
        assertNotNull(lagoonGUI);
        assertEquals("Lagoon", lagoonGUI.getTitle());
        assertTrue(lagoonGUI.isFocusable());
    }

    @Test
    public void testLoadProperties() {
        lagoonGUI.loadProperties(new File("testProperties.txt"));
        assertNotNull(lagoonGUI.getSitemapFileValue());
        assertNotNull(lagoonGUI.getSourceDirValue());
        assertNotNull(lagoonGUI.getTargetURLValue());
        assertEquals("testProperties.txt", lagoonGUI.getSitemapFileValue());
        assertEquals(".", lagoonGUI.getSourceDirValue());
        assertEquals(".", lagoonGUI.getTargetURLValue());
    }

    @Test(expected = LagoonException.class)
    public void testLoadPropertiesWithMissingProperty() throws Exception {
        lagoonGUI.loadProperties(new File("testPropertiesMissingProperty.txt"));
        fail("LoadProperties method should have thrown LagoonException");
    }

    @Test
    public void testSaveProperties() {
        lagoonGUI.setSitemapFileValue("testSaveProperties.txt");
        lagoonGUI.setSourceDirValue("testSavePropertiesSourceDir");
        lagoonGUI.setTargetURLValue("testSavePropertiesTargetURL");
        lagoonGUI.savePropertyFile();
        File testFile = new File("testSaveProperties.txt");
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(testFile);
        properties.load(fis);
        fis.close();
        assertEquals("testSaveProperties.txt", properties.getProperty("sitemapFile"));
        assertEquals("testSavePropertiesSourceDir", properties.getProperty("sourceDir"));
        assertEquals("testSavePropertiesTargetURL", properties.getProperty("targetURL"));
    }

    @Test
    public void testBuild() {
        lagoonGUI.setSitemapFileValue("testBuild.xml");
        lagoonGUI.setSourceDirValue("testBuildSourceDir");
        lagoonGUI.setTargetURLValue("testBuildTargetURL");
        lagoonGUI.build(false);
        assertTrue(lagoonGUI.getStatusLabelText().contains("Building..."));
        // Add more assertions based on expected behavior of the build method
    }

    @Test(expected = LagoonException.class)
    public void testBuildWithMissingSitemapFile() {
        lagoonGUI.setSitemapFileValue(null);
        lagoonGUI.setSourceDirValue("testBuildSourceDir");
        lagoonGUI.setTargetURLValue("testBuildTargetURL");
        lagoonGUI.build(false);
        fail("Build method should have thrown LagoonException");
    }

    @Test
    public void testWindowListeners() {
        lagoonGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                assertEquals(WindowEvent.WINDOW_CLOSING, e.getID());
            }
        });
        lagoonGUI.setVisible(true);
        lagoonGUI.dispatchEvent(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSING));
    }
}