package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NewzgrabberTest {

    @Before
    public void setUp() {
        // Set up any required objects or initializations here
    }

    @After
    public void tearDown() {
        // Teardown any objects or resources used during tests here
    }

    @Test
    public void testNewzgrabberConstructorWithString() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber);
    }

    @Test(expected = Exception.class)
    public void testNewzgrabberConstructorWithException() {
        Newzgrabber newzgrabber = new Newzgrabber("Exception");
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.setupBasicSystem();
        String newzdirectory = newzgrabber.Newzdirectory;
        assertTrue(!newzdirectory.isEmpty());
    }

    @Test(expected = Exception.class)
    public void testFindDirectoryWithInvalidPath() {
        Newzgrabber newzgrabber = new Newzgrabber();
        String classfile = "Newz.jar";
        String result = newzgrabber.findDirectory(classfile);
        assertNull(result);
    }

    @Test
    public void testFindDirectoryWithValidPath() {
        Newzgrabber newzgrabber = new Newzgrabber();
        String classfile = "Newz.jar";
        String expectedPath = "path/to/Newz.jar";
        String result = newzgrabber.findDirectory(classfile);
        assertEquals(expectedPath, result);
    }

    @Test
    public void testUpdateIniWithEmptyIniFile() {
        Newzgrabber newzgrabber = new Newzgrabber();
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        newzgrabber.updateIni(inifile, prop, key, value);
        LinkedList<Object[]> iniList = newzgrabber.readIni(inifile, prop);
        Object[] newKeyValue = null;
        for (Object[] obj : iniList) {
            if (obj[0].equals(key)) {
                newKeyValue = obj;
                break;
            }
        }
        assertNotNull(newKeyValue);
        assertEquals(key, newKeyValue[0]);
        assertEquals(value, newKeyValue[1]);
    }

    @Test
    public void testUpdateIniWithExistingKey() {
        Newzgrabber newzgrabber = new Newzgrabber();
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String oldValue = "oldValue";
        String newValue = "newValue";
        newzgrabber.updateIni(inifile, prop, key, oldValue);
        newzgrabber.updateIni(inifile, prop, key, newValue);
        LinkedList<Object[]> iniList = newzgrabber.readIni(inifile, prop);
        Object[] newKeyValue = null;
        for (Object[] obj : iniList) {
            if (obj[0].equals(key)) {
                newKeyValue = obj;
                break;
            }
        }
        assertNotNull(newKeyValue);
        assertEquals(key, newKeyValue[0]);
        assertEquals(newValue, newKeyValue[1]);
    }

    @Test(expected = Exception.class)
    public void testUpdateIniWithInvalidKey() {
        Newzgrabber newzgrabber = new Newzgrabber();
        String inifile = "Newzgrabber.ini";
        String prop = "InvalidProp";
        String key = "testKey";
        String value = "testValue";
        newzgrabber.updateIni(inifile, prop, key, value);
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        newzgrabber.addWindowListener(e -> newzgrabber.windowClosing(e));
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
        assertTrue(newzgrabber.isVisible() == false);
    }

    // Add more test cases here as needed
}