package NewzgrabberTest;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

import Newzgrabber.*;

public class NewzgrabberTest {
    

    @Test
    public void testNewzgrabberConstructorWithString() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber.getContentPane());
        assertEquals("Newzgrabber", newzgrabber.getTitle());
        assertEquals(5, newzgrabber.jtp.getTabCount());
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        String newzdirectory = Newzgrabber.Newzdirectory;
        assertNotNull(newzdirectory);
        assertTrue(new File(newzdirectory).exists());
    }

    @Test(expected = Exception.class)
    public void testWindowClosingWithException() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String newzdirectory = Newzgrabber.findDirectory(classfile);
        assertNotNull(newzdirectory);
        assertTrue(new File(newzdirectory).exists());
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, prop, key, value);
        Vector<String[]> iniVector = newzgrabber.readIniFile(inifile, prop);
        String[] testValueArray = null;
        for (String[] stringArr : iniVector) {
            if (stringArr[0].equalsIgnoreCase(key)) {
                testValueArray = stringArr;
                break;
            }
        }
        assertNotNull(testValueArray);
        assertArrayEquals(new String[] { key, value }, testValueArray);
    }

    @Test
    public void testMain() {
        String[] args = new String[0];
        Newzgrabber.main(args);
        assertEquals(false, Newzgrabber.IsBatch);
        assertEquals(false, Newzgrabber.batch);
    }

    @Test
    public void testBatchMode() {
        String[] args = new String[] { "-batch" };
        Newzgrabber newzgrabber = null;
        try {
            Newzgrabber.main(args);
            newzgrabber = new Newzbatch();
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertTrue(newzgrabber instanceof Newzbatch);
    }

    @Test
    public void testVerboseMode() {
        String[] args = new String[] { "-verbose" };
        Newzgrabber newzgrabber = null;
        try {
            Newzgrabber.main(args);
            newzgrabber = new Newzgrabber("Newzgrabber");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertTrue(Newzgrabber.verbose);
    }

    @Test
    public void testNewzgrabberConstructorWithStringAndString() {
        String title = "Newzgrabber";
        Newzgrabber newzgrabber = new Newzgrabber(title);
        assertEquals(title, newzgrabber.getTitle());
    }

    @Test
    public void testSetupBasicSystemWithWindowsOperatingSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        String javaLibraryPath = System.getProperty("java.library.path");
        assertTrue(javaLibraryPath.contains(Newzgrabber.Newzdirectory));
    }

    @Test(expected = NullPointerException.class)
    public void testNullFileNameInfindDirectory() {
        String classfile = null;
        String newzdirectory = Newzgrabber.findDirectory(classfile);
        fail("Expected NullPointerException, but got: " + newzdirectory);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInvalidFileNameInfindDirectory() {
        String classfile = "InvalidFileName.jar";
        String newzdirectory = Newzgrabber.findDirectory(classfile);
        fail("Expected FileNotFoundException, but got: " + newzdirectory);
    }

    @Test
    public void testEmptyIniFile() {
        String inifile = "empty.ini";
        Vector<String[]> iniVector = Newzgrabber.readIniFile(inifile, "Messageids");
        assertTrue(iniVector.isEmpty());
    }

    @Test
    public void testUpdateIniWithDuplicateKey() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value1 = "testValue1";
        String value2 = "testValue2";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, prop, key, value1);
        newzgrabber.updateIni(inifile, prop, key, value2);
        Vector<String[]> iniVector = newzgrabber.readIniFile(inifile, prop);
        String[] testValueArray = null;
        for (String[] stringArr : iniVector) {
            if (stringArr[0].equalsIgnoreCase(key)) {
                testValueArray = stringArr;
                break;
            }
        }
        assertNotNull(testValueArray);
        assertEquals(value2, testValueArray[1]);
    }

    @Test
    public void testWindowClosingWithNullException() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosing(null);
    }

    @Test
    public void testWindowClosingWithExceptionMessage() {
        Newzgrabber newzgrabber = new Newzgrabber();
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING);
        newzgrabber.windowClosing(we);
        assertEquals("Closing the window...", we.getMessage());
    }

    @Test
    public void testWindowActivated() {
        Newzgrabber newzgrabber = new Newzgrabber();
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_ACTIVATED);
        newzgrabber.windowActivated(we);
    }

    @Test
    public void testWindowDeactivated() {
        Newzgrabber newzgrabber = new Newzgrabber();
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_DEACTIVATED);
        newzgrabber.windowDeactivated(we);
    }

    @Test
    public void testWindowIconified() {
        Newzgrabber newzgrabber = new Newzgrabber();
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_ICONIFIED);
        newzgrabber.windowIconified(we);
    }

    @Test
    public void testWindowDeiconified() {
        Newzgrabber newzgrabber = new Newzgrabber();
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_DEICONIFIED);
        newzgrabber.windowDeiconified(we);
    }

    @Test
    public void testWindowClosingWithWindowClosed() {
        Newzgrabber newzgrabber = new Newzgrabber();
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSED);
        newzgrabber.windowClosing(we);
        fail("Expected WindowEvent.WINDOW_CLOSED, but got: " + we.getID());
    }

    @Test
    public void testNewzgrabberConstructorWithNullString() {
        String title = null;
        try {
            Newzgrabber newzgrabber = new Newzgrabber(title);
            fail("Expected IllegalArgumentException, but got: Newzgrabber object");
        } catch (IllegalArgumentException iae) {
            // expected
        }
    }

    @Test
    public void testNewzgrabberConstructorWithEmptyString() {
        String title = "";
        Newzgrabber newzgrabber = new Newzgrabber(title);
        assertEquals("", newzgrabber.getTitle());
    }

    @Test
    public void testNewzbatchConstructor() {
        Newzbatch newzbatch = new Newzbatch();
        assertTrue(newzbatch instanceof Newzbatch);
    }

    @Test
    public void testNewzgrabberConstructorWithStringAndBoolean() {
        String title = "Newzgrabber";
        boolean verbose = true;
        Newzgrabber newzgrabber = new Newzgrabber(title, verbose);
        assertEquals(title, newzgrabber.getTitle());
        assertEquals(verbose, Newzgrabber.verbose);
    }

    @Test
    public void testNewzgrabberConstructorWithStringAndBoolean_False() {
        String title = "Newzgrabber";
        boolean verbose = false;
        Newzgrabber newzgrabber = new Newzgrabber(title, verbose);
        assertEquals(title, newzgrabber.getTitle());
        assertEquals(false, Newzgrabber.verbose);
    }

    
}