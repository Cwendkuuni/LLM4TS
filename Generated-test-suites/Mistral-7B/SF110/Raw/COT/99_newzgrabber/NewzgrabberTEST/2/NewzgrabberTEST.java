package NewzgrabberTest;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import Newzgrabber.*;
import static Newzgrabber.*;

public class NewzgrabberTest {
    @BeforeClass
    public static void setupClass() {
        // Set up static variables and initialize the class
        setupBasicSystem();
        Newzgrabber.Newzdirectory = "test_directory";
        Newzgrabber.JarFilename = "test_jar_filename.jar";
        Newzgrabber.BatchFilename = "test_batch_filename.ini";
        Newzgrabber.NewzInifilename = "test_newzgrabber_ini.ini";
    }

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        assertNotNull(n);
    }

    @Test
    public void testNewzgrabberBatchConstructor() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        assertTrue(Newzgrabber.IsBatch);
    }

    @Test(expected = NullPointerException.class)
    public void testNewzgrabberNullConstructor() {
        Newzgrabber n = new Newzgrabber(null);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.Newzdirectory);
        assertNotNull(Newzgrabber.nf);
    }

    @Test
    public void testFindDirectory() {
        String classfile = Newzgrabber.class.getName().replace(".", "/") + ".class";
        assertEquals(Newzgrabber.Newzdirectory, findDirectory(classfile));
    }

    @Test(expected = NullPointerException.class)
    public void testFindDirectoryNullClassfile() {
        String classfile = null;
        assertEquals(null, findDirectory(classfile));
    }

    @Test
    public void testUpdateIni() {
        String inifile = "test_ini_file.ini";
        String prop = "Messageids";
        String key = "test_key";
        String value = "test_value";
        updateIni(inifile, prop, key, value);

        LinkedList<Object[]> data = new LinkedList<>();
        Ini ini = new Ini(inifile, prop);
        Vector<String[]> Newids = ini.getIniVector();

        int count = 0;
        for (String[] stringarr : Newids) {
            if (stringarr[0].equals(key)) {
                count++;
                assertEquals(value, stringarr[1]);
            }
        }

        assertEquals(1, count);
    }

    @Test
    public void testMain() {
        try {
            String[] args = {"-verbose", "-batch"};
            Newzgrabber.main(args);
            assertTrue(Newzgrabber.verbose);
            assertTrue(Newzgrabber.batch);
            assertTrue(Newzgrabber.IsBatch);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}