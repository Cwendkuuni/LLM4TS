package nu.staldal.lagoon; // make sure to import all necessary classes

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LagoonGUITest {
    // create an instance of the class under test
    private LagoonGUI appFrame;

    @Before
    public void setUp() {
        appFrame = new LagoonGUI();
    }

    

    // Test constructor
    @Test
    public void testLagoonGUIConstruction() {
        assertNotNull(appFrame);
    }

    // Test window listener methods
    @Test
    public void testWindowListenerMethods() {
        appFrame.windowOpened(new WindowEvent(appFrame, WindowEvent.WINDOW_OPENED));
        appFrame.windowClosing(new WindowEvent(appFrame, WindowEvent.WINDOW_CLOSING));
        appFrame.windowClosed(new WindowEvent(appFrame, WindowEvent.WINDOW_CLOSED));
        appFrame.windowIconified(new WindowEvent(appFrame, WindowEvent.WINDOW_ICONIFIED));
        appFrame.windowDeiconified(new WindowEvent(appFrame, WindowEvent.WINDOW_DEICONIFIED));
        appFrame.windowActivated(new WindowEvent(appFrame, WindowEvent.WINDOW_ACTIVATED));
        appFrame.windowDeactivated(new WindowEvent(appFrame, WindowEvent.WINDOW_DEACTIVATED));
    }

    // Test build method with default parameters
    @Test
    public void testBuildDefault() {
        appFrame.build(false);
        // add assertions here to check the state of the application after build method execution
    }

    // Test build method with force parameter set to true
    @Test
    public void testBuildForce() {
        appFrame.build(true);
        // add assertions here to check the state of the application after build method execution with force parameter
    }

    // Test init method
    @Test
    public void testInit() {
        appFrame.init();
        // add assertions here to check the state of the application after init method execution
    }

    // Test init method with invalid sitemap file
    @Test(expected = FileNotFoundException.class)
    public void testInitInvalidSitemapFile() {
        appFrame.sitemapFile.setValue("nonexistentfile.xml");
        appFrame.init();
    }

    // Test init method with invalid source directory
    @Test(expected = FileNotFoundException.class)
    public void testInitInvalidSourceDirectory() {
        appFrame.sourceDir.setValue("nonexistentdirectory");
        appFrame.init();
    }

    // Test loadPropertyFile method
    @Test
    public void testLoadPropertyFile() {
        File propertyFile = new File("test.properties");
        appFrame.loadPropertyFile();
        Properties prop = appFrame.getProperties();
        // add assertions here to check the properties loaded from file
    }

    // Test loadPropertyFile method with null property file
    @Test
    public void testLoadPropertyFileNull() {
        appFrame.loadPropertyFile();
        Properties prop = appFrame.getProperties();
        // add assertions here to check the properties when no property file is passed
    }

    // Test savePropertyFile method
    @Test
    public void testSavePropertyFile() {
        File propertyFile = new File("test.properties");
        appFrame.savePropertyFile();
        // add assertions here to check the property file after save method execution
    }

    // Test getProperty method
    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "testfile.xml");
        String result = LagoonGUI.getProperty(prop, "sitemapFile");
        assertEquals("testfile.xml", result);
    }

    // Test getProperty method with null property and name
    @Test(expected = LagoonException.class)
    public void testGetPropertyNull() {
        Properties prop = null;
        String result = LagoonGUI.getProperty(prop, "sitemapFile");
    }

    
}