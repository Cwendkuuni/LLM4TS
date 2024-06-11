package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

import static org.junit.Assert.*;

public class NewzgrabberTest {

    private static Newzgrabber newzgrabber;
    private static final String NEWZGRABBER_CLASS_NAME = "Newzgrabber.Newzgrabber";

    @Before
    public void setUp() {
        System.setProperty("java.library.path", System.getProperty("user.dir") + File.separator + "lib");
        try {
            newzgrabber = new Newzgrabber("Test Newzgrabber");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
    }

    @Test
    public void testNewzgrabberConstruction() {
        assertNotNull(newzgrabber);
        assertEquals(false, Newzgrabber.IsBatch);
        assertEquals("Test Newzgrabber", newzgrabber.getTitle());
    }

    @Test
    public void testNewzgrabberBatchConstruction() {
        System.setProperty("java.library.path", System.getProperty("user.dir") + File.separator + "lib");
        System.setProperty("Newzgrabber.IsBatch", "true");
        try {
            newzgrabber = new Newzgrabber("Test Newzgrabber Batch");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown: " + e.getMessage());
        }
        assertEquals(true, Newzgrabber.IsBatch);
    }

    @Test
    public void testFindDirectory() {
        assertEquals(System.getProperty("user.dir"), Newzgrabber.findDirectory("NewzgrabberTest.class"));
    }

    @Test
    public void testUpdateIni() {
        final String inifile = "test.ini";
        try {
            newzgrabber.updateIni(inifile, "Messageids", "TestGroup", "TestValue");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown: " + e.getMessage());
        }

        Vector<String[]> iniVector = Newzgrabber.readIni(inifile);
        String testKey = "TestGroup";
        String testValue = "TestValue";

        for (int i = 0; i < iniVector.size(); i++) {
            String[] iniRecord = iniVector.get(i);
            assertEquals(testKey, iniRecord[0]);
            assertEquals(testValue, iniRecord[1]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithInvalidKey() {
        final String inifile = "test.ini";
        try {
            newzgrabber.updateIni(inifile, "InvalidSection", "TestGroup", "TestValue");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMain() {
        String[] args = {"-verbose"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.verbose);

        args = new String[]{"-batch"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.batch);

        args = new String[]{"-debug"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.debug);
    }
}