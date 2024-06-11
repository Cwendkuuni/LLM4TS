import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    private Convert convert;
    private String[] videoFiles;
    private File listDir;

    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }

            @Override
            public void MessageBox(String message, int type) {
                // Implement this method to handle the GUI's message box functionality in tests
            }

            @Override
            public String strOutputDir() {
                return "output_dir";
            }

            @Override
            public int spnMinutes() {
                return 1;
            }

            @Override
            public int spnSeconds() {
                return 1;
            }

            @Override
            public String[] dvdirList() {
                return new String[]{"file1.dv", "file2.dv"};
            }
        });
        listDir = new File("output_dir/dv/");
        videoFiles = listDir.list();
    }

    @Test
    public void testInit_noThread() {
        convert.init();
        assertFalse(convert.m_Error);
        assertFalse(convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log"));
        assertFalse(convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test
    public void testInit_thread() {
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        assertTrue(convert.m_Thread.isAlive());
        convert.m_Thread.interrupt();
        try {
            convert.m_Thread.join();
        } catch (InterruptedException e) {
            fail();
        }
        assertFalse(convert.m_Error);
        assertFalse(convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log"));
        assertFalse(convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscode_invalidDirectory() {
        convert.thread_track = 0;
        try {
            convert.Transcode();
            fail();
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testTranscode_validDirectory() {
        convert.thread_track = 0;
        convert.video_files[convert.thread_track] = "file1.dv";
        convert.Transcode();
        String transcodeLogFilePath = convert.m_GUI.strOutputDir + "/log/transcode.log";
        String mplexLogFilePath = convert.m_GUI.strOutputDir + "/log/mplex.log";
        assertTrue(new File(transcodeLogFilePath).exists());
        assertTrue(new File(mplexLogFilePath).exists());
        convert.thread_track++;
        convert.Transcode();
        assertTrue(new File(transcodeLogFilePath).exists());
        assertTrue(new File(mplexLogFilePath).exists());
    }

    @Test
    public void testAverageFpsCalculation() {
        double[] averageFps = { 24.9, 25.0, 25.1, 24.9, 25.0 };
        double totalTime = 1.0 * 60.0 * 60.0 + 1.0 * 60.0 * 60.0 + 1.0 * 60.0 * 60.0 + 1.0 * 60.0 * 60.0 + 1.0 * 60.0 * 60.0 + 1.0 * 60.0 * 60.0 + 1.0 * 60.0 * 60.0 + 1.0 * 60.0 * 60.0;

        convert.thread_track = 0;
        for (int i = 0; i < 8; i++) {
            convert.video_files[convert.thread_track] = "file" + (i + 1) + ".dv";
            try {
                convert.Transcode();
            } catch (IOException | InterruptedException | NullPointerException | IllegalArgumentException | Exception e) {
                fail();
            }
            double fpsSum = 0.0;
            for (double fps : averageFps) {
                fpsSum += fps;
            }
            fpsSum /= 5.0;
            assertEquals(fpsSum, convert.m_GUI.prgConvert.getValue() / 100.0, 0.01);
            convert.thread_track++;
        }
    }
}