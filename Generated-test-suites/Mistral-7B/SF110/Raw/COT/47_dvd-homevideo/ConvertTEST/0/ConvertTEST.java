import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithFormatAndAspectRatio() {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testConvertConstructorWithQualityFormatAndAspectRatio() {
        Convert convert = new Convert(5, "dvd-ntsc", "4:3");
        Assert.assertEquals(5, convert.m_Quality);
        Assert.assertEquals("dvd-ntsc", convert.m_Format);
        Assert.assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testInitWithoutThreadCheck() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return false;
            }
        });
        Assert.assertFalse(convert.init());
        Assert.assertTrue(convert.m_Error);
    }

    @Test
    public void testInitWithThreadCheckAndSuccess() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        Assert.assertTrue(convert.init());
        Assert.assertFalse(convert.m_Error);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeCommandWithEmptyDVDDirectory() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public String strOutputDir() {
                return "";
            }
        });
        convert.init();
    }

    @Test
    public void testTranscodeCommandWithOneFile() {
        String[] expectedVideoFiles = {"test1.dv"};
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public String strOutputDir() {
                return "/temp/output/";
            }

            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        convert.init();
        Assert.assertArrayEquals(expectedVideoFiles, convert.video_files);
    }

    @Test
    public void testTranscodeCommandWithMultipleFiles() {
        String[] expectedVideoFiles = {"test1.dv", "test2.dv", "test3.dv"};
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public String strOutputDir() {
                return "/temp/output/";
            }

            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        convert.init();
        Assert.assertArrayEquals(expectedVideoFiles, convert.video_files);
    }

    @Test
    public void testTranscodeCommandWithInvalidFileFormat() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public String strOutputDir() {
                return "/temp/output/";
            }

            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        convert.video_files = new String[]{"test1.mp4"};
        convert.init();
        Assert.assertTrue(convert.m_Error);
    }
}