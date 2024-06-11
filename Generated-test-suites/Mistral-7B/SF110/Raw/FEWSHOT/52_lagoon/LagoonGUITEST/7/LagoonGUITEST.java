package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.getProcessor());
        assertNull(appFrame.getStatusLabel().getText());
        assertNull(appFrame.getProgressArea().getText());
        appFrame.init();
        assertNotNull(appFrame.getProcessor());
        assertNotNull(appFrame.getStatusLabel().getText());
        assertNotNull(appFrame.getProgressArea().getText());
    }

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        assertNull(appFrame.getProcessor().isBuilt());
        appFrame.build(false);
        assertTrue(appFrame.getProcessor().isBuilt);
    }

    @Test
    public void testBuildWithForce() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        File sitemapFile = new File("testSitemap.xml");
        appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
        appFrame.build(true);
        assertTrue(appFrame.getProcessor().isBuilt);
        assertTrue(sitemapFile.lastModified() > appFrame.sitemapLastModified);
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        appFrame.exit();
        // TESTING EXIT METHOD IS NOT POSSIBLE IN JUNIT
    }
}