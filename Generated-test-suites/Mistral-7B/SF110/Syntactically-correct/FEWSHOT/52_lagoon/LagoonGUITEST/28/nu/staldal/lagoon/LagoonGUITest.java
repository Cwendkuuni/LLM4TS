package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        assertNull(appFrame.processor);
        appFrame.init();
        assertNotNull(appFrame.processor);
    }

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        appFrame.build(false);
        // Add asserts here to test the behavior of the GUI after building.
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        appFrame.exit();
        // Add asserts here to test the expected behavior when the application exits.
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        File propertyFile = new File("path/to/propertyfile.properties");
        appFrame.loadProperties(propertyFile);
        // Add asserts here to test the properties loaded from the file.
    }

    @Test
    public void testSaveProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        File propertyFile = new File("path/to/savepropertyfile.properties");
        appFrame.savePropertyFile();
        // Add asserts here to test the properties saved in the file.
    }
}