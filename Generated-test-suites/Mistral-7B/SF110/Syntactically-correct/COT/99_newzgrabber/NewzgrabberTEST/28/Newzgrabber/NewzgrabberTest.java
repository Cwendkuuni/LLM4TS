package Newzgrabber;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewzgrabberTest {
    

    @Test
    public void testNewzgrabberConstructor() throws Exception {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        Assert.assertNotNull(newzgrabber);
    }

    @Test
    public void testNewzgrabberConstructorWithArguments() throws Exception {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        Assert.assertNotNull(newzgrabber.getJtp());
        Assert.assertNotNull(newzgrabber.getOptionspanel());
        Assert.assertNotNull(newzgrabber.getGroupspanel());
        Assert.assertNotNull(newzgrabber.getSearchpanel());
        Assert.assertNotNull(newzgrabber.getTransferpanel());
    }

    @Test
    public void testSetupBasicSystem() throws Exception {
        Newzgrabber newzgrabber = new Newzgrabber();
        Assert.assertNotNull(newzgrabber.getNewzdirectory());
        Assert.assertNotNull(newzgrabber.getNf());
    }

    @Test
    public void testFindDirectory() throws Exception {
        String classfile = "Newz.jar";
        String expected = System.getProperty("user.dir") + File.separator + "Newzgrabber";
        String result = Newzgrabber.findDirectory(classfile);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUpdateIni() throws Exception {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "TestKey";
        String value = "TestValue";
        Newzgrabber newzgrabber = mock(Newzgrabber.class);
        Ini ini = mock(Ini.class);
        LinkedList<Object[]> iniVector = new LinkedList<>();
        iniVector.add(new Object[]{"TestKey1", "TestValue1"});
        when(newzgrabber.new Ini(inifile, prop)).thenReturn(ini);
        when(ini.getIniVector()).thenReturn(iniVector);

        newzgrabber.updateIni(inifile, prop, key, value);

        Object[] expected = new Object[]{"TestKey", "TestValue"};
        Object[] result = iniVector.getLast();

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = Exception.class)
    public void testWindowClosingWithException() throws Exception {
        Newzgrabber newzgrabber = mock(Newzgrabber.class);
        when(newzgrabber.new Optionspanel()).thenThrow(new Exception("Test Exception"));
        when(newzgrabber.new Groupspanel()).thenThrow(new Exception("Test Exception"));
        when(newzgrabber.new Transferpanel()).thenThrow(new Exception("Test Exception"));

        newzgrabber.windowClosing(null);
    }

    @Test
    public void testMain() {
        String[] args = new String[]{"-verbose"};
        Newzgrabber newzgrabber = null;
        Newzbatch newzbatch = null;

        try {
            Newzgrabber.main(args);
            newzgrabber = Newzgrabber.newzgrabber;
            Assert.assertNotNull(newzgrabber);
            Assert.assertFalse(Newzgrabber.IsBatch);
            Assert.assertTrue(Newzgrabber.verbose);
        } catch (Exception e) {
            e.printStackTrace();
        }

        args = new String[]{"-batch"};
        try {
            Newzgrabber.main(args);
            newzbatch = (Newzbatch) Newzgrabber.newzgrabber;
            Assert.assertNotNull(newzbatch);
            Assert.assertTrue(Newzgrabber.IsBatch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        args = new String[]{"-debug"};
        try {
            Newzgrabber.main(args);
            Assert.assertTrue(Newzgrabber.debug);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStaticInitializer() {
        Assert.assertFalse(Newzgrabber.verbose);
        Assert.assertFalse(Newzgrabber.debug);
        Assert.assertFalse(Newzgrabber.batch);
        Assert.assertFalse(Newzgrabber.IsBatch);
        Assert.assertEquals("Newz.jar", Newzgrabber.JarFilename);
        Assert.assertEquals("batch.ini", Newzgrabber.BatchFilename);
        Assert.assertEquals("Newzgrabber.ini", Newzgrabber.NewzInifilename);
    }

    @Test
    public void testWindowListenerMethods() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        newzgrabber.windowClosed(null);
        newzgrabber.windowOpened(null);
        newzgrabber.windowActivated(null);
        newzgrabber.windowDeactivated(null);
        newzgrabber.windowIconified(null);
        newzgrabber.windowDeiconified(null);
    }

    @Test
    public void testBatchMode() throws Exception {
        String[] args = new String[]{"-batch"};
        Newzgrabber newzgrabber = null;
        Newzbatch newzbatch = null;

        try {
            Newzgrabber.main(args);
            newzbatch = (Newzbatch) Newzgrabber.newzgrabber;
            Assert.assertNotNull(newzbatch);
            Assert.assertTrue(Newzgrabber.IsBatch);
            Assert.assertNull(newzgrabber.getJtp());
            Assert.assertNull(newzgrabber.getOptionspanel());
            Assert.assertNull(newzgrabber.getGroupspanel());
            Assert.assertNull(newzgrabber.getSearchpanel());
            Assert.assertNull(newzgrabber.getTransferpanel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVerboseMode() throws Exception {
        String[] args = new String[]{"-verbose"};
        Newzgrabber newzgrabber = null;

        try {
            Newzgrabber.main(args);
            newzgrabber = Newzgrabber.newzgrabber;
            Assert.assertNotNull(newzgrabber);
            Assert.assertTrue(Newzgrabber.verbose);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(newzgrabber.getJtp());
        Assert.assertNotNull(newzgrabber.getOptionspanel());
        Assert.assertNotNull(newzgrabber.getGroupspanel());
        Assert.assertNotNull(newzgrabber.getSearchpanel());
        Assert.assertNotNull(newzgrabber.getTransferpanel());
    }

    @Test
    public void testDebugMode() throws Exception {
        String[] args = new String[]{"-debug"};
        Newzgrabber newzgrabber = null;

        try {
            Newzgrabber.main(args);
            newzgrabber = Newzgrabber.newzgrabber;
            Assert.assertNotNull(newzgrabber);
            Assert.assertTrue(Newzgrabber.debug);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}