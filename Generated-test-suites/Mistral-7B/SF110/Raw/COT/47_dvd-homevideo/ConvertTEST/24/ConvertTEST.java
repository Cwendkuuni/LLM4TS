import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithFormat() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        assertEquals("dvd-ntsc", convert.getm_Format());
        assertEquals(29.97, convert.getm_fps(), 0.01);
    }

    @Test
    public void testConvertConstructorWithFormatAndQuality() {
        Convert convert = new Convert(3, "dvd-ntsc", "4:3", new GUI());
        assertEquals(3, convert.getm_Quality());
    }

    @Test
    public void testInitWithThreadNotSelected() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        assertFalse(convert.init());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithInvalidNumberOfFilesInDirectory() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File dir = new File("path/to/directory");
        convert.init();
        String[] files = dir.list();
        Arrays.stream(files).limit(convert.video_files.length).forEach(filename -> {
            convert.video_files = new String[]{filename};
            convert.Transcode();
        });
        fail("Expected Transcoding Error message but none was displayed.");
    }

    @Test
    public void testTranscodeWithSuccessfulExecution() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File dir = new File("path/to/directory");
        convert.init();
        String[] files = dir.list();
        Arrays.stream(files).limit(convert.video_files.length).forEach(filename -> {
            convert.video_files = new String[]{filename};
            convert.Transcode();
            assertTrue(new File(convert.m_GUI.strOutputDir + "/log/transcode.log").exists());
            assertTrue(new File(convert.m_GUI.strOutputDir + "/log/mplex.log").exists());
        });
    }

    @Test
    public void testMatrixFileCreation() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        convert.matrix();
        assertTrue(file.exists());
    }

    @Test(expected = IOException.class)
    public void testMatrixFileCreationWithIOException() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        try {
            convert.matrix();
        } catch (IOException e) {
            throw e;
        }
        fail("Expected IOException but none was thrown.");
    }
}