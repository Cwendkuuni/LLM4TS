package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {
    // Test constant values
    private static final String NEWZDIRECTORY = "test/newzgrabber/newzdirectory";
    private static final String JARFILENAME = "Newz.jar";
    private static final String BATCHFILENAME = "batch.ini";
    private static final String NEWZINIFILENAME = "Newzgrabber.ini";

    @BeforeClass
    public static void setup() {
        System.setProperty("java.library.path", NEWZDIRECTORY + File.pathSeparator + System.getProperty("java.library.path"));
        Newzgrabber.Newzdirectory = NEWZDIRECTORY;
        Newzgrabber.JarFilename = JARFILENAME;
        Newzgrabber.BatchFilename = BATCHFILENAME;
        Newzgrabber.NewzInifilename = NEWZINIFILENAME;
    }

    @Test
    public void testNewzgrabberConstructorWithString() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber);
    }

    @Test
    public void testNewzgrabberConstructorWithStringAndBoolean() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber", false);
        assertNotNull(newzgrabber);
    }

    @Test(expected = NullPointerException.class)
    public void testNewzgrabberConstructorWithNullString() {
        Newzgrabber newzgrabber = new Newzgrabber(null);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.setupBasicSystem();
        assertEquals(NEWZDIRECTORY, Newzgrabber.Newzdirectory);
        // Add more assertions for other constants and objects initialized in setupBasicSystem
    }

    @Test(expected = Exception.class)
    public void testWindowClosingWithNullWe() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosing(null);
    }

    @Test
    public void testFindDirectory() {
        String classfile = Newzgrabber.class.getName().replace(".","/") + ".class";
        String expectedDirectory = NEWZDIRECTORY;
        String actualDirectory = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test(expected = NullPointerException.class)
    public void testFindDirectoryWithNullClassfile() {
        String classfile = null;
        String actualDirectory = Newzgrabber.findDirectory(classfile);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "test/newzgrabber/Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testkey";
        String value = "testvalue";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, prop, key, value);
        // Add assertions to check if the key-value pair was added to the ini file
    }

    @Test(expected = Exception.class)
    public void testUpdateIniWithNullInifile() {
        String inifile = null;
        String prop = "Messageids";
        String key = "testkey";
        String value = "testvalue";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, prop, key, value);
    }

    @Test
    public void testMain() {
        String[] args = new String[0];
        String expectedOut = "The user directory is:\n" + NEWZDIRECTORY + "\n";
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec("java Newzgrabber " + String.join(" ", args));
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = input.readLine();
            assertEquals(expectedOut, output);
            // Add more assertions for verbose, batch, and debug mode
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            try {
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}