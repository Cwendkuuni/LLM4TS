import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConvertConstructor1() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI());
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("aspectRatio1", convert.m_AspectRatio);
        Assert.assertEquals(29.97, convert.m_fps, 0.01);
        Assert.assertEquals("8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2", convert.m_flags);
        Assert.assertEquals("8500", convert.m_bitrate);
        Assert.assertNull(convert.m_Quality);
        Assert.assertEquals(0, convert.thread_track);
        Assert.assertNull(convert.m_Error);
        Assert.assertNull(convert.m_transcode);
        Assert.assertNull(convert.mplex);
    }

    @Test
    public void testConvertConstructor2() {
        Convert convert = new Convert(5, "dvd-ntsc", "aspectRatio1", new GUI());
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("aspectRatio1", convert.m_AspectRatio);
        Assert.assertEquals(29.97, convert.m_fps, 0.01);
        Assert.assertNull(convert.m_Quality);
        Assert.assertEquals("8, -c -q 6 -4 3 -2 3 -N 0.5 -E -10 -K tmpgenc -R 0", convert.m_flags);
        Assert.assertEquals("8500", convert.m_bitrate);
        Assert.assertEquals(5, convert.m_Quality);
        Assert.assertEquals(0, convert.thread_track);
        Assert.assertNull(convert.m_Error);
        Assert.assertNull(convert.m_transcode);
        Assert.assertNull(convert.mplex);
    }

    @Test
    public void testInitFalse_ThreadNotStarted() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI());
        Assert.assertFalse(convert.init());
    }

    @Test
    public void testInitFalse_ThreadError() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI()) {
            @Override
            public boolean init() {
                throw new RuntimeException("Thread init error");
            }
        };
        Assert.assertTrue(convert.m_Error);
    }

    @Test
    public void testInitFalse_LogFileErrors() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI()) {
            @Override
            public boolean init() {
                return false;
            }
        };
        Assert.assertTrue(convert.m_Error);
    }

    @Test
    public void testTranscodeWithOneFile() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI());
        convert.init();

        File outputDir = new File("outputDir");
        Assert.assertTrue(outputDir.mkdir());
        File listDir = new File(outputDir.getPath() + "/dv/");
        File inputFile = new File("inputFile.dv");
        File[] files = { inputFile };
        Assert.assertArrayEquals(files, listDir.listFiles());

        convert.video_files = listDir.listFiles();
        convert.Transcode();

        Assert.assertEquals(1, convert.thread_track);
        Assert.assertNull(convert.m_Error);
    }

    @Test
    public void testTranscodeWithMultipleFiles() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI());
        convert.init();

        File outputDir = new File("outputDir");
        Assert.assertTrue(outputDir.mkdir());
        File listDir = new File(outputDir.getPath() + "/dv/");
        File inputFile1 = new File("inputFile1.dv");
        File inputFile2 = new File("inputFile2.dv");
        File[] files = { inputFile1, inputFile2 };
        listDir.mkdirs();
        Arrays.stream(files).forEach(file -> {
            try {
                Files.write(file.toPath(), new byte[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        convert.video_files = listDir.listFiles();
        convert.Transcode();

        Assert.assertEquals(2, convert.thread_track);
        Assert.assertNull(convert.m_Error);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithNonDVFile() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI());
        convert.init();

        File outputDir = new File("outputDir");
        File listDir = new File(outputDir.getPath() + "/dv/");
        File nonDVFile = new File("nonDVFile");
        listDir.mkdirs();
        nonDVFile.createNewFile();

        convert.video_files = listDir.listFiles();
        convert.Transcode();
    }

    @Test
    public void testMatrixFileCreation() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI());
        convert.matrix();

        File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        Assert.assertTrue(matrixFile.exists());
    }

    @Test(expected = IOException.class)
    public void testMatrixFileCreationFailure() {
        Convert convert = new Convert("dvd-ntsc", "aspectRatio1", new GUI()) {
            @Override
            public void matrix() {
                throw new IOException("File creation error");
            }
        };
        convert.matrix();
    }
}