package test;

import static org.junit.Assert.*;
import Newzgrabber.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

    private static Newzgrabber newzgrabber;
    private static String newzDirectory;
    private static String jarFilename = "Newz.jar";
    private static String batchFilename = "batch.ini";
    private static String newzIniFilename = "Newzgrabber.ini";

    @BeforeClass
    public static void setup() throws Exception {
        Newzgrabber.setupBasicSystem();
        newzDirectory = Newzgrabber.findDirectory(jarFilename);
        newzgrabber = new Newzgrabber("Newzgrabber");
    }

    @Test
    public void testSetupBasicSystem() {
        assertNotNull(Newzgrabber.Newzdirectory);
        assertTrue(new File(Newzgrabber.Newzdirectory).exists());
    }

    @Test
    public void testWindowListener() {
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
        assertTrue(newzgrabber.isDisplayable());
    }

    @Test
    public void testFindDirectory() {
        String classFile = "Newzgrabber.class";
        assertNotNull(Newzgrabber.findDirectory(classFile));
    }

    @Test(expected = Exception.class)
    public void testFindDirectoryFailed() {
        String classFile = "NewzgrabberNonexistent.class";
        Newzgrabber.findDirectory(classFile);
    }

    @Test
    public void testUpdateIni() throws Exception {
        String iniFile = newzDirectory + File.separator + "Newzgrabber.ini";
        Newzgrabber.updateIni(iniFile, "Messageids", "TestGroup", "TestValue");
        Vector<String[]> iniData = Ini.load(iniFile, "Messageids");
        String[] iniEntry = iniData.get(0);
        assertEquals("TestGroup", iniEntry[0]);
        assertEquals("TestValue", iniEntry[1]);
    }

    @Test
    public void testMain() {
        String[] args = new String[1];
        args[0] = "-verbose";
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.verbose);
    }

    @Test
    public void testBatchMode() {
        String[] args = new String[1];
        args[0] = "-batch";
        Newzgrabber newzbatch = null;
        try {
            Newzgrabber.main(args);
            fail("Batch mode should not start GUI");
        } catch (Exception e) {
            // expected
        }
        newzbatch = new Newzbatch();
        assertNotNull(newzbatch);
    }
}