package NewzgrabberTest;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.Newzgrabber;

public class NewzgrabberTest {

    private Newzgrabber newzgrabber;
    private String iniFile;

    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber("NewzgrabberTest");
        iniFile = "test.ini";
    }

    @Test
    public void testFindDirectory() {
        String classfile = Newzgrabber.class.getName().replace(".", "/") + ".class";
        String expectedDirectory = System.getProperty("user.dir") + File.separator;
        String actualDirectory = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testUpdateIni() {
        LinkedList<Object[]> iniData = new LinkedList<>();
        iniData.add(new Object[]{"Group1", "Value1"});
        Ini ini = new Ini(iniFile, "Messageids");
        ini.setIniData(iniData);

        newzgrabber.updateIni(iniFile, "Messageids", "Group1", "Value2");

        Ini updatedIni = new Ini(iniFile, "Messageids");
        assertEquals(2, updatedIni.getIniData().size());
        Object[] expectedData = {"Group1", "Value2"};
        assertArrayEquals(expectedData, (Object[]) updatedIni.getIniData().get(1));
    }

    @Test
    public void testMain() throws Exception {
        String[] args = new String[]{"-verbose", "-batch"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.verbose);
        assertTrue(Newzgrabber.IsBatch);

        args = new String[]{};
        Newzgrabber.main(args);
        assertFalse(Newzgrabber.verbose);
        assertFalse(Newzgrabber.IsBatch);

        args = new String[]{"-debug"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.debug);
        assertFalse(Newzgrabber.IsBatch);

        args = new String[]{"-batch", "-debug"};
        Newzgrabber.main(args);
        assertFalse(Newzgrabber.verbose);
        assertTrue(Newzgrabber.debug);
        assertTrue(Newzgrabber.IsBatch);
    }

    @Test
    public void testConstructions() {
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
        assertNotNull(newzgrabber.getContentPane());
        assertNotNull(Newzgrabber.jtp);
        assertNotNull(Newzgrabber.searchpanel);
        assertNotNull(Newzgrabber.optionspanel);
        assertNotNull(Newzgrabber.groupspanel);
        assertNotNull(Newzgrabber.transferpanel);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
        String expectedNewzdirectory = System.getProperty("user.dir");
        String expectedJavaLibraryPath = System.getProperty("java.library.path");

        Newzgrabber.setupBasicSystem();
        String actualNewzdirectory = Newzgrabber.Newzdirectory;
        String actualJavaLibraryPath = System.getProperty("java.library.path");

        assertEquals(expectedNewzdirectory, actualNewzdirectory);
        assertTrue(actualJavaLibraryPath.contains(expectedJavaLibraryPath));
    }

    @Test
    public void testWindowEventHandlers() {
        Newzgrabber newzgrabber = new Newzgrabber("NewzgrabberTest");
        newzgrabber.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fail("windowClosing should not be called");
            }
        });
        newzgrabber.setVisible(true);
        newzgrabber.dispose();
    }

    @Test
    public void testNewzbatchConstruction() {
        Newzbatch newzbatch = new Newzbatch();
        assertNull(newzbatch.getContentPane());
        assertNull(Newzbatch.jtp);
        assertNull(Newzbatch.searchpanel);
        assertNull(Newzbatch.optionspanel);
        assertNull(Newzbatch.groupspanel);
        assertNull(Newzbatch.transferpanel);
    }
}