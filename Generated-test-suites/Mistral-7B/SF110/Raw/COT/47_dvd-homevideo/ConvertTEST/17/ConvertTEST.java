import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConstructorWithFormat() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals(29.97, convert.m_fps, 0.01);
    }

    @Test
    public void testConstructorWithQualityAndFormat() {
        Convert convert = new Convert(2, "dvd-ntsc", "1:1", new GUI());
        assertEquals(2, convert.m_Quality);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals(29.97, convert.m_fps, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatInConstructor() {
        new Convert("invalidFormat", "1:1", new GUI());
    }

    @Test
    public void testInitMethod() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        convert.init();
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInitMethodWithThreadError() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        assertTrue(convert.m_Error);
    }

    @Test(expected = IOException.class)
    public void testTranscodeMethodWithIOException() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        convert.Transcode();
    }

    @Test(expected = NullPointerException.class)
    public void testTranscodeMethodWithNullPointerException() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        convert.m_transcode = null;
        convert.Transcode();
    }

    @Test
    public void testTranscodeMethodWithValidFiles() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        File directory = new File("path/to/dv/directory");
        File[] files = directory.listFiles();
        convert.video_files = Arrays.copyOfRange(files, 0, 5);
        convert.Transcode();
    }

    @Test
    public void testTranscodeMethodWithInvalidFiles() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        File directory = new File("path/to/dv/directory");
        convert.video_files = new String[] {"file1.dv", "file2.txt", "file3.dv"};
        try {
            convert.Transcode();
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testMatrixMethod() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        convert.matrix();
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(file.exists());
    }

    @Test(expected = IOException.class)
    public void testMatrixMethodWithIOException() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI());
        convert.matrix();
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        file.delete();
        convert.matrix();
    }
}