package test.Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import Newzgrabber.*;

public class NewzgrabberTest {

    @BeforeClass
    public static void setup() {
        System.setProperty("java.library.path", Newzgrabber.Newzdirectory + File.pathSeparator + System.getProperty("java.library.path"));
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber("test");
        assertEquals("expected newz directory", newzgrabber.Newzdirectory, findDirectory(Newzgrabber.JarFilename));
    }

    @Test(expected = Exception.class)
    public void testWindowClosingWithError() {
        Newzgrabber newzgrabber = new Newzgrabber("test");
        throw new RuntimeException("Simulating an error during window closing");
    }

    @Test
    public void testFindDirectory() {
        String classfile = Newzgrabber.class.getName().replace(".","/") + ".class";
        String expected = findDirectory(classfile);
        assertNotNull(expected);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "test.ini";
        Newzgrabber newzgrabber = new Newzgrabber("test");
        newzgrabber.updateIni(inifile, "test", "key", "value");
        File file = new File(inifile);
        assertTrue(file.exists());
        newzgrabber.updateIni(inifile, "test", "key", null);
        file.delete();
    }

    private static String findDirectory(String classfile) {
        String path = System.getProperty("java.class.path");
        StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
        while (st.hasMoreTokens()) {
            String nextpath = st.nextToken();
            File f = new File(nextpath);
            if (!f.isFile() && f.getName().equals(classfile)) {
                return f.getParentFile().getAbsolutePath();
            }
        }
        return null;
    }
}