package test;

import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.Newzdirectory);
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String expectedDirectory = "path/to/Your/Jar/Directory";
        String result = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, result);
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber frame = new Newzgrabber("Newzgrabber");
        frame.windowClosing(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        assertTrue(frame.isVisible() == false);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);

        // You need to implement this function to read the INI file and check the value.
        // For example, you can use Apache Commons Configuration library.
    }

    @Test
    public void testMain() {
        String[] args = new String[0];
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.IsBatch == false);

        args = new String[]{"-verbose"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.verbose == true);

        args = new String[]{"-batch"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.IsBatch == true);

        args = new String[]{"-debug"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.debug == true);

        args = new String[]{"-batch", "-debug"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.IsBatch == true);
        assertTrue(Newzgrabber.debug == true);
    }
}