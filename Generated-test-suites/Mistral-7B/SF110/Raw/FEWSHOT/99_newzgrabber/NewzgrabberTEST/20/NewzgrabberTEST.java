package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

public class NewzgrabberTest {

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String expectedDirectory = System.getProperty("user.dir") + File.separator + ".."; // Assume Newzgrabber is run from project root
        String actualDirectory = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "test.ini";
        String prop = "Section1";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);

        // Check if value was written to the ini file
        Ini ini = new Ini(inifile, prop);
        Vector<String[]> iniValues = ini.getIniVector();
        String actualValue = "";
        for (String[] valuePair : iniValues) {
            if (valuePair[0].equals(key)) {
                actualValue = valuePair[1];
                break;
            }
        }
        assertEquals(value, actualValue);
    }

    @Test
    public void testNewzgrabberConstruction() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber.jtp);
        assertNotNull(newzgrabber.optionspanel);
        assertNotNull(newzgrabber.groupspanel);
        assertNotNull(newzgrabber.searchpanel);
        assertNotNull(newzgrabber.transferpanel);
    }

    @Test
    public void testSetupBasicSystem() {
        String newzdirectory = null;
        try {
            Newzgrabber.setupBasicSystem();
            newzdirectory = Newzgrabber.Newzdirectory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(newzdirectory);
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, 0));
        assertTrue(newzgrabber.isClosed());
    }
}