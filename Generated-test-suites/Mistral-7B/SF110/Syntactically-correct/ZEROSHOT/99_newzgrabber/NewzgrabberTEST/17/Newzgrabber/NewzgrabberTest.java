package test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {
    private static Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        Newzgrabber.Newzdirectory = "test_directory";
        Newzgrabber.JarFilename = "test_jar_filename.jar";
        Newzgrabber.BatchFilename = "test_batch_filename.ini";
        Newzgrabber.NewzInifilename = "test_newzini_filename.ini";
        newzgrabber = new Newzgrabber("Test Newzgrabber");
    }

    @Test
    public void testSetupBasicSystem() {
        setupBasicSystemForTest();
        assertTrue(Newzgrabber.Newzdirectory.endsWith("test_directory"));
    }

    @Test
    public void testFindDirectory() {
        String classfile = "test_classfile.class";
        String result = findDirectory(classfile);
        assertNotNull(result);
        assertTrue(result.endsWith("test_directory"));
    }

    @Test
    public void testUpdateIni() {
        String inifile = "test_ini_filename.ini";
        updateIniForTest(inifile, "prop", "key", "value");
        Ini ini = new Ini(inifile, "prop");
        LinkedList<Object[]> iniVector = ini.getIniVector();
        assertEquals(1, iniVector.size());
        Object[] result = iniVector.get(0);
        assertEquals("key", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testWindowClosing() {
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING);
        newzgrabber.windowClosing(we);
        assertTrue(Newzgrabber.IsBatch);
    }

    @Test(expected = Exception.class)
    public void testExceptionInWindowClosing() {
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING);
        throw new RuntimeException("Test Exception");
        newzgrabber.windowClosing(we);
    }

    private void setupBasicSystemForTest() {
        try {
            Newzgrabber.setupBasicSystem();
        } catch (Exception e) {
            fail("Exception thrown during setupBasicSystem: " + e.getMessage());
        }
    }

    private void updateIniForTest(String inifile, String prop, String key, String value) {
        try {
            newzgrabber.updateIni(inifile, prop, key, value);
        } catch (Exception e) {
            fail("Exception thrown during updateIni: " + e.getMessage());
        }
    }

    private static FindDirectory findDirectory(String classfile) {
        return new FindDirectory() {
            @Override
            public File findDirectory() throws IOException {
                return new File("test_directory");
            }
        };
    }

    private static Ini ini;

    @Test
    public void testInitIni() {
        ini = new Ini("test_ini_filename.ini", "prop");
        assertEquals(0, ini.getIniVector().size());
    }

    @Test
    public void testInitIniWithData() {
        String[] data = {"Group1", "Value1"};
        ini = new Ini("test_ini_filename.ini", "prop");
        ini.writeIni(Arrays.asList(data));
        assertEquals(1, ini.getIniVector().size());
        assertArrayEquals(new String[]{"Group1", "Value1"}, ini.getIniVector().toArray(new String[0][2]));
    }
}