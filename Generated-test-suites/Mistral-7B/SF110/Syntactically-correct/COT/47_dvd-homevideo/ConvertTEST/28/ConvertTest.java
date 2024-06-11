import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.util.Arrays;

public class ConvertTest
{
    @Test
    public void testConvertConstructorWithFormatDvdNtsc()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        assertEquals(29.97, convert.m_fps, 0.01);
    }

    @Test
    public void testConvertConstructorWithFormatDvdPAL()
    {
        Convert convert = new Convert("dvd-pal", "16:9", new GUI() {});
        assertEquals(25.0, convert.m_fps, 0.01);
    }

    @Test
    public void testConvertConstructorWithQualityAndFormatDvdNtsc()
    {
        Convert convert = new Convert(5, "dvd-ntsc", "4:3", new GUI() {});
        assertEquals(29.97, convert.m_fps, 0.01);
    }

    @Test
    public void testConvertConstructorWithQualityAndFormatDvdPAL()
    {
        Convert convert = new Convert(2, "dvd-pal", "16:9", new GUI() {});
        assertEquals(25.0, convert.m_fps, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullFormat()
    {
        new Convert(null, "4:3", new GUI() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullAspectRatio()
    {
        new Convert("dvd-ntsc", null, new GUI() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullGui()
    {
        new Convert("dvd-ntsc", "4:3", null);
    }

    @Test
    public void testInitWithThreadCheckboxUnchecked()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        assertFalse(convert.init());
        File dir = new File(convert.m_GUI.strOutputDir + "/dv/");
        assertNull(dir.list());
    }

    @Test
    public void testInitWithThreadCheckboxChecked()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.init();
        File dir = new File(convert.m_GUI.strOutputDir + "/dv/");
        Assert.assertArrayEquals(new String[]{"test1.dv", "test2.dv", "test3.dv"}, dir.list());
    }

    @Test(expected = InterruptedException.class)
    public void testInitInterruptedException()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        Thread.currentThread().interrupt();
        convert.init();
    }

    @Test
    public void testTranscodeWithEmptyDvDirectory()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.init();
        convert.Thread = new Thread(convert);
        convert.Thread.start();
        try {
            convert.Thread.join(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File dir = new File(convert.m_GUI.strOutputDir + "/dv/");
        assertNull(dir.list());
    }

    @Test
    public void testTranscodeWithFiles()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.init();
        String[] files = {"test1.dv", "test2.dv", "test3.dv"};
        convert.video_files = files;
        convert.Thread = new Thread(convert);
        convert.Thread.start();
        try {
            convert.Thread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File dir = new File(convert.m_GUI.strOutputDir + "/dv/");
        Assert.assertArrayEquals(files, dir.list());
    }

    @Test
    public void testTranscodeIOException()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.init();
        convert.Thread = new Thread(convert);
        convert.Thread.start();
        try {
            convert.Thread.join(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File dir = new File(convert.m_GUI.strOutputDir + "/dv/");
        File file = new File(dir.getAbsolutePath() + "/test1.dv");
        assertFalse(file.exists());
        try {
            convert.Transcode();
        } catch (IOException e) {
            Assert.assertEquals("Transcoding Error - Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", e.getMessage());
        }
    }

    @Test
    public void testMplexIOException()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.init();
        convert.Thread = new Thread(convert);
        convert.Thread.start();
        try {
            convert.Thread.join(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File dir = new File(convert.m_GUI.strOutputDir + "/dv/");
        File[] files = dir.listFiles();
        Arrays.stream(files).forEach(file -> assertFalse(file.getName().equals("inp.vob")));
        try {
            convert.mplex();
        } catch (IOException e) {
            Assert.assertEquals("Transcoding Error - IO Error\n", e.getMessage());
        }
    }

    @Test
    public void testMatrixFileCreation()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.matrix();
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(file.exists());
    }

    @Test(expected = IOException.class)
    public void testMatrixFileIOException()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.matrix();
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        file.delete();
        try {
            convert.matrix();
        } catch (IOException e) {
            Assert.assertEquals("Transcoding Error - IO Error\n", e.getMessage());
        }
    }
}