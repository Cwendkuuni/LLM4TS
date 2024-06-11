package Newzgrabber;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        Assert.assertNotNull(newzgrabber);
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String expectedDirectory = System.getProperty("user.dir") + File.separator + ".."; // Assuming the test is run from the project directory
        String actualDirectory = Newzgrabber.findDirectory(classfile);
        Assert.assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testUpdateIniFileWithExistingKey() {
        String inifile = "test.ini";
        String prop = "testProp";
        String key = "TestKey";
        String value = "TestValue";

        Newzgrabber.updateIni(inifile, prop, key, value);

        Ini ini = new Ini(inifile, prop);
        Vector<String[]> iniVector = ini.getIniVector();
        String[] result = null;
        for (String[] stringArr : iniVector) {
            if (stringArr[0].equals(key)) {
                result = stringArr;
                break;
            }
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result[1]);
    }

    @Test(expected = Exception.class)
    public void testUpdateIniFileWithNonExistentKey() {
        String inifile = "test.ini";
        String prop = "testProp";
        String key = "NonExistentKey";
        String value = "TestValue";

        Newzgrabber.updateIni(inifile, prop, key, value);
    }

    @Test(expected = Exception.class)
    public void testWindowClosingWithoutUpdatingIni() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
    }

    @Test
    public void testBatchMode() {
        System.setProperty("java.class.path", System.getProperty("java.class.path") + File.pathSeparator + "NewzgrabberTest.class");
        String[] args = {"-batch"};
        Newzgrabber newzgrabber = new Newzgrabber(null);
        newzgrabber.main(args);

        // Add assertions for specific batch mode behaviors here
    }

    @Test
    public void testVerboseMode() {
        System.setProperty("java.class.path", System.getProperty("java.class.path") + File.pathSeparator + "NewzgrabberTest.class");
        String[] args = {"-verbose"};
        Newzgrabber newzgrabber = new Newzgrabber(null);
        newzgrabber.main(args);

        // Add assertions for specific verbose mode behaviors here
    }

    @Test
    public void testMainMethodWithInvalidArguments() {
        System.setProperty("java.class.path", System.getProperty("java.class.path") + File.pathSeparator + "NewzgrabberTest.class");
        String[] args = {"-invalid"};
        Newzgrabber newzgrabber = new Newzgrabber(null);

        try {
            newzgrabber.main(args);
            Assert.fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Expected exception, no need to assert message
        }
    }

    @Test
    public void testMainMethodWithoutAnyArguments() {
        System.setProperty("java.class.path", System.getProperty("java.class.path") + File.pathSeparator + "NewzgrabberTest.class");
        String[] args = {};
        Newzgrabber newzgrabber = new Newzgrabber(null);

        try {
            newzgrabber.main(args);
            Assert.fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Expected exception, no need to assert message
        }
    }
}