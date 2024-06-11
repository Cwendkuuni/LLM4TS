package NewzgrabberTest;

import org.junit.Assert;
import org.junit.Test;

import Newzgrabber.Newzgrabber;

public class NewzgrabberTest {

    

    @Test
    public void testNewzgrabberConstructorWithStringArgument() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        Assert.assertNotNull(n);
    }

    @Test(expected = Exception.class)
    public void testNewzgrabberConstructorWithNullArgument() {
        Newzgrabber n = new Newzgrabber(null);
    }

    @Test
    public void testFindDirectoryMethod() {
        String classfile = "Newz.jar";
        String expectedDirectory = "path/to/Newz.jar"; // Replace with the actual directory of Newz.jar
        String actualDirectory = Newzgrabber.findDirectory(classfile);
        Assert.assertEquals(expectedDirectory, actualDirectory);
    }

    @Test(expected = Exception.class)
    public void testFindDirectoryMethodWithNullClassfile() {
        String classfile = null;
        String actualDirectory = Newzgrabber.findDirectory(classfile);
    }

    @Test
    public void testUpdateIniMethod() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);

        // You can add additional assertions here to test the contents of the ini file
    }

    @Test(expected = Exception.class)
    public void testUpdateIniMethodWithNullInifile() {
        String inifile = null;
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        Newzgrabber.updateIni(inifile, prop, key, value);
    }

    @Test
    public void testWindowClosingMethod() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        n.windowClosing(null);
        Assert.assertTrue(true); // You can add additional assertions here to test the behavior of the application when it's closing
    }

    @Test(expected = Exception.class)
    public void testNewzgrabberConstructorWithIsBatchTrueAndArgsNull() {
        Newzgrabber n = new Newzgrabber(true, null);
    }

    @Test(expected = Exception.class)
    public void testNewzgrabberConstructorWithIsBatchTrueAndArgsEmpty() {
        Newzgrabber n = new Newzgrabber(true, new String[0]);
    }

    @Test
    public void testNewzgrabberConstructorWithIsBatchFalse() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        Assert.assertFalse(Newzgrabber.IsBatch);
    }

    @Test
    public void testNewzgrabberConstructorWithIsBatchTrue() {
        Newzgrabber n = new Newzgrabber(true, new String[]{"-verbose"});
        Assert.assertTrue(Newzgrabber.IsBatch);
    }

    
}