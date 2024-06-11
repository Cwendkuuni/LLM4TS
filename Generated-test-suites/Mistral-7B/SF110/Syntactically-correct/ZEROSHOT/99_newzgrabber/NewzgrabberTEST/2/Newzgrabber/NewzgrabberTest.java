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
        // Initialize static variables and objects
        Newzgrabber.Newzdirectory = "test_newz_directory";
        Newzgrabber.JarFilename = "test_jar_filename.jar";
        Newzgrabber.BatchFilename = "test_batch_filename.ini";
        Newzgrabber.NewzInifilename = "test_newzgrabber_ini.ini";
        Newzgrabber.verbose = false;
        Newzgrabber.debug = false;
        Newzgrabber.batch = false;
        Newzgrabber.IsBatch = false;
        Newzgrabber.setupBasicSystem();
        Newzgrabber n = new Newzgrabber("Test Newzgrabber");
        Newzgrabber.updateIni(Newzgrabber.NewzInifilename, "Messageids", "TestGroup", "TestLastid");
    }

    @Test
    public void testFindDirectory() {
        // Test findDirectory method
        String actualClassfile = Newzgrabber.class.getName().replace(".", "/") + ".class";
        String expectedDirectory = new File(".").getAbsolutePath();
        File expectedFile = new File(expectedDirectory, Newzgrabber.Newzdirectory);
        String actualDirectory = Newzgrabber.findDirectory(actualClassfile);
        assertEquals(expectedDirectory, actualDirectory);
        File actualFile = new File(actualDirectory);
        assertTrue(actualFile.exists());
    }

    @Test
    public void testUpdateIni() {
        // Test updateIni method
        String inifile = Newzgrabber.NewzInifilename;
        String prop = "Messageids";
        String key = "TestGroup";
        String value = "TestValue";
        Newzgrabber.updateIni(inifile, prop, key, value);
        Vector<String[]> iniVector = NewIni(inifile).getIniVector();
        String actualKey = "";
        String actualValue = "";
        for (String[] stringarr : iniVector) {
            actualKey = stringarr[0];
            actualValue = stringarr[1];
            if (actualKey.equals(key)) {
                assertEquals(value, actualValue);
                break;
            }
        }
    }

    @Test
    public void testConstructorWithTitle() {
        // Test constructor with title argument
        Newzgrabber newzgrabber = new Newzgrabber("Test Newzgrabber");
        assertNotNull(newzgrabber);
        assertEquals("Test Newzgrabber", newzgrabber.getTitle());
    }

    @Test
    public void testConstructorWithoutTitle() {
        // Test constructor without title argument
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber);
        assertNull(newzgrabber.getTitle());
    }

    @Test(expected = Exception.class)
    public void testConstructorWithIllegalArgument() {
        // Test constructor with null title argument
        Newzgrabber newzgrabber = new Newzgrabber(null);
    }

    @Test
    public void testSetupBasicSystem() {
        // Test setupBasicSystem method
        assertNotNull(Newzgrabber.Newzdirectory);
        File newzdirFile = new File(Newzgrabber.Newzdirectory);
        assertTrue(newzdirFile.exists());
        assertTrue(newzdirFile.isDirectory());
    }

    @Test
    public void testWindowListeners() {
        // Test window listeners
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber.getWindowListener());
    }

    @Test
    public void testBatchMode() {
        // Test batch mode
        Newzgrabber.IsBatch = true;
        Newzgrabber n = new Newzgrabber();
        assertFalse(n.isVisible());
        assertNull(Newzgrabber.jtp);
        assertNull(Newzgrabber.optionspanel);
        assertNull(Newzgrabber.groupspanel);
        assertNull(Newzgrabber.searchpanel);
        assertNull(Newzgrabber.transferpanel);
    }

    @Test
    public void testNonBatchMode() {
        // Test non-batch mode
        Newzgrabber.IsBatch = false;
        Newzgrabber n = new Newzgrabber("Test Newzgrabber");
        assertTrue(n.isVisible());
        assertNotNull(Newzgrabber.jtp);
        assertNotNull(Newzgrabber.optionspanel);
        assertNotNull(Newzgrabber.groupspanel);
        assertNotNull(Newzgrabber.searchpanel);
        assertNotNull(Newzgrabber.transferpanel);
    }

    @Test
    public void testBatchModeWithArguments() {
        // Test batch mode with command-line arguments
        Newzgrabber.IsBatch = true;
        Newzgrabber n = new Newzgrabber();
        String[] args = {"-verbose", "-batch"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.verbose);
        assertTrue(Newzgrabber.batch);
        assertFalse(Newzgrabber.debug);
        assertFalse(Newzgrabber.isVisible());
        assertNull(Newzgrabber.jtp);
        assertNull(Newzgrabber.optionspanel);
        assertNull(Newzgrabber.groupspanel);
        assertNull(Newzgrabber.searchpanel);
        assertNull(Newzgrabber.transferpanel);
    }

    @Test
    public void testNonBatchModeWithArguments() {
        // Test non-batch mode with command-line arguments
        Newzgrabber.IsBatch = false;
        String[] args = {"-verbose"};
        Newzgrabber n = new Newzgrabber("Test Newzgrabber", args);
        assertTrue(Newzgrabber.verbose);
        assertFalse(Newzgrabber.batch);
        assertFalse(Newzgrabber.debug);
        assertTrue(n.isVisible());
        assertNotNull(Newzgrabber.jtp);
        assertNotNull(Newzgrabber.optionspanel);
        assertNotNull(Newzgrabber.groupspanel);
        assertNotNull(Newzgrabber.searchpanel);
        assertNotNull(Newzgrabber.transferpanel);
    }

    private Ini newIni(String inifile) {
        try {
            return new Ini(inifile, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}