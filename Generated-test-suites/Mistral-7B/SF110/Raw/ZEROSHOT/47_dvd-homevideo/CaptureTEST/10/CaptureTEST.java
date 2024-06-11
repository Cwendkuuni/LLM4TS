import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class CaptureTest {
    private Capture capture;
    private GUI guiMock;

    @Before
    public void setUp() {
        this.guiMock = new GUIMock();
        this.capture = new Capture(0, 0, this.guiMock);
    }

    @After
    public void tearDown() throws Exception {
        this.capture = null;
        this.guiMock = null;
    }

    @Test
    public void testCaptureEmptyConstructor() {
        Capture emptyCapture = new Capture();
        assertNotNull(emptyCapture);
        assertEquals("Capture Error - ", emptyCapture.m_BaseErr);
    }

    @Test
    public void testCaptureConstructorWithArgs() {
        Capture withArgsCapture = new Capture(1, 2, this.guiMock);
        assertNotNull(withArgsCapture);
        assertEquals("Capture Error - ", withArgsCapture.m_BaseErr);
        assertEquals(1, withArgsCapture.m_Minutes);
        assertEquals(2, withArgsCapture.m_Seconds);
        assertNotNull(withArgsCapture.m_GUI);
        assertNull(withArgsCapture.m_Thread);
        assertFalse(withArgsCapture.m_Error);
    }

    @Test
    public void testInitThreadRunning() {
        this.capture.init();
        assertFalse(this.capture.m_Error);
        assertTrue(this.capture.m_Thread.isAlive());
    }

    @Test
    public void testInitThreadNotRunning() {
        this.guiMock.menuChkThread.setSelected(false);
        this.capture.init();
        assertFalse(this.capture.m_Error);
        assertNull(this.capture.m_Thread);
    }

    @Test
    public void testInitErrorCheckOutputDir() throws IOException {
        File outputDir = new File("outputDirectory");
        this.capture.m_GUI = this.guiMock;
        this.capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
        this.capture.init();
        assertTrue(this.capture.m_Error);
        assertEquals("Please choose a different name for your\ntitle or move the directory to another location.", this.capture.m_GUI.strErrorMessage);
    }

    @Test
    public void testInitErrorDeleteFile() throws IOException, InterruptedException {
        File fileToDelete = new File("fileToDelete.txt");
        this.capture.m_GUI = this.guiMock;
        this.capture.m_GUI.strOutputDir = fileToDelete.getParent();
        this.capture.init();
        assertTrue(this.capture.m_Error);
        assertEquals("Please choose a different name for your\ntitle or move the file to another location.", this.capture.m_GUI.strErrorMessage);
    }

    @Test
    public void testInitErrorDeleteOutputDir() throws IOException, InterruptedException {
        File outputDir = new File("outputDirectory");
        this.capture.m_GUI = this.guiMock;
        this.capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "rm -fr " + outputDir.getAbsolutePath()});
        } catch (IOException ex) {
            fail("Failed to delete output directory");
        }
        this.capture.init();
        assertTrue(this.capture.m_Error);
        assertEquals("The Output Directory outputDirectory already exists!", this.capture.m_GUI.strErrorMessage);
    }

    @Test
    public void testDV_CaptureCreateDirectories() throws IOException {
        this.capture.init();
        assertTrue(new File(this.capture.m_GUI.strOutputDir + "/DVD").exists());
        assertTrue(new File(this.capture.m_GUI.strOutputDir + "/log").exists());
        assertTrue(new File(this.capture.m_GUI.strOutputDir + "/dv").exists());
    }

    @Test
    public void testDV_CaptureErrorCommandNotFound() throws IOException, InterruptedException {
        String baseErr = "Capture Error - ";
        String dvgrabCommand = "dvgrabCommandNotfound --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
        Field mDvgrabField = Capture.class.getDeclaredField("m_dvgrab");
        mDvgrabField.setAccessible(true);
        mDvgrabField.set(this.capture, dvgrabCommand);
        this.capture.DV_Capture();
        assertTrue(this.capture.m_Error);
        assertEquals(baseErr + "dvgrab: command not found", this.capture.m_GUI.strErrorMessage);
    }

    @Test
    public void testDV_CaptureSuccessCapture() throws IOException, InterruptedException {
        this.capture.init();
        this.capture.DV_Capture();
        assertFalse(this.capture.m_Error);
        assertTrue(new File(this.capture.m_GUI.strOutputDir + "/DVD").exists());
        assertTrue(new File(this.capture.m_GUI.strOutputDir + "/log/dvgrab.log").exists());
        assertTrue(new File(this.capture.m_GUI.strOutputDir + "/log/dvgrab.err").exists());
    }
}

class GUIMock {
    public JButton menuChkThread;
    public JLabel lblCapture;
    public JProgressBar prgCapture;
    public JLabel lblCaptureProg;
    public JTextArea txtAreaOutput;
    public String strOutputDir;
    public String strErrorMessage;
    public int blnBegin;

    public int MessageBox(String message, int responseType) {
        if (responseType == 0) {
            this.strErrorMessage = message;
        }
        return 0;
    }
}