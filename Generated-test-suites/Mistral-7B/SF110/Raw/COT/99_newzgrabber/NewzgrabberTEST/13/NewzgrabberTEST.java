package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class NewzgrabberTest {
    // Test initialization
    @BeforeClass
    public static void setupBeforeClass() {
        // Initialize the class under test
        Newzgrabber newzgrabber = new Newzgrabber();
    }

    // Test constructor with no arguments
    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber);
    }

    // Test constructor with a title argument
    @Test
    public void testNewzgrabberConstructorWithTitle() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber);
        assertEquals("Newzgrabber", newzgrabber.getTitle());
    }

    // Test findDirectory method with a valid classfile
    @Test
    public void testFindDirectoryWithValidClassfile() {
        String newzdirectory = Newzgrabber.findDirectory("Newz.jar");
        assertNotNull(newzdirectory);
    }

    // Test findDirectory method with an invalid classfile
    @Test(expected = NullPointerException.class)
    public void testFindDirectoryWithInvalidClassfile() {
        Newzgrabber.findDirectory("InvalidClassfile.class");
    }

    // Test updateIni method with valid parameters
    @Test
    public void testUpdateIniWithValidParameters() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);
    }

    // Test updateIni method with invalid parameters (key already exists)
    @Test
    public void testUpdateIniWithExistingKey() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);
        String newvalue = "newTestValue";
        Newzgrabber.updateIni(inifile, prop, key, newvalue);
        String expectedValue = "testValue";
        String actualValue = Newzgrabber.readIni(inifile, prop, key).getValue();
        assertEquals(expectedValue, actualValue);
    }

    // Test updateIni method with invalid parameters (prop does not exist)
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithInvalidProp() {
        String inifile = "Newzgrabber.ini";
        String prop = "InvalidProp";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);
    }

    // Test updateIni method with invalid parameters (key is null)
    @Test(expected = NullPointerException.class)
    public void testUpdateIniWithNullKey() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = null;
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);
    }

    // Test updateIni method with invalid parameters (value is null)
    @Test(expected = NullPointerException.class)
    public void testUpdateIniWithNullValue() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = null;
        Newzgrabber.updateIni(inifile, prop, key, value);
    }
}