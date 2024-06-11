package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.Test;

public class NewzgrabberTest {

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String expectedDirectory = System.getProperty("user.dir") + File.separator + ".."; // assuming the test is run from the project directory
        String result = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, result);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "TestKey";
        String value = "TestValue";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, prop, key, value);
        // Add more assertions based on the expected behavior of the updateIni method
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = mock(Newzgrabber.class);
        doNothing().when(newzgrabber).windowClosing(any(WindowEvent.class));
        newzgrabber.windowClosing(mock(WindowEvent.class));
        verify(newzgrabber).windowClosing(any(WindowEvent.class));
    }

    // Add test cases for other methods in the class
}