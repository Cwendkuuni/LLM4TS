import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithFormatAndAspectRatio() {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testConvertConstructorWithQualityAndFormatAndAspectRatio() {
        Convert convert = new Convert(1, "dvd-ntsc", "4:3");
        Assert.assertEquals(1, convert.m_Quality);
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testInitMethodNoThreadCheck() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThreadIsSelected() {
                return false;
            }
        });
        Assert.assertFalse(convert.init());
        Assert.assertTrue(convert.m_Error);
    }

    @Test
    public void testInitMethodThreadCheckSuccess() throws InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThreadIsSelected() {
                return true;
            }
        });
        convert.init();
        Assert.assertFalse(convert.m_Error);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeMethodWithNonStandardFileInDvdDirectory() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThreadIsSelected() {
                return true;
            }

            @Override
            public String strOutputDir() {
                return "test_output";
            }
        });
        convert.init();
        File dir = new File("test_output/dv/");
        File[] files = dir.listFiles();
        File nonStandardFile = new File("test_output/dv/non_standard_file.avi");
        Arrays.stream(files).forEach(file -> convert.video_files = new String[]{file.getName()});
        convert.Transcode();
        Assert.assertTrue(convert.m_Error);
    }

    @Test
    public void testTranscodeMethodSuccess() throws Exception {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThreadIsSelected() {
                return true;
            }

            @Override
            public String strOutputDir() {
                return "test_output";
            }
        });
        convert.init();
        File dir = new File("test_output/dv/");
        File[] files = dir.listFiles();
        String[] expectedVideoFiles = new String[]{"test_output/dv/test1.dv", "test_output/dv/test2.dv", "test_output/dv/test3.dv", "test_output/dv/test4.dv"};
        Arrays.stream(files).forEachIndexed(i -> {
            convert.video_files = new String[]{files[i].getName()};
            convert.Transcode();
            Assert.assertFalse(convert.m_Error);
            Assert.assertEquals(expectedVideoFiles[i], convert.video_files[0]);
        });
    }

    @Test(expected = IOException.class)
    public void testTranscodeMethodIOException() throws Exception {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThreadIsSelected() {
                return true;
            }

            @Override
            public String strOutputDir() {
                return "test_output";
            }
        });
        convert.init();
        File outputDir = new File("test_output");
        File logTranscodeFile = new File(outputDir, "log/transcode.log");
        File logMplexFile = new File(outputDir, "log/mplex.log");
        outputDir.mkdirs();
        logTranscodeFile.createNewFile();
        logMplexFile.createNewFile();
        try (FileWriter transcodeLogWriter = new FileWriter(logTranscodeFile);
             BufferedWriter transcodeErrWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logTranscodeFile), "UTF-8"))) {
            transcodeLogWriter.write("encoding error");
            transcodeErrWriter.write("encoding error");
        }
        try (FileWriter mplexLogWriter = new FileWriter(logMplexFile);
             BufferedWriter mplexErrWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logMplexFile), "UTF-8"))) {
            mplexLogWriter.write("encoding error");
            mplexErrWriter.write("encoding error");
        }
        convert.m_GUI = new GUI() {
            @Override
            public boolean ErrorCheck(String filePath) {
                return true;
            }
        };
        convert.Transcode();
    }
}