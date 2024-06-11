import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithFormat() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        assertEquals("dvd-ntsc", convert.getFormat());
    }

    @Test
    public void testConvertConstructorWithQualityFormatAndAspectRatio() {
        Convert convert = new Convert(5, "dvd-ntsc", "4:3", new GUI() { /* stub */ });
        assertEquals(5, convert.getQuality());
        assertEquals("dvd-ntsc", convert.getFormat());
        assertEquals("4:3", convert.getAspectRatio());
    }

    @Test
    public void testInitNoThread() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        assertFalse(convert.init());
    }

    @Test
    public void testInitThread() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        convert.m_GUI.menuChkThread.setSelected(true);
        assertTrue(convert.init());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeCommandWithInvalidDirectory() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        File dir = new File("invalid_directory");
        List<String> videoFiles = dir.list();
        convert.video_files = videoFiles.toArray(new String[0]);
        convert.thread_track = 0;
        convert.Transcode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranscodeCommandWithNegativeQuality() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        convert.m_Quality = -1;
        convert.init();
        convert.Transcode();
    }

    @Test
    public void testTranscodeCommandWithValidInputs() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        String[] videoFiles = {"test1.dv", "test2.dv", "test3.dv"};
        convert.video_files = videoFiles;
        convert.thread_track = 0;
        convert.Transcode();
    }

    @Test(expected = NullPointerException.class)
    public void testTranscodeCommandWithNullInput() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        convert.video_files = null;
        convert.thread_track = 0;
        convert.Transcode();
    }

    @Test
    public void testMatrixFileCreation() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
        try {
            convert.matrix();
        } catch (IOException e) {
            fail("IOException should not be thrown when creating matrix file.");
        }
    }
}