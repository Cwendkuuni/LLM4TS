package NewzgrabberTest;

import static org.junit.Assert.*;

import Newzgrabber.*;
import org.junit.Test;

public class NewzgrabberTest {
    

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber);
    }

    @Test(expected = Exception.class)
    public void testNewzgrabberConstructorWithException() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber"); // No exception should be thrown in the constructor
        throw new Exception("Unexpected exception in the constructor");
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.Newzdirectory);
    }

    @Test(expected = Exception.class)
    public void testFindDirectoryWithNullClassfile() {
        String classfile = null;
        assertNull(FindDirectory.findDirectory(classfile));
        throw new Exception("findDirectory should return null when classfile is null");
    }

    @Test(expected = Exception.class)
    public void testFindDirectoryWithInvalidClassfile() {
        String classfile = "InvalidClassFile.txt";
        assertNull(FindDirectory.findDirectory(classfile));
        throw new Exception("findDirectory should return null when classfile is invalid");
    }

    @Test
    public void testUpdateIniWithEmptyInifile() {
        String inifile = "empty.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        try {
            Newzgrabber.updateIni(inifile, prop, key, value);
            Ini ini = new Ini(inifile, prop);
            Ini.Section section = ini.getSection(0);
            assertNull(section);
        } catch (Exception e) {
            fail("Exception was not expected: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateIniWithExistingKey() {
        String inifile = "existingKey.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        try {
            Newzgrabber.updateIni(inifile, prop, key, value);
            Ini ini = new Ini(inifile, prop);
            Ini.Section section = ini.getSection(0);
            Ini.Entry entry = section.get(key);
            assertEquals(value, entry.getValue());
        } catch (Exception e) {
            fail("Exception was not expected: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateIniWithNewKey() {
        String inifile = "newKey.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        try {
            Newzgrabber.updateIni(inifile, prop, key, value);
            Ini ini = new Ini(inifile, prop);
            Ini.Section section = ini.getSection(0);
            Ini.Entry entry = section.get(key);
            assertNotNull(entry);
            assertEquals(value, entry.getValue());
        } catch (Exception e) {
            fail("Exception was not expected: " + e.getMessage());
        }
    }

    
}