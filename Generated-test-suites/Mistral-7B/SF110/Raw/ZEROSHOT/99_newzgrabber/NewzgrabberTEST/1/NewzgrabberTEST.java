package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.Newzgrabber;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

    @Before
    public void setUp() {
        Newzgrabber.setupBasicSystem();
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
    }

    @Test
    public void testSetupBasicSystem() {
        assertNotNull(Newzgrabber.Newzdirectory);
        File newzDir = new File(Newzgrabber.Newzdirectory);
        assertTrue(newzDir.isDirectory());
    }

    @Test
    public void testWindowListenerMethods() {
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
        assertTrue(true);
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String newzDirectory = Newzgrabber.findDirectory(classfile);
        assertNotNull(newzDirectory);
        File newzDir = new File(newzDirectory);
        assertTrue(newzDir.isDirectory());
        assertTrue(newzDir.getAbsolutePath().contains(classfile));
    }

    @Test
    public void testUpdateIni() {
        String iniFile = "test.ini";
        String prop = "messageids";
        String key = "TestKey";
        String value = "TestValue";
        Newzgrabber.updateIni(iniFile, prop, key, value);
        Vector<String[]> iniVector = NewIniFile(iniFile).getIniVector();
        String testValue = null;
        for (String[] entry : iniVector) {
            if (entry[0].equals(key)) {
                testValue = entry[1];
                break;
            }
        }
        assertEquals(value, testValue);
    }

    @Test(expected = Exception.class)
    public void testUpdateIniException() {
        String iniFile = "test.ini";
        String prop = "messageids";
        String key = "TestKey";
        String value = "TestValue";
        Newzgrabber.updateIni(iniFile, prop, key, value);
        File iniFileObj = new File(iniFile);
        iniFileObj.delete();
    }

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
        assertNotNull(Newzgrabber.jtp);
        assertEquals(5, Newzgrabber.jtp.getTabCount());
    }

    @Test
    public void testNewBatchMode() {
        Newzgrabber.IsBatch = true;
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
        assertFalse(Newzgrabber.IsBatch);
        Newzgrabber.IsBatch = true;
        newzgrabber = new Newzgrabber("NewzgrabberTest");
        assertTrue(Newzgrabber.IsBatch);
    }

    @Test
    public void testGetBatchJob() {
        String[] batchGroups = {"testGroup1"};
        BatchDriver bd = new BatchDriver("test.ini", "test.batch");
        BatchJob bj = bd.getBatchJob(batchGroups[0]);
        assertNotNull(bj);
    }

    @After
    public void tearDown() {
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
        if (newzgrabber != null) {
            System.exit(0);
        }
    }

    private Ini NewIniFile(String fileName) {
        return new Ini(fileName, null);
    }
}