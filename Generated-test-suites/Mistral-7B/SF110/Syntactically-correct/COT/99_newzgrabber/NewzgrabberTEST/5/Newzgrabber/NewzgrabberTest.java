package test.Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Newzgrabber.*;

public class NewzgrabberTest {
    /**
     * 
     */

    private static final String TEST_DIR = "testData";
    private static final String JAR_FILENAME = "testNewzgrabber.jar";
    private static final String NEWZ_DIRECTORY = TEST_DIR + File.separator + "newzDirectory";
    private static final String BATCH_FILENAME = "testBatch.ini";
    private static final String NEWZINI_FILENAME = "testNewzgrabber.ini";
    private static final String GROUPS_FILE = "testGroups.ini";

    private static final boolean VERBOSE = true;
    private static final boolean DEBUG = false;
    private static final boolean BATCH = false;

    private static Newzgrabber newzgrabber;

    @Before
    public void setUp() throws Exception {
        File newzDirectoryFile = new File(NEWZ_DIRECTORY);
        newzDirectoryFile.mkdir();

        System.setProperty("java.library.path", NEWZ_DIRECTORY + File.separator + "lib" + File.pathSeparator + System.getProperty("java.library.path"));

        Newzgrabber.Newzdirectory = NEWZ_DIRECTORY;
        Newzgrabber.JarFilename = JAR_FILENAME;
        Newzgrabber.BatchFilename = BATCH_FILENAME;
        Newzgrabber.NewzInifilename = NEWZINI_FILENAME;

        newzgrabber = new Newzgrabber("testNewzgrabber");
    }

    @After
    public void tearDown() throws Exception {
        if (newzgrabber != null) {
            newzgrabber.windowClosing(null);
        }

        File newzDirectoryFile = new File(NEWZ_DIRECTORY);
        File[] files = newzDirectoryFile.listFiles();
        for (File file : files) {
            file.delete();
        }

        newzDirectoryFile.delete();
    }

    // Test methods for the Newzgrabber class

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber("testNewzgrabber");
        assertNotNull(newzgrabber);
    }

    @Test
    public void testNewzgrabberConstructorWithArguments() {
        Newzgrabber newzgrabber = new Newzgrabber("testNewzgrabber", "-verbose", "-batch");
        assertTrue(Newzgrabber.verbose);
        assertTrue(Newzgrabber.batch);
        assertFalse(Newzgrabber.IsBatch);
    }

    @Test
    public void testSetupBasicSystem() {
        newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.nf);
        assertEquals(Newzgrabber.Newzdirectory, NEWZ_DIRECTORY);
    }

    @Test(expected = Exception.class)
    public void testFindDirectoryWithNullClassfile() {
        String nullClassfile = null;
        Newzgrabber.findDirectory(nullClassfile);
    }

    @Test
    public void testUpdateIni() {
        newzgrabber.updateIni(NEWZINI_FILENAME, "Messageids", "testGroup1", "testValue1");
        newzgrabber.updateIni(NEWZINI_FILENAME, "Messageids", "testGroup1", "testValue2");

        Vector<Object[]> iniVector = Newzgrabber.readIni(NEWZINI_FILENAME, "Messageids");
        Object[] testEntry = iniVector.get(0);
        assertEquals(testEntry[0].toString(), "testGroup1");
        assertEquals(testEntry[1].toString(), "testValue2");
    }

    @Test(expected = Exception.class)
    public void testUpdateIniWithNullInifile() {
        newzgrabber.updateIni(null, "Messageids", "testGroup1", "testValue1");
    }

    @Test(expected = Exception.class)
    public void testUpdateIniWithNullProp() {
        newzgrabber.updateIni(NEWZINI_FILENAME, null, "testGroup1", "testValue1");
    }

    @Test(expected = Exception.class)
    public void testUpdateIniWithNullKey() {
        newzgrabber.updateIni(NEWZINI_FILENAME, "Messageids", null, "testValue1");
    }

    @Test(expected = Exception.class)
    public void testUpdateIniWithNullValue() {
        newzgrabber.updateIni(NEWZINI_FILENAME, "Messageids", "testGroup1", null);
    }

    /**
     * 
     */
}