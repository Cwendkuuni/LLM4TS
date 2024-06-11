package Newzgrabber;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

    

    @Test
    public void testNewzgrabberConstructorWithString() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        Assert.assertNotNull(newzgrabber);
    }

    @Test
    public void testNewzgrabberStaticMethods() {
        Assert.assertNotNull(Newzgrabber.jtp);
        Assert.assertNotNull(Newzgrabber.optionspanel);
        Assert.assertNotNull(Newzgrabber.groupspanel);
        Assert.assertNotNull(Newzgrabber.searchpanel);
        Assert.assertFalse(Newzgrabber.IsBatch);
        Assert.assertNull(Newzgrabber.transferpanel);
        Assert.assertFalse(Newzgrabber.IsBatch);
        Assert.assertNotNull(Newzgrabber.nf);
        Assert.assertEquals("", Newzgrabber.Newzdirectory);
        Assert.assertEquals("Newz.jar", Newzgrabber.JarFilename);
        Assert.assertEquals("batch.ini", Newzgrabber.BatchFilename);
        Assert.assertEquals("Newzgrabber.ini", Newzgrabber.NewzInifilename);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assert(newzgrabber.Newzdirectory != null);
        newzgrabber.setupBasicSystem();
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            try {
                File javaLibraryPathFile = new File(Newzgrabber.Newzdirectory + File.separator + "lib" + File.separator + "jwsock2.dll");
                Assert.assertTrue(javaLibraryPathFile.exists());
            } catch (Exception e) {
                Assert.fail("Failed to set the java.library.path: " + e.getMessage());
            }
        }
        Assert.assertNotNull(Newzgrabber.nf);
    }

    @Test
    public void testWindowListenerMethods() {
        Newzgrabber newzgrabber = new Newzgrabber();
        WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSED);
        newzgrabber.windowClosing(we);
        Assert.assertFalse(newzgrabber.isVisible());
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newzgrabber.class";
        String newzdirectory = Newzgrabber.findDirectory(classfile);
        Assert.assertNotNull(newzdirectory);
        File newzdirFile = new File(newzdirectory);
        Assert.assertTrue(newzdirFile.exists() && newzdirFile.isDirectory());
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, "Messageids", "testgroup", "testvalue");
        Vector<String[]> iniVector = new Vector<>();
        iniVector.add(new String[]{"Messageids", "testvalue"});
        Ini ini = new Ini(inifile, "Messageids");
        Assert.assertEquals(iniVector, ini.getIniVector());
    }

    @Test(expected = Exception.class)
    public void testUpdateIniWithException() {
        String inifile = "Newzgrabber.ini";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, "Messageids", "testgroup", null);
    }

    @Test
    public void testMain() {
        String[] args = {"-verbose"};
        Newzgrabber.main(args);
        Assert.assertTrue(Newzgrabber.verbose);
    }

    @Test
    public void testMainWithBatchArgs() {
        String[] args = {"-batch"};
        Newzgrabber.main(args);
        Assert.assertTrue(Newzgrabber.IsBatch);
    }

    @Test
    public void testNewzbatchConstructor() {
        Newzbatch newzbatch = new Newzbatch();
        Assert.assertNotNull(newzbatch);
    }

    
}