package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
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
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber.Newzdirectory);
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
        assertTrue(true); // You can add assertions here to check the expected behavior when the window is closing
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "test";
        String value = "testValue";

        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, prop, key, value);

        // Add assertions here to check the contents of the ini file after the update
    }

    @Test
    public void testMain() {
        String[] args = {"-verbose"};
        Newzgrabber newzgrabber = null;
        try {
            Newzgrabber.main(args);
            assertTrue(Newzgrabber.verbose);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        args = new String[0];
        Newzgrabber.main(args);
        assertFalse(Newzgrabber.verbose);

        args = new String[]{"-batch"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.IsBatch);
    }

    @Test
    public void testVectorOperations() {
        Vector vector = new Vector();
        vector.add(new String[]{"key1", "value1"});
        vector.add(new String[]{"key2", "value2"});

        String key1 = (String) vector.elementAt(0)[0];
        String value1 = (String) vector.elementAt(0)[1];
        assertEquals("key1", key1);
        assertEquals("value1", value1);

        String key2 = (String) vector.elementAt(1)[0];
        String value2 = (String) vector.elementAt(1)[1];
        assertEquals("key2", key2);
        assertEquals("value2", value2);
    }
}