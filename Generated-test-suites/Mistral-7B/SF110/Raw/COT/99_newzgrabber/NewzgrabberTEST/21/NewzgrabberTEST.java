package Newzgrabber; // import necessary packages here if needed

import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabberConstructorWithString() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber);
    }

    @Test
    public void testNewzgrabberStaticMethods() {
        assertNotNull(Newzgrabber.jtp);
        assertNotNull(Newzgrabber.optionspanel);
        assertNotNull(Newzgrabber.groupspanel);
        assertNotNull(Newzgrabber.searchpanel);
        assertNotNull(Newzgrabber.transferpanel);
        assertFalse(Newzgrabber.IsBatch);
    }

    @Test
    public void testSetupBasicSystem() {
        try {
            Newzgrabber.setupBasicSystem();
            assertTrue(Newzgrabber.Newzdirectory != null);
        } catch (Exception e) {
            fail("SetupBasicSystem() threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindDirectoryWithValidClassfile() {
        String classfile = "Newz.jar";
        String dir = Newzgrabber.findDirectory(classfile);
        assertNotNull(dir);
    }

    @Test
    public void testFindDirectoryWithInvalidClassfile() {
        String classfile = "InvalidClassfile.jar";
        String dir = Newzgrabber.findDirectory(classfile);
        assertNull(dir);
    }

    @Test
    public void testUpdateIniWithValidParameters() {
        String inifile = "test.ini";
        String prop = "TestProp";
        String key = "TestKey";
        String value = "TestValue";
        try {
            Newzgrabber.updateIni(inifile, prop, key, value);
            Ini ini = new Ini(inifile, prop);
            Vector Newids = ini.getIniVector();
            int count = 0;
            for (int i = 0; i < Newids.size(); ++i) {
                String[] stringarr = Newids.elementAt(i);
                String nextkey = stringarr[0];
                if (nextkey.equals(key)) {
                    String nextvalue = stringarr[1];
                    assertEquals(value, nextvalue);
                    count++;
                }
            }
            assertEquals(1, count);
        } catch (Exception e) {
            fail("UpdateIni() threw an exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithInvalidKey() {
        String inifile = "test.ini";
        String prop = "TestProp";
        String key = null;
        String value = "TestValue";
        try {
            Newzgrabber.updateIni(inifile, prop, key, value);
            fail("UpdateIni() did not throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithInvalidValue() {
        String inifile = "test.ini";
        String prop = "TestProp";
        String key = "TestKey";
        String value = null;
        try {
            Newzgrabber.updateIni(inifile, prop, key, value);
            fail("UpdateIni() did not throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }
}