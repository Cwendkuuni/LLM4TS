package Newzgrabber.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import Newzgrabber.*;

@Category(IntegrationTests.class)
public class NewzgrabberTest {

    private static final String NEWZDIRECTORY = "testNewzDirectory";
    private static final String JARFILENAME = "testJarFilename.jar";
    private static final String BATCHFILENAME = "testBatchFilename.ini";
    private static final String NEWZINIFILENAME = "testNewzIniFileName.ini";

    @BeforeClass
    public static void setupClass() {
        System.setProperty("java.library.path", NEWZDIRECTORY);
        System.setProperty("Newzgrabber.Newzdirectory", NEWZDIRECTORY);
        System.setProperty("Newzgrabber.JarFilename", JARFILENAME);
        System.setProperty("Newzgrabber.BatchFilename", BATCHFILENAME);
        System.setProperty("Newzgrabber.NewzInifilename", NEWZINIFILENAME);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupBasicSystem();
    }

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        assertNotNull(n);
    }

    @Test
    public void testNewzgrabberConstructorWithArguments() {
        Newzgrabber n = new Newzgrabber("Newzgrabber");
        assertEquals("Newzgrabber", n.getTitle());
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber n = new Newzgrabber();
        assertNotNull(n.getNewzdirectory());
        assertNotNull(n.getNf());
    }

    @Test(expected = NullPointerException.class)
    public void testFindDirectoryWithNullClassfile() {
        Newzgrabber.findDirectory(null);
    }

    @Test
    public void testFindDirectoryWithValidClassfile() {
        Newzgrabber n = new Newzgrabber();
        String classfile = "Newz.jar";
        String expectedDirectory = NEWZDIRECTORY + File.separator + "lib";
        String actualDirectory = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, actualDirectory);
    }

    @Test
    public void testUpdateIniWithEmptyIniFile() {
        Newzgrabber n = new Newzgrabber();
        String inifile = "testNewzGrabber.ini";
        String prop = "Messageids";
        String key = "TestKey";
        String value = "TestValue";
        n.updateIni(inifile, prop, key, value);
        assertTrue(new File(inifile).exists());
    }

    @Test
    public void testUpdateIniWithExistingKey() {
        Newzgrabber n = new Newzgrabber();
        String inifile = "testNewzGrabber.ini";
        String prop = "Messageids";
        String key = "TestKey";
        String value = "TestValue";
        Ini iniMock = mock(Ini.class);
        when(iniMock.getIniVector()).thenReturn( Collections.singletonList(new String[]{"TestKey", "TestValueExisting"}) );
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Ini ini = invocation.getArgument(0);
                ini.writeIni(anyList());
                return null;
            }
        }).when(iniMock).writeIni(anyList());
        doAnswer(new Answer<Ini>() {
            @Override
            public Ini answer(InvocationOnMock invocation) {
                return iniMock;
            }
        }).when(iniMock).newInstance(eq(prop), anyString());
        n.setIni(iniMock);
        n.updateIni(inifile, prop, key, value);
        String expectedValue = "TestValue";
        String actualValue = n.readIni(inifile, prop, key).getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithNullInifile() {
        Newzgrabber n = new Newzgrabber();
        String prop = "Messageids";
        String key = "TestKey";
        String value = "TestValue";
        n.updateIni(null, prop, key, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithNullProp() {
        Newzgrabber n = new Newzgrabber();
        String inifile = "testNewzGrabber.ini";
        String prop = null;
        String key = "TestKey";
        String value = "TestValue";
        n.updateIni(inifile, prop, key, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithNullKey() {
        Newzgrabber n = new Newzgrabber();
        String inifile = "testNewzGrabber.ini";
        String prop = "Messageids";
        String key = null;
        String value = "TestValue";
        n.updateIni(inifile, prop, key, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIniWithNullValue() {
        Newzgrabber n = new Newzgrabber();
        String inifile = "testNewzGrabber.ini";
        String prop = "Messageids";
        String key = "TestKey";
        String value = null;
        n.updateIni(inifile, prop, key, value);
    }

    @After
    public void tearDown() {
        Newzgrabber.Newzdirectory = null;
        Newzgrabber.JarFilename = null;
        Newzgrabber.BatchFilename = null;
        Newzgrabber.NewzInifilename = null;
    }
}