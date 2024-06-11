package test.Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import Newzgrabber.*;

public class NewzgrabberTest {

    @BeforeClass
    public static void setup() {
        System.setProperty("java.library.path", Newzgrabber.Newzdirectory + File.pathSeparator + System.getProperty("java.library.path"));
    }

    @Test
    public void testNewzgrabber() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber Test");
        assertTrue(newzgrabber.isVisible());
        assertEquals(5, newzgrabber.getJtp().getTabCount());
        newzgrabber.dispose();
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.Newzdirectory);
        assertFalse(Newzgrabber.IsBatch);
    }

    @Test(expected = Exception.class)
    public void testWindowClosingWithException() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber Test");
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
    }

    @Test
    public void testFindDirectory() {
        String classfile = Newzgrabber.class.getName().replace(".", "/") + ".class";
        String directory = Newzgrabber.findDirectory(classfile);
        assertNotNull(directory);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "test.ini";
        Newzgrabber.updateIni(inifile, "TestProp", "TestKey", "TestValue");
        Vector<String[]> iniData = Ini.load(inifile);
        String key = "TestKey";
        String value = "TestValue";
        for (String[] data : iniData) {
            assertEquals(key, data[0]);
            assertEquals(value, data[1]);
        }
    }
}