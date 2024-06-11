import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    private Convert convert;
    private String[] videoFiles;
    private File listDir;

    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        convert.init();
        listDir = new File(convert.m_GUI.strOutputDir + "/dv/");
        videoFiles = listDir.list();
    }

    @Test
    public void testConstructor() {
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInitWithThreadCheckUnchecked() {
        assertFalse(convert.init());
    }

    @Test(expected = InterruptedException.class)
    public void testInitWithThreadCheckInterrupted() {
        convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        convert.init();
        Thread.currentThread().interrupt();
        convert.init();
    }

    @Test
    public void testTranscodeWithOneFile() {
        assertEquals(1, videoFiles.length);
        String transcodeCmd = convert.m_transcode.replaceAll("\\$", "");
        String[] transcodeCmdArgs = Arrays.copyOf(transcodeCmd.split("\\s+"), transcodeCmd.split("\\s+").length);
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", Arrays.toString(transcodeCmdArgs));
        pb.directory(new File(convert.m_GUI.strOutputDir));
        try {
            Process process = pb.start();
            process.waitFor();
            assertEquals(0, process.exitValue());
        } catch (IOException | InterruptedException e) {
            fail("Transcode failed: " + e.getMessage());
        }
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithNonStandardFilesInDvDirectory() {
        convert.video_files = new String[]{"non_standard_file.mp4"};
        convert.Transcode();
    }

    @Test
    public void testTranscodeWithMultipleFiles() {
        assertEquals(2, videoFiles.length);
        for (int i = 0; i < videoFiles.length; i++) {
            String transcodeCmd = convert.m_transcode.replaceAll("\\$", "").replaceAll("test1", videoFiles[i].substring(0, 11));
            String[] transcodeCmdArgs = Arrays.copyOf(transcodeCmd.split("\\s+"), transcodeCmd.split("\\s+").length);
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", Arrays.toString(transcodeCmdArgs));
            pb.directory(new File(convert.m_GUI.strOutputDir));
            try {
                Process process = pb.start();
                process.waitFor();
                assertEquals(0, process.exitValue());
            } catch (IOException | InterruptedException e) {
                fail("Transcode failed: " + e.getMessage());
            }
        }
    }
}