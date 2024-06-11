package test.Newzgrabber;

import static org.junit.Assert.*;
import Newzgrabber.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

    @BeforeClass
    public static void setup() {
        // Set up any static variables or initialize any static objects here
    }

    @Test
    public void testNewzgrabberConstructorBatchMode() {
        Newzgrabber n = new Newzgrabber("");
        assertTrue(n.IsBatch);
    }

    @Test
    public void testNewzgrabberConstructorGUIMode() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        assertFalse(n.IsBatch);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber n = new Newzgrabber("");
        String newzdirectory = Newzgrabber.Newzdirectory;
        assertTrue(newzdirectory != null);
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String newzdirectory = Newzgrabber.findDirectory(classfile);
        assertNotNull(newzdirectory);
    }

    @Test
    public void testUpdateIni() {
        // TODO: Implement test for updateIni method
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber n = new Newzgrabber("");
        n.windowClosing(new WindowEvent(n, WindowEvent.WINDOW_CLOSING));
        assertEquals(0, n.getWidth()); // or any other condition to check that window has closed properly
    }
}