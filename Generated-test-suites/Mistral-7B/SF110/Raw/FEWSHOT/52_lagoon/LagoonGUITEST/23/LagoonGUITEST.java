package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.getProcessor());
        assertEquals("Not initialized", appFrame.getStatusLabel().getText());

        appFrame.sitemapFile.setValue("test.xml");
        appFrame.init();

        assertNotNull(appFrame.getProcessor());
        assertEquals("Initializing...", appFrame.getStatusLabel().getText());

        try {
            appFrame.getProcessor().build(false);
            fail("Expected initialization exception");
        } catch (Exception e) {
            // Expected
        }

        assertEquals("Initializing...", appFrame.getStatusLabel().getText());

        appFrame.sitemapFile.setValue("valid_sitemap.xml");
        appFrame.init();

        assertNotNull(appFrame.getProcessor());
        assertEquals("Building...", appFrame.getStatusLabel().getText());

        // Add more test cases for different scenarios here
    }

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.sitemapFile.setValue("valid_sitemap.xml");
        appFrame.init();

        appFrame.buildButton.doClick();

        // Add assertions for build result here
    }

    // Add more test cases for other methods in LagoonGUI class here

    private LagoonGUI getAppFrame() {
        return new LagoonGUI();
    }
}