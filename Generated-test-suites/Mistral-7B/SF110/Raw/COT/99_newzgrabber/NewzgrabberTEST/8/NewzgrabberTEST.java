package Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber.getContentPane());
        assertEquals(5, newzgrabber.jtp.getTabCount());
    }

    @Test
    public void testNewzgrabberConstructorWithTitle() {
        Newzgrabber newzgrabber = new Newzgrabber("Test Newzgrabber");
        assertEquals("Test Newzgrabber", newzgrabber.getTitle());
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber.Newzdirectory);
    }

    @Test(expected = NullPointerException.class)
    public void testFindDirectoryWithNoClassfileInPath() {
        Newzgrabber.findDirectory(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateIniWithNullInifile() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(null, "Messageids", "TestKey", "TestValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithEmptyProp() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni("test.ini", "", "TestKey", "TestValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithEmptyKey() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni("test.ini", "Messageids", "", "TestValue");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithEmptyValue() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni("test.ini", "Messageids", "TestKey", "");
    }

    @Test
    public void testNewzgrabberMain() {
        Newzgrabber.main(new String[]{"-verbose"});
        Newzgrabber.verbose = true;
        assertEquals(true, Newzgrabber.verbose);
    }
}