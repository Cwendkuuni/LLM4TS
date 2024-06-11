import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConvertConstructor() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("4:3", convert.m_AspectRatio);
        Assert.assertNull(convert.m_Quality);
    }

    @Test
    public void testConvertConstructorWithQuality() {
        Convert convert = new Convert(2, "dvd-ntsc", "4:3", new GUI());
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("4:3", convert.m_AspectRatio);
        Assert.assertEquals(2, convert.m_Quality);
    }

    @Test
    public void testInit() throws InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        Assert.assertFalse(convert.init());
        Assert.assertTrue(convert.m_Error);

        convert = new Convert("dvd-ntsc", "4:3", new GUI(), true);
        convert.thread_track = 0;
        Assert.assertTrue(convert.init());
        Assert.assertFalse(convert.m_Error);

        convert.video_files = new File("testDir").list();
        convert.thread_track = 1;
        Assert.assertTrue(convert.init());
        Assert.assertFalse(convert.m_Error);
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        convert.thread_track = 0;
        convert.init();

        String transcodeCommand = convert.m_transcode;
        Assert.assertNotNull(transcodeCommand);

        convert.thread_track = 1;
        convert.init();

        String mplexCommand = convert.mplex;
        Assert.assertNotNull(mplexCommand);

        convert.matrix();
        File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        Assert.assertTrue(matrixFile.exists());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithInvalidFile() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        convert.thread_track = 1;
        convert.init();

        convert.video_files = new File("testDir").list();
        String invalidFile = convert.video_files[convert.video_files.length];
        convert.m_transcode = convert.m_transcode.replaceAll("test1", invalidFile);
        convert.mplex = convert.mplex.replaceAll("inp", invalidFile.substring(0, 11));

        convert.Transcode();
    }
}