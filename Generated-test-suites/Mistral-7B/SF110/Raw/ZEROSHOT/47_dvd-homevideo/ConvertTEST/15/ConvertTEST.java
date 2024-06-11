import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    private Convert convert;
    private String[] videoFiles;
    private GUI gui;

    @Before
    public void setUp() {
        this.gui = new GUI() {{
            strOutputDir = "output_dir";
            menuChkThread = true;
        }};
        this.convert = new Convert("dvd-ntsc", "16:9", this.gui);
        this.videoFiles = new File("output_dir/dv/").listFile();
    }

    @Test
    public void testInit_NoThread() {
        assertFalse(convert.init());
        assertEquals("Transcoding Error - Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", convert.m_GUI.strErrorMessage);
    }

    @Test
    public void testInit_ThreadRunning() {
        this.convert.m_GUI.menuChkThread = false;
        assertFalse(convert.init());
        assertFalse(convert.m_Thread.isAlive());
    }

    @Test
    public void testInit_ThreadFinished() {
        this.convert.m_Thread.run();
        assertTrue(convert.init());
        assertFalse(convert.m_Error);
        assertFalse(convert.m_GUI.ErrorCheck("output_dir/log/transcode.log"));
        assertFalse(convert.m_GUI.ErrorCheck("output_dir/log/mplex.log"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscode_InvalidVideoFiles() {
        this.convert.Transcode();
    }

    @Test
    public void testTranscode_ValidVideoFiles() {
        this.convert.thread_track = 0;
        this.convert.Transcode();
        assertEquals(this.videoFiles[0], convert.video_files[0]);
        assertEquals(this.videoFiles[0].substring(0, 11), convert.video_files[0].substring(0, 11));
        assertEquals(this.videoFiles[0].substring(0, 11), convert.mplex.replaceAll("inp.m2v", this.videoFiles[0].substring(0, 11)));
        this.convert.thread_track++;
        this.convert.Transcode();
        assertEquals(this.videoFiles[1], convert.video_files[1]);
        assertEquals(this.videoFiles[1].substring(0, 11), convert.video_files[1].substring(0, 11));
        assertEquals(this.videoFiles[1].substring(0, 11), convert.mplex.replaceAll("inp.m2v", this.videoFiles[1].substring(0, 11)));
    }
}