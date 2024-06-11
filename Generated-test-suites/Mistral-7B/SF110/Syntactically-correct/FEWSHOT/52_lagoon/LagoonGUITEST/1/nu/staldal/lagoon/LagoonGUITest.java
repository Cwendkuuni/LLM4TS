package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.getProcessor());
        assertNull(appFrame.getStatusLabel().getText());
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.loadPropertyFile(new File("test.properties"));
        assertEquals("testSitemapFile", appFrame.getSitemapFile().getValue());
        assertEquals("testSourceDir", appFrame.getSourceDir().getValue());
        assertEquals("testTargetURL", appFrame.getTargetURL().getValue());
        assertNull(appFrame.getPassword());
        assertEquals("Properties loaded", appFrame.getStatusLabel().getText());
    }

    @Test(expected = LagoonException.class)
    public void testLoadPropertiesError() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.loadProperties(new File("testError.properties"));
        fail("Exception expected");
    }

    @Test
    public void testSaveProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.loadPropertyFile(new File("test.properties"));
        appFrame.savePropertyFile();
        File testFile = new File("test.properties");
        assertEquals("Lagoon properties", testFile.getName());
        assertTrue(testFile.length() > 0);
    }

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.build(false);
        assertFalse(appFrame.isEnabled());
        assertNull(appFrame.getStatusLabel().getText());
    }

    @Test
    public void testBuildForce() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.build(true);
        assertFalse(appFrame.isEnabled());
        assertNull(appFrame.getStatusLabel().getText());
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.exit();
        fail("System.exit expected");
    }
}