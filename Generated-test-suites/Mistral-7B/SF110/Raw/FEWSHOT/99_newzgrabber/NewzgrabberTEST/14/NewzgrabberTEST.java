package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.Test;

public class NewzgrabberTest {

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String expectedDirectory = System.getProperty("user.dir") + File.separator + ".."; // Assuming the test is run from the project directory
        String actualDirectory = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testSetupBasicSystem() {
        assertFalse(Newzgrabber.IsBatch);
        assertFalse(Newzgrabber.verbose);
        assertFalse(Newzgrabber.debug);
        assertFalse(Newzgrabber.batch);
        String expectedNewzdirectory = System.getProperty("user.dir") + File.separator; // Assuming the test is run from the project directory
        assertEquals(expectedNewzdirectory, Newzgrabber.Newzdirectory);
    }

    @Test
    public void testConstructor() {
        String windowTitle = "Newzgrabber";
        Newzgrabber n = new Newzgrabber(windowTitle);
        assertTrue(n instanceof JFrame);
        assertTrue(n.getTitle().equals(windowTitle));
    }

    @Test
    public void testWindowListenerMethods() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        n.addWindowListener(e -> {}); // Empty lambda to prevent real window events
        Newzgrabber.windowClosing(new WindowEvent(n, WindowEvent.WINDOW_CLOSING));
        assertTrue(n.isVisible()); // The window should not close immediately
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "TestKey";
        String value = "TestValue";
        Newzgrabber n = new Newzgrabber("Newzgrabber") {
            @Override
            public void updateIni(String inifile, String prop, String key, String value) {
                Vector<Object[]> iniData = new Vector<>();
                iniData.add(new Object[]{"TestKey", "TestValue"});
                Ini ini = new Ini(inifile, prop);
                ini.writeIni(iniData);
            }
        };
        n.updateIni(inifile, prop, key, value);
        // Additional tests for Ini class and its methods can be added here
    }

    @Test
    public void testMain() {
        String[] args = {"-verbose"};
        Newzgrabber n = null;
        try {
            Newzgrabber.main(args);
            assertTrue(Newzgrabber.verbose);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        args = new String[0];
        try {
            Newzgrabber.main(args);
            assertFalse(Newzgrabber.verbose);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        args = new String[]{"-batch"};
        Newzbatch nb = null;
        try {
            Newzgrabber.main(args);
            assertTrue(Newzbatch.class.isInstance(nb));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}